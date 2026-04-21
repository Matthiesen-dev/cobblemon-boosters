package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGui;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record ExperienceGui(ServerPlayer player) implements IGui {

    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("Cobblemon Boosters - Experience Boosts")
        );
    }

    public Page getPage() {
        PlaceholderButton placeholder = new PlaceholderButton();

        List<Button> buttons = new ArrayList<>();

        Button frame = GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();

        ChestTemplate template = ChestTemplate.builder(3)
                .row(0, frame)
                .set(1, 0, frame)
                .set(1, 1, placeholder)
                .set(1, 2, placeholder)
                .set(1, 3, placeholder)
                .set(1, 4, placeholder)
                .set(1, 5, placeholder)
                .set(1, 6, placeholder)
                .set(1, 7, placeholder)
                .set(1, 8, frame)
                .row(2, frame)
                .build();

        GooeyPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);

        page.setTitle(getTitle());

        return page;
    }

    @Override
    public void open() {
        UIManager.openUIForcefully(player, getPage());
    }
}
