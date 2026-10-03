package com.librasmart.plume.rider.repository;

import com.librasmart.plume.rider.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> { }
