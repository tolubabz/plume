package com.librasmart.plume.rider.exception;

import com.librasmart.plume.rider.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@NullMarked
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedError(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.internalServerError().body(ApiResponse.failure("An unexpected error occurred.", null));
    }

    @ExceptionHandler(InvalidTripStatusTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTripStatusTransition(InvalidTripStatusTransitionException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        errors.put("currentStatus", ex.getFrom().name());
        errors.put("requestedStatus", ex.getTo().name());
        errors.put("allowedStatuses", ex.getAllowed().toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage(), errors));
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTripNotFound(TripNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidSortField(InvalidSortFieldException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("allowedProperties", ex.getAllowedProperties().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage(), errors));
    }

    @ExceptionHandler(DriverAssignmentRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleDriverAssignmentRequired(DriverAssignmentRequiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage(), null));
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        log.warn("Optimistic lock failure: {}", ex.getMessage());
        String message = "This resource was modified by another request. Please refresh and try again.";

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(message, null));
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.merge(
                        error.getField(),
                        Objects.requireNonNullElse(error.getDefaultMessage(), "invalid value"),
                        (existing, added) -> existing + "; " + added
                )
        );

        return handleExceptionInternal(ex, ApiResponse.failure("Validation failed", fieldErrors), headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, statusCode, request);

        if (response == null) return null;
        if (response.getBody() instanceof ApiResponse<?>) return response;

        String message = (response.getBody() instanceof ProblemDetail problemDetail && problemDetail.getDetail() != null)
                ? problemDetail.getDetail()
                : "The request could not be processed.";

        return new ResponseEntity<>(ApiResponse.failure(message, null), response.getHeaders(), response.getStatusCode());
    }
}
