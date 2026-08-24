package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.utils.*;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

public final class SpawnBucketBoost extends AbstractSimpleBoost {
    public String bucket;

    public SpawnBucketBoost(float multiplier, int duration) {
        super(multiplier, duration);
        this.bucket = "common";
    }

    public SpawnBucketBoost(float multiplier, int duration, long timeRemaining, String bucket) {
        super(multiplier, duration, timeRemaining);
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

    public SpawnBucketBoost setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String serialize() {
        return multiplier + ";" + duration + ";" + timeRemaining + ";" + bucket;
    }

    public String getBucket() {
        return this.bucket;
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
    public BoostMessagesConfig getMessages() {
        return BoostersConfig.getSpawnBucketMessages();
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
