package com.librasmart.plume.rider.dto;

import com.librasmart.plume.rider.entity.TripStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TripStatusUpdateDto(@NotNull TripStatus status, UUID driverId) { }
