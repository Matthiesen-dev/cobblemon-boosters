package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class PermissionsStartupConfig {

    // Permission Levels
    public ModConfigSpec.IntValue command_boosters_reload;
    public ModConfigSpec.IntValue command_boosters_clearQueues;
    public ModConfigSpec.IntValue command_boosters_checkQueues;
    public ModConfigSpec.IntValue command_boosters_queuePriority;

    public ModConfigSpec.IntValue command_boosters_bucket;
    public ModConfigSpec.IntValue command_boosters_bucket_start;
    public ModConfigSpec.IntValue command_boosters_bucket_stop;
    public ModConfigSpec.IntValue command_boosters_bucket_status;

    public ModConfigSpec.IntValue command_boosters_catch;
    public ModConfigSpec.IntValue command_boosters_catch_start;
    public ModConfigSpec.IntValue command_boosters_catch_stop;
    public ModConfigSpec.IntValue command_boosters_catch_status;

    public ModConfigSpec.IntValue command_boosters_experience;
    public ModConfigSpec.IntValue command_boosters_experience_start;
    public ModConfigSpec.IntValue command_boosters_experience_stop;
    public ModConfigSpec.IntValue command_boosters_experience_status;

    public ModConfigSpec.IntValue command_boosters_shiny;
    public ModConfigSpec.IntValue command_boosters_shiny_start;
    public ModConfigSpec.IntValue command_boosters_shiny_stop;
    public ModConfigSpec.IntValue command_boosters_shiny_status;

    public PermissionsStartupConfig(ModConfigSpec.Builder builder) {
        builder.comment("Permission Configuration").push("permissions");
        builder.comment("Command Permissions").push("command");
        command_boosters_reload = builder.comment("Permission level required to use the '/boosters reload' command", "Permission Node: 'cobblemon_boosters.command.boosters.reload'")
                .defineInRange("reload", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_clearQueues = builder.comment("Permission level required to use the '/boosters clear-queues' command", "Permission Node: 'cobblemon_boosters.command.boosters.clear_queues'")
                .defineInRange("clear_queues", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_checkQueues = builder.comment("Permission level required to use the '/boosters check-queues' command", "Permission Node: 'cobblemon_boosters.command.boosters.check_queues'")
                .defineInRange("check_queues", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_queuePriority = builder.comment("Permission level required to use the '/boosters queue-priority' command", "Permission Node: 'cobblemon_boosters.command.boosters.queue_priority'")
                .defineInRange("queue_priority", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);

        builder.comment("Spawn Bucket Permissions").push("spawnBucket");
        command_boosters_bucket = builder.comment("Permission level required to use the '/boosters bucket' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket'")
                .defineInRange("bucket", PermissionLevel.NONE.getLevel(), 0, 4);
        command_boosters_bucket_start = builder.comment("Permission level required to use the '/boosters bucket start' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.start'")
                .defineInRange("bucket_start", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_bucket_stop = builder.comment("Permission level required to use the '/boosters bucket stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.stop'")
                .defineInRange("bucket_stop", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_bucket_status = builder.comment("Permission level required to use the '/boosters bucket status' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.status'")
                .defineInRange("bucket_status", PermissionLevel.NONE.getLevel(), 0, 4);
        builder.pop();

        builder.comment("Catch Permissions").push("catch");
        command_boosters_catch = builder.comment("Permission level required to use the '/boosters catch' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch'")
                .defineInRange("catch", PermissionLevel.NONE.getLevel(), 0, 4);
        command_boosters_catch_start = builder.comment("Permission level required to use the '/boosters catch start' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.start'")
                .defineInRange("catch_start", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_catch_stop = builder.comment("Permission level required to use the '/boosters catch stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.stop'")
                .defineInRange("catch_stop", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_catch_status = builder.comment("Permission level required to use the '/boosters catch status' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.status'")
                .defineInRange("catch_status", PermissionLevel.NONE.getLevel(), 0, 4);
        builder.pop();

        builder.comment("Experience Permissions").push("experience");
        command_boosters_experience = builder.comment("Permission level required to use the '/boosters experience' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience'")
                .defineInRange("experience", PermissionLevel.NONE.getLevel(), 0, 4);
        command_boosters_experience_start = builder.comment("Permission level required to use the '/boosters experience start' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.start'")
                .defineInRange("experience_start", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_experience_stop = builder.comment("Permission level required to use the '/boosters experience stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.stop'")
                .defineInRange("experience_stop", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_experience_status = builder.comment("Permission level required to use the '/boosters experience status' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.status'")
                .defineInRange("experience_status", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        builder.pop();

        builder.comment("Shiny Permissions").push("shiny");
        command_boosters_shiny = builder.comment("Permission level required to use the '/boosters shiny' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny'")
                .defineInRange("shiny", PermissionLevel.NONE.getLevel(), 0, 4);
        command_boosters_shiny_start = builder.comment("Permission level required to use the '/boosters shiny start' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.start'")
                .defineInRange("shiny_start", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_shiny_stop = builder.comment("Permission level required to use the '/boosters shiny stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.stop'")
                .defineInRange("shiny_stop", PermissionLevel.ALL_COMMANDS.getLevel(), 0, 4);
        command_boosters_shiny_status = builder.comment("Permission level required to use the '/boosters shiny status' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.status'")
                .defineInRange("shiny_status", PermissionLevel.NONE.getLevel(), 0, 4);
        builder.pop();

        builder.pop();
        builder.pop();
    }
}
