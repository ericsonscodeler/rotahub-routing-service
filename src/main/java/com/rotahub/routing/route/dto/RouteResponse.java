package com.rotahub.routing.route.dto;

import com.rotahub.routing.route.Route;
import com.rotahub.routing.route.RouteStatus;
import com.rotahub.routing.route.Stop;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RouteResponse(
    UUID id,
    RouteStatus status,
    List<Stop> stops,
    double totalDistanceKm,
    Instant createdAt
) {

    public static RouteResponse from(Route route, double totalDistanceKm) {
        return new RouteResponse(
            route.getId(),
            route.getStatus(),
            route.getStops(),
            totalDistanceKm,
            route.getCreatedAt()
        );
    }
}
