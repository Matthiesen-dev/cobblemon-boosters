package dev.matthiesen.cobblemon_boosters.common.boosts;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;

import java.util.LinkedList;
import java.util.Queue;

public final class ShinyBoostController implements Booster<ShinyBoost> {
    private volatile ObservableSubscription<ShinyChanceCalculationEvent> subscription;

    private volatile ShinyBoost activeBoost;
    private final Queue<ShinyBoost> queue = new LinkedList<>();

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.SHINY;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(event -> {
            ShinyBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            event.addModificationFunction(((rate, player, pokemon) ->
                    Math.max(rate / activeBoost.getMultiplier(), 1)));
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
    public ShinyBoost getActiveBoost() {
        return activeBoost;
    }

    @Override
    public void setActiveBoost(ShinyBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<ShinyBoost> getBoostQueue() {
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<ShinyBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void clearBoostQueue() {
        this.queue.clear();
    }

    @Override
    public void internal_addToQueue(ShinyBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getShinyEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getShinyEventEndEmbed();
    }
}
