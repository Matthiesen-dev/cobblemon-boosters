package dev.matthiesen.common.cobblemon_boosters.data;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.BossBar;
import dev.matthiesen.common.cobblemon_boosters.utils.ItemBuilder;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SpawnBucketBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public BossBar bossBar;
    public String bucket;

    public SpawnBucketBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = createBossBar();
    }

    public SpawnBucketBoost() {}

    @Override
    public float getMultiplier() {
        return this.multiplier;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public long getTimeRemaining() {
        return this.timeRemaining;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public SpawnBucketBoost setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getBucket() {
        return this.bucket;
    }

    @Override
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = createBossBar();
    }

    public String getBucketDisplayName() {
        Map<String, String> bucketDisplayNames = Map.of(
                "common", "Common",
                "uncommon", "Uncommon",
                "rare", "Rare",
                "ultra-rare", "Ultra Rare"
        );
        return bucketDisplayNames.getOrDefault(this.bucket.toLowerCase(), this.bucket);
    }

    public String getBucketName() {
        return this.bucket;
    }

    @Override
    public BossBar getBossBar() {
        if (this.bossBar == null) {
            this.bossBar = createBossBar();
        }
        return this.bossBar;
    }

    private BossBar createBossBar() {
        return new BossBar(
                getBossBarText(),
                1F,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.barColor,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.barOverlay
        );
    }

    @Override
    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.barText,
                        this
                )
        );
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new ItemBuilder(MenuUtils.getBucketItem())
                .hideAdditional()
                .setCustomName(TextUtils.deserialize(TextUtils.parse("&a%bucket% Spawn Bucket Boost&r", this)))
                .addLore(lore)
                .build();
    }
}
