package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.BoostersItemBuilder;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.utility.BossBar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

public final class SpawnBucketBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public transient BossBar bossBar;
    public String bucket;

    public SpawnBucketBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
    }

    public SpawnBucketBoost(float multiplier, int duration, long timeRemaining, String bucket) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
        this.bucket = bucket;
    }

    public static Optional<SpawnBucketBoost> fromString(String raw) {
        try {
            String[] parts = raw.split(";");
            if (parts.length != 4) return Optional.empty();

            float multiplier = Float.parseFloat(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            long timeRemaining = Long.parseLong(parts[2]);
            String bucket = parts[3];

            return Optional.of(new SpawnBucketBoost(multiplier, duration, timeRemaining, bucket));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String serialize() {
        return multiplier + ";" + duration + ";" + timeRemaining + ";" + bucket;
    }

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
    public BossBar.Builder getBossBar() {
        if (this.bossBar == null) {
            this.bossBar = createBossBar();
        }
        return this.bossBar.getBuilder();
    }

    private BoostMessagesConfig getMessages() {
        return BoostersConfig.getSpawnBucketMessages();
    }

    private BossBar createBossBar() {
        return new BossBar(
                getBossBarText(),
                1F,
                getMessages().barColor(),
                getMessages().barOverlay()
        );
    }

    @Override
    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        getMessages().barText(),
                        this
                )
        );
    }

    @Override
    public Component getSidebarText() {
        var cfg = getMessages();
        String format = (cfg.sidebarLine() == null || cfg.sidebarLine().isBlank()) ? cfg.barText() : cfg.sidebarLine();
        return TextUtils.deserialize(TextUtils.parse(format, this));
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new BoostersItemBuilder(MenuUtils.getBucketItem())
                .hideAdditional()
                .setCustomName(TextUtils.deserialize(TextUtils.parse("&a%bucket% Spawn Bucket Boost&r", this)))
                .addLore(lore)
                .build();
    }
}
