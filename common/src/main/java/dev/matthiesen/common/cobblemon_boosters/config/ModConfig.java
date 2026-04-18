package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import net.kyori.adventure.bossbar.BossBar;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    @SerializedName("permissionlevels")
    public PermissionLevels permissionLevels = new PermissionLevels();

    public static class PermissionLevels {
        @SerializedName("command.boosters.reload")
        public int RELOAD_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch")
        public int CATCH_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.catch.start")
        public int CATCH_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch.stop")
        public int CATCH_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch.status")
        public int CATCH_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.shiny")
        public int SHINY_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.shiny.start")
        public int SHINY_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.shiny.stop")
        public int SHINY_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.shiny.status")
        public int SHINY_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.clear_queues")
        public int CLEAR_QUEUES_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.check_queue")
        public int CHECK_QUEUE_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();
    }

    @SerializedName("messages")
    public MessagesConfig messages = new MessagesConfig();

    public static class MessagesConfig {
        @SerializedName("prefix")
        public String prefix = "<gray>[<gold>Boosters<gray>]";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% <green>Reloaded the config!";

        @SerializedName("shinyMessages")
        public ShinyMessagesConfig shinyMessages = new ShinyMessagesConfig();

        @SerializedName("catchBoostMessages")
        public CatchBoostMessagesConfig catchBoostMessages = new CatchBoostMessagesConfig();
    }

    public static class ShinyMessagesConfig {
        @SerializedName("shinyBarColor")
        public BossBar.Color shinyBarColor = BossBar.Color.YELLOW;

        @SerializedName("shinyBarOverlay")
        public BossBar.Overlay shinyBarOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("shinyBarText")
        public String shinyBarText = "<gold> %multiplier%x Shiny Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Shiny Boosts!";

        @SerializedName("shinyBoostStarted")
        public String shinyBoostStarted = "%prefix% <green>Started a Shiny %multiplier%x boost for %duration%!";

        @SerializedName("shinyBoostAddedToQueue")
        public String shinyBoostAddedToQueued = "%prefix% <green>Added a Shiny %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("shinyBoostStopped")
        public String shinyBoostStopped = "%prefix% <green>Stopped the current Shiny boost!";

        @SerializedName("shinyBoostQueueCleared")
        public String shinyBoostQueueCleared = "%prefix% <green>Cleared the Shiny queued boosts!";

        @SerializedName("shinyBoostInfo")
        public String shinyBoostInfo = "%prefix% <white><bold>Multiplier: <reset><gold>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Shiny Boosts in the queue!";
    }

    public static class CatchBoostMessagesConfig {
        @SerializedName("catchBoostBarColor")
        public BossBar.Color catchBoostBarColor = BossBar.Color.GREEN;

        @SerializedName("catchBoostBarOverlay")
        public BossBar.Overlay catchBoostBarOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("catchBoostBarText")
        public String catchBoostBarText = "<green> %multiplier%x Catch Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Catch Boosts!";

        @SerializedName("catchBoostStarted")
        public String catchBoostStarted = "%prefix% <green>Started a Catch %multiplier%x boost for %duration%!";

        @SerializedName("catchBoostAddedToQueue")
        public String catchBoostAddedToQueued = "%prefix% <green>Added a Catch %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("catchBoostStopped")
        public String catchBoostStopped = "%prefix% <green>Stopped the current Catch boost!";

        @SerializedName("catchBoostQueueCleared")
        public String catchBoostQueueCleared = "%prefix% <green>Cleared the Catch queued boosts!";

        @SerializedName("catchBoostInfo")
        public String catchBoostInfo = "%prefix% <white><bold>Multiplier: <reset><gold>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Catch Boosts in the queue!";
    }

    @SerializedName("activeShinyBoost")
    public ShinyBoost activeShinyBoost = null;

    @SerializedName("activeCatchBoost")
    public CatchBoost activeCatchBoost = null;

    @SerializedName("queuedShinyBoosts")
    public List<ShinyBoost> queuedShinyBoosts = new ArrayList<>();

    @SerializedName("queuedCatchBoosts")
    public List<CatchBoost> queuedCatchBoosts = new ArrayList<>();

    private void saveShinyBoostData() {
        ShinyBoost activeShinyBoost = null;
        if (CobblemonBoosters.INSTANCE.activeShinyBoost != null) {
            activeShinyBoost = CobblemonBoosters.INSTANCE.activeShinyBoost;
        }
        CobblemonBoosters.INSTANCE.config.activeShinyBoost = activeShinyBoost;
        List<ShinyBoost> queuedShinyBoosts = CobblemonBoosters.INSTANCE.queuedShinyBoosts.stream().toList();
        CobblemonBoosters.INSTANCE.config.queuedShinyBoosts.clear();
        CobblemonBoosters.INSTANCE.config.queuedShinyBoosts.addAll(queuedShinyBoosts);
    }

    private void saveCatchBoostData() {
        CatchBoost activeCatchBoost = null;
        if (CobblemonBoosters.INSTANCE.activeCatchBoost != null) {
            activeCatchBoost = CobblemonBoosters.INSTANCE.activeCatchBoost;
        }
        CobblemonBoosters.INSTANCE.config.activeCatchBoost = activeCatchBoost;
        List<CatchBoost> queuedCatchBoosts = CobblemonBoosters.INSTANCE.queuedCatchBoosts.stream().toList();
        CobblemonBoosters.INSTANCE.config.queuedCatchBoosts.clear();
        CobblemonBoosters.INSTANCE.config.queuedCatchBoosts.addAll(queuedCatchBoosts);
    }

    public void saveGlobalBoostData() {
        saveShinyBoostData();
        saveCatchBoostData();
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
