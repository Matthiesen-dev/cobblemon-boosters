package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
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

        @SerializedName("command.boosters.shiny")
        public int SHINY_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

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

        @SerializedName("shinyBarColor")
        public BossBar.Color shinyBarColor = BossBar.Color.YELLOW;

        @SerializedName("shinyBarOverlay")
        public BossBar.Overlay shinyBarOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("shinyBarText")
        public String shinyBarText = "<gold> %multiplier% Shiny Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Shiny Boosts!";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% <green>Reloaded the config!";

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

        public String noQueuedBoosts = "%prefix% <green>There are currently no Shiny Boosts in the queue!";
    }

    @SerializedName("activeShinyBoost")
    public ShinyBoost activeShinyBoost = null;

    @SerializedName("queuedShinyBoosts")
    public List<ShinyBoost> queuedShinyBoosts = new ArrayList<>();

    public void saveGlobalBoostData() {
        ShinyBoost activeBoost = null;

        if (CobblemonBoosters.INSTANCE.globalBoost != null) {
            activeBoost = CobblemonBoosters.INSTANCE.globalBoost;
        }

        CobblemonBoosters.INSTANCE.config.activeShinyBoost = activeBoost;

        List<ShinyBoost> queuedBoosts = CobblemonBoosters.INSTANCE.queuedShinyBoosts.stream().toList();

        CobblemonBoosters.INSTANCE.config.queuedShinyBoosts.clear();
        CobblemonBoosters.INSTANCE.config.queuedShinyBoosts.addAll(queuedBoosts);
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
