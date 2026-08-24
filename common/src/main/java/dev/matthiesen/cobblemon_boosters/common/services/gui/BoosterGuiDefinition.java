package dev.matthiesen.cobblemon_boosters.common.services.gui;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import net.minecraft.world.item.ItemStack;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BoosterGuiDefinition<T extends IBoost> {
    public enum BuilderType {
        MULTIPLIER,
        SPAWN_BUCKET
    }

    private final String commandId;
    private final String displayName;
    private final String menuTitle;
    private final Supplier<ItemStack> menuItemSupplier;
    private final Supplier<Booster<T>> boosterSupplier;
    private final Supplier<BoostMessagesConfig> messagesSupplier;
    private final Permission rootPermission;
    private final Permission startPermission;
    private final Permission stopPermission;
    private final Permission statusPermission;
    private final Permission queuePermission;
    private final BuilderType builderType;
    private final Class<T> boostClass;
    private final Consumer<T> queueAppender;

    public BoosterGuiDefinition(
            String commandId,
            String displayName,
            String menuTitle,
            Supplier<ItemStack> menuItemSupplier,
            Supplier<Booster<T>> boosterSupplier,
            Supplier<BoostMessagesConfig> messagesSupplier,
            Permission rootPermission,
            Permission startPermission,
            Permission stopPermission,
            Permission statusPermission,
            Permission queuePermission,
            BuilderType builderType,
            Class<T> boostClass,
            Consumer<T> queueAppender
    ) {
        this.commandId = commandId;
        this.displayName = displayName;
        this.menuTitle = menuTitle;
        this.menuItemSupplier = menuItemSupplier;
        this.boosterSupplier = boosterSupplier;
        this.messagesSupplier = messagesSupplier;
        this.rootPermission = rootPermission;
        this.startPermission = startPermission;
        this.stopPermission = stopPermission;
        this.statusPermission = statusPermission;
        this.queuePermission = queuePermission;
        this.builderType = builderType;
        this.boostClass = boostClass;
        this.queueAppender = queueAppender;
    }

    public String getCommandId() {
        return commandId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public ItemStack getMenuItem() {
        return menuItemSupplier.get().copy();
    }

    public Booster<T> getBooster() {
        return boosterSupplier.get();
    }

    public Queue<T> getBoostQueue() {
        return getBooster().getBoostQueue();
    }

    public T getActiveBoost() {
        return getBooster().getActiveBoost();
    }

    public BoostMessagesConfig getMessages() {
        return messagesSupplier.get();
    }

    public Permission getRootPermission() {
        return rootPermission;
    }

    public Permission getStartPermission() {
        return startPermission;
    }

    public Permission getStopPermission() {
        return stopPermission;
    }

    public Permission getStatusPermission() {
        return statusPermission;
    }

    public Permission getQueuePermission() {
        return queuePermission;
    }

    public BuilderType getBuilderType() {
        return builderType;
    }

    public Class<T> getBoostClass() {
        return boostClass;
    }

    @SuppressWarnings("unchecked")
    public void queueBoost(IBoost boost) {
        queueAppender.accept((T) boost);
    }
}

