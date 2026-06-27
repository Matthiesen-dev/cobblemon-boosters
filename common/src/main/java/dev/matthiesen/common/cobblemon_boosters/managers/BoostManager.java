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
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.config.CoreConfig;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.cobblemon_boosters.utils.SpawnBucketOverrideSelector;
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
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().activeShinyBoost,
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().queuedShinyBoosts
            );
    private static final BoostRecord<CatchBoost, PokemonCatchRateEvent> CATCH_RECORD =
            new BoostRecord<>(
                    CatchBoost.class,
                    CobblemonEvents.POKEMON_CATCH_RATE,
                    (boost, event) -> {
                        float baseCatchRate = event.getCatchRate();
                        event.setCatchRate(Math.min(baseCatchRate * boost.getMultiplier(), 255F));
                    },
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().activeCatchBoost,
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().queuedCatchBoosts
            );
    private static final BoostRecord<ExperienceBoost, ExperienceGainedEvent.Pre> EXPERIENCE_RECORD =
            new BoostRecord<>(
                    ExperienceBoost.class,
                    CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE,
                    (boost, event) -> {
                        int exp = event.getExperience();
                        event.setExperience(Math.round(exp * boost.getMultiplier()));
                    },
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().activeExperienceBoost,
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().queuedExperienceBoosts
            );
    private static final BoostRecord<SpawnBucketBoost, SpawnBucketChosenEvent> SPAWN_BUCKET_RECORD =
            new BoostRecord<>(
                    SpawnBucketBoost.class,
                    CobblemonEvents.SPAWN_BUCKET_CHOSEN,
                    (boost, event) -> {
                        SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, boost);
                        event.setBucket(newBucket);
                    },
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().activeSpawnBucketBoost,
                    CobblemonBoosters.INSTANCE.getCacheConfigManager().getConfig().queuedSpawnBucketBoosts
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
            SHINY_RECORD.addPlayer(player);
            CATCH_RECORD.addPlayer(player);
            EXPERIENCE_RECORD.addPlayer(player);
            SPAWN_BUCKET_RECORD.addPlayer(player);
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.createErrorLog("Error appending player to boosts in BoostManager", e);
        }
    }

    public static void clearPlayer(ServerPlayer player) {
        try {
            SHINY_RECORD.clearPlayer(player);
            CATCH_RECORD.clearPlayer(player);
            EXPERIENCE_RECORD.clearPlayer(player);
            SPAWN_BUCKET_RECORD.clearPlayer(player);
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.createErrorLog("Error clearing player from boosts in BoostManager", e);
        }
    }

    public static void setupSubscriptions() {
        try {
            SHINY_RECORD.setupSubscription();
            CATCH_RECORD.setupSubscription();
            EXPERIENCE_RECORD.setupSubscription();
            SPAWN_BUCKET_RECORD.setupSubscription();
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.createErrorLog("Error setting up event subscriptions in BoostManager", e);
        }
    }

    public static void teardownSubscriptions() {
        try {
            SHINY_RECORD.teardown();
            CATCH_RECORD.teardown();
            EXPERIENCE_RECORD.teardown();
            SPAWN_BUCKET_RECORD.teardown();
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.createErrorLog("Error tearing down event subscriptions in BoostManager", e);
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
                if (CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    Constants.createInfoLog("Re-Initializing BoostRecord for " + boostClass.getSimpleName() + " with non-empty queue. Queue size: " + queue.size());
                }
            } else {
                this.queue = new LinkedList<>();
            }
            if (activeBoost != null) {
                this.active = activeBoost;
                if (CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    Constants.createInfoLog("Re-Initialized BoostRecord for " + boostClass.getSimpleName() + " with active boost: " + activeBoost.getMultiplier());
                }
            } else {
                this.active = null;
            }
            this.manager = new IBoostManager<>(() -> this.active, b -> this.active = b, this.queue);
            this.subscription = null;
        }

        public void addPlayer(ServerPlayer player) {
            if (this.active != null) this.active.getBossBar().addPlayer(player);
        }

        public void clearPlayer(ServerPlayer player) {
            if (this.active != null) this.active.getBossBar().removePlayer(player);
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
            var server = MatthiesenLibApi.getMinecraftServer();
            if (server != null) {
                oldActive.getBossBar().hideBossBarFromPlayerList(server.getPlayerList());
            }
            this.setActive(newActive);
            if (server != null) {
                newActive.getBossBar().showBossBarFromPlayerList(server.getPlayerList());
            }
        }
    }

    private static QueuePrioritySettings getQueuePrioritySettings() {
        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
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
