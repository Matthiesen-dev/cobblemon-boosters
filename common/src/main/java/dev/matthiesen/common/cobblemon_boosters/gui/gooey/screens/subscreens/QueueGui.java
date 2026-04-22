package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlotDelegate;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGui;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public record QueueGui(ServerPlayer player, String queueName, Queue<? extends IBoost> queuedBoosts) implements IGui {

    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("<gold>Cobblemon Boosters <reset>- <aqua>" + queueName + " Queue")
        );
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
        PlaceholderButton placeholder = new PlaceholderButton();

        List<Button> buttons = new ArrayList<>();

        if (!queuedBoosts.isEmpty()) {
            List<IBoost> boosts = new ArrayList<>(queuedBoosts);
            for (IBoost boost : boosts) {
                List<Component> loreComponents = new ArrayList<>();

                loreComponents.add(TextUtils.deserializeMC(TextUtils.parse("<white>Duration: <green>%duration%", boost)));

                if (boost instanceof SpawnBucketBoost && ((SpawnBucketBoost) boost).bucket != null) {
                    loreComponents.add(TextUtils.deserializeMC(TextUtils.parse("<green>Bucket: <red>%bucket%", boost)));
                }

                Component[] lore = new Component[loreComponents.size()];
                for (int i = 0; i < loreComponents.size(); i++) {
                    lore[i] = loreComponents.get(i);
                }

                Button boostButton = GooeyButton.builder()
                        .display(
                                MenuUtils.getQueueEntryBuilder()
                                    .setCustomName(
                                            TextUtils.deserializeMC(
                                                    TextUtils.parse(
                                                            "<green>%multiplier%x " + boost.getBoostType() + "</green>",
                                                            boost
                                                    )
                                            )
                                    )
                                    .addLore(lore)
                                    .build())
                        .build();

                buttons.add(boostButton);
            }
        }

        Button frame = GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();

        LinkedPageButton previous = LinkedPageButton.builder()
                .display(MenuUtils.getNavItem("Previous"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(MenuUtils.getNavItem("Next"))
                .linkType(LinkType.Next)
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
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(TextUtils.parse(rawMessage)));
    }
}
