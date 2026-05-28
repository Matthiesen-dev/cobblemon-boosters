package dev.matthiesen.common.cobblemon_boosters.utils;

import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;

public class NoOpWebhookService implements IWebhookService {
    @Override
    public void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost) {
        // No operation performed, as this is a no-op implementation.
    }
}
