package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BoostersGuiTemplate extends BaseMenuGuiTemplate {
    public final String boostType;
    public final IBoost activeBoost;
    public final Queue<? extends IBoost> queuedBoosts;
    public final String noActiveBoost;
    public final String stopBoostMsg;
    public final ModPermission startPermission;
    public final ModPermission stopPermission;
    public final ModPermission statusPermission;
    public final ModPermission queuePermission;

    public BoostersGuiTemplate(
            String boostType,
            ServerPlayer player,
            IBoost activeBoost,
            Queue<? extends IBoost> queuedBoosts,
            String noActiveBoost,
            String stopBoostMsg,
            ModPermission startPermission,
            ModPermission stopPermission,
            ModPermission statusPermission,
            ModPermission queuePermission
    ) {
        super(player);
        this.boostType = boostType;
        this.activeBoost = activeBoost;
        this.queuedBoosts = queuedBoosts;
        this.noActiveBoost = noActiveBoost;
        this.stopBoostMsg = stopBoostMsg;
        this.startPermission = startPermission;
        this.stopPermission = stopPermission;
        this.statusPermission = statusPermission;
        this.queuePermission = queuePermission;
    }

    public Button getStopButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getStopButton(activeBoost != null))
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        "<red>Confirm to stop active boost!",
                        () -> {
                            if (activeBoost != null) {
                                activeBoost.setTimeRemaining(1);
                                player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(stopBoostMsg)));
                                close();
                            } else {
                                player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(noActiveBoost)));
                                open();
                            }
                        },
                        this::open
                ).open())
                .build();
    }

    public Button getQueueButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getQueueItem(boostType))
                .onClick(() -> new QueueGui(
                        player,
                        boostType,
                        queuedBoosts
                ).open())
                .build();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

//        if (ModPermissions.checkPermission(player, startPermission)) {
//            // TODO: Start
//        }

        if (ModPermissions.checkPermission(player, stopPermission)) {
            buttons.add(getStopButton());
        }

//        if (ModPermissions.checkPermission(player, statusPermission)) {
//            // TODO: Status
//        }

        if (ModPermissions.checkPermission(player, queuePermission)) {
            buttons.add(getQueueButton());
        }

        return buttons;
    }
}
