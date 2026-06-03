package dev.matthiesen.fabric.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.fabricmc.api.ModInitializer;

public final class CobblemonBoostersFabric implements ModInitializer {
    CobblemonBoosters core = new CobblemonBoosters();

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        core.initialize();
    }
}
