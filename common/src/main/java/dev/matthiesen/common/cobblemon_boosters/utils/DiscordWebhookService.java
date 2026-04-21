package dev.matthiesen.common.cobblemon_boosters.utils;

import com.n1netails.n1netails.discord.api.DiscordWebhookClient;
import com.n1netails.n1netails.discord.exception.DiscordWebhookException;
import com.n1netails.n1netails.discord.internal.DiscordWebhookClientImpl;
import com.n1netails.n1netails.discord.model.*;
import com.n1netails.n1netails.discord.service.WebhookService;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;

import java.util.ArrayList;
import java.util.List;

public class DiscordWebhookService {
    private static DiscordWebhookClient webhookClient;

    public DiscordWebhookService() {
        webhookClient = new DiscordWebhookClientImpl(new WebhookService());
    }

    public static DiscordWebhookClient getWebhookClient() {
        return webhookClient;
    }

    public static Embed parseEventEmbed(ModConfig.DiscordEmbed embed, IBoost boost) {
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
            for (ModConfig.DiscordEmbedField field : embed.fields) {
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
            if (embed.author.icon_url != null) author.setUrl(TextUtils.parse(embed.author.icon_url, boost));
            if (embed.author.url != null) author.setUrl(TextUtils.parse(embed.author.url, boost));
            embedBuilder.withAuthor(author);
        }
        return embedBuilder.build();
    }

    public void sendMessage(ModConfig.DiscordEmbed embed, IBoost boost) throws DiscordWebhookException {
        if (!CobblemonBoosters.INSTANCE.config.discordWebhookConfig.enabled) return;
        if (!CobblemonBoosters.INSTANCE.config.discordWebhookConfig.webhookUrl.startsWith("https://")) {
            Constants.createErrorLog("Discord webhooks are enabled but an invalid Discord Webhook URL is set! Please check your configuration. (Must start with 'https://')");
            return;
        }
        WebhookMessage message = new WebhookMessageBuilder()
                .withAvatarUrl(TextUtils.parse(embed.author.icon_url, boost))
                .withUsername(TextUtils.parse(embed.author.name, boost))
                .withEmbeds(List.of(parseEventEmbed(embed, boost)))
                .build();
        getWebhookClient().sendMessage(CobblemonBoosters.INSTANCE.config.discordWebhookConfig.webhookUrl, message);
    }
}
