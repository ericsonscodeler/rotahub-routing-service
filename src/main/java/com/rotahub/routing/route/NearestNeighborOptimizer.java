package com.rotahub.routing.route;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NearestNeighborOptimizer {

    private static final double EARTH_RADIUS_KM = 6371;

    public List<Stop> optimize(List<Stop> stops) {
        if (stops.size() <= 2) {
            return List.copyOf(stops);
        }

        List<Stop> remaining = new ArrayList<>(stops);
        List<Stop> ordered = new ArrayList<>(stops.size());

        Stop current = remaining.remove(0);
        ordered.add(current);

        while (!remaining.isEmpty()) {
            Stop nearest = remaining.get(0);
            double nearestDistance = distanceKm(current, nearest);
            for (Stop candidate : remaining) {
                double distance = distanceKm(current, candidate);
                if (distance < nearestDistance) {
                    nearest = candidate;
                    nearestDistance = distance;
                }
            }
            ordered.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        return ordered;
    }

    public double totalDistanceKm(List<Stop> stops) {
        double total = 0;
        for (int i = 0; i < stops.size() - 1; i++) {
            total += distanceKm(stops.get(i), stops.get(i + 1));
        }
        return total;
    }

    private double distanceKm(Stop a, Stop b) {
        double dLat = Math.toRadians(b.lat() - a.lat());
        double dLng = Math.toRadians(b.lng() - a.lng());
        double lat1 = Math.toRadians(a.lat());
        double lat2 = Math.toRadians(b.lat());

        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));

        return EARTH_RADIUS_KM * c;
    }
}
