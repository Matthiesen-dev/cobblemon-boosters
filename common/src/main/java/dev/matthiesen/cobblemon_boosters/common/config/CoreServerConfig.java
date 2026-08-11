package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.cobblemon_boosters.common.interfaces.BoostDisplayMode;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CoreServerConfig {

    // General Configuration
    public ModConfigSpec.IntValue saveIntervalTicks;
    public ModConfigSpec.BooleanValue verboseCacheLogging;
    public ModConfigSpec.EnumValue<BoostDisplayMode> displayMode;
    public ModConfigSpec.BooleanValue queuePriorityEnabled;
    public ModConfigSpec.EnumValue<BoostManager.QueuePriorityMode> queuePriorityMode;
    public ModConfigSpec.EnumValue<BoostManager.TimePriorityDirection> timePriorityDirection;
    public ModConfigSpec.BooleanValue activePreemptionEnabled;

    // Messages Configuration
    public ModConfigSpec.ConfigValue<String> messages_prefix;
    public ModConfigSpec.ConfigValue<String> messages_sidebarTitle;
    public ModConfigSpec.ConfigValue<String> messages_commandReload;
    public ModConfigSpec.ConfigValue<String> messages_queuePriorityStatus;
    public ModConfigSpec.ConfigValue<String> messages_queuePriorityUpdated;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_shiny_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_shiny_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_shiny_barText;
    public ModConfigSpec.ConfigValue<String> messages_shiny_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_shiny_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_shiny_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_catch_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_catch_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_catch_barText;
    public ModConfigSpec.ConfigValue<String> messages_catch_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_catch_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_catch_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_experience_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_experience_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_experience_barText;
    public ModConfigSpec.ConfigValue<String> messages_experience_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_experience_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_experience_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_spawnBucket_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_spawnBucket_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_barText;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_noQueuedBoosts;

    public CoreServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("General Configuration").push("general");
        saveIntervalTicks = builder.comment("The interval in ticks at which the server saves data.")
                .defineInRange("saveIntervalTicks", 600, 1, Integer.MAX_VALUE);
        verboseCacheLogging = builder.comment("Enable verbose logging for cache operations.")
                .define("verboseCacheLogging", false);
        displayMode = builder.comment("The display mode for the booster UI.")
                .defineEnum("displayMode", BoostDisplayMode.BOSSBAR);
        queuePriorityEnabled = builder.comment("Enable queue priority system.")
                .define("queuePriorityEnabled", true);
        queuePriorityMode = builder.comment("The mode for queue priority.")
                .defineEnum("queuePriorityMode", BoostManager.QueuePriorityMode.FIFO);
        timePriorityDirection = builder.comment("The direction for time-based priority.")
                .defineEnum("timePriorityDirection", BoostManager.TimePriorityDirection.SHORTEST_FIRST);
        activePreemptionEnabled = builder.comment("Enable active preemption in the queue.")
                .define("activePreemptionEnabled", false);
        builder.pop(); // Closes "general" block

        builder.comment("Messages Configuration").push("messages");
        messages_prefix = builder.comment("The prefix for all messages.")
                .define("prefix", "&7[&6Boosters&7]");
        messages_sidebarTitle = builder.comment("The title for the sidebar display.")
                .define("sidebarTitle", "&6&lBoosters");
        messages_commandReload = builder.comment("Message displayed when the booster services are reloaded.")
                .define("commandReload", "%prefix% &aReloaded Cobblemon Boosters!");
        messages_queuePriorityStatus = builder.comment("Message displayed for queue priority status.")
                .define("queuePriorityStatus", "%prefix% &7Queue priority -> enabled: &f%s&7, mode: &f%s&7, timeDirection: &f%s&7, preemption: &f%s");
        messages_queuePriorityUpdated = builder.comment("Message displayed when queue priority is updated.")
                .define("queuePriorityUpdated", "%prefix% &aUpdated queue priority setting: &f%s");

        builder.comment("Shiny Boost Messages").push("shinyMessages");
        messages_shiny_barColor = builder.comment("The color of the boss bar for shiny boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.YELLOW);
        messages_shiny_barOverlay = builder.comment("The overlay style of the boss bar for shiny boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_shiny_barText = builder.comment("The text displayed on the boss bar for shiny boosts.")
                .define("barText", "&6%multiplier%x Shiny Boost &7| &a%time_remaining% Remaining");
        messages_shiny_sidebarLine = builder.comment("The line text for the sidebar display for shiny boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &6Shiny &f%multiplier%x");
        messages_shiny_noActiveBoosts = builder.comment("Message displayed when there are no active shiny boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Shiny Boosts!");
        messages_shiny_boostStarted = builder.comment("Message displayed when a shiny boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Shiny %multiplier%x boost for %duration%!");
        messages_shiny_boostAddedToQueue = builder.comment("Message displayed when a shiny boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Shiny %multiplier%x boost with a %duration% duration to queue!");
        messages_shiny_boostStopped = builder.comment("Message displayed when a shiny boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Shiny boost!");
        messages_shiny_boostQueueCleared = builder.comment("Message displayed when the shiny boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Shiny queued boosts!");
        messages_shiny_boostInfo = builder.comment("Message displayed for shiny boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&6%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_shiny_noQueuedBoosts = builder.comment("Message displayed when there are no queued shiny boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Shiny Boosts in the queue!");
        builder.pop(); // Closes "messages.shinyMessages" block

        builder.comment("Catch Boost Messages").push("catchBoostMessages");
        messages_catch_barColor = builder.comment("The color of the boss bar for catch boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.PURPLE);
        messages_catch_barOverlay = builder.comment("The overlay style of the boss bar for catch boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_catch_barText = builder.comment("The text displayed on the boss bar for catch boosts.")
                .define("barText", "&d%multiplier%x Catch Boost &7| &a%time_remaining% Remaining");
        messages_catch_sidebarLine = builder.comment("The line text for the sidebar display for catch boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &dCatch &f%multiplier%x");
        messages_catch_noActiveBoosts = builder.comment("Message displayed when there are no active catch boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Catch Boosts!");
        messages_catch_boostStarted = builder.comment("Message displayed when a catch boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Catch %multiplier%x boost for %duration%!");
        messages_catch_boostAddedToQueue = builder.comment("Message displayed when a catch boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Catch %multiplier%x boost with a %duration% duration to queue!");
        messages_catch_boostStopped = builder.comment("Message displayed when a catch boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Catch boost!");
        messages_catch_boostQueueCleared = builder.comment("Message displayed when the catch boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Catch queued boosts!");
        messages_catch_boostInfo = builder.comment("Message displayed for catch boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&d%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_catch_noQueuedBoosts = builder.comment("Message displayed when there are no queued catch boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Catch Boosts in the queue!");
        builder.pop(); // Closes "messages.catchBoostMessages" block

        builder.comment("Experience Boost Messages").push("experienceBoostMessages");
        messages_experience_barColor = builder.comment("The color of the boss bar for experience boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.GREEN);
        messages_experience_barOverlay = builder.comment("The overlay style of the boss bar for experience boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_experience_barText = builder.comment("The text displayed on the boss bar for experience boosts.")
                .define("barText", "&a%multiplier%x Experience Boost &7| &a%time_remaining% Remaining");
        messages_experience_sidebarLine = builder.comment("The line text for the sidebar display for experience boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &aExp &f%multiplier%x");
        messages_experience_noActiveBoosts = builder.comment("Message displayed when there are no active experience boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Experience Boosts!");
        messages_experience_boostStarted = builder.comment("Message displayed when an experience boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Experience %multiplier%x boost for %duration%!");
        messages_experience_boostAddedToQueue = builder.comment("Message displayed when an experience boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Experience %multiplier%x boost with a %duration% duration to queue!");
        messages_experience_boostStopped = builder.comment("Message displayed when an experience boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Experience boost!");
        messages_experience_boostQueueCleared = builder.comment("Message displayed when the experience boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Experience queued boosts!");
        messages_experience_boostInfo = builder.comment("Message displayed for experience boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&a%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_experience_noQueuedBoosts = builder.comment("Message displayed when there are no queued experience boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Experience Boosts in the queue!");
        builder.pop(); // Closes "messages.experienceBoostMessages" block

        builder.comment("Spawn Bucket Boost Messages").push("spawnBucketBoostMessages");
        messages_spawnBucket_barColor = builder.comment("The color of the boss bar for spawn bucket boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.BLUE);
        messages_spawnBucket_barOverlay = builder.comment("The overlay style of the boss bar for spawn bucket boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_spawnBucket_barText = builder.comment("The text displayed on the boss bar for spawn bucket boosts.")
                .define("barText", "&b%bucket% Spawn Bucket Boost &7| &a%time_remaining% Remaining");
        messages_spawnBucket_sidebarLine = builder.comment("The line text for the sidebar display for spawn bucket boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &b%bucket% &f%multiplier%x");
        messages_spawnBucket_noActiveBoosts = builder.comment("Message displayed when there are no active spawn bucket boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Spawn Bucket Boosts!");
        messages_spawnBucket_boostStarted = builder.comment("Message displayed when a spawn bucket boost is started.")
                .define("boostStarted", "%prefix% &aStarted a %bucket% Spawn Bucket boost for %duration%!");
        messages_spawnBucket_boostAddedToQueue = builder.comment("Message displayed when a spawn bucket boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded a %bucket% Spawn Bucket Boost with a %duration% duration to queue!");
        messages_spawnBucket_boostStopped = builder.comment("Message displayed when a spawn bucket boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Spawn Bucket boost!");
        messages_spawnBucket_boostQueueCleared = builder.comment("Message displayed when the spawn bucket boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Spawn Bucket queued boosts!");
        messages_spawnBucket_boostInfo = builder.comment("Message displayed for spawn bucket boost information.")
                .define("boostInfo", "%prefix% &f&lBucket: &r&b%bucket% &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_spawnBucket_noQueuedBoosts = builder.comment("Message displayed when there are no queued spawn bucket boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Spawn Bucket Boosts in the queue!");
        builder.pop(); // Closes "messages.spawnBucketBoostMessages" block

        builder.pop(); // Closes "messages" block
    }
}
