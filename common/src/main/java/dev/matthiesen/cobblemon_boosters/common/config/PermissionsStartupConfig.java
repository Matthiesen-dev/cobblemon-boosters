package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class PermissionsStartupConfig {

    // Permission Levels
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_reload;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_clearQueues;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_checkQueues;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_queuePriority;

    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_bucket;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_bucket_start;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_bucket_stop;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_bucket_status;

    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_catch;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_catch_start;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_catch_stop;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_catch_status;

    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_experience;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_experience_start;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_experience_stop;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_experience_status;

    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_shiny;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_shiny_start;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_shiny_stop;
    public ModConfigSpec.EnumValue<PermissionLevel> command_boosters_shiny_status;

    public PermissionsStartupConfig(ModConfigSpec.Builder builder) {
        builder.comment("Permission Configuration").push("permissions");
        builder.comment("Command Permissions").push("command");
        command_boosters_reload = builder.comment("Permission level required to use the '/boosters reload' command", "Permission Node: 'cobblemon_boosters.command.boosters.reload'")
                .defineEnum("reload", PermissionLevel.ALL_COMMANDS);
        command_boosters_clearQueues = builder.comment("Permission level required to use the '/boosters clear-queues' command", "Permission Node: 'cobblemon_boosters.command.boosters.clear_queues'")
                .defineEnum("clear_queues", PermissionLevel.ALL_COMMANDS);
        command_boosters_checkQueues = builder.comment("Permission level required to use the '/boosters check-queues' command", "Permission Node: 'cobblemon_boosters.command.boosters.check_queues'")
                .defineEnum("check_queues", PermissionLevel.ALL_COMMANDS);
        command_boosters_queuePriority = builder.comment("Permission level required to use the '/boosters queue-priority' command", "Permission Node: 'cobblemon_boosters.command.boosters.queue_priority'")
                .defineEnum("queue_priority", PermissionLevel.ALL_COMMANDS);

        builder.comment("Spawn Bucket Permissions").push("spawnBucket");
        command_boosters_bucket = builder.comment("Permission level required to use the '/boosters bucket' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket'")
                .defineEnum("bucket", PermissionLevel.NONE);
        command_boosters_bucket_start = builder.comment("Permission level required to use the '/boosters bucket start' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.start'")
                .defineEnum("bucket_start", PermissionLevel.ALL_COMMANDS);
        command_boosters_bucket_stop = builder.comment("Permission level required to use the '/boosters bucket stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.stop'")
                .defineEnum("bucket_stop", PermissionLevel.ALL_COMMANDS);
        command_boosters_bucket_status = builder.comment("Permission level required to use the '/boosters bucket status' command", "Permission Node: 'cobblemon_boosters.command.boosters.bucket.status'")
                .defineEnum("bucket_status", PermissionLevel.NONE);
        builder.pop();

        builder.comment("Catch Permissions").push("catch");
        command_boosters_catch = builder.comment("Permission level required to use the '/boosters catch' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch'")
                .defineEnum("catch", PermissionLevel.NONE);
        command_boosters_catch_start = builder.comment("Permission level required to use the '/boosters catch start' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.start'")
                .defineEnum("catch_start", PermissionLevel.ALL_COMMANDS);
        command_boosters_catch_stop = builder.comment("Permission level required to use the '/boosters catch stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.stop'")
                .defineEnum("catch_stop", PermissionLevel.ALL_COMMANDS);
        command_boosters_catch_status = builder.comment("Permission level required to use the '/boosters catch status' command", "Permission Node: 'cobblemon_boosters.command.boosters.catch.status'")
                .defineEnum("catch_status", PermissionLevel.NONE);
        builder.pop();

        builder.comment("Experience Permissions").push("experience");
        command_boosters_experience = builder.comment("Permission level required to use the '/boosters experience' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience'")
                .defineEnum("experience", PermissionLevel.NONE);
        command_boosters_experience_start = builder.comment("Permission level required to use the '/boosters experience start' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.start'")
                .defineEnum("experience_start", PermissionLevel.ALL_COMMANDS);
        command_boosters_experience_stop = builder.comment("Permission level required to use the '/boosters experience stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.stop'")
                .defineEnum("experience_stop", PermissionLevel.ALL_COMMANDS);
        command_boosters_experience_status = builder.comment("Permission level required to use the '/boosters experience status' command", "Permission Node: 'cobblemon_boosters.command.boosters.experience.status'")
                .defineEnum("experience_status", PermissionLevel.NONE);
        builder.pop();

        builder.comment("Shiny Permissions").push("shiny");
        command_boosters_shiny = builder.comment("Permission level required to use the '/boosters shiny' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny'")
                .defineEnum("shiny", PermissionLevel.NONE);
        command_boosters_shiny_start = builder.comment("Permission level required to use the '/boosters shiny start' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.start'")
                .defineEnum("shiny_start", PermissionLevel.ALL_COMMANDS);
        command_boosters_shiny_stop = builder.comment("Permission level required to use the '/boosters shiny stop' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.stop'")
                .defineEnum("shiny_stop", PermissionLevel.ALL_COMMANDS);
        command_boosters_shiny_status = builder.comment("Permission level required to use the '/boosters shiny status' command", "Permission Node: 'cobblemon_boosters.command.boosters.shiny.status'")
                .defineEnum("shiny_status", PermissionLevel.NONE);
        builder.pop();

        builder.pop();
        builder.pop();
    }
}
