package com.rotahub.routing.route;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "route_stops", joinColumns = @JoinColumn(name = "route_id"))
    @OrderColumn(name = "sequence")
    private List<Stop> stops;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Route() {
    }

    public Route(List<Stop> stops) {
        this.status = RouteStatus.PLANNED;
        this.stops = stops;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public RouteStatus getStatus() {
        return status;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
