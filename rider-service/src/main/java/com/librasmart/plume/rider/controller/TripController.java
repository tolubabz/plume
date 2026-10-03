package com.librasmart.plume.rider.controller;

import com.librasmart.plume.rider.dto.ApiResponse;
import com.librasmart.plume.rider.dto.TripRequestDto;
import com.librasmart.plume.rider.dto.TripResponseDto;
import com.librasmart.plume.rider.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<ApiResponse<TripResponseDto>> requestTrip(@Valid @RequestBody TripRequestDto tripRequestDto) {
        TripResponseDto tripResponseDto = TripResponseDto.from(tripService.createTrip(tripRequestDto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tripResponseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.success("Trip created successfully", tripResponseDto));
    }
}
