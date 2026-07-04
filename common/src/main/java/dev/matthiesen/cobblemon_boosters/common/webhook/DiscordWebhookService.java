package dev.matthiesen.cobblemon_boosters.common.webhook;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.WebhooksConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IWebhookService;
import dev.matthiesen.cobblemon_boosters.common.managers.MetricManager;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.common.matthiesen_lib_webhooks.MatthiesenLibWebhooks;
import dev.matthiesen.common.matthiesen_lib_api.core.discord.model.Embed;
import dev.matthiesen.common.matthiesen_lib_api.core.discord.model.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public final class DiscordWebhookService implements IWebhookService {
    private static MatthiesenLibWebhooks.Webhooks webhooks;

    public DiscordWebhookService() {
        webhooks = getClient();
        Constants.createInfoLog("Matthiesen Lib Webhooks detected, using it for Discord Webhook integration");
    }

    public MatthiesenLibWebhooks.Webhooks getClient() {
        if (!CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig.enabled) return null;
        if (!CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig.webhookUrl.startsWith("https://")) {
            Constants.createErrorLog("Discord webhooks are enabled but an invalid Discord Webhook URL is set! Please check your configuration. (Must start with 'https://')");
            return null;
        }
        return new MatthiesenLibWebhooks.Webhooks(CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig.webhookUrl);
    }

    public static Embed parseEventEmbed(WebhooksConfig.DiscordEmbed embed, IBoost boost) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (embed.title != null)
            embedBuilder.withTitle(TextUtils.parse(embed.title, boost));
        if (embed.description != null)
            embedBuilder.withDescription(TextUtils.parse(embed.description, boost));
        if (embed.color != null)
            embedBuilder.withColor(embed.color);
        if (embed.timestamp != null)
            embedBuilder.withTimestamp(TextUtils.parse(embed.timestamp, boost));
        List<Embed.EmbedField> fields = new ArrayList<>();
        if (embed.fields != null) {
            for (WebhooksConfig.DiscordEmbedField field : embed.fields) {
                Embed.EmbedField embedField = new Embed.EmbedField();
                if (field.name != null)
                    embedField.setName(TextUtils.parse(field.name, boost));
                if (field.value != null)
                    embedField.setValue(TextUtils.parse(field.value, boost));
                embedField.setInline(field.inline);
                fields.add(embedField);
            }
            embedBuilder.withFields(fields);
        }
        if (embed.author != null) {
            Embed.Author author = new Embed.Author();
            if (embed.author.name != null) author.setName(TextUtils.parse(embed.author.name, boost));
            if (embed.author.icon_url != null) author.setIconUrl(TextUtils.parse(embed.author.icon_url, boost));
            if (embed.author.url != null) author.setUrl(TextUtils.parse(embed.author.url, boost));
            embedBuilder.withAuthor(author);
        }
        return embedBuilder.build();
    }

    @Override
    public void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost) {
        if (webhooks == null) return;
        try {
            String userName = embed.author != null && embed.author.name != null
                    ? embed.author.name
                    : "Cobblemon Boosters";
            String avatarUrl = embed.author != null && embed.author.icon_url != null
                    ? embed.author.icon_url
                    : "https://raw.githubusercontent.com/Matthiesen-dev/cobblemon-boosters/refs/heads/main/assets/logo.png";

            webhooks.sendMessage(message -> message
                    .withUsername(TextUtils.parse(userName, boost))
                    .withAvatarUrl(TextUtils.parse(avatarUrl, boost))
                    .withEmbeds(List.of(parseEventEmbed(embed, boost)))
            );
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.createErrorLog("Failed to send Discord webhook message! Check your webhook URL and ensure that your server can connect to Discord's servers.", e);
        }
    }
}
