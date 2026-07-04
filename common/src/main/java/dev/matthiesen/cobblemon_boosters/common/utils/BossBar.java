package dev.matthiesen.cobblemon_boosters.common.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.BossEvent;

public final class BossBar {
    private final Builder builder;

    public BossBar(Component comp, float initProgress, BossEvent.BossBarColor bbColor, BossEvent.BossBarOverlay bbOverlay) {
        this.builder = new Builder(comp, initProgress, bbColor, bbOverlay);
    }

    public Builder getBuilder() {
        return this.builder;
    }

    public static class Builder {
        private final ServerBossEvent bossBar;

        public Builder(Component component, float initialProgress, BossEvent.BossBarColor bossBarColor, BossEvent.BossBarOverlay bossBarOverlay) {
            this.bossBar = new ServerBossEvent(component, bossBarColor, bossBarOverlay);
            this.bossBar.setProgress(initialProgress);
        }

        public void addPlayer(ServerPlayer player) {
            this.bossBar.addPlayer(player);
        }

        public void removePlayer(ServerPlayer player) {
            this.bossBar.removePlayer(player);
        }

        public void updateProgress(float progress) {
            this.bossBar.setProgress(progress);
        }

        public void setName(Component component) {
            this.bossBar.setName(component);
        }

        public void verifyPlayerList(PlayerList list) {
            for (ServerPlayer sp : list.getPlayers()) {
                if (!this.bossBar.getPlayers().contains(sp)) {
                    this.bossBar.addPlayer(sp);
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
}
