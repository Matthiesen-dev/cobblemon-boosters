package dev.matthiesen.cobblemon_boosters.common.services.display;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoostDisplayService;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Default display: a vanilla boss bar per active boost, stacked at the top of the screen.
 * Preserves the mod's original behavior.
 */
public final class BossBarDisplay implements IBoostDisplayService {

    @Override
    public void onPlayerJoin(ServerPlayer player) {
        for (IBoost boost : BoostManager.getActiveBoosts()) {
            boost.getBossBar().addPlayer(player);
        }
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        for (IBoost boost : BoostManager.getActiveBoosts()) {
            boost.getBossBar().removePlayer(player);
        }
    }

    @Override
    public void onBoostActivated(IBoost boost) {
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server != null) {
            boost.getBossBar().showBossBarFromPlayerList(server.getPlayerList());
        }
    }

    @Override
    public void onBoostDeactivated(IBoost boost) {
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server != null) {
            boost.getBossBar().hideBossBarFromPlayerList(server.getPlayerList());
        }
    }

    @Override
    public void tick(MinecraftServer server) {
        boolean verifyPlayers = server.getTickCount() % 20 == 0;
        for (IBoost boost : BoostManager.getActiveBoosts()) {
            updateBossBar(boost);
            if (verifyPlayers) {
                boost.getBossBar().verifyPlayerList(server.getPlayerList());
            }
        }
    }

    @Override
    public void shutdown(MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (IBoost boost : BoostManager.getActiveBoosts()) {
            boost.getBossBar().hideBossBarFromPlayerList(server.getPlayerList());
        }
    }

    private static void updateBossBar(IBoost boost) {
        float progressRate = 1.0F / (boost.getDuration() * 20L);
        float total = progressRate * boost.getTimeRemaining();

        if (total < 0F) {
            total = 0F;
        }
        if (total > 1F) {
            total = 1F;
        }

        boost.getBossBar().updateProgress(total);
        boost.getBossBar().setName(boost.getBossBarText());
    }
}
