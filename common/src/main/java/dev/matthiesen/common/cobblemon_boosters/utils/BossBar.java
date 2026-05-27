package dev.matthiesen.common.cobblemon_boosters.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.BossEvent;

public class BossBar {
    private static ServerBossEvent bossBar;

    public BossBar(Component component, float initialProgress, BossEvent.BossBarColor bossBarColor, BossEvent.BossBarOverlay bossBarOverlay) {
        bossBar = new ServerBossEvent(component, bossBarColor, bossBarOverlay);
        bossBar.setProgress(initialProgress);
    }

    public void addPlayer(ServerPlayer player) {
        if (bossBar != null) {
            bossBar.addPlayer(player);
        }
    }

    public void removePlayer(ServerPlayer player) {
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }

    public void updateProgress(float progress) {
        if (bossBar != null) {
            bossBar.setProgress(progress);
        }
    }

    public void setName(Component component) {
        if (bossBar != null) {
            bossBar.setName(component);
        }
    }

    public void verifyPlayerList(PlayerList list) {
        if (bossBar != null) {
            for (ServerPlayer sp : list.getPlayers()) {
                if (!bossBar.getPlayers().contains(sp)) {
                    bossBar.addPlayer(sp);
                }
            }
        }
    }

    public void hideBossBarFromPlayerList(PlayerList list) {
        for (ServerPlayer sp : list.getPlayers()) {
            removePlayer(sp);
        }
    }

    public void showBossBarFromPlayerList(PlayerList list) {
        for (ServerPlayer sp : list.getPlayers()) {
            addPlayer(sp);
        }
    }
}
