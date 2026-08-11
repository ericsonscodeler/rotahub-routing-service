package com.rotahub.routing.route;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NearestNeighborOptimizerTest {

    private final NearestNeighborOptimizer optimizer = new NearestNeighborOptimizer();

    @Test
    void ordersStopsByProximityStartingFromTheFirstStop() {
        Stop start = stop("Start", -23.5613, -46.6565);
        Stop far = stop("Far", -23.6266, -46.6553);
        Stop near = stop("Near", -23.5620, -46.6570);

        List<Stop> optimized = optimizer.optimize(List.of(start, far, near));

        assertThat(optimized).containsExactly(start, near, far);
    }

    @Test
    void returnsInputUnchangedForTwoOrFewerStops() {
        Stop a = stop("A", -23.5, -46.6);
        Stop b = stop("B", -23.6, -46.7);

        assertThat(optimizer.optimize(List.of(a, b))).containsExactly(a, b);
        assertThat(optimizer.optimize(List.of(a))).containsExactly(a);
    }

    @Test
    void computesZeroDistanceForASingleStop() {
        Stop a = stop("A", -23.5, -46.6);

        assertThat(optimizer.totalDistanceKm(List.of(a))).isZero();
    }

    @Test
    void computesPositiveTotalDistanceForMultipleStops() {
        Stop a = stop("A", -23.5613, -46.6565);
        Stop b = stop("B", -23.6266, -46.6553);

        double distance = optimizer.totalDistanceKm(List.of(a, b));

        assertThat(distance).isGreaterThan(0).isLessThan(20);
    }

    private Stop stop(String address, double lat, double lng) {
        return new Stop(UUID.randomUUID(), address, lat, lng);
    }
}
