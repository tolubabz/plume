package com.librasmart.plume.rider.entity;

import com.librasmart.plume.rider.exception.InvalidTripStatusTransitionException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "trips")
public class Trip {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID riderId;

    private UUID driverId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude", nullable = false)),
        @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude", nullable = false))
    })
    private Location pickupLocation;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "dropoff_latitude", nullable = false)),
        @AttributeOverride(name = "longitude", column = @Column(name = "dropoff_longitude", nullable = false))
    })
    private Location dropoffLocation;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.requestedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void transitionTo(TripStatus next) {
        if (!TripStatus.VALID_TRANSITIONS.get(status).contains(next)) {
            throw new InvalidTripStatusTransitionException("Cannot change trip status from " + status + " to " + next);
        }
        status = next;
    }
}
