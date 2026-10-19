package com.librasmart.plume.rider.entity;

import java.util.Set;

public enum TripStatus {
    REQUESTED, MATCHED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED, NO_DRIVERS_AVAILABLE;

    public Set<TripStatus> allowedNext() {
        return switch (this) {
            case REQUESTED -> Set.of(MATCHED, CANCELLED, NO_DRIVERS_AVAILABLE);
            case MATCHED -> Set.of(ACCEPTED, CANCELLED, NO_DRIVERS_AVAILABLE);
            case ACCEPTED -> Set.of(IN_PROGRESS, CANCELLED);
            case IN_PROGRESS -> Set.of(COMPLETED);
            case COMPLETED, CANCELLED, NO_DRIVERS_AVAILABLE -> Set.of();
        };
    }
}
