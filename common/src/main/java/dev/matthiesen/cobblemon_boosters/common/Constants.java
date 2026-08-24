package dev.matthiesen.cobblemon_boosters.common;

import net.minecraft.resources.ResourceLocation;

public final class Constants {

    public static class COMPAT {
        public static final String GOOEYLIBS = "gooeylibs";
        public static final String MATTHIESEN_LIB_WEBHOOKS = "matthiesen_lib_webhooks";
        public static final String COBBREEDING = "cobbreeding";
        public static final ResourceLocation COBBREEDING_EGG = ResourceLocation.parse("cobbreeding:manaphy_egg");
    }

    public enum SupportedBoosterTypes {
        SPAWN_BUCKET,
        CATCH,
        EXPERIENCE,
        SHINY
    }
}
