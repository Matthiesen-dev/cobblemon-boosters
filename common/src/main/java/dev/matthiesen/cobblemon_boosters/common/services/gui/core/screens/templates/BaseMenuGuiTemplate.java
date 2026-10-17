package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.templates;

import com.google.common.collect.Lists;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.helpers.PaginationHelper;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.ServerPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class BaseMenuGuiTemplate implements IGui {
    public ServerPlayer player;

    public BaseMenuGuiTemplate(ServerPlayer player) {
        this.player = player;
    }

    public Component getTitle() {
        return Helpers.text("&6Cobblemon Boosters&r");
    }

    public List<Button> getButtons() {
        return Lists.newArrayList();
    }

    public Page getPage() {
        List<Button> buttons = getButtons();

        ChestTemplate template = Helpers.getBaseChestTemplate().build();

        ServerPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        page.setTitle(getTitle());
        return page;
    }

    @Override
    public void open() {
        ServerUIManager.openUIForcefully(player, getPage());
    }

    @Override
    public void close() {
        ServerUIManager.closeUI(player);
    }

    @Override
    public void sendPlayerMessage(String rawMessage) {
        Helpers.sendPlayerMessage(player, rawMessage);
    }
}
