package dev.matthiesen.cobblemon_boosters.common.interfaces;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.utility.BossBar;
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
	ItemStack getGUIItem(Component[] lore);
	BoostMessagesConfig getMessages();

	default BossBar createBossBar() {
		return new BossBar(
				getBossBarText(),
				1F,
				getMessages().barColor(),
				getMessages().barOverlay()
		);
	}

	default Component getBossBarText() {
		return TextUtils.deserialize(
				TextUtils.parse(
						getMessages().barText(),
						this
				)
		);
	}

	default Component getSidebarText() {
		var cfg = getMessages();
		String format = (cfg.sidebarLine() == null || cfg.sidebarLine().isBlank()) ? cfg.barText() : cfg.sidebarLine();
		return TextUtils.deserialize(TextUtils.parse(format, this));
	}
}
