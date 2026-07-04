package dev.matthiesen.cobblemon_boosters.common.event_handlers;

import dev.matthiesen.cobblemon_boosters.common.managers.BoostManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerEventHandler implements MatthiesenLibPlayerEventHandler {
    @Override
    public void onPlayerJoin(ServerPlayer player) {
        BoostManager.appendPlayer(player);
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        BoostManager.clearPlayer(player);
    }
}
