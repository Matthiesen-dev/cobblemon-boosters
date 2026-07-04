package dev.matthiesen.cobblemon_boosters.common.interfaces;

import dev.matthiesen.cobblemon_boosters.common.utils.BossBar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IBoost {
	float getMultiplier();
	void setMultiplier(float multiplier);

	int getDuration();
	void setDuration(int duration);

	long getTimeRemaining();
	void setTimeRemaining(long timeRemaining);

	BossBar.Builder getBossBar();

	Component getBossBarText();

	Component getSidebarText();

	ItemStack getGUIItem(Component[] lore);
}
