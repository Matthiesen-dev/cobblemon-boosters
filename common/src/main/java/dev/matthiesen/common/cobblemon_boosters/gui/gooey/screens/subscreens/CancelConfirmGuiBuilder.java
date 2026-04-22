package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
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
import java.util.function.Consumer;

public record CancelConfirmGuiBuilder(
        ServerPlayer player,
        String title,
        Consumer<ButtonAction> yesButtonAction,
        Consumer<ButtonAction> noButtonAction
) implements IGui {

    public CancelConfirmGuiBuilder(
            ServerPlayer player,
            String title,
            Runnable yesButtonAction,
            Runnable noButtonAction
    ) {
        this(
                player,
                title,
                (yesButtonAction != null) ? (action) -> yesButtonAction.run() : null,
                (noButtonAction != null) ? (action) -> noButtonAction.run() : null
        );
    }

    public Component getTitle() {
        return TextUtils.deserializeMC(TextUtils.parse(title));
    }

    public Page getPage() {
        PlaceholderButton placeholder = new PlaceholderButton();

        List<Button> buttons = new ArrayList<>();

        buttons.add(
                GooeyButton.builder()
                        .display(MenuUtils.getNoItem())
                        .onClick(noButtonAction)
                        .build()
        );
        buttons.add(
                GooeyButton.builder()
                        .display(MenuUtils.getYesItem())
                        .onClick(yesButtonAction)
                        .build()
        );

        Button frame = GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();

        ChestTemplate template = ChestTemplate.builder(3)
                .row(0, frame)
                .row(1, frame)
                .set(1, 3, placeholder)
                .set(1, 5, placeholder)
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
