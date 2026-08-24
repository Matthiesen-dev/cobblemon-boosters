package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.BoostBuilderGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.BucketBoostBuilderGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates.BoostersGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuGui extends BaseMenuGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    public static void sendServerPlayerMessage(ServerPlayer player, String rawMessage, IBoost boost) {
        player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(rawMessage, boost)));
    }

    public static void queueBoostAndNotify(ServerPlayer player, BoosterGuiDefinition<?> definition, IBoost boost) {
        definition.queueBoost(boost);
        sendServerPlayerMessage(player, definition.getMessages().boostAddedToQueue(), boost);
        CacheServerConfig.setGlobalBoostData();
    }

    public static void openStartBuilder(ServerPlayer player, BoosterGuiDefinition<?> definition) {
        switch (definition.getBuilderType()) {
            case MULTIPLIER -> new BoostBuilderGui(
                    player,
                    definition.getDisplayName(),
                    definition.getBoostClass(),
                    boost -> queueBoostAndNotify(player, definition, boost)
            ).open();
            case SPAWN_BUCKET -> new BucketBoostBuilderGui(
                    player,
                    definition.getDisplayName(),
                    boost -> queueBoostAndNotify(player, definition, boost)
            ).open();
        }
    }

    public static void openBoosterGui(ServerPlayer player, String boosterId) {
        var definition = BoostController.getGuiDefinition(boosterId);
        if (definition == null) {
            player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse("%prefix% &cUnknown booster type: &f" + boosterId + "&c.")));
            return;
        }

        new BoostersGuiTemplate(
                player,
                definition,
                () -> openStartBuilder(player, definition)
        ).open();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        var permissions = PermissionRegistry.getPermissions();

        for (BoosterGuiDefinition<?> definition : BoostController.getGuiDefinitions()) {
            if (!PermissionRegistry.checkPermission(player, definition.getRootPermission())) {
                continue;
            }

            buttons.add(GooeyButton.builder()
                    .display(definition.getMenuItem())
                    .onClick(() -> openBoosterGui(player, definition.getCommandId()))
                    .build()
            );
        }

        // Check Queues
        if (PermissionRegistry.checkPermission(player, permissions.CHECK_QUEUE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getQueueItem("All", true))
                    .onClick(() -> new CheckQueuesGui(player).open())
                    .build()
            );

        // Admin Options
        if (PermissionRegistry.checkPermission(player, permissions.RELOAD_PERMISSION) ||
                PermissionRegistry.checkPermission(player, permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getAdminItem())
                    .onClick(() -> new AdminGui(player).open())
                    .build()
            );

        return buttons;
    }
}
