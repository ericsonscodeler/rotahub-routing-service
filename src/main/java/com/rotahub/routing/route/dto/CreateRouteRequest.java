package com.rotahub.routing.route.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateRouteRequest(
    @NotEmpty @Valid List<StopRequest> stops
) {
}
