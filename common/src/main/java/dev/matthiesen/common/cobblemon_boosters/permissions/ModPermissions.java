package dev.matthiesen.common.cobblemon_boosters.permissions;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.permission.PermissionLevel;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.minecraft.commands.CommandSourceStack;

public class ModPermissions {
    public final ModPermission RELOAD_PERMISSION;
    public final ModPermission CATCH_PERMISSION;
    public final ModPermission CATCH_START_PERMISSION;
    public final ModPermission CATCH_STOP_PERMISSION;
    public final ModPermission CATCH_STATUS_PERMISSION;
    public final ModPermission EXPERIENCE_PERMISSION;
    public final ModPermission EXPERIENCE_START_PERMISSION;
    public final ModPermission EXPERIENCE_STOP_PERMISSION;
    public final ModPermission EXPERIENCE_STATUS_PERMISSION;
    public final ModPermission SHINY_PERMISSION;
    public final ModPermission SHINY_START_PERMISSION;
    public final ModPermission SHINY_STOP_PERMISSION;
    public final ModPermission SHINY_STATUS_PERMISSION;
    public final ModPermission BUCKET_PERMISSION;
    public final ModPermission BUCKET_START_PERMISSION;
    public final ModPermission BUCKET_STOP_PERMISSION;
    public final ModPermission BUCKET_STATUS_PERMISSION;
    public final ModPermission CLEAR_QUEUES_PERMISSION;
    public final ModPermission CHECK_QUEUE_PERMISSION;

    public ModPermissions() {
        this.RELOAD_PERMISSION = toModPerm(
                "command.boosters.reload",
                CobblemonBoosters.INSTANCE.config.permissionLevels.RELOAD_PERMISSION
        );
        this.CATCH_PERMISSION = toModPerm(
                "command.boosters.catch",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CATCH_PERMISSION
        );
        this.CATCH_START_PERMISSION = toModPerm(
                "command.boosters.catch.start",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CATCH_START_PERMISSION
        );
        this.CATCH_STOP_PERMISSION = toModPerm(
                "command.boosters.catch.stop",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CATCH_STOP_PERMISSION
        );
        this.CATCH_STATUS_PERMISSION = toModPerm(
                "command.boosters.catch.status",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CATCH_STATUS_PERMISSION
        );
        this.EXPERIENCE_PERMISSION = toModPerm(
                "command.boosters.experience",
                CobblemonBoosters.INSTANCE.config.permissionLevels.EXPERIENCE_PERMISSION
        );
        this.EXPERIENCE_START_PERMISSION = toModPerm(
                "command.boosters.experience.start",
                CobblemonBoosters.INSTANCE.config.permissionLevels.EXPERIENCE_START_PERMISSION
        );
        this.EXPERIENCE_STOP_PERMISSION = toModPerm(
                "command.boosters.experience.stop",
                CobblemonBoosters.INSTANCE.config.permissionLevels.EXPERIENCE_STOP_PERMISSION
        );
        this.EXPERIENCE_STATUS_PERMISSION = toModPerm(
                "command.boosters.experience.status",
                CobblemonBoosters.INSTANCE.config.permissionLevels.EXPERIENCE_STATUS_PERMISSION
        );
        this.SHINY_PERMISSION = toModPerm(
                "command.boosters.shiny",
                CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_PERMISSION
        );
        this.SHINY_START_PERMISSION = toModPerm(
                "command.boosters.shiny.start",
                CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_START_PERMISSION
        );
        this.SHINY_STOP_PERMISSION = toModPerm(
                "command.boosters.shiny.stop",
                CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_STOP_PERMISSION
        );
        this.SHINY_STATUS_PERMISSION = toModPerm(
                "command.boosters.shiny.status",
                CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_STATUS_PERMISSION
        );
        this.BUCKET_PERMISSION = toModPerm(
                "command.boosters.bucket",
                CobblemonBoosters.INSTANCE.config.permissionLevels.BUCKET_PERMISSION
        );
        this.BUCKET_START_PERMISSION = toModPerm(
                "command.boosters.bucket.start",
                CobblemonBoosters.INSTANCE.config.permissionLevels.BUCKET_START_PERMISSION
        );
        this.BUCKET_STOP_PERMISSION = toModPerm(
                "command.boosters.bucket.stop",
                CobblemonBoosters.INSTANCE.config.permissionLevels.BUCKET_STOP_PERMISSION
        );
        this.BUCKET_STATUS_PERMISSION = toModPerm(
                "command.boosters.bucket.status",
                CobblemonBoosters.INSTANCE.config.permissionLevels.BUCKET_STATUS_PERMISSION
        );
        this.CLEAR_QUEUES_PERMISSION = toModPerm(
                "command.boosters.clear_queues",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CLEAR_QUEUES_PERMISSION
        );
        this.CHECK_QUEUE_PERMISSION = toModPerm(
                "command.boosters.check_queue",
                CobblemonBoosters.INSTANCE.config.permissionLevels.CHECK_QUEUE_PERMISSION
        );
    }

    public ModPermission toModPerm(String permission, int level) {
        return new ModPermission(
                Constants.MOD_ID + "." + permission,
                toPermLevel(level)
        );
    }

    public PermissionLevel toPermLevel(int permLevel) {
        for (PermissionLevel value : PermissionLevel.values()) {
            if (value.ordinal() == permLevel) {
                return value;
            }
        }
        return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS;
    }

    public static boolean checkPermission(CommandSourceStack source, ModPermission permission) {
        return Cobblemon.INSTANCE.getPermissionValidator().hasPermission(source, permission);
    }
}