package dev.matthiesen.neoforge.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class CobblemonBoostersNeoForge {
    CobblemonBoosters core = new CobblemonBoosters();

    public CobblemonBoostersNeoForge() {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        core.initialize();
    }
}
