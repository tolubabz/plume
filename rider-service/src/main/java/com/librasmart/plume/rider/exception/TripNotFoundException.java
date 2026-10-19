package com.librasmart.plume.rider.exception;

import java.util.UUID;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(UUID tripId) {
        super("Trip not found: " + tripId);
    }
}
