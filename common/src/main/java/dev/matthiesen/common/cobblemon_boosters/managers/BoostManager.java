package dev.matthiesen.common.cobblemon_boosters.managers;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
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
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BoostManager {
    private ShinyBoost activeShinyBoost = null;
    private final Queue<ShinyBoost> queuedShinyBoosts = new LinkedList<>();
    private ObservableSubscription<ShinyChanceCalculationEvent> shinySubscription = null;
    private final Consumer<ShinyChanceCalculationEvent> shinyEventHandler = event -> {
        if (this.activeShinyBoost != null) {
            event.addModificationFunction(((rate, player, pokemon) -> Math.max(rate / this.activeShinyBoost.getMultiplier(), 1)));
        }
    };

    private CatchBoost activeCatchBoost = null;
    private final Queue<CatchBoost> queuedCatchBoosts = new LinkedList<>();
    private ObservableSubscription<PokemonCatchRateEvent> catchSubscription = null;
    private final Consumer<PokemonCatchRateEvent> catchEventHandler = event -> {
        if (this.activeCatchBoost != null) {
            float baseCatchRate = event.getCatchRate();
            event.setCatchRate(Math.min(baseCatchRate * this.activeCatchBoost.getMultiplier(), 1F));
        }
    };

    private ExperienceBoost activeExperienceBoost = null;
    private final Queue<ExperienceBoost> queuedExperienceBoosts = new LinkedList<>();
    private ObservableSubscription<ExperienceGainedEvent.Pre> experienceSubscription = null;
    private final Consumer<ExperienceGainedEvent.Pre> experienceEventHandler = event -> {
        if (this.activeExperienceBoost != null) {
            int exp = event.getExperience();
            event.setExperience(Math.round(exp * this.activeExperienceBoost.getMultiplier()));
        }
    };

    private SpawnBucketBoost activeSpawnBucketBoost = null;
    private final Queue<SpawnBucketBoost> queuedSpawnBucketBoosts = new LinkedList<>();
    private ObservableSubscription<SpawnBucketChosenEvent> spawnBucketSubscription = null;
    private final Consumer<SpawnBucketChosenEvent> spawnBucketEventHandler = event -> {
        if (this.activeSpawnBucketBoost != null) {
            SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, this.activeSpawnBucketBoost);
            event.setBucket(newBucket);
        }
    };

    private final IBoostManager<ShinyBoost> shinyManager;
    private final IBoostManager<CatchBoost> catchManager;
    private final IBoostManager<ExperienceBoost> experienceManager;
    private final IBoostManager<SpawnBucketBoost> spawnBucketManager;

    public BoostManager() {
        this.shinyManager = new IBoostManager<>(() -> activeShinyBoost, b -> activeShinyBoost = b, queuedShinyBoosts);
        this.catchManager = new IBoostManager<>(() -> activeCatchBoost, b -> activeCatchBoost = b, queuedCatchBoosts);
        this.experienceManager = new IBoostManager<>(() -> activeExperienceBoost, b -> activeExperienceBoost = b, queuedExperienceBoosts);
        this.spawnBucketManager = new IBoostManager<>(() -> activeSpawnBucketBoost, b -> activeSpawnBucketBoost = b, queuedSpawnBucketBoosts);
    }

    public IBoostManager<ShinyBoost> getShinyBoostManager() {
        return shinyManager;
    }

    public IBoostManager<CatchBoost> getCatchBoostManager() {
        return catchManager;
    }

    public IBoostManager<ExperienceBoost> getExperienceBoostManager() {
        return experienceManager;
    }

    public IBoostManager<SpawnBucketBoost> getSpawnBucketBoostManager() {
        return spawnBucketManager;
    }

    public void appendPlayer(ServerPlayer player) {
        if (activeShinyBoost != null) activeShinyBoost.getBossBar().addPlayer(player);
        if (activeCatchBoost != null) activeCatchBoost.getBossBar().addPlayer(player);
        if (activeExperienceBoost != null) activeExperienceBoost.getBossBar().addPlayer(player);
        if (activeSpawnBucketBoost != null) activeSpawnBucketBoost.getBossBar().addPlayer(player);
    }

    public void clearPlayer(ServerPlayer player) {
        if (activeShinyBoost != null) activeShinyBoost.getBossBar().removePlayer(player);
        if (activeCatchBoost != null) activeCatchBoost.getBossBar().removePlayer(player);
        if (activeExperienceBoost != null) activeExperienceBoost.getBossBar().removePlayer(player);
        if (activeSpawnBucketBoost != null) activeSpawnBucketBoost.getBossBar().removePlayer(player);
    }

    public void setupSubscriptions() {
        shinySubscription = CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, this.shinyEventHandler);
        catchSubscription = CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.NORMAL, this.catchEventHandler);
        experienceSubscription = CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.NORMAL, this.experienceEventHandler);
        spawnBucketSubscription = CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(Priority.NORMAL, this.spawnBucketEventHandler);
    }

    public void teardownSubscriptions() {
        activeShinyBoost = null;
        queuedShinyBoosts.clear();

        activeCatchBoost = null;
        queuedCatchBoosts.clear();

        activeExperienceBoost = null;
        queuedExperienceBoosts.clear();

        activeSpawnBucketBoost = null;
        queuedSpawnBucketBoosts.clear();

        if (shinySubscription != null) shinySubscription.unsubscribe();
        if (catchSubscription != null) catchSubscription.unsubscribe();
        if (experienceSubscription != null) experienceSubscription.unsubscribe();
        if (spawnBucketSubscription != null) spawnBucketSubscription.unsubscribe();
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
