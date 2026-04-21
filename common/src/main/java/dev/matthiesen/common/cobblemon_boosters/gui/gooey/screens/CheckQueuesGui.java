package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoosterGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class CheckQueuesGui extends BoosterGuiTemplate {

    public CheckQueuesGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("Cobblemon Boosters - Queues")
        );
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        // Add your boost buttons here, for example:
        // buttons.add(...);

        return buttons;
    }
}
