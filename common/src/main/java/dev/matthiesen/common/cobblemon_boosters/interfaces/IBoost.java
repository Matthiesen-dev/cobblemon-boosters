package dev.matthiesen.common.cobblemon_boosters.interfaces;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public interface IBoost {
	float getMultiplier();
	void setMultiplier(float multiplier);

	int getDuration();
	void setDuration(int duration);

	long getTimeRemaining();
	void setTimeRemaining(long timeRemaining);

	BossBar getBossBar();

	Component getBossBarText();

	String getBoostType();
}
