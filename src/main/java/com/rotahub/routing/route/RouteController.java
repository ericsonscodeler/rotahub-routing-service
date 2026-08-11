package com.rotahub.routing.route;

import com.rotahub.routing.route.dto.CreateRouteRequest;
import com.rotahub.routing.route.dto.RouteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteRepository routeRepository;
    private final NearestNeighborOptimizer optimizer;

    public RouteController(RouteRepository routeRepository, NearestNeighborOptimizer optimizer) {
        this.routeRepository = routeRepository;
        this.optimizer = optimizer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteResponse create(@Valid @RequestBody CreateRouteRequest request) {
        List<Stop> stops = request.stops().stream()
            .map(s -> new Stop(s.orderId(), s.address(), s.lat(), s.lng()))
            .toList();
        List<Stop> optimized = optimizer.optimize(stops);
        Route route = routeRepository.save(new Route(optimized));
        return RouteResponse.from(route, optimizer.totalDistanceKm(optimized));
    }

    @GetMapping("/{id}")
    public RouteResponse getById(@PathVariable UUID id) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new RouteNotFoundException(id));
        return RouteResponse.from(route, optimizer.totalDistanceKm(route.getStops()));
    }
}
