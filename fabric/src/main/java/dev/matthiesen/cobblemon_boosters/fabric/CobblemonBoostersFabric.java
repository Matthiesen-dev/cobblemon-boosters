package dev.matthiesen.cobblemon_boosters.fabric;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import net.fabricmc.api.ModInitializer;

public final class CobblemonBoostersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var instance = CobblemonBoosters.INSTANCE;
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
    }
}
