package dev.matthiesen.common.cobblemon_boosters.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;

public interface IConfigManager {
    ModConfig loadConfig();
    JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig);
    void saveConfig();
}
