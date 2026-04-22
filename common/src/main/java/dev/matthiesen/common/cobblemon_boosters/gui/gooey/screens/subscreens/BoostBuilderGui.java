package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGui;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class BoostBuilderGui implements IGui {
    public final ServerPlayer player;
    public final String queueName;

    public BoostBuilderGui(ServerPlayer player, String queueName) {
        this.player = player;
        this.queueName = queueName;
    }

    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("Cobblemon Boosters - Builder")
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
                .row(1, frame)
                .set(1, 1, placeholder)
                .set(1, 2, placeholder)
                .set(1, 3, placeholder)
                .set(1, 4, placeholder)
                .set(1, 5, placeholder)
                .set(1, 6, placeholder)
                .set(1, 7, placeholder)
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

    @Override
    public void close() {
        UIManager.closeUI(player);
    }

    @Override
    public void sendPlayerMessage(String rawMessage) {
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(TextUtils.parse(rawMessage)));
    }
}
