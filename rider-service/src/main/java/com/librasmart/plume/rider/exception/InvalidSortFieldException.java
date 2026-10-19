package com.librasmart.plume.rider.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidSortFieldException extends RuntimeException {

    private final Set<String> allowedProperties;

    public InvalidSortFieldException(String property, Set<String> allowedProperties) {
        super("Sorting by property '" + property + "' is not allowed.");
        this.allowedProperties = allowedProperties;
    }
}
