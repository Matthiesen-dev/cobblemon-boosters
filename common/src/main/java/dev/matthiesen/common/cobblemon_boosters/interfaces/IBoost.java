package dev.matthiesen.common.cobblemon_boosters.interfaces;

import dev.matthiesen.common.cobblemon_boosters.utils.BossBar;
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
