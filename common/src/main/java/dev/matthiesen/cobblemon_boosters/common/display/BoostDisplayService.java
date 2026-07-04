package dev.matthiesen.cobblemon_boosters.common.display;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Renders active boosts to players. Implementations decide the medium (boss bar, scoreboard
 * sidebar, or nothing). The service is selected from {@code CoreConfig.displayMode} and can be
 * swapped at runtime on a config reload.
 *
 * <p>The four boost types can be active at the same time, so implementations are expected to
 * render every currently-active boost, not a single one.</p>
 */
public interface BoostDisplayService {
    /** A player joined: make sure they can see the currently active boosts. */
    void onPlayerJoin(ServerPlayer player);

    /** A player left: drop any per-player display state. */
    void onPlayerLeave(ServerPlayer player);

    /** A boost just became active. */
    void onBoostActivated(IBoost boost);

    /** A boost just ended or was replaced. */
    void onBoostDeactivated(IBoost boost);

    /** Called every server tick to refresh progress/remaining-time text. */
    void tick(MinecraftServer server);

    /** Remove all visuals owned by this service (used when switching modes or disabling). */
    void shutdown(MinecraftServer server);
}
