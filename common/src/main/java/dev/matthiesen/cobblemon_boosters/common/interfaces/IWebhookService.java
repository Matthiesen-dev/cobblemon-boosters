package dev.matthiesen.cobblemon_boosters.common.interfaces;

import dev.matthiesen.cobblemon_boosters.common.config.WebhooksConfig;

public interface IWebhookService {
    void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost);
}
