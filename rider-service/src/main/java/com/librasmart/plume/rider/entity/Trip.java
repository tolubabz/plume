package com.librasmart.plume.rider.entity;

import com.librasmart.plume.rider.exception.InvalidTripStatusTransitionException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
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

    @Setter(AccessLevel.NONE)
    private UUID driverId;

    @Setter(AccessLevel.NONE)
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

    @Version
    @Setter(AccessLevel.NONE)
    private Long version;

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
        if (next == TripStatus.ACCEPTED) {
            throw new IllegalArgumentException("Use accept(driverId) to move a trip to ACCEPTED");
        }
        moveTo(next);
    }

    public void accept(UUID driverId) {
        Objects.requireNonNull(driverId, "driverId must not be null");
        moveTo(TripStatus.ACCEPTED);
        this.driverId = driverId;
    }

    private void moveTo(TripStatus next) {
        Set<TripStatus> allowed = status.allowedNext();
        if (!allowed.contains(next)) {
            throw new InvalidTripStatusTransitionException(status, next, allowed);
        }
        this.status = next;
    }
}
