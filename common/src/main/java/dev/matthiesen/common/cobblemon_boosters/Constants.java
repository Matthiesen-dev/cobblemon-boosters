package dev.matthiesen.common.cobblemon_boosters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Constants {
    public static final String MOD_ID = "cobblemon_boosters";
    public static final String ModName = "Cobblemon Boosters";
    public static final List<String> CURRENT_BOOSTERS = List.of(
            "bucket",
            "catch",
            "experience",
            "shiny"
    );

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }
}
