package dev.matthiesen.cobblemon_boosters.common.boosts;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;
import dev.matthiesen.cobblemon_boosters.common.utils.SpawnBucketOverrideSelector;

import java.util.LinkedList;
import java.util.Queue;

public final class SpawnBucketBoostController implements Booster<SpawnBucketBoost> {
    private volatile ObservableSubscription<SpawnBucketChosenEvent> subscription;

    private volatile SpawnBucketBoost activeBoost;
    private final Queue<SpawnBucketBoost> queue = new LinkedList<>();

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.SPAWN_BUCKET;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(event -> {
            SpawnBucketBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, activeBoost);
            event.setBucket(newBucket);
        });
    }

    @Override
    public void teardownSubscriber() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public SpawnBucketBoost getActiveBoost() {
        return activeBoost;
    }

    @Override
    public void setActiveBoost(SpawnBucketBoost boost) {
        this.activeBoost = boost;

    }

    @Override
    public Queue<SpawnBucketBoost> getBoostQueue() {
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<SpawnBucketBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void clearBoostQueue() {
        this.queue.clear();
    }

    @Override
    public void internal_addToQueue(SpawnBucketBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getSpawnBucketEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getSpawnBucketEventEndEmbed();
    }
}
