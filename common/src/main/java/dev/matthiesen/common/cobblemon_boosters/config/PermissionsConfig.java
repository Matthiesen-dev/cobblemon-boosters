package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.matthiesen_lib_api.permission.PermissionLevel;

public final class PermissionsConfig {
    @SerializedName("permissionlevels")
    public PermissionLevels permissionLevels = new PermissionLevels();

    public static class PermissionLevels {
        @SerializedName("command.boosters.reload")
        public int RELOAD_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.catch")
        public int CATCH_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.catch.start")
        public int CATCH_START_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.catch.stop")
        public int CATCH_STOP_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.catch.status")
        public int CATCH_STATUS_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.experience")
        public int EXPERIENCE_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.experience.start")
        public int EXPERIENCE_START_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.experience.stop")
        public int EXPERIENCE_STOP_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.experience.status")
        public int EXPERIENCE_STATUS_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.shiny")
        public int SHINY_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.shiny.start")
        public int SHINY_START_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.shiny.stop")
        public int SHINY_STOP_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.shiny.status")
        public int SHINY_STATUS_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.bucket")
        public int BUCKET_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.bucket.start")
        public int BUCKET_START_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.bucket.stop")
        public int BUCKET_STOP_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.bucket.status")
        public int BUCKET_STATUS_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();

        @SerializedName("command.boosters.clear_queues")
        public int CLEAR_QUEUES_PERMISSION =
                PermissionLevel.ALL_COMMANDS.getNumericalValue();

        @SerializedName("command.boosters.check_queue")
        public int CHECK_QUEUE_PERMISSION =
                PermissionLevel.NONE.getNumericalValue();
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
