package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public final class CoreConfig {

    @SerializedName("saveIntervalTicks")
    public int saveIntervalTicks = 1200;

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
