package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public final class CoreConfig {

    @SerializedName("saveIntervalTicks")
    public int saveIntervalTicks = 1200;

    public boolean verboseCacheLogging = false;

    @SerializedName("queuePriorityEnabled")
    public boolean queuePriorityEnabled = false;

    @SerializedName("queuePriorityMode")
    public String queuePriorityMode = "FIFO";

    @SerializedName("timePriorityDirection")
    public String timePriorityDirection = "SHORTEST_FIRST";

    @SerializedName("activePreemptionEnabled")
    public boolean activePreemptionEnabled = false;

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
