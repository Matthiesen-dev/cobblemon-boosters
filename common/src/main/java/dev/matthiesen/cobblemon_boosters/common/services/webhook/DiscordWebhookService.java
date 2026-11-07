package dev.matthiesen.cobblemon_boosters.common.services.webhook;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.*;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.discord.*;
import dev.matthiesen.matthiesen_core.common.api.discord.exceptions.DiscordWebhookException;
import dev.matthiesen.matthiesen_core.common.core.discord.model.Embed;

import java.util.ArrayList;
import java.util.List;

public final class DiscordWebhookService implements IWebhookService {
    private static WebhookNotifierInstance WEBHOOK_INSTANCE;

    public DiscordWebhookService() {
        WebhookNotifierService WEBHOOK_SERVICE = getService();
        if (WEBHOOK_SERVICE != null) {
            WEBHOOK_INSTANCE = WEBHOOK_SERVICE.makeInstance(BoostersConfig.WEBHOOKS_SERVER_CONFIG.webhookUrl.get());
            CobblemonBoostersCommon.INSTANCE.createInfoLog("Matthiesen Core Webhooks detected, using it for Discord Webhook integration");
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
        Embed.Builder embedBuilder = new Embed.Builder();
        var placeholders = TextUtils.getPlaceholders(boost);
        if (embed.title() != null)
            embedBuilder.withTitle(TextUtils.parse(embed.title(), placeholders));
        if (embed.description() != null)
            embedBuilder.withDescription(TextUtils.parse(embed.description(), placeholders));
        if (embed.color() != null)
            embedBuilder.withColor(embed.color());
        if (embed.timestamp() != null)
            embedBuilder.withTimestamp(TextUtils.parse(embed.timestamp(), placeholders));
        List<Embed.Field> fields = new ArrayList<>();
        if (embed.fields() != null) {
            for (DiscordEmbed.DiscordEmbedField field : embed.fields()) {
                Embed.Field embedField = new Embed.Field();
                if (field.name() != null)
                    embedField.setName(TextUtils.parse(field.name(), placeholders));
                if (field.value() != null)
                    embedField.setValue(TextUtils.parse(field.value(), placeholders));
                embedField.setInline(field.inline());
                fields.add(embedField);
            }
            embedBuilder.withFields(fields);
        }
        if (embed.author() != null) {
            Embed.Author author = new Embed.Author();
            if (embed.author().name() != null) author.setName(TextUtils.parse(embed.author().name(), placeholders));
            if (embed.author().icon_url() != null) author.setIconUrl(TextUtils.parse(embed.author().icon_url(), placeholders));
            embedBuilder.withAuthor(author);
        }
        return embedBuilder.build();
    }

    private static String buildRolePing(String roleId) {
        if (roleId == null || roleId.isEmpty()) return null;
        return "<@&" + roleId + ">";
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

            String rolePing;
            if (embed.rolePing().pingEnabled()) {
                rolePing = buildRolePing(embed.rolePing().roleId());
            } else {
                rolePing = null;
            }

            var placeholders = TextUtils.getPlaceholders(boost);
            WEBHOOK_INSTANCE.sendMessage(message -> {
                if (rolePing != null) {
                    message.withContent(rolePing);
                }
                message
                        .withUsername(TextUtils.parse(userName, placeholders))
                        .withAvatarUrl(TextUtils.parse(avatarUrl, placeholders))
                        .withEmbeds(List.of(parseEventEmbed(embed, boost)));
            });
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to send Discord webhook message! Check your webhook URL and ensure that your server can connect to Discord's servers.", e);
        } catch (DiscordWebhookException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to send Discord webhook message due to a DiscordWebhookException!", e);
        }
    }
}
