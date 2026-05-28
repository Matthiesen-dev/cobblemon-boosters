package dev.matthiesen.common.cobblemon_boosters.interfaces;

import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;

public interface IWebhookService {
    void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost);
}
