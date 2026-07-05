package dev.matthiesen.cobblemon_boosters.neoforge;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import net.neoforged.fml.common.Mod;

@Mod(CobblemonBoostersCommon.MOD_ID)
public final class CobblemonBoostersNeoForge {
    public CobblemonBoostersNeoForge() {
        var instance = CobblemonBoostersCommon.INSTANCE;
        instance.createInfoLog("Loading for NeoForge Mod Loader");
        instance.initialize();
    }
}
