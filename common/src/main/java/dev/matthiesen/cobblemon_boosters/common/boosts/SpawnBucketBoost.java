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

    public SpawnBucketBoost(BoostParts parts) {
        super(parts.multiplier(), parts.duration(), parts.timeRemaining());
        this.bucket = parts.getRemainingPart(0);
    }

    public static Optional<SpawnBucketBoost> fromString(String raw) {
        try {
            BoostParts parts = parseRawStringToParts(raw, 4);
            if (parts == null) return Optional.empty();
            return Optional.of(new SpawnBucketBoost(parts));
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
