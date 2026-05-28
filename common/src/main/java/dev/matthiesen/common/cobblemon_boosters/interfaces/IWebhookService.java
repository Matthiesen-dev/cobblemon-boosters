package dev.matthiesen.common.cobblemon_boosters.interfaces;

import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;

public interface IWebhookService {
    void sendMessage(ModConfig.DiscordEmbed embed, IBoost boost);
}
