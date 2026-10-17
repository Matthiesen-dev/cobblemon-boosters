package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens;

import dev.matthiesen.cobblemon_boosters.common.services.BoostControllerServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens.QueueGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class CheckQueuesGui extends BaseMenuGuiTemplate {

    public CheckQueuesGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserialize(
                TextUtils.parse("&aAll Boost Queues&r")
        );
    }

    public Button getQueueButton(BoosterGuiDefinition<?> definition) {
        return ServerButton.builder()
                .display(MenuUtils.getQueueItem(definition.getMenuItem().getItem(), definition.getDisplayName(), false))
                .onClick(() -> new QueueGui(
                        player,
                        definition.getDisplayName(),
                        definition.getBoostQueue()
                ).open())
                .build();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        for (BoosterGuiDefinition<?> definition : BoostControllerServiceManager.getGuiDefinitions()) {
            buttons.add(getQueueButton(definition));
        }
        return buttons;
    }
}
