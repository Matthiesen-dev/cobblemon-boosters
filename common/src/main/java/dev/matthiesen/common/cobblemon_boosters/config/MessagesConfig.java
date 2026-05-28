package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.BossEvent;

public class MessagesConfig {
    @SerializedName("messages")
    public GeneralMessagesConfig messages = new GeneralMessagesConfig();

    public static class GeneralMessagesConfig {
        @SerializedName("prefix")
        public String prefix = "&7[&6Boosters&7]";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% &aReloaded the config!";

        @SerializedName("shinyMessages")
        public ShinyMessagesConfig shinyMessages = new ShinyMessagesConfig();

        @SerializedName("catchBoostMessages")
        public CatchBoostMessagesConfig catchBoostMessages = new CatchBoostMessagesConfig();

        @SerializedName("experienceBoostMessages")
        public ExperienceBoostMessagesConfig experienceBoostMessages = new ExperienceBoostMessagesConfig();

        @SerializedName("spawnBucketBoostMessages")
        public SpawnBucketBoostMessagesConfig spawnBucketBoostMessages = new SpawnBucketBoostMessagesConfig();
    }

    public static class ShinyMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.YELLOW;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "&6%multiplier%x Shiny Boost &7| &a%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% &aThere are currently no active Shiny Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% &aStarted a Shiny %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% &aAdded a Shiny %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% &aStopped the current Shiny boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% &aCleared the Shiny queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% &f&lMultiplier: &r&6%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% &aThere are currently no Shiny Boosts in the queue!";
    }

    public static class CatchBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.PURPLE;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "&d%multiplier%x Catch Boost &7| &a%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% &aThere are currently no active Catch Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% &aStarted a Catch %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% &aAdded a Catch %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% &aStopped the current Catch boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% &aCleared the Catch queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% &f&lMultiplier: &r&d%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% &aThere are currently no Catch Boosts in the queue!";
    }

    public static class ExperienceBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.GREEN;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "&a%multiplier%x Experience Boost &7| &a%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% &aThere are currently no active Experience Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% &aStarted a Experience %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% &aAdded a Experience %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% &aStopped the current Experience boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% &aCleared the Experience queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% &f&lMultiplier: &r&a%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% &aThere are currently no Experience Boosts in the queue!";
    }

    public static class SpawnBucketBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.BLUE;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "&b%bucket% Spawn Bucket Boost &7| &a%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% &aThere are currently no active Spawn Bucket Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% &aStarted a %bucket% Spawn Bucket Boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% &aAdded a %bucket% Spawn Bucket Boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% &aStopped the current Spawn Bucket boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% &aCleared the Spawn Bucket queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% &f&lBucket: &r&b%bucket% &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% &aThere are currently no Spawn Bucket Boosts in the queue!";
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
