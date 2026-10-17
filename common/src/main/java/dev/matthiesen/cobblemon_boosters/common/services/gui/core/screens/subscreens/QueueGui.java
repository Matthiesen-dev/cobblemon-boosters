package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens;

import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.linked.LinkType;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.linked.LinkedPageButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.helpers.PaginationHelper;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.LinkedPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public record QueueGui(ServerPlayer player, String queueName, Queue<? extends IBoost> queuedBoosts) implements IGui {

    public Component getTitle() {
        return Helpers.text("&b" + queueName + " Queue");
    }

    private Button getInfoButton(int currentPage, int pageLength) {
        return ServerButton.builder()
                .display(MenuUtils.getPageItem(currentPage, pageLength))
                .build();
    }

    private TemplateSlotDelegate getInfoButtonTemplate(int currentPage, int pageLength) {
        Button infoButton = getInfoButton(pageLength, currentPage);
        return new TemplateSlotDelegate(infoButton, 22);
    }

    private void setPageTitleInternal(LinkedPage page, int pageLength) {
        int currentPage = page.getCurrentPage();
        page.setTitle(getTitle());
        page.getTemplate().setSlot(22, getInfoButtonTemplate(pageLength, currentPage));
    }

    private void setPageTitleRecursive(LinkedPage page) {
        int pageLength = page.getTotalPages();
        setPageTitleInternal(page, pageLength);
        LinkedPage next = page.getNext();
        if (next != null) {
            setPageTitleInternal(next, pageLength);
            setPageTitleRecursive(next);
        }
    }

    public Page getPage() {
        List<Button> buttons = new ArrayList<>();

        if (!queuedBoosts.isEmpty()) {
            List<IBoost> boosts = new ArrayList<>(queuedBoosts);
            for (IBoost boost : boosts) {
                List<Component> loreComponents = new ArrayList<>();

                loreComponents.add(Helpers.text("&fDuration: &a%duration%", boost));

                if (boost instanceof SpawnBucketBoost && ((SpawnBucketBoost) boost).bucket != null) {
                    loreComponents.add(Helpers.text("&aBucket: &c%bucket%", boost));
                }

                Component[] lore = Helpers.toComponentArray(loreComponents);

                Button boostButton = ServerButton.builder()
                        .display(boost.getGUIItem(lore))
                        .build();

                buttons.add(boostButton);
            }
        }

        LinkedPageButton previous = LinkedPageButton.builder()
                .display(MenuUtils.getNavItem("Previous"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(MenuUtils.getNavItem("Next"))
                .linkType(LinkType.Next)
                .build();

        ChestTemplate template = Helpers.getBaseChestTemplate()
                .set(2, 0, previous)
                .set(2, 4, getInfoButton(1, 1))
                .set(2, 8, next)
                .build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        setPageTitleRecursive(page);
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
