package dev.matthiesen.cobblemon_boosters.common.interfaces;

import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.QueuePriorityMode;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.QueuePrioritySettings;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.TimePriorityDirection;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import net.minecraft.commands.CommandSourceStack;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public interface IBoostController<T extends IBoost> {
    SupportedBoosterTypes getType();

    void register();

    BoosterGuiDefinition<T> getGuiDefinition();

    void queueResponseHandler(CommandContext<CommandSourceStack> ctx);
    void queueClearHandler(CommandContext<CommandSourceStack> ctx);

    void setupSubscriber();
    void teardownSubscriber();

    T getActiveBoost();
    void setActiveBoost(T boost);

    Queue<T> getBoostQueue();
    void setBoostQueue(Queue<T> boostQueue);
    void internal_addToQueue(T boost);

    DiscordEmbed getBoostStartEmbed();
    DiscordEmbed getBoostEndEmbed();

    default List<T> getBoostQueueAsList() {
        return List.copyOf(getBoostQueue());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    default void tickBoosts() {
        T activeBoost = getActiveBoost();
        var queue = getBoostQueue();

        if (activeBoost == null && queue.isEmpty()) return;

        if (activeBoost != null) {
            activeBoost.setTimeRemaining(activeBoost.getTimeRemaining() - 1);

            if (activeBoost.getTimeRemaining() > 0) {
                return;
            }

            T expiredBoost = activeBoost;
            T nextBoost = queue.poll();
            setActiveBoost(nextBoost);

            Set<String> loggedFailures = new HashSet<>();
            runSafely("deactivate", expiredBoost, loggedFailures, () ->
                    ServiceManager.getDisplayService().onBoostDeactivated(expiredBoost));
            runSafely("webhook-end", expiredBoost, loggedFailures, () ->
                    ServiceManager.getDiscordWebhookService().sendMessage(getBoostEndEmbed(), expiredBoost));

            if (nextBoost != null) {
                runSafely("activate", nextBoost, loggedFailures, () ->
                        ServiceManager.getDisplayService().onBoostActivated(nextBoost));
                runSafely("webhook-start", nextBoost, loggedFailures, () ->
                        ServiceManager.getDiscordWebhookService().sendMessage(getBoostStartEmbed(), nextBoost));
            }

            CacheServerConfig.setGlobalBoostData();
            return;
        }

        T nextBoost = queue.poll();
        if (nextBoost != null) {
            setActiveBoost(nextBoost);
            Set<String> loggedFailures = new HashSet<>();
            runSafely("activate", nextBoost, loggedFailures, () ->
                    ServiceManager.getDisplayService().onBoostActivated(nextBoost));
            runSafely("webhook-start", nextBoost, loggedFailures, () ->
                    ServiceManager.getDiscordWebhookService().sendMessage(getBoostStartEmbed(), nextBoost));
            CacheServerConfig.setGlobalBoostData();
        }
    }

    default void refreshQueuePriority() {
        QueuePrioritySettings settings = BoostersConfig.getQueuePrioritySettings();
        if (!settings.enabled() || settings.mode() == QueuePriorityMode.FIFO || getBoostQueue().isEmpty()) {
            return;
        }

        T activeBoost = getActiveBoost();
        List<T> sortedQueue = getBoostQueueAsList();
        sortedQueue.sort(priorityComparator(settings));

        handleActivePreemption(settings, activeBoost, sortedQueue);
        setBoostQueue(new LinkedList<>(sortedQueue));
    }

    default void appendToQueue(T boost) {
        QueuePrioritySettings settings = BoostersConfig.getQueuePrioritySettings();
        if (!settings.enabled() || settings.mode() == QueuePriorityMode.FIFO || getBoostQueue().isEmpty()) {
            internal_addToQueue(boost);
            return;
        }

        T activeBoost = getActiveBoost();
        List<T> sortedQueue = getBoostQueueAsList();
        sortedQueue.add(boost);
        sortedQueue.sort(priorityComparator(settings));

        handleActivePreemption(settings, activeBoost, sortedQueue);
        setBoostQueue(new LinkedList<>(sortedQueue));
    }

    default void handleActivePreemption(QueuePrioritySettings settings, T activeBoost, List<T> sortedQueue) {
        if (settings.activePreemptionEnabled() && activeBoost != null && !sortedQueue.isEmpty()) {
            T candidateBoost = sortedQueue.getFirst();
            if (hasHigherPriority(candidateBoost, activeBoost, settings)) {
                sortedQueue.removeFirst();
                sortedQueue.add(activeBoost);
                sortedQueue.sort(priorityComparator(settings));
                setBoostQueue(new LinkedList<>(sortedQueue));
                switchActiveBoost(activeBoost, candidateBoost);
            }
        }
    }

    default Comparator<T> priorityComparator(QueuePrioritySettings settings) {
        return (left, right) -> comparePriority(left, right, settings);
    }

    default int comparePriority(T left, T right, QueuePrioritySettings settings) {
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

    default boolean hasHigherPriority(T candidate, T active, QueuePrioritySettings settings) {
        if (settings.mode() == QueuePriorityMode.FIFO) {
            return false;
        }
        return comparePriority(candidate, active, settings) < 0;
    }

    default void switchActiveBoost(T activeBoost, T candidateBoost) {
        setActiveBoost(candidateBoost);

        Set<String> loggedFailures = new HashSet<>();
        runSafely("deactivate", activeBoost, loggedFailures, () ->
                ServiceManager.getDisplayService().onBoostDeactivated(activeBoost));
        runSafely("activate", candidateBoost, loggedFailures, () ->
                ServiceManager.getDisplayService().onBoostActivated(candidateBoost));
        CacheServerConfig.setGlobalBoostData();
    }

    default void runSafely(String action, @SuppressWarnings("unused") T boost, Set<String> loggedFailures, Runnable task) {
        try {
            task.run();
        } catch (RuntimeException e) {
            String key = getType() + ":" + action;
            if (loggedFailures.add(key)) {
                CobblemonBoostersCommon.INSTANCE.createErrorLog(
                        "Failed boost lifecycle action [type=" + getType() + ", action=" + action + "]",
                        e
                );
            }
        }
    }
}
