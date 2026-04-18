package dev.matthiesen.common.cobblemon_boosters.permissions;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.permission.PermissionLevel;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class ModPermissions {
    public final ModPermission RELOAD_PERMISSION;
    public final ModPermission SHINY_PERMISSION;
    public final ModPermission SHINY_START_PERMISSION;
    public final ModPermission SHINY_STOP_PERMISSION;
    public final ModPermission SHINY_STATUS_PERMISSION;
    public final ModPermission CLEAR_QUEUES_PERMISSION;
    public final ModPermission CHECK_QUEUE_PERMISSION;

    public ModPermissions() {
        this.RELOAD_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.reload",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.RELOAD_PERMISSION)
        );
        this.SHINY_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.shiny",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_PERMISSION)
        );
        this.SHINY_START_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.shiny.start",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_START_PERMISSION)
        );
        this.SHINY_STOP_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.shiny.stop",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_STOP_PERMISSION)
        );
        this.SHINY_STATUS_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.shiny.status",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.SHINY_STATUS_PERMISSION)
        );
        this.CLEAR_QUEUES_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.clear_queues",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.CLEAR_QUEUES_PERMISSION)
        );
        this.CHECK_QUEUE_PERMISSION = new ModPermission(
                Constants.MOD_ID + ".command.boosters.check_queue",
                toPermLevel(CobblemonBoosters.INSTANCE.config.permissionLevels.CHECK_QUEUE_PERMISSION)
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