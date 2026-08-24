package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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

    public static class QueueListEntry {
        Queue<? extends IBoost> queueEntry;
        String clearedMessage;

        public QueueListEntry(Queue<? extends IBoost> queueEntry, String clearedMessage) {
            this.queueEntry = queueEntry;
            this.clearedMessage = clearedMessage;
        }
    }

    private void getQueuesAndClear() {
        List<QueueListEntry> queueEntries = new ArrayList<>();
        queueEntries.add(new QueueListEntry(
                BoostController.getShinyBoostManager().getBoostQueue(),
                BoostersConfig.CORE_SERVER_CONFIG.messages_shiny_boostQueueCleared.get()
        ));
        queueEntries.add(new QueueListEntry(
                BoostController.getCatchBoostManager().getBoostQueue(),
                BoostersConfig.CORE_SERVER_CONFIG.messages_catch_boostQueueCleared.get()
        ));
        queueEntries.add(new QueueListEntry(
                BoostController.getExperienceBoostManager().getBoostQueue(),
                BoostersConfig.CORE_SERVER_CONFIG.messages_experience_boostQueueCleared.get()
        ));
        queueEntries.add(new QueueListEntry(
                BoostController.getSpawnBucketBoostManager().getBoostQueue(),
                BoostersConfig.CORE_SERVER_CONFIG.messages_spawnBucket_boostQueueCleared.get()
        ));
        for (QueueListEntry entry : queueEntries) {
            entry.queueEntry.clear();
            sendPlayerMessage(entry.clearedMessage);
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
