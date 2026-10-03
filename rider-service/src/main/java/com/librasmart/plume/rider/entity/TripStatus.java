package com.librasmart.plume.rider.entity;

import java.util.Map;
import java.util.Set;

public enum TripStatus {
    REQUESTED, MATCHED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED, NO_DRIVERS_FOUND;

    static final Map<TripStatus, Set<TripStatus>> VALID_TRANSITIONS = Map.of(
            REQUESTED, Set.of(MATCHED, CANCELLED, NO_DRIVERS_FOUND),
            MATCHED, Set.of(ACCEPTED, CANCELLED, NO_DRIVERS_FOUND),
            ACCEPTED, Set.of(IN_PROGRESS, CANCELLED),
            IN_PROGRESS, Set.of(COMPLETED),
            COMPLETED, Set.of(),
            CANCELLED, Set.of(),
            NO_DRIVERS_FOUND, Set.of()
    );
}
