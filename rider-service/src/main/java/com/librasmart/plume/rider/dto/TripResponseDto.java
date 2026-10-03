package com.librasmart.plume.rider.dto;

import com.librasmart.plume.rider.entity.Location;
import com.librasmart.plume.rider.entity.Trip;
import com.librasmart.plume.rider.entity.TripStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record TripResponseDto(UUID id, UUID riderId, UUID driverId, TripStatus status, Location pickupLocation, Location dropoffLocation, Instant requestedAt, Instant updatedAt) {

    public static TripResponseDto from(Trip trip) {
        return TripResponseDto.builder()
                .id(trip.getId())
                .riderId(trip.getRiderId())
                .driverId(trip.getDriverId())
                .status(trip.getStatus())
                .pickupLocation(trip.getPickupLocation())
                .dropoffLocation(trip.getDropoffLocation())
                .requestedAt(trip.getRequestedAt())
                .updatedAt(trip.getUpdatedAt())
                .build();
    }
}
