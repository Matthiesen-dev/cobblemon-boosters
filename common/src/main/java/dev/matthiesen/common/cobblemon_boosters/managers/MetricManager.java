package dev.matthiesen.common.cobblemon_boosters.managers;

import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.metric.UniversalMetricContext;
import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Metrics;

@SuppressWarnings("unused")
public final class MetricManager {
    public static final ErrorTracker ERROR_TRACKER = MatthiesenLibApiMetricsManager.getErrorTracker();
    private static final UniversalMetricContext metricContext = new UniversalMetricContext.Factory(
            Constants.MOD_ID,
            Constants.METRICS_TOKEN
    )
            .metrics(Metrics.Factory::create)
            .errorTrackerService(ERROR_TRACKER)
            .create();

    public static UniversalMetricContext getMetricContext() {
        return metricContext;
    }

    public static void ready() {
        MatthiesenLibApi.registerModToMetrics(Constants.MOD_ID);
        metricContext.ready();
    }
}
