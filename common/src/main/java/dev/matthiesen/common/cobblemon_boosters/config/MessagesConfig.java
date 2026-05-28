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
        public String prefix = "<gray>[<gold>Boosters<gray>]";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% <green>Reloaded the config!";

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
        public String barText = "<gold>%multiplier%x Shiny Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Shiny Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Shiny %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Shiny %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Shiny boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Shiny queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><gold>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Shiny Boosts in the queue!";
    }

    public static class CatchBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.PURPLE;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<light_purple>%multiplier%x Catch Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Catch Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Catch %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Catch %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Catch boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Catch queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><light_purple>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Catch Boosts in the queue!";
    }

    public static class ExperienceBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.GREEN;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<green>%multiplier%x Experience Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Experience Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Experience %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Experience %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Experience boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Experience queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><green>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Experience Boosts in the queue!";
    }

    public static class SpawnBucketBoostMessagesConfig {
        @SerializedName("barColor")
        public BossEvent.BossBarColor barColor = BossEvent.BossBarColor.BLUE;

        @SerializedName("barOverlay")
        public BossEvent.BossBarOverlay barOverlay = BossEvent.BossBarOverlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<aqua>%bucket% Spawn Bucket Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Spawn Bucket Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a %bucket% Spawn Bucket Boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a %bucket% Spawn Bucket Boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Spawn Bucket boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Spawn Bucket queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Bucket: <reset><aqua>%bucket% <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Spawn Bucket Boosts in the queue!";
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
