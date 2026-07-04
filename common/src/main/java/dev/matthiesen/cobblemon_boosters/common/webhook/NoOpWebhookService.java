package dev.matthiesen.cobblemon_boosters.common.webhook;

import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.WebhooksConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IWebhookService;

public final class NoOpWebhookService implements IWebhookService {
    public NoOpWebhookService() {
        Constants.createInfoLog("Matthiesen Lib Webhooks not detected, using no-op implementation for Discord Webhook integration");
    }

    @Override
    public void sendMessage(WebhooksConfig.DiscordEmbed embed, IBoost boost) {
        // No operation performed, as this is a no-op implementation.
    }
}
