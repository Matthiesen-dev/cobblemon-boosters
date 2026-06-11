package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.matthiesen_lib_api.core.discord.DiscordColor;

import java.util.List;

public final class WebhooksConfig {
    @SerializedName("discordWebhookConfig")
    public DiscordWebhookConfig discordWebhookConfig = new DiscordWebhookConfig();

    public static class DiscordWebhookConfig {
        @SerializedName("enabled")
        public boolean enabled = false;

        @SerializedName("webhookUrl")
        public String webhookUrl = "DISCORD_WEBHOOK_URL_HERE";

        @SerializedName("discordAuthorName")
        public String discordAuthorName = "Cobblemon Boosters";

        @SerializedName("discordAuthorIconUrl")
        public String discordAuthorIconUrl = "https://raw.githubusercontent.com/Matthiesen-Dev/cobblemon-boosters/refs/heads/main/assets/logo.png";

        @SerializedName("catchEventStartEmbed")
        public DiscordEmbed catchEventStartEmbed = new DiscordEmbed().create(
                "Catch Event Started!",
                "A new Catch Boost event has started! Here are the details:",
                DiscordColor.PURPLE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("catchEventEndEmbed")
        public DiscordEmbed catchEventEndEmbed = new DiscordEmbed().create(
                "Catch Event Ended!",
                "The %multiplier%x Catch Boost event has ended!",
                DiscordColor.PURPLE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("experienceEventStartEmbed")
        public DiscordEmbed experienceEventStartEmbed = new DiscordEmbed().create(
                "Experience Event Started!",
                "A new Experience Boost event has started! Here are the details:",
                DiscordColor.GREEN.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("experienceEventEndEmbed")
        public DiscordEmbed experienceEventEndEmbed = new DiscordEmbed().create(
                "Experience Event Ended!",
                "The %multiplier%x Experience Boost event has ended!",
                DiscordColor.GREEN.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("shinyEventStartEmbed")
        public DiscordEmbed shinyEventStartEmbed = new DiscordEmbed().create(
                "Shiny Event Started!",
                "A new Shiny Boost event has started! Here are the details:",
                DiscordColor.GOLD.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("shinyEventEndEmbed")
        public DiscordEmbed shinyEventEndEmbed = new DiscordEmbed().create(
                "Shiny Event Ended!",
                "The %multiplier%x Shiny Boost event has ended!",
                DiscordColor.GOLD.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("spawnBucketEventStartEmbed")
        public DiscordEmbed spawnBucketEventStartEmbed = new DiscordEmbed().create(
                "Spawn Bucket Event Started!",
                "A new Spawn Bucket Boost event has started! Here are the details:",
                DiscordColor.BLUE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Bucket",
                                "%bucket%",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("spawnBucketEventEndEmbed")
        public DiscordEmbed spawnBucketEventEndEmbed = new DiscordEmbed().create(
                "Spawn Bucket Event Ended!",
                "The %bucket% Spawn Bucket Boost event has ended!",
                DiscordColor.BLUE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );
    }

    public static class DiscordEmbed {
        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("color")
        public Integer color;

        @SerializedName("author")
        public DiscordAuthor author;

        @SerializedName("fields")
        public List<DiscordEmbedField> fields;

        @SerializedName("timestamp")
        public String timestamp;

        public DiscordEmbed create(
                String title,
                String description,
                Integer color,
                DiscordAuthor author,
                List<DiscordEmbedField> fields,
                String timestamp
        ) {
            DiscordEmbed embed = new DiscordEmbed();
            embed.title = title;
            embed.description = description;
            embed.color = color;
            embed.author = author;
            embed.fields = fields;
            embed.timestamp = timestamp;
            return embed;
        }
    }

    public static class DiscordAuthor {
        @SerializedName("name")
        public String name;

        @SerializedName("url")
        public String url;

        @SerializedName("icon_url")
        public String icon_url;

        public DiscordAuthor create(String name, String url, String icon_url) {
            DiscordAuthor author = new DiscordAuthor();
            author.name = name;
            author.url = url;
            author.icon_url = icon_url;
            return author;
        }
    }

    public static class DiscordEmbedField {
        @SerializedName("name")
        public String name;

        @SerializedName("value")
        public String value;

        @SerializedName("inline")
        public boolean inline;

        public DiscordEmbedField create(String name, String value, boolean inline) {
            DiscordEmbedField field = new DiscordEmbedField();
            field.name = name;
            field.value = value;
            field.inline = inline;
            return field;
        }
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
