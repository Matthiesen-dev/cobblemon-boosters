package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.matthiesen_core.common.core.discord.DiscordColor;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class WebhooksServerConfig {

    // General Webhook Config
    public ModConfigSpec.BooleanValue enabled;
    public ModConfigSpec.ConfigValue<String> webhookUrl;
    public ModConfigSpec.ConfigValue<String> discordAuthorName;
    public ModConfigSpec.ConfigValue<String> discordAuthorIconUrl;

    // Bucket Events
    public ModConfigSpec.ConfigValue<String> bucket_start_title;
    public ModConfigSpec.ConfigValue<String> bucket_start_description;
    public ModConfigSpec.EnumValue<DiscordColor> bucket_start_embedColor;
    public ModConfigSpec.ConfigValue<String> bucket_end_title;
    public ModConfigSpec.ConfigValue<String> bucket_end_description;
    public ModConfigSpec.EnumValue<DiscordColor> bucket_end_embedColor;

    // Catch Events
    public ModConfigSpec.ConfigValue<String> catch_start_title;
    public ModConfigSpec.ConfigValue<String> catch_start_description;
    public ModConfigSpec.EnumValue<DiscordColor> catch_start_embedColor;
    public ModConfigSpec.ConfigValue<String> catch_end_title;
    public ModConfigSpec.ConfigValue<String> catch_end_description;
    public ModConfigSpec.EnumValue<DiscordColor> catch_end_embedColor;

    // Experience Events
    public ModConfigSpec.ConfigValue<String> experience_start_title;
    public ModConfigSpec.ConfigValue<String> experience_start_description;
    public ModConfigSpec.EnumValue<DiscordColor> experience_start_embedColor;
    public ModConfigSpec.ConfigValue<String> experience_end_title;
    public ModConfigSpec.ConfigValue<String> experience_end_description;
    public ModConfigSpec.EnumValue<DiscordColor> experience_end_embedColor;

    // Shiny Events
    public ModConfigSpec.ConfigValue<String> shiny_start_title;
    public ModConfigSpec.ConfigValue<String> shiny_start_description;
    public ModConfigSpec.EnumValue<DiscordColor> shiny_start_embedColor;
    public ModConfigSpec.ConfigValue<String> shiny_end_title;
    public ModConfigSpec.ConfigValue<String> shiny_end_description;
    public ModConfigSpec.EnumValue<DiscordColor> shiny_end_embedColor;

    public WebhooksServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Discord Webhooks Configuration").push("discordWebhooks");
        enabled = builder.comment("Enable or disable Discord webhooks for booster events")
                .define("enabled", false);
        webhookUrl = builder.comment("The Discord Webhook URL to send booster event notifications to")
                .define("webhookUrl", "DISCORD_WEBHOOK_URL_HERE");
        discordAuthorName = builder.comment("The name to display as the author of the Discord webhook messages")
                .define("discordAuthorName", "Cobblemon Boosters");
        discordAuthorIconUrl = builder.comment("The URL of the icon to display as the author of the Discord webhook messages")
                .define("discordAuthorIconUrl", "https://raw.githubusercontent.com/Matthiesen-Dev/cobblemon-boosters/refs/heads/main/assets/logo.png");
        builder.pop();

        builder.comment("Bucket Event Webhook Configuration").push("bucketEvent");
        builder.comment("Bucket Start Event").push("bucketStart");
        bucket_start_title = builder.comment("The title of the Discord webhook message for the start of a Spawn Bucket Boost event")
                .define("title", "Spawn Bucket Event Started!");
        bucket_start_description = builder.comment("The description of the Discord webhook message for the start of a Spawn Bucket Boost event")
                .define("description", "A new Spawn Bucket Boost event has started! Here are the details:");
        bucket_start_embedColor = builder.comment("The color of the Discord webhook embed for the start of a Spawn Bucket Boost event")
                .defineEnum("embedColor", DiscordColor.BLUE);
        builder.pop();
        builder.comment("Bucket End Event").push("bucketEnd");
        bucket_end_title = builder.comment("The title of the Discord webhook message for the end of a Spawn Bucket Boost event")
                .define("title", "Spawn Bucket Event Ended!");
        bucket_end_description = builder.comment("The description of the Discord webhook message for the end of a Spawn Bucket Boost event")
                .define("description", "The %bucket% Spawn Bucket Boost event has ended!");
        bucket_end_embedColor = builder.comment("The color of the Discord webhook embed for the end of a Spawn Bucket Boost event")
                .defineEnum("embedColor", DiscordColor.BLUE);
        builder.pop();
        builder.pop();

        builder.comment("Catch Event Webhook Configuration").push("catchEvent");
        builder.comment("Catch Start Event").push("catchStart");
        catch_start_title = builder.comment("The title of the Discord webhook message for the start of a Catch Boost event")
                .define("title", "Catch Event Started!");
        catch_start_description = builder.comment("The description of the Discord webhook message for the start of a Catch Boost event")
                .define("description", "A new Catch Boost event has started! Here are the details:");
        catch_start_embedColor = builder.comment("The color of the Discord webhook embed for the start of a Catch Boost event")
                .defineEnum("embedColor", DiscordColor.PURPLE);
        builder.pop();
        builder.comment("Catch End Event").push("catchEnd");
        catch_end_title = builder.comment("The title of the Discord webhook message for the end of a Catch Boost event")
                .define("title", "Catch Event Ended!");
        catch_end_description = builder.comment("The description of the Discord webhook message for the end of a Catch Boost event")
                .define("description", "The %multiplier%x Catch Boost event has ended!");
        catch_end_embedColor = builder.comment("The color of the Discord webhook embed for the end of a Catch Boost event")
                .defineEnum("embedColor", DiscordColor.PURPLE);
        builder.pop();
        builder.pop();

        builder.comment("Experience Event Webhook Configuration").push("experienceEvent");
        builder.comment("Experience Start Event").push("experienceStart");
        experience_start_title = builder.comment("The title of the Discord webhook message for the start of an Experience Boost event")
                .define("title", "Experience Event Started!");
        experience_start_description = builder.comment("The description of the Discord webhook message for the start of an Experience Boost event")
                .define("description", "A new Experience Boost event has started! Here are the details:");
        experience_start_embedColor = builder.comment("The color of the Discord webhook embed for the start of an Experience Boost event")
                .defineEnum("embedColor", DiscordColor.GREEN);
        builder.pop();
        builder.comment("Experience End Event").push("experienceEnd");
        experience_end_title = builder.comment("The title of the Discord webhook message for the end of an Experience Boost event")
                .define("title", "Experience Event Ended!");
        experience_end_description = builder.comment("The description of the Discord webhook message for the end of an Experience Boost event")
                .define("description", "The %multiplier%x Experience Boost event has ended!");
        experience_end_embedColor = builder.comment("The color of the Discord webhook embed for the end of an Experience Boost event")
                .defineEnum("embedColor", DiscordColor.GREEN);
        builder.pop();
        builder.pop();

        builder.comment("Shiny Event Webhook Configuration").push("shinyEvent");
        builder.comment("Shiny Start Event").push("shinyStart");
        shiny_start_title = builder.comment("The title of the Discord webhook message for the start of a Shiny Boost event")
                .define("title", "Shiny Event Started!");
        shiny_start_description = builder.comment("The description of the Discord webhook message for the start of a Shiny Boost event")
                .define("description", "A new Shiny Boost event has started! Here are the details:");
        shiny_start_embedColor = builder.comment("The color of the Discord webhook embed for the start of a Shiny Boost event")
                .defineEnum("embedColor", DiscordColor.GOLD);
        builder.pop();
        builder.comment("Shiny End Event").push("shinyEnd");
        shiny_end_title = builder.comment("The title of the Discord webhook message for the end of a Shiny Boost event")
                .define("title", "Shiny Event Ended!");
        shiny_end_description = builder.comment("The description of the Discord webhook message for the end of a Shiny Boost event")
                .define("description", "The %multiplier%x Shiny Boost event has ended!");
        shiny_end_embedColor = builder.comment("The color of the Discord webhook embed for the end of a Shiny Boost event")
                .defineEnum("embedColor", DiscordColor.GOLD);
        builder.pop();
        builder.pop();
    }
}
