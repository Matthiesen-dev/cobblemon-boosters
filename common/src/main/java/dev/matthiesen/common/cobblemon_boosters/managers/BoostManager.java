package dev.matthiesen.common.cobblemon_boosters.managers;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.EventObservable;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.SpawnBucketOverrideSelector;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BoostManager {
    private static final BoostRecord<ShinyBoost, ShinyChanceCalculationEvent> SHINY_RECORD =
            new BoostRecord<>(
                    ShinyBoost.class,
                    CobblemonEvents.SHINY_CHANCE_CALCULATION,
                    (boost, event) ->
                            event.addModificationFunction(((rate, player, pokemon) ->
                                    Math.max(rate / boost.getMultiplier(), 1)))
            );
    private static final BoostRecord<CatchBoost, PokemonCatchRateEvent> CATCH_RECORD =
            new BoostRecord<>(
                    CatchBoost.class,
                    CobblemonEvents.POKEMON_CATCH_RATE,
                    (boost, event) -> {
                        float baseCatchRate = event.getCatchRate();
                        event.setCatchRate(Math.min(baseCatchRate * boost.getMultiplier(), 1F));
                    }
            );
    private static final BoostRecord<ExperienceBoost, ExperienceGainedEvent.Pre> EXPERIENCE_RECORD =
            new BoostRecord<>(
                    ExperienceBoost.class,
                    CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE,
                    (boost, event) -> {
                        int exp = event.getExperience();
                        event.setExperience(Math.round(exp * boost.getMultiplier()));
                    }
            );
    private static final BoostRecord<SpawnBucketBoost, SpawnBucketChosenEvent> SPAWN_BUCKET_RECORD =
            new BoostRecord<>(
                    SpawnBucketBoost.class,
                    CobblemonEvents.SPAWN_BUCKET_CHOSEN,
                    (boost, event) -> {
                        SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, boost);
                        event.setBucket(newBucket);
                    }
            );

    public BoostManager() {}

    public IBoostManager<ShinyBoost> getShinyBoostManager() {
        return SHINY_RECORD.getManager();
    }

    public IBoostManager<CatchBoost> getCatchBoostManager() {
        return CATCH_RECORD.getManager();
    }

    public IBoostManager<ExperienceBoost> getExperienceBoostManager() {
        return EXPERIENCE_RECORD.getManager();
    }

    public IBoostManager<SpawnBucketBoost> getSpawnBucketBoostManager() {
        return SPAWN_BUCKET_RECORD.getManager();
    }

    public void appendPlayer(ServerPlayer player) {
        SHINY_RECORD.addPlayer(player);
        CATCH_RECORD.addPlayer(player);
        EXPERIENCE_RECORD.addPlayer(player);
        SPAWN_BUCKET_RECORD.addPlayer(player);
    }

    public void clearPlayer(ServerPlayer player) {
        SHINY_RECORD.clearPlayer(player);
        CATCH_RECORD.clearPlayer(player);
        EXPERIENCE_RECORD.clearPlayer(player);
        SPAWN_BUCKET_RECORD.clearPlayer(player);
    }

    public void setupSubscriptions() {
        SHINY_RECORD.setupSubscription();
        CATCH_RECORD.setupSubscription();
        EXPERIENCE_RECORD.setupSubscription();
        SPAWN_BUCKET_RECORD.setupSubscription();
    }

    public void teardownSubscriptions() {
        SHINY_RECORD.teardown();
        CATCH_RECORD.teardown();
        EXPERIENCE_RECORD.teardown();
        SPAWN_BUCKET_RECORD.teardown();
    }

    public static class BoostRecord<T extends IBoost, K> {
        private final EventObservable<K> observable;
        private ObservableSubscription<K> subscription;
        private final BiConsumer<T, K> eventHandler;
        private T active;
        private final Queue<T> queue;
        private final IBoostManager<T> manager;

        @SuppressWarnings("unused")
        public BoostRecord(Class<T> boostClass, EventObservable<K> observable, BiConsumer<T, K> eventHandler) {
            this.observable = observable;
            this.eventHandler = eventHandler;
            this.queue = new LinkedList<>();
            this.active = null;
            this.manager = new IBoostManager<>(() -> active, b -> active = b, this.queue);
            this.subscription = null;
        }

        public void addPlayer(ServerPlayer player) {
            if (active != null) active.getBossBar().addPlayer(player);
        }

        public void clearPlayer(ServerPlayer player) {
            if (active != null) active.getBossBar().removePlayer(player);
        }

        public IBoostManager<T> getManager() {
            return manager;
        }

        public void setupSubscription() {
            this.subscription = observable.subscribe(Priority.NORMAL, event -> {
                T activeBoost = manager.getActive();
                if (activeBoost != null) {
                    eventHandler.accept(activeBoost, event);
                }
            });
        }

        public void teardown() {
            this.active = null;
            this.queue.clear();
            if (this.subscription != null) subscription.unsubscribe();
        }
    }

    public static class IBoostManager<T extends IBoost> {
        private final Supplier<T> getter;
        private final Consumer<T> setter;
        private final Queue<T> queue;

        public IBoostManager(Supplier<T> getter, Consumer<T> setter, Queue<T> queue) {
            this.getter = getter;
            this.setter = setter;
            this.queue = queue;
        }

        public T getActive() {
            return getter.get();
        }

        public void setActive(T boost) {
            setter.accept(boost);
        }

        public Queue<T> getQueue() {
            return queue;
        }

        public void setQueue(Queue<T> replacement) {
            this.queue.clear();
            this.queue.addAll(replacement);
        }

        public void appendToQueue(T boost) {
            this.queue.add(boost);
        }
    }
}
