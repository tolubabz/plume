package com.librasmart.plume.rider.exception;

import com.librasmart.plume.rider.entity.TripStatus;
import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidTripStatusTransitionException extends RuntimeException {

    private final TripStatus from;
    private final TripStatus to;
    private final Set<TripStatus> allowed;

    public InvalidTripStatusTransitionException(TripStatus from, TripStatus to, Set<TripStatus> allowed) {
        super("Cannot change trip status from " + from + " to " + to);
        this.from = from;
        this.to = to;
        this.allowed = allowed;
    }
}
