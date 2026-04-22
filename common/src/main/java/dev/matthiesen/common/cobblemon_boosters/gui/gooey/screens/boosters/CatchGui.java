package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.boosters;

import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoostersGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Queue;

public class CatchGui extends BoostersGuiTemplate {

    public CatchGui(String boostType, ServerPlayer player, IBoost activeBoost, Queue<? extends IBoost> queuedBoosts, String noActiveBoost, String stopBoostMsg, String boostInfo, ModPermission startPermission, ModPermission stopPermission, ModPermission statusPermission, ModPermission queuePermission, Runnable startOnClick) {
        super(boostType, player, activeBoost, queuedBoosts, noActiveBoost, stopBoostMsg, boostInfo, startPermission, stopPermission, statusPermission, queuePermission, startOnClick);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("<light_purple>Catch Boosts<reset>")
        );
    }
}
