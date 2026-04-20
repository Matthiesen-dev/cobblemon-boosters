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
        // Process Author
        Embed.Author author = new Embed.Author();
        author.setName(TextUtils.parse(embed.author.name, boost));
        author.setIcon_url(TextUtils.parse(embed.author.icon_url, boost));

        // Process Fields
        List<Embed.EmbedField> fields = new ArrayList<>();
        if (embed.fields != null) {
            for (ModConfig.DiscordEmbedField field : embed.fields) {
                Embed.EmbedField embedField = new Embed.EmbedField();
                embedField.setName(TextUtils.parse(field.name, boost));
                embedField.setValue(TextUtils.parse(field.value, boost));
                embedField.setInline(field.inline);
                fields.add(embedField);
            }
        }

        return new EmbedBuilder()
                .withTitle(TextUtils.parse(embed.title, boost))
                .withDescription(TextUtils.parse(embed.description, boost))
                .withColor(embed.color)
                .withAuthor(author)
                .withFields(fields)
                .withTimestamp(TextUtils.parse(embed.timestamp, boost))
                .build();
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
