package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.collect.Lists;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGui;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class BaseMenuGuiTemplate implements IGui {
    public ServerPlayer player;

    public BaseMenuGuiTemplate(ServerPlayer player) {
        this.player = player;
    }

    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("<gold>Cobblemon Boosters<reset>")
        );
    }

    public List<Button> getButtons() {
        return Lists.newArrayList();
    }

    public Page getPage() {
        List<Button> buttons = getButtons();

        ChestTemplate template = ChestTemplate.builder(3)
                .row(0, Helpers.getFrame())
                .row(1, Helpers.getFrame())
                .set(1, 1, Helpers.getPlaceholder())
                .set(1, 2, Helpers.getPlaceholder())
                .set(1, 3, Helpers.getPlaceholder())
                .set(1, 4, Helpers.getPlaceholder())
                .set(1, 5, Helpers.getPlaceholder())
                .set(1, 6, Helpers.getPlaceholder())
                .set(1, 7, Helpers.getPlaceholder())
                .row(2, Helpers.getFrame())
                .build();

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
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(TextUtils.parse(rawMessage)));
    }
}
