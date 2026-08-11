package dev.matthiesen.cobblemon_boosters.common.services.webhook;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IWebhookService;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.exceptions.DiscordWebhookException;
import dev.matthiesen.matthiesen_core.common.core.discord.model.Embed;
import dev.matthiesen.matthiesen_core.common.core.discord.model.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public final class DiscordWebhookService implements IWebhookService {
    private static WebhookNotifierInstance WEBHOOK_INSTANCE;

    public DiscordWebhookService() {
        WebhookNotifierService WEBHOOK_SERVICE = getService();
        if (WEBHOOK_SERVICE != null) {
            WEBHOOK_INSTANCE = WEBHOOK_SERVICE.makeInstance(BoostersConfig.WEBHOOKS_SERVER_CONFIG.webhookUrl.get());
            CobblemonBoostersCommon.INSTANCE.createInfoLog("Matthiesen Lib Webhooks detected, using it for Discord Webhook integration");
        }
    }

    public WebhookNotifierService getService() {
        if (!BoostersConfig.WEBHOOKS_SERVER_CONFIG.enabled.getAsBoolean()) return null;
        if (!BoostersConfig.WEBHOOKS_SERVER_CONFIG.webhookUrl.get().startsWith("https://")) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Discord webhooks are enabled but an invalid Discord Webhook URL is set! Please check your configuration. (Must start with 'https://')");
            return null;
        }
        if (!CobblemonBoostersCommon.INSTANCE.getWebhookService().isAvailable()) return null;
        return CobblemonBoostersCommon.INSTANCE.getWebhookService();
    }

    public static Embed parseEventEmbed(DiscordEmbed embed, IBoost boost) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (embed.title() != null)
            embedBuilder.withTitle(TextUtils.parse(embed.title(), boost));
        if (embed.description() != null)
            embedBuilder.withDescription(TextUtils.parse(embed.description(), boost));
        if (embed.color() != null)
            embedBuilder.withColor(embed.color());
        if (embed.timestamp() != null)
            embedBuilder.withTimestamp(TextUtils.parse(embed.timestamp(), boost));
        List<Embed.EmbedField> fields = new ArrayList<>();
        if (embed.fields() != null) {
            for (DiscordEmbed.DiscordEmbedField field : embed.fields()) {
                Embed.EmbedField embedField = new Embed.EmbedField();
                if (field.name() != null)
                    embedField.setName(TextUtils.parse(field.name(), boost));
                if (field.value() != null)
                    embedField.setValue(TextUtils.parse(field.value(), boost));
                embedField.setInline(field.inline());
                fields.add(embedField);
            }
            embedBuilder.withFields(fields);
        }
        if (embed.author() != null) {
            Embed.Author author = new Embed.Author();
            if (embed.author().name() != null) author.setName(TextUtils.parse(embed.author().name(), boost));
            if (embed.author().icon_url() != null) author.setIconUrl(TextUtils.parse(embed.author().icon_url(), boost));
            embedBuilder.withAuthor(author);
        }
        return embedBuilder.build();
    }

    @Override
    public void sendMessage(DiscordEmbed embed, IBoost boost) {
        if (WEBHOOK_INSTANCE == null) return;
        try {
            String userName = embed.author() != null && embed.author().name() != null
                    ? embed.author().name()
                    : "Cobblemon Boosters";
            String avatarUrl = embed.author() != null && embed.author().icon_url() != null
                    ? embed.author().icon_url()
                    : "https://raw.githubusercontent.com/Matthiesen-dev/cobblemon-boosters/refs/heads/main/assets/logo.png";

            WEBHOOK_INSTANCE.sendMessage(message -> message
                    .withUsername(TextUtils.parse(userName, boost))
                    .withAvatarUrl(TextUtils.parse(avatarUrl, boost))
                    .withEmbeds(List.of(parseEventEmbed(embed, boost)))
            );
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to send Discord webhook message! Check your webhook URL and ensure that your server can connect to Discord's servers.", e);
        } catch (DiscordWebhookException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to send Discord webhook message due to a DiscordWebhookException!", e);
        }
    }
}
