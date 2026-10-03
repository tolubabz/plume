package com.librasmart.plume.rider.dto;

import com.librasmart.plume.rider.entity.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/*
 * TODO: riderId is client-submitted here only because auth isn't built yet.
 *  Once it exists, remove the field entirely and extract the rider's identity
 *  from the authenticated principal instead.
 */
public record TripRequestDto (
        @NotNull UUID riderId,
        @NotNull @Valid Location pickupLocation,
        @NotNull @Valid Location dropoffLocation
) { }
