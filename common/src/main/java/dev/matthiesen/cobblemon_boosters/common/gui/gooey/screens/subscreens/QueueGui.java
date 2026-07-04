package dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlotDelegate;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.cobblemon_boosters.common.data.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
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
        return GooeyButton.builder()
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

                Button boostButton = GooeyButton.builder()
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
