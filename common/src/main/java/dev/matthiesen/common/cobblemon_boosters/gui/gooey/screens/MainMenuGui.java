package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.AdminGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoosterGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainMenuGui extends BoosterGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.BUCKET_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getBucketItem())
                    .onClick(() -> new BucketGui(player).open())
                    .build()
            );

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CATCH_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getCatchItem())
                    .onClick(() -> new CatchGui(player).open())
                    .build()
            );

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getExperienceItem())
                    .onClick(() -> new ExperienceGui(player).open())
                    .build()
            );

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getShinyItem())
                    .onClick(() -> new ShinyGui(player).open())
                    .build()
            );

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getQueueItem())
                    .onClick(() -> new CheckQueuesGui(player).open())
                    .build()
            );

        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION) || ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getAdminItem())
                    .onClick(() -> new AdminGui(player).open())
                    .build()
            );

        return buttons;
    }
}
