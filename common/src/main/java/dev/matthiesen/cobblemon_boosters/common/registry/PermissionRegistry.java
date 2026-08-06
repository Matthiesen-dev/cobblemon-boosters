package dev.matthiesen.cobblemon_boosters.common.registry;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import dev.matthiesen.matthiesen_core.common.utility.AbstractPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class PermissionRegistry {
    private static final Permissions PERMISSIONS = new Permissions();

    public static class Permissions {
        public Permission RELOAD_PERMISSION = toModPerm(
                "command.boosters.reload",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_reload.getAsInt()
        );
        public Permission CATCH_PERMISSION = toModPerm(
                "command.boosters.catch",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_catch.getAsInt()
        );
        public Permission CATCH_START_PERMISSION = toModPerm(
                "command.boosters.catch.start",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_catch_start.getAsInt()
        );
        public Permission CATCH_STOP_PERMISSION = toModPerm(
                "command.boosters.catch.stop",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_catch_stop.getAsInt()
        );
        public Permission CATCH_STATUS_PERMISSION = toModPerm(
                "command.boosters.catch.status",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_catch_status.getAsInt()
        );
        public Permission EXPERIENCE_PERMISSION = toModPerm(
                "command.boosters.experience",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_experience.getAsInt()
        );
        public Permission EXPERIENCE_START_PERMISSION = toModPerm(
                "command.boosters.experience.start",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_experience_start.getAsInt()
        );
        public Permission EXPERIENCE_STOP_PERMISSION = toModPerm(
                "command.boosters.experience.stop",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_experience_stop.getAsInt()
        );
        public Permission EXPERIENCE_STATUS_PERMISSION = toModPerm(
                "command.boosters.experience.status",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_experience_status.getAsInt()
        );
        public Permission SHINY_PERMISSION = toModPerm(
                "command.boosters.shiny",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_shiny.getAsInt()
        );
        public Permission SHINY_START_PERMISSION = toModPerm(
                "command.boosters.shiny.start",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_shiny_start.getAsInt()
        );
        public Permission SHINY_STOP_PERMISSION = toModPerm(
                "command.boosters.shiny.stop",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_shiny_stop.getAsInt()
        );
        public Permission SHINY_STATUS_PERMISSION = toModPerm(
                "command.boosters.shiny.status",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_shiny_status.getAsInt()
        );
        public Permission BUCKET_PERMISSION = toModPerm(
                "command.boosters.bucket",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_bucket.getAsInt()
        );
        public Permission BUCKET_START_PERMISSION = toModPerm(
                "command.boosters.bucket.start",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_bucket_start.getAsInt()
        );
        public Permission BUCKET_STOP_PERMISSION = toModPerm(
                "command.boosters.bucket.stop",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_bucket_stop.getAsInt()
        );
        public Permission BUCKET_STATUS_PERMISSION = toModPerm(
                "command.boosters.bucket.status",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_bucket_status.getAsInt()
        );
        public Permission CLEAR_QUEUES_PERMISSION = toModPerm(
                "command.boosters.clear_queues",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_clearQueues.getAsInt()
        );
        public Permission CHECK_QUEUE_PERMISSION = toModPerm(
                "command.boosters.check_queue",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_checkQueues.getAsInt()
        );
        public Permission QUEUE_PRIORITY_PERMISSION = toModPerm(
                "command.boosters.queue_priority",
                BoostersConfig.PERMISSIONS_STARTUP_CONFIG.command_boosters_queuePriority.getAsInt()
        );
    }

    public static Permissions getPermissions() {
        return PERMISSIONS;
    }

    private PermissionRegistry() {
    }

    public static void init() {}

    public static boolean checkPermission(CommandSourceStack source, Permission permission) {
        return CobblemonBoostersCommon.INSTANCE.getPermissionsManager().getPermissionValidator().hasPermission(source, permission);
    }

    public static boolean checkPermission(ServerPlayer source, Permission permission) {
        return CobblemonBoostersCommon.INSTANCE.getPermissionsManager().getPermissionValidator().hasPermission(source, permission);
    }

    private static Permission toModPerm(String permission, int level) {
        return register(modPermission(
                CobblemonBoostersCommon.MOD_ID + "." + permission,
                toPermLevel(level)
        ));
    }

    public static PermissionLevel toPermLevel(int permLevel) {
        for (PermissionLevel value : PermissionLevel.values()) {
            if (value.ordinal() == permLevel) {
                return value;
            }
        }
        return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS;
    }

    private static Permission register(Permission permission) {
        CobblemonBoostersCommon.INSTANCE.getPermissionsManager().registerPermission(permission);
        return permission;
    }

    private static Permission modPermission(String node, PermissionLevel level) {
        return new AbstractPermission(node, level) {
            @Override
            protected String getModId() {
                return CobblemonBoostersCommon.MOD_ID;
            }

            @Override
            protected String getPermissionNamespace() {
                return "CobblemonBoostersCommon";
            }
        };
    }
}
