package dev.matthiesen.cobblemon_boosters.common.boosts;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;

import java.util.LinkedList;
import java.util.Queue;

public final class CatchBoostController implements Booster<CatchBoost> {
    private volatile ObservableSubscription<PokemonCatchRateEvent> subscription;

    private volatile CatchBoost activeBoost;
    private final Queue<CatchBoost> queue = new LinkedList<>();

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.CATCH;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.POKEMON_CATCH_RATE.subscribe(event -> {
            CatchBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            float baseCatchRate = event.getCatchRate();
            event.setCatchRate(Math.min(baseCatchRate * activeBoost.getMultiplier(), 255F));
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
    public CatchBoost getActiveBoost() {
        return activeBoost;
    }

    @Override
    public void setActiveBoost(CatchBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<CatchBoost> getBoostQueue() {
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<CatchBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);

    }

    @Override
    public void clearBoostQueue() {
        this.queue.clear();
    }

    @Override
    public void internal_addToQueue(CatchBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getCatchEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getCatchEventEndEmbed();
    }
}
