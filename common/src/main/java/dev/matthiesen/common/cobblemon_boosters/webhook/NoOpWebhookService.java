package dev.matthiesen.common.cobblemon_boosters.webhook;

import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;

public class NoOpWebhookService implements IWebhookService {
    public NoOpWebhookService() {
        Constants.createInfoLog("Matthiesen Lib Webhooks not detected, using no-op implementation for Discord Webhook integration");
    }

    @Override
    public void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost) {
        // No operation performed, as this is a no-op implementation.
    }
}
