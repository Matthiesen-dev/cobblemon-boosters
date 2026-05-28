package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.cobblemon_boosters.Constants;

public class PermissionsConfig {
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

        @SerializedName("command.boosters.experience")
        public int EXPERIENCE_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.experience.start")
        public int EXPERIENCE_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.experience.stop")
        public int EXPERIENCE_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.experience.status")
        public int EXPERIENCE_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

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

        @SerializedName("command.boosters.bucket")
        public int BUCKET_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.bucket.start")
        public int BUCKET_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.bucket.stop")
        public int BUCKET_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.bucket.status")
        public int BUCKET_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.clear_queues")
        public int CLEAR_QUEUES_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.check_queue")
        public int CHECK_QUEUE_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
