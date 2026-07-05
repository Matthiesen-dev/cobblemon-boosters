package dev.matthiesen.cobblemon_boosters.fabric;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import net.fabricmc.api.ModInitializer;

public final class CobblemonBoostersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var instance = CobblemonBoostersCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
    }
}
