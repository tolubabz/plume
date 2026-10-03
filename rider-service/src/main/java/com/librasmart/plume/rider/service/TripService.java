package com.librasmart.plume.rider.service;

import com.librasmart.plume.rider.dto.TripRequestDto;
import com.librasmart.plume.rider.entity.Trip;
import com.librasmart.plume.rider.entity.TripStatus;
import com.librasmart.plume.rider.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public Trip createTrip(TripRequestDto tripRequestDto) {
        Trip trip = Trip.builder()
                .riderId(tripRequestDto.riderId())
                .status(TripStatus.REQUESTED)
                .pickupLocation(tripRequestDto.pickupLocation())
                .dropoffLocation(tripRequestDto.dropoffLocation())
                .build();

        return tripRepository.save(trip);
    }
}
