package dev.matthiesen.cobblemon_boosters.common.interfaces.queue;

public record QueuePrioritySettings(
        boolean enabled,
        QueuePriorityMode mode,
        TimePriorityDirection timeDirection,
        boolean activePreemptionEnabled
) {
}