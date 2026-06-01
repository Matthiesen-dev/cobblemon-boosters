package dev.matthiesen.common.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.config.MessagesConfig;
import dev.matthiesen.common.cobblemon_boosters.config.PermissionsConfig;
import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class Constants {
    public static final String MOD_ID = "cobblemon_boosters";
    public static final String ModName = "Cobblemon Boosters";
    public static final List<String> CURRENT_BOOSTERS = List.of(
            "bucket",
            "catch",
            "experience",
            "shiny"
    );

    public enum CONFIGS {
        CACHE("cache", CacheConfig.class),
        MESSAGES("messages", MessagesConfig.class),
        PERMISSIONS("permissions", PermissionsConfig.class),
        WEBHOOKS("webhooks", WebhooksConfig.class);

        private final String configName;
        private final Class<?> configClass;

        CONFIGS(String configName, Class<?> configClass) {
            this.configName = configName;
            this.configClass = configClass;
        }

        public String getConfigName() {
            return configName;
        }

        public Class<?> getConfigClass() {
            return configClass;
        }
    }

    public static class COMPAT {
        public static final String GOOEYLIBS = "gooeylibs";
        public static final String MATTHIESEN_LIB_WEBHOOKS = "matthiesen_lib_webhooks";
        public static final String COBBREEDING = "cobbreeding";
        public static final ResourceLocation COBBREEDING_EGG = ResourceLocation.parse("cobbreeding:manaphy_egg");
    }

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }
}
