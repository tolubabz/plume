package com.librasmart.plume.rider.dto;

import java.time.Instant;

public record ApiResponse<T> (T data, boolean success, String message, Object errors, Instant timestamp) {

    public static <S> ApiResponse<S> success(String message, S data) {
        return new ApiResponse<>(data, true, message, null, Instant.now());
    }

    public static <U> ApiResponse<U> failure(String message, Object errors) {
        return new ApiResponse<>(null, false, message, errors, Instant.now());
    }
};