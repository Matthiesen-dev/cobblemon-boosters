package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.BossEvent;

@SuppressWarnings("unused")
public final class MessagesConfig {
    @SerializedName("messages")
    public GeneralMessagesConfig messages = new GeneralMessagesConfig();

    public static class GeneralMessagesConfig {
        @SerializedName("prefix")
        public String prefix = "&7[&6Boosters&7]";

        @SerializedName("sidebarTitle")
        public String sidebarTitle = "&6&lBoosters";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% &aReloaded the config!";

        @SerializedName("queuePriorityStatus")
        public String queuePriorityStatus = "%prefix% &7Queue priority -> enabled: &f%s&7, mode: &f%s&7, timeDirection: &f%s&7, preemption: &f%s";

        @SerializedName("queuePriorityUpdated")
        public String queuePriorityUpdated = "%prefix% &aUpdated queue priority setting: &f%s";

        @SerializedName("shinyMessages")
        public BoostMessagesConfig shinyMessages = new BoostMessagesConfig(
                BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS,
                "&6%multiplier%x Shiny Boost &7| &a%time_remaining% Remaining",
                "%prefix% &aThere are currently no active Shiny Boosts!",
                "%prefix% &aStarted an Shiny %multiplier%x boost for %duration%!",
                "%prefix% &aAdded an Shiny %multiplier%x boost with a %duration% duration to queue!",
                "%prefix% &aStopped the current Shiny boost!",
                "%prefix% &aCleared the Shiny queued boosts!",
                "%prefix% &f&lMultiplier: &r&6%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%",
                "%prefix% &aThere are currently no Shiny Boosts in the queue!",
                "&7%time_remaining_short2% &6Shiny &f%multiplier%x"
        );

        @SerializedName("catchBoostMessages")
        public BoostMessagesConfig catchBoostMessages = new BoostMessagesConfig(
                BossEvent.BossBarColor.PURPLE,
                BossEvent.BossBarOverlay.PROGRESS,
                "&d%multiplier%x Catch Boost &7| &a%time_remaining% Remaining",
                "%prefix% &aThere are currently no active Catch Boosts!",
                "%prefix% &aStarted an Catch %multiplier%x boost for %duration%!",
                "%prefix% &aAdded an Catch %multiplier%x boost with a %duration% duration to queue!",
                "%prefix% &aStopped the current Catch boost!",
                "%prefix% &aCleared the Catch queued boosts!",
                "%prefix% &f&lMultiplier: &r&d%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%",
                "%prefix% &aThere are currently no Catch Boosts in the queue!",
                "&7%time_remaining_short2% &dCatch &f%multiplier%x"
        );

        @SerializedName("experienceBoostMessages")
        public BoostMessagesConfig experienceBoostMessages = new BoostMessagesConfig(
                BossEvent.BossBarColor.GREEN,
                BossEvent.BossBarOverlay.PROGRESS,
                "&a%multiplier%x Experience Boost &7| &a%time_remaining% Remaining",
                "%prefix% &aThere are currently no active Experience Boosts!",
                "%prefix% &aStarted an Experience %multiplier%x boost for %duration%!",
                "%prefix% &aAdded an Experience %multiplier%x boost with a %duration% duration to queue!",
                "%prefix% &aStopped the current Experience boost!",
                "%prefix% &aCleared the Experience queued boosts!",
                "%prefix% &f&lMultiplier: &r&a%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%",
                "%prefix% &aThere are currently no Experience Boosts in the queue!",
                "&7%time_remaining_short2% &aExp &f%multiplier%x"
        );

        @SerializedName("spawnBucketBoostMessages")
        public BoostMessagesConfig spawnBucketBoostMessages = new BoostMessagesConfig(
                BossEvent.BossBarColor.BLUE,
                BossEvent.BossBarOverlay.PROGRESS,
                "&b%bucket% Spawn Bucket Boost &7| &a%time_remaining% Remaining",
                "%prefix% &aThere are currently no active Spawn Bucket Boosts!",
                "%prefix% &aStarted a %bucket% Spawn Bucket boost for %duration%!",
                "%prefix% &aAdded a %bucket% Spawn Bucket Boost with a %duration% duration to queue!",
                "%prefix% &aStopped the current Spawn Bucket boost!",
                "%prefix% &aCleared the Spawn Bucket queued boosts!",
                "%prefix% &f&lBucket: &r&b%bucket% &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%",
                "%prefix% &aThere are currently no Spawn Bucket Boosts in the queue!",
                "&7%time_remaining_short2% &b%bucket% &f%multiplier%x"
        );
    }

    public static class BoostMessagesConfig {
        public BossEvent.BossBarColor barColor;
        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay;
        @SerializedName("barText")
        public String barText;
        @SerializedName("noActiveBoosts")
        public String noActiveBoosts;
        @SerializedName("boostStarted")
        public String boostStarted;
        @SerializedName("boostAddedToQueue")
        public String boostAddedToQueued;
        @SerializedName("boostStopped")
        public String boostStopped;
        @SerializedName("boostQueueCleared")
        public String boostQueueCleared;
        @SerializedName("boostInfo")
        public String boostInfo;
        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts;

        // Optional. Falls back to barText when null/blank so pre-existing configs keep working.
        @SerializedName("sidebarLine")
        public String sidebarLine = null;

        public BoostMessagesConfig(
                BossEvent.BossBarColor barColor,
                BossEvent.BossBarOverlay barOverlay,
                String barText,
                String noActiveBoosts,
                String boostStarted,
                String boostAddedToQueued,
                String boostStopped,
                String boostQueueCleared,
                String boostInfo,
                String noQueuedBoosts,
                String sidebarLine
        ) {
            this.barColor = barColor;
            this.barOverlay = barOverlay;
            this.barText = barText;
            this.noActiveBoosts = noActiveBoosts;
            this.boostStarted = boostStarted;
            this.boostAddedToQueued = boostAddedToQueued;
            this.boostStopped = boostStopped;
            this.boostQueueCleared = boostQueueCleared;
            this.boostInfo = boostInfo;
            this.noQueuedBoosts = noQueuedBoosts;
            this.sidebarLine = sidebarLine;
        }
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
