package dev.matthiesen.cobblemon_boosters.neoforge;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class CobblemonBoostersNeoForge {
    public CobblemonBoostersNeoForge() {
        var instance = CobblemonBoosters.INSTANCE;
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        instance.initialize();
    }
}
