package dev.matthiesen.cobblemon_boosters.common.interfaces;

import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;

public interface IWebhookService {
    void sendMessage(DiscordEmbed embed, IBoost boost);
}
