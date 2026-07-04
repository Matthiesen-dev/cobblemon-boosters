package dev.matthiesen.cobblemon_boosters.common.display;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

import java.util.List;

/**
 * Compact display: a vanilla scoreboard sidebar on the right side of the screen, with one line
 * per active boost. Uses the server-global scoreboard, so every player sees the same sidebar
 * (boosts are global anyway). The score numbers are hidden and each line's text is set directly
 * via {@link ScoreAccess#display}.
 */
public final class SidebarDisplay implements BoostDisplayService {
    private static final String OBJECTIVE_NAME = "cb_boosters";
    private static final String ENTRY_PREFIX = "cbb_";

    // How many lines are currently shown, so stale entries can be reset when the count shrinks.
    private int shownLines = 0;

    @Override
    public void onPlayerJoin(ServerPlayer player) {
        // Global scoreboard: the client is synced automatically on join.
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        // Nothing per-player to clean up.
    }

    @Override
    public void onBoostActivated(IBoost boost) {
        rebuild(getServer());
    }

    @Override
    public void onBoostDeactivated(IBoost boost) {
        rebuild(getServer());
    }

    @Override
    public void tick(MinecraftServer server) {
        // Remaining time is second-granular, so refreshing once per second is enough.
        if (server.getTickCount() % 20 != 0) {
            return;
        }
        rebuild(server);
    }

    @Override
    public void shutdown(MinecraftServer server) {
        if (server == null) {
            return;
        }
        ServerScoreboard scoreboard = server.getScoreboard();
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        if (objective != null) {
            scoreboard.removeObjective(objective);
        }
        shownLines = 0;
    }

    private void rebuild(MinecraftServer server) {
        if (server == null) {
            return;
        }

        ServerScoreboard scoreboard = server.getScoreboard();
        List<IBoost> active = BoostManager.getActiveBoosts();

        if (active.isEmpty()) {
            shutdown(server);
            return;
        }

        Objective objective = ensureObjective(scoreboard);

        for (int i = 0; i < active.size(); i++) {
            IBoost boost = active.get(i);
            ScoreHolder holder = ScoreHolder.forNameOnly(ENTRY_PREFIX + i);
            ScoreAccess score = scoreboard.getOrCreatePlayerScore(holder, objective);
            // Higher score sorts higher on the sidebar, so the first boost stays on top.
            score.set(active.size() - i);
            score.display(boost.getSidebarText());
            score.numberFormatOverride(BlankFormat.INSTANCE);
        }

        // Drop any lines left over from a previous, longer render.
        for (int i = active.size(); i < shownLines; i++) {
            scoreboard.resetSinglePlayerScore(ScoreHolder.forNameOnly(ENTRY_PREFIX + i), objective);
        }
        shownLines = active.size();
    }

    private Objective ensureObjective(ServerScoreboard scoreboard) {
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        var title = TextUtils.deserialize(TextUtils.parse(
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.sidebarTitle));

        if (objective == null) {
            objective = scoreboard.addObjective(
                    OBJECTIVE_NAME,
                    ObjectiveCriteria.DUMMY,
                    title,
                    ObjectiveCriteria.RenderType.INTEGER,
                    false,
                    BlankFormat.INSTANCE
            );
        } else {
            // Keep the title in sync with the config (e.g. after a reload).
            objective.setDisplayName(title);
        }

        scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, objective);
        return objective;
    }

    private static MinecraftServer getServer() {
        return dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi.getMinecraftServer();
    }
}
