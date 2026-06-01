package dev.matthiesen.common.cobblemon_boosters.config;

import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class BoostersConfigManager<T> extends ConfigManager<T> {
    public BoostersConfigManager(Class<T> configClass, String configName) {
        super(configClass, configName, Constants.MOD_ID);
    }

    // Static methods for Cobblemon Boosters configs

    public static Map<Constants.CONFIGS, ConfigManager<?>> configManagers = new HashMap<>();

    public static <T> void registerConfigManager(Class<T> configClass, Constants.CONFIGS configName) {
        if (configManagers.containsKey(configName)) {
            Constants.LOGGER.warn("Config manager for {} already exists, skipping registration", configName.getConfigName());
            return;
        }
        BoostersConfigManager<T> manager = new BoostersConfigManager<>(configClass, configName.getConfigName());
        configManagers.put(configName, manager);
        Constants.LOGGER.info("Registered config manager for {}", configName);
    }

    public static void registerConfigs() {
        registerConfigManager(CacheConfig.class, Constants.CONFIGS.CACHE);
        registerConfigManager(MessagesConfig.class, Constants.CONFIGS.MESSAGES);
        registerConfigManager(PermissionsConfig.class, Constants.CONFIGS.PERMISSIONS);
        registerConfigManager(WebhooksConfig.class, Constants.CONFIGS.WEBHOOKS);
    }

    public static void saveAll() {
        for (Map.Entry<Constants.CONFIGS, ConfigManager<?>> entry : configManagers.entrySet()) {
            Constants.LOGGER.info("Saving config: {}", entry.getKey().getConfigName());
            entry.getValue().saveConfig();
        }
    }

    public static void loadAll() {
        for (Map.Entry<Constants.CONFIGS, ConfigManager<?>> entry : configManagers.entrySet()) {
            Constants.LOGGER.info("Loading config: {}", entry.getKey().getConfigName());
            entry.getValue().loadConfig();
        }
    }

    public static ConfigManager<?> getConfigManager(Constants.CONFIGS configName) {
        if (!configManagers.containsKey(configName)) {
            Constants.LOGGER.warn("Config manager for {} does not exist, returning null", configName.getConfigName());
            return null;
        }
        return configManagers.get(configName);
    }

    public static ConfigManager<CacheConfig> getCacheConfigManager() {
        return getTypedConfigManager(Constants.CONFIGS.CACHE);
    }

    public static ConfigManager<MessagesConfig> getMessagesConfigManager() {
        return getTypedConfigManager(Constants.CONFIGS.MESSAGES);
    }

    public static ConfigManager<PermissionsConfig> getPermissionsConfigManager() {
        return getTypedConfigManager(Constants.CONFIGS.PERMISSIONS);
    }

    public static ConfigManager<WebhooksConfig> getWebhooksConfigManager() {
        return getTypedConfigManager(Constants.CONFIGS.WEBHOOKS);
    }

    @SuppressWarnings("unchecked")
    private static  <T> ConfigManager<T> getTypedConfigManager(Constants.CONFIGS configName) {
        ConfigManager<?> manager = getConfigManager(configName);
        if (manager == null) {
            return null;
        }

        Object config = manager.getConfig();
        if (config != null && !configName.getConfigClass().isInstance(config)) {
            Constants.LOGGER.error(
                    "Config manager type mismatch for {}. Expected {}, got {}",
                    configName.getConfigName(),
                    configName.getConfigClass().getSimpleName(),
                    config.getClass().getSimpleName()
            );
            return null;
        }

        return (ConfigManager<T>) manager;
    }
}
