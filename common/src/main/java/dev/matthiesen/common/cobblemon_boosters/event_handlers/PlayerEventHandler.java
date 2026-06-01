package dev.matthiesen.common.cobblemon_boosters.event_handlers;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerEventHandler implements MatthiesenLibPlayerEventHandler {
    @Override
    public void onPlayerJoin(ServerPlayer player) {
        CobblemonBoosters.INSTANCE.boostManager.appendPlayer(player);
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        CobblemonBoosters.INSTANCE.boostManager.clearPlayer(player);
    }
}
