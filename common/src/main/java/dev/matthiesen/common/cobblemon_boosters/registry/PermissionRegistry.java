package dev.matthiesen.common.cobblemon_boosters.registry;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.permission.AbstractPermission;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
import dev.matthiesen.common.matthiesen_lib_api.permission.PermissionLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class PermissionRegistry {
    private static final Permissions PERMISSIONS = new Permissions();

    public static class Permissions {
        public Permission RELOAD_PERMISSION = toModPerm(
                "command.boosters.reload",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.RELOAD_PERMISSION
        );
        public Permission CATCH_PERMISSION = toModPerm(
                "command.boosters.catch",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CATCH_PERMISSION
        );
        public Permission CATCH_START_PERMISSION = toModPerm(
                "command.boosters.catch.start",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CATCH_START_PERMISSION
        );
        public Permission CATCH_STOP_PERMISSION = toModPerm(
                "command.boosters.catch.stop",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CATCH_STOP_PERMISSION
        );
        public Permission CATCH_STATUS_PERMISSION = toModPerm(
                "command.boosters.catch.status",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CATCH_STATUS_PERMISSION
        );
        public Permission EXPERIENCE_PERMISSION = toModPerm(
                "command.boosters.experience",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.EXPERIENCE_PERMISSION
        );
        public Permission EXPERIENCE_START_PERMISSION = toModPerm(
                "command.boosters.experience.start",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.EXPERIENCE_START_PERMISSION
        );
        public Permission EXPERIENCE_STOP_PERMISSION = toModPerm(
                "command.boosters.experience.stop",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.EXPERIENCE_STOP_PERMISSION
        );
        public Permission EXPERIENCE_STATUS_PERMISSION = toModPerm(
                "command.boosters.experience.status",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.EXPERIENCE_STATUS_PERMISSION
        );
        public Permission SHINY_PERMISSION = toModPerm(
                "command.boosters.shiny",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.SHINY_PERMISSION
        );
        public Permission SHINY_START_PERMISSION = toModPerm(
                "command.boosters.shiny.start",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.SHINY_START_PERMISSION
        );
        public Permission SHINY_STOP_PERMISSION = toModPerm(
                "command.boosters.shiny.stop",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.SHINY_STOP_PERMISSION
        );
        public Permission SHINY_STATUS_PERMISSION = toModPerm(
                "command.boosters.shiny.status",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.SHINY_STATUS_PERMISSION
        );
        public Permission BUCKET_PERMISSION = toModPerm(
                "command.boosters.bucket",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.BUCKET_PERMISSION
        );
        public Permission BUCKET_START_PERMISSION = toModPerm(
                "command.boosters.bucket.start",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.BUCKET_START_PERMISSION
        );
        public Permission BUCKET_STOP_PERMISSION = toModPerm(
                "command.boosters.bucket.stop",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.BUCKET_STOP_PERMISSION
        );
        public Permission BUCKET_STATUS_PERMISSION = toModPerm(
                "command.boosters.bucket.status",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.BUCKET_STATUS_PERMISSION
        );
        public Permission CLEAR_QUEUES_PERMISSION = toModPerm(
                "command.boosters.clear_queues",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CLEAR_QUEUES_PERMISSION
        );
        public Permission CHECK_QUEUE_PERMISSION = toModPerm(
                "command.boosters.check_queue",
                CobblemonBoosters.INSTANCE.PERMISSIONS_CONFIG_MANAGER.getConfig().permissionLevels.CHECK_QUEUE_PERMISSION
        );
    }

    public static Permissions getPermissions() {
        return PERMISSIONS;
    }

    private PermissionRegistry() {
    }

    public static void init() {}

    public static boolean checkPermission(CommandSourceStack source, Permission permission) {
        return MatthiesenLibApi.getPermissionValidator().hasPermission(source, permission);
    }

    public static boolean checkPermission(ServerPlayer source, Permission permission) {
        return MatthiesenLibApi.getPermissionValidator().hasPermission(source, permission);
    }

    private static Permission toModPerm(String permission, int level) {
        return register(modPermission(
                Constants.MOD_ID + "." + permission,
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
        MatthiesenLibApi.registerPermission(permission);
        return permission;
    }

    private static Permission modPermission(String node, PermissionLevel level) {
        return new AbstractPermission(node, level) {
            @Override
            protected String getModId() {
                return Constants.MOD_ID;
            }

            @Override
            protected String getPermissionNamespace() {
                return "CobblemonBoosters";
            }
        };
    }
}
