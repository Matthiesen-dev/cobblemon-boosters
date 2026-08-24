package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class AdminGui extends BaseMenuGuiTemplate {

    public AdminGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserialize(
                TextUtils.parse("&cAdmin Menu&r")
        );
    }

    private void getQueuesAndClear() {
        List<BoosterGuiDefinition<?>> definitions = BoostController.getGuiDefinitions();
        for (BoosterGuiDefinition<?> definition : definitions) {
            definition.getBoostQueue().clear();
            sendPlayerMessage(definition.getMessages().boostQueueCleared());
        }
        CacheServerConfig.setGlobalBoostData();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        var permissions = PermissionRegistry.getPermissions();

        // Reload
        if (PermissionRegistry.checkPermission(player, permissions.RELOAD_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getReloadItem())
                    .onClick(() -> new CancelConfirmGuiBuilder(
                            player,
                            "&cConfirm to reload",
                            () -> {
                                CobblemonBoostersCommon.INSTANCE.reloadTask();
                                sendPlayerMessage(BoostersConfig.CORE_SERVER_CONFIG.messages_commandReload.get());
                                close();
                            },
                            this::open
                    ).open())
                    .build()
            );

        // Clear Queues
        if (PermissionRegistry.checkPermission(player, permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getClearQueueItem())
                    .onClick(() -> new CancelConfirmGuiBuilder(
                            player,
                            "&cConfirm to clear all Queues",
                            () -> {
                                getQueuesAndClear();
                                close();
                            },
                            this::open
                    ).open())
                    .build()
            );

        return buttons;
    }
}
