package com.librasmart.plume.rider.exception;

public class InvalidTripStatusTransitionException extends RuntimeException {
    public InvalidTripStatusTransitionException(String message) {
        super(message);
    }
}
