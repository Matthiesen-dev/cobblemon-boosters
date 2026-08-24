package dev.matthiesen.cobblemon_boosters.common.boosts;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;

import java.util.LinkedList;
import java.util.Queue;

public final class ExperienceBoostController implements Booster<ExperienceBoost> {
    private volatile ObservableSubscription<ExperienceGainedEvent.Pre> subscription;

    private volatile ExperienceBoost activeBoost;
    private final Queue<ExperienceBoost> queue = new LinkedList<>();

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.EXPERIENCE;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(event -> {
            ExperienceBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            int exp = event.getExperience();
            event.setExperience(Math.round(exp * activeBoost.getMultiplier()));
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
    public ExperienceBoost getActiveBoost() {
        return activeBoost;
    }

    @Override
    public void setActiveBoost(ExperienceBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<ExperienceBoost> getBoostQueue() {
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<ExperienceBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void clearBoostQueue() {
        this.queue.clear();
    }

    @Override
    public void internal_addToQueue(ExperienceBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getExperienceEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getExperienceEventEndEmbed();
    }
}
