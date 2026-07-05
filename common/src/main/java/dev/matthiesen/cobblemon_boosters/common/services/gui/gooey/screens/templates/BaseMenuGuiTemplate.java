package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.collect.Lists;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
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

        GooeyPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        page.setTitle(getTitle());
        return page;
    }

    @Override
    public void open() {
        UIManager.openUIForcefully(player, getPage());
    }

    @Override
    public void close() {
        UIManager.closeUI(player);
    }

    @Override
    public void sendPlayerMessage(String rawMessage) {
        Helpers.sendPlayerMessage(player, rawMessage);
    }
}
