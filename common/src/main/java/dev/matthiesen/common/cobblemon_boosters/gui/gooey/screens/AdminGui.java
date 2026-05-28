package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AdminGui extends BaseMenuGuiTemplate {

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
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostQueueCleared
        ));
        queueEntries.add(new QueueListEntry(
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostQueueCleared
        ));
        queueEntries.add(new QueueListEntry(
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostQueueCleared
        ));
        queueEntries.add(new QueueListEntry(
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostQueueCleared
        ));
        for (QueueListEntry entry : queueEntries) {
            entry.queueEntry.clear();
            sendPlayerMessage(entry.clearedMessage);
        }
        CacheConfig.setGlobalBoostData();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        // Reload
        if (PermissionRegistry.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getReloadItem())
                    .onClick(() -> new CancelConfirmGuiBuilder(
                            player,
                            "&cConfirm to reload",
                            () -> {
                                CobblemonBoosters.INSTANCE.reload(true);
                                sendPlayerMessage(CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.commandReload);
                                close();
                            },
                            this::open
                    ).open())
                    .build()
            );

        // Clear Queues
        if (PermissionRegistry.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION))
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
