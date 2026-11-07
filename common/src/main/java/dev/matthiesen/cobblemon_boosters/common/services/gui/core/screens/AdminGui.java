package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.services.BoostControllerServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
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
        return TextUtils.getTextParser().parse(BoostersConfig.TEXT_CONFIG.gui_adminGui_title.get(), TextUtils.getPlaceholders());
    }

    private void getQueuesAndClear() {
        List<BoosterGuiDefinition<?>> definitions = BoostControllerServiceManager.getGuiDefinitions();
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
        var config = BoostersConfig.TEXT_CONFIG;

        // Reload
        if (PermissionRegistry.checkPermission(player, permissions.RELOAD_PERMISSION))
            buttons.add(ServerButton.builder()
                    .display(MenuUtils.getReloadItem())
                    .onClick(() -> new CancelConfirmGuiBuilder(
                            player,
                            config.gui_adminGui_button_reloadTitle.get(),
                            () -> {
                                CobblemonBoostersCommon.INSTANCE.reloadTask();
                                sendPlayerMessage(BoostersConfig.TEXT_CONFIG.messages_commandReload.get());
                                close();
                            },
                            this::open
                    ).open())
                    .build()
            );

        // Clear Queues
        if (PermissionRegistry.checkPermission(player, permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(ServerButton.builder()
                    .display(MenuUtils.getClearQueueItem())
                    .onClick(() -> new CancelConfirmGuiBuilder(
                            player,
                            config.gui_adminGui_button_clearQueuesTitle.get(),
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
