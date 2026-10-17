package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens;

import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ButtonAction;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.ServerPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public record CancelConfirmGuiBuilder(
        ServerPlayer player,
        String title,
        Consumer<ButtonAction> yesButtonAction,
        Consumer<ButtonAction> noButtonAction,
        Button optionalDetails
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
                (noButtonAction != null) ? (action) -> noButtonAction.run() : null,
                null
        );
    }

    public CancelConfirmGuiBuilder(
            ServerPlayer player,
            String title,
            Runnable yesButtonAction,
            Runnable noButtonAction,
            Button optionalDetails
    ) {
        this(
                player,
                title,
                (yesButtonAction != null) ? (action) -> yesButtonAction.run() : null,
                (noButtonAction != null) ? (action) -> noButtonAction.run() : null,
                optionalDetails
        );
    }

    public Component getTitle() {
        return Helpers.text(title);
    }

    public Page getPage() {
        Button cancelButton = ServerButton.builder()
                .display(MenuUtils.getNoItem())
                .onClick(noButtonAction)
                .build();

        Button confirmButton = ServerButton.builder()
                .display(MenuUtils.getYesItem())
                .onClick(yesButtonAction)
                .build();

        ChestTemplate.Builder builder = ChestTemplate.builder(3)
                .row(0, Helpers.getFrame())
                .row(1, Helpers.getFrame())
                .set(1, 3, cancelButton)
                .set(1, 5, confirmButton)
                .row(2, Helpers.getFrame());

        if (optionalDetails != null) {
            builder = builder.set(1, 4, optionalDetails);
        }

        ChestTemplate template = builder.build();
        ServerPage page = ServerPage.builder().template(template).build();
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
