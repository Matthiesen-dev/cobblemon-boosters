package dev.matthiesen.common.cobblemon_boosters.utils;

import dev.matthiesen.common.matthiesen_lib_api.utility.ItemBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BoostersItemBuilder {
    private final ItemBuilder libItemBuilder;
    private final List<Consumer<ItemStack>> customDataComponentMutators = new ArrayList<>();

    @SuppressWarnings({"SameParameterValue"})
    private <T> void appendData(DataComponentType<T> dataComponentType, T dataValue) {
        customDataComponentMutators.add(itemStack -> itemStack.set(dataComponentType, dataValue));
    }

    public BoostersItemBuilder(Item item) {
        this.libItemBuilder = new ItemBuilder(new ItemStack(item));
    }

    public BoostersItemBuilder(ItemStack item) {
        this.libItemBuilder = new ItemBuilder(item);
    }

    public BoostersItemBuilder addLore(Component[] newLore) {
        libItemBuilder.addLore(newLore);
        return this;
    }

    public BoostersItemBuilder hideAdditional() {
        libItemBuilder.hideAdditional();
        return this;
    }

    public BoostersItemBuilder setCustomName(Component customName) {
        libItemBuilder.setCustomName(customName);
        return this;
    }

    public BoostersItemBuilder setEnchanted(boolean enchanted) {
        appendData(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, enchanted);
        return this;
    }

    public ItemStack build() {
        ItemStack initial = libItemBuilder.build();
        customDataComponentMutators.forEach(mutator -> mutator.accept(initial));
        return initial;
    }
}
