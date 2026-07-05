package dev.matthiesen.cobblemon_boosters.common.services.managers;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.EventObservable;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.config.CoreConfig;
import dev.matthiesen.cobblemon_boosters.common.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.SpawnBucketOverrideSelector;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BoostManager {
    private static final BoostRecord<ShinyBoost, ShinyChanceCalculationEvent> SHINY_RECORD =
            new BoostRecord<>(
                    ShinyBoost.class,
                    CobblemonEvents.SHINY_CHANCE_CALCULATION,
                    (boost, event) ->
                            event.addModificationFunction(((rate, player, pokemon) ->
                                    Math.max(rate / boost.getMultiplier(), 1))),
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().activeShinyBoost,
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().queuedShinyBoosts
            );
    private static final BoostRecord<CatchBoost, PokemonCatchRateEvent> CATCH_RECORD =
            new BoostRecord<>(
                    CatchBoost.class,
                    CobblemonEvents.POKEMON_CATCH_RATE,
                    (boost, event) -> {
                        float baseCatchRate = event.getCatchRate();
                        event.setCatchRate(Math.min(baseCatchRate * boost.getMultiplier(), 255F));
                    },
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().activeCatchBoost,
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().queuedCatchBoosts
            );
    private static final BoostRecord<ExperienceBoost, ExperienceGainedEvent.Pre> EXPERIENCE_RECORD =
            new BoostRecord<>(
                    ExperienceBoost.class,
                    CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE,
                    (boost, event) -> {
                        int exp = event.getExperience();
                        event.setExperience(Math.round(exp * boost.getMultiplier()));
                    },
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().activeExperienceBoost,
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().queuedExperienceBoosts
            );
    private static final BoostRecord<SpawnBucketBoost, SpawnBucketChosenEvent> SPAWN_BUCKET_RECORD =
            new BoostRecord<>(
                    SpawnBucketBoost.class,
                    CobblemonEvents.SPAWN_BUCKET_CHOSEN,
                    (boost, event) -> {
                        SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, boost);
                        event.setBucket(newBucket);
                    },
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().activeSpawnBucketBoost,
                    CobblemonBoostersCommon.INSTANCE.getCacheConfigManager().getConfig().queuedSpawnBucketBoosts
            );

    public static void init() {}

    public static void reapplyQueuePriorities() {
        SHINY_RECORD.getManager().refreshQueuePriority();
        CATCH_RECORD.getManager().refreshQueuePriority();
        EXPERIENCE_RECORD.getManager().refreshQueuePriority();
        SPAWN_BUCKET_RECORD.getManager().refreshQueuePriority();
    }

    public static IBoostManager<ShinyBoost> getShinyBoostManager() {
        return SHINY_RECORD.getManager();
    }

    public static IBoostManager<CatchBoost> getCatchBoostManager() {
        return CATCH_RECORD.getManager();
    }

    public static IBoostManager<ExperienceBoost> getExperienceBoostManager() {
        return EXPERIENCE_RECORD.getManager();
    }

    public static IBoostManager<SpawnBucketBoost> getSpawnBucketBoostManager() {
        return SPAWN_BUCKET_RECORD.getManager();
    }

    public static void appendPlayer(ServerPlayer player) {
        try {
            ServiceManager.getDisplayService().onPlayerJoin(player);
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Error appending player to boosts in BoostManager", e);
        }
    }

    public static void clearPlayer(ServerPlayer player) {
        try {
            ServiceManager.getDisplayService().onPlayerLeave(player);
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Error clearing player from boosts in BoostManager", e);
        }
    }

    /** All currently-active boosts in a stable order (shiny, catch, experience, spawn bucket). */
    public static List<IBoost> getActiveBoosts() {
        List<IBoost> active = new ArrayList<>(4);
        addIfActive(active, SHINY_RECORD);
        addIfActive(active, CATCH_RECORD);
        addIfActive(active, EXPERIENCE_RECORD);
        addIfActive(active, SPAWN_BUCKET_RECORD);
        return active;
    }

    private static void addIfActive(List<IBoost> target, BoostRecord<? extends IBoost, ?> record) {
        IBoost active = record.getManager().getActive();
        if (active != null) {
            target.add(active);
        }
    }

    public static void setupSubscriptions() {
        try {
            SHINY_RECORD.setupSubscription();
            CATCH_RECORD.setupSubscription();
            EXPERIENCE_RECORD.setupSubscription();
            SPAWN_BUCKET_RECORD.setupSubscription();
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Error setting up event subscriptions in BoostManager", e);
        }
    }

    public static void teardownSubscriptions() {
        try {
            SHINY_RECORD.teardown();
            CATCH_RECORD.teardown();
            EXPERIENCE_RECORD.teardown();
            SPAWN_BUCKET_RECORD.teardown();
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Error tearing down event subscriptions in BoostManager", e);
        }
    }

    public static class BoostRecord<T extends IBoost, K> {
        private final EventObservable<K> observable;
        private ObservableSubscription<K> subscription;
        private final BiConsumer<T, K> eventHandler;
        private T active;
        private final Queue<T> queue;
        private final IBoostManager<T> manager;

        @SuppressWarnings("unused")
        public BoostRecord(Class<T> boostClass, EventObservable<K> observable, BiConsumer<T, K> eventHandler, T activeBoost, List<T> queue) {
            this.observable = observable;
            this.eventHandler = eventHandler;
            if (!queue.isEmpty()) {
                this.queue = new LinkedList<>(queue);
                if (CobblemonBoostersCommon.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    CobblemonBoostersCommon.INSTANCE.createInfoLog("Re-Initializing BoostRecord for " + boostClass.getSimpleName() + " with non-empty queue. Queue size: " + queue.size());
                }
            } else {
                this.queue = new LinkedList<>();
            }
            if (activeBoost != null) {
                this.active = activeBoost;
                if (CobblemonBoostersCommon.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    CobblemonBoostersCommon.INSTANCE.createInfoLog("Re-Initialized BoostRecord for " + boostClass.getSimpleName() + " with active boost: " + activeBoost.getMultiplier());
                }
            } else {
                this.active = null;
            }
            this.manager = new IBoostManager<>(() -> this.active, b -> this.active = b, this.queue);
            this.subscription = null;
        }

        public IBoostManager<T> getManager() {
            return this.manager;
        }

        public void setupSubscription() {
            this.subscription = this.observable.subscribe(Priority.NORMAL, event -> {
                T activeBoost = this.manager.getActive();
                if (activeBoost != null) {
                    this.eventHandler.accept(activeBoost, event);
                }
            });
        }

        public void teardown() {
            this.active = null;
            this.queue.clear();
            if (this.subscription != null) this.subscription.unsubscribe();
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
            return this.getter.get();
        }

        public void setActive(T boost) {
            this.setter.accept(boost);
        }

        public Queue<T> getQueue() {
            return this.queue;
        }

        public List<T> getQueueList() {
            return this.queue.stream().toList();
        }

        public void setQueue(Queue<T> replacement) {
            this.queue.clear();
            this.queue.addAll(replacement);
        }

        public void refreshQueuePriority() {
            QueuePrioritySettings settings = getQueuePrioritySettings();
            if (!settings.enabled() || settings.mode() == QueuePriorityMode.FIFO || this.queue.isEmpty()) {
                return;
            }

            List<T> sorted = new ArrayList<>(this.queue);
            sorted.sort(priorityComparator(settings));

            T active = this.getActive();
            if (settings.activePreemptionEnabled() && active != null && !sorted.isEmpty()) {
                T candidate = sorted.getFirst();
                if (hasHigherPriority(candidate, active, settings)) {
                    sorted.removeFirst();
                    sorted.add(active);
                    sorted.sort(priorityComparator(settings));
                    this.setQueue(new LinkedList<>(sorted));
                    switchActiveBoost(active, candidate);
                    return;
                }
            }

            this.setQueue(new LinkedList<>(sorted));
        }

        public void appendToQueue(T boost) {
            QueuePrioritySettings settings = getQueuePrioritySettings();
            if (!settings.enabled() || settings.mode() == QueuePriorityMode.FIFO) {
                this.queue.add(boost);
                return;
            }

            List<T> sorted = new ArrayList<>(this.queue);
            sorted.add(boost);
            sorted.sort(priorityComparator(settings));

            T active = this.getActive();
            if (settings.activePreemptionEnabled() && active != null && !sorted.isEmpty()) {
                T candidate = sorted.getFirst();
                if (hasHigherPriority(candidate, active, settings)) {
                    sorted.removeFirst();
                    sorted.add(active);
                    sorted.sort(priorityComparator(settings));
                    this.setQueue(new LinkedList<>(sorted));
                    switchActiveBoost(active, candidate);
                    return;
                }
            }

            this.setQueue(new LinkedList<>(sorted));
        }

        private Comparator<T> priorityComparator(QueuePrioritySettings settings) {
            return (left, right) -> comparePriority(left, right, settings);
        }

        private int comparePriority(T left, T right, QueuePrioritySettings settings) {
            return switch (settings.mode()) {
                case FIFO -> 0;
                case MULTIPLIER -> Float.compare(right.getMultiplier(), left.getMultiplier());
                case TIME_REMAINING -> {
                    if (settings.timeDirection() == TimePriorityDirection.LONGEST_FIRST) {
                        yield Long.compare(right.getTimeRemaining(), left.getTimeRemaining());
                    }
                    yield Long.compare(left.getTimeRemaining(), right.getTimeRemaining());
                }
            };
        }

        private boolean hasHigherPriority(T candidate, T active, QueuePrioritySettings settings) {
            if (settings.mode() == QueuePriorityMode.FIFO) {
                return false;
            }
            return comparePriority(candidate, active, settings) < 0;
        }

        private void switchActiveBoost(T oldActive, T newActive) {
            ServiceManager.getDisplayService().onBoostDeactivated(oldActive);
            this.setActive(newActive);
            ServiceManager.getDisplayService().onBoostActivated(newActive);
        }
    }

    private static QueuePrioritySettings getQueuePrioritySettings() {
        CoreConfig config = CobblemonBoostersCommon.INSTANCE.getCoreConfigManager().getConfig();
        if (config == null) {
            return QueuePrioritySettings.defaults();
        }

        return new QueuePrioritySettings(
                config.queuePriorityEnabled,
                QueuePriorityMode.fromString(config.queuePriorityMode),
                TimePriorityDirection.fromString(config.timePriorityDirection),
                config.activePreemptionEnabled
        );
    }

    public enum QueuePriorityMode {
        FIFO,
        MULTIPLIER,
        TIME_REMAINING;

        public static QueuePriorityMode fromString(String value) {
            if (value == null) {
                return FIFO;
            }

            String normalized = value.trim().toUpperCase(Locale.ROOT);
            for (QueuePriorityMode mode : values()) {
                if (mode.name().equals(normalized)) {
                    return mode;
                }
            }
            return FIFO;
        }
    }

    public enum TimePriorityDirection {
        SHORTEST_FIRST,
        LONGEST_FIRST;

        public static TimePriorityDirection fromString(String value) {
            if (value == null) {
                return SHORTEST_FIRST;
            }

            String normalized = value.trim().toUpperCase(Locale.ROOT);
            for (TimePriorityDirection direction : values()) {
                if (direction.name().equals(normalized)) {
                    return direction;
                }
            }
            return SHORTEST_FIRST;
        }
    }

    private record QueuePrioritySettings(
            boolean enabled,
            QueuePriorityMode mode,
            TimePriorityDirection timeDirection,
            boolean activePreemptionEnabled
    ) {
        private static QueuePrioritySettings defaults() {
            return new QueuePrioritySettings(false, QueuePriorityMode.FIFO, TimePriorityDirection.SHORTEST_FIRST, false);
        }
    }
}
