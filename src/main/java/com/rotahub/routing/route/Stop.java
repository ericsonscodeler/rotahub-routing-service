package com.rotahub.routing.route;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record Stop(UUID orderId, String address, double lat, double lng) {
}
