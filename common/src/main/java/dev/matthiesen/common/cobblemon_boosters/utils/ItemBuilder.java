package dev.matthiesen.common.cobblemon_boosters.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Item item) {
        this.stack = new ItemStack(item);
    }

    public ItemBuilder(ItemStack item) {
        this.stack = item;
    }

    public ItemBuilder addLore(Component[] newLore) {
        ItemLore itemLore = stack.get(DataComponents.LORE);
        if (itemLore == null) {
            itemLore = new ItemLore(List.of());
        }

        List<Component> list = Stream.concat(itemLore.lines().stream(), Arrays.stream(newLore))
                .collect(Collectors.toList());

        // Go through every Component, and get the current style and set Italic to false
        list = list.stream()
                .map(component ->
                        component.copy().withStyle(component.getStyle().withItalic(false))
                )
                .collect(Collectors.toList());

        itemLore = new ItemLore(list);
        stack.set(DataComponents.LORE, itemLore);
        return this;
    }

    public ItemBuilder setCustomData(CustomData data) {
        // If the stack has custom data, get it and append the new data to it,
        // otherwise create a new custom data with the new data
        CustomData customData = stack.has(DataComponents.CUSTOM_DATA)
                ? stack.get(DataComponents.CUSTOM_DATA)
                : CustomData.of(new CompoundTag());

        assert customData != null;
        CompoundTag newTag = customData.copyTag();
        CompoundTag tag = data.copyTag();

        tag.getAllKeys().forEach(key -> newTag.put(key, tag.get(key)));

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
        return this;
    }

    public ItemBuilder hideAdditional() {
        stack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        return this;
    }

    public ItemBuilder setCustomName(Component customName) {
        stack.set(DataComponents.CUSTOM_NAME, customName);
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, enchanted);
        return this;
    }

    public ItemStack build() {
        return this.stack;
    }
}
