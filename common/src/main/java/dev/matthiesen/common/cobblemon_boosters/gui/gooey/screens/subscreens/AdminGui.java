package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoosterGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdminGui extends BoosterGuiTemplate {

    public AdminGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("Cobblemon Boosters - Admin Menu")
        );
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(GooeyButton.builder()
                .display(MenuUtils.getReloadItem())
                .onClick(() -> {
                    // TODO
                })
                .build()
        );

        buttons.add(GooeyButton.builder()
                .display(MenuUtils.getClearQueueItem())
                .onClick(() -> {
                    // TODO
                })
                .build()
        );

        return buttons;
    }
}
