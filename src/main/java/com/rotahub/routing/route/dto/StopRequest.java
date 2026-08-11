package com.rotahub.routing.route.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StopRequest(
    @NotNull UUID orderId,
    @NotBlank String address,
    @NotNull Double lat,
    @NotNull Double lng
) {
}
