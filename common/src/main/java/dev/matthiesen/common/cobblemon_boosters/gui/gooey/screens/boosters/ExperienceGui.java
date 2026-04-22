package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.boosters;

import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoostersGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Queue;

public class ExperienceGui extends BoostersGuiTemplate {

    public ExperienceGui(String boostType, ServerPlayer player, IBoost activeBoost, Queue<? extends IBoost> queuedBoosts, String noActiveBoost, String stopBoostMsg, ModPermission startPermission, ModPermission stopPermission, ModPermission statusPermission, ModPermission queuePermission) {
        super(boostType, player, activeBoost, queuedBoosts, noActiveBoost, stopBoostMsg, startPermission, stopPermission, statusPermission, queuePermission);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("<gold>Cobblemon Boosters <reset>- <green>Experience Boosts<reset>")
        );
    }
}
