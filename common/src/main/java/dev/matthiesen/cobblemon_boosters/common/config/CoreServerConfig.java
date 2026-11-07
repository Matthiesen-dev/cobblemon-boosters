package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.cobblemon_boosters.common.interfaces.BoostDisplayMode;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.QueuePriorityMode;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.TimePriorityDirection;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CoreServerConfig {

    // General Configuration
    public ModConfigSpec.IntValue saveIntervalTicks;
    public ModConfigSpec.BooleanValue verboseCacheLogging;
    public ModConfigSpec.BooleanValue boosterLifecycleDebug;
    public ModConfigSpec.EnumValue<BoostDisplayMode> displayMode;
    public ModConfigSpec.BooleanValue queuePriorityEnabled;
    public ModConfigSpec.EnumValue<QueuePriorityMode> queuePriorityMode;
    public ModConfigSpec.EnumValue<TimePriorityDirection> timePriorityDirection;
    public ModConfigSpec.BooleanValue activePreemptionEnabled;

    public CoreServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("General Configuration").push("general");
        saveIntervalTicks = builder.comment("The interval in ticks at which the server saves data.")
                .defineInRange("saveIntervalTicks", 600, 1, Integer.MAX_VALUE);
        verboseCacheLogging = builder.comment("Enable verbose logging for cache operations.")
                .define("verboseCacheLogging", false);
        boosterLifecycleDebug = builder.comment("Temporary debug logging for boost lifecycle transitions.")
                .define("boosterLifecycleDebug", false);
        displayMode = builder.comment("The display mode for the booster UI.")
                .defineEnum("displayMode", BoostDisplayMode.BOSSBAR);
        queuePriorityEnabled = builder.comment("Enable queue priority system.")
                .define("queuePriorityEnabled", true);
        queuePriorityMode = builder.comment("The mode for queue priority.")
                .defineEnum("queuePriorityMode", QueuePriorityMode.FIFO);
        timePriorityDirection = builder.comment("The direction for time-based priority.")
                .defineEnum("timePriorityDirection", TimePriorityDirection.SHORTEST_FIRST);
        activePreemptionEnabled = builder.comment("Enable active preemption in the queue.")
                .define("activePreemptionEnabled", false);
        builder.pop(); // Closes "general" block
    }
}
