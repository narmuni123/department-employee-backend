package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private LocalDateTime timestamp;
    
    /**
     * Static factory method for creating successful API responses.
     * 
     * @param message Success message describing the operation
     * @param data The response data payload
     * @param <T> The type of the data payload
     * @return ApiResponse with success=true and timestamp set to current time
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .error(null)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * Static factory method for creating failure API responses.
     * 
     * @param message Error message describing the failure
     * @param error Error details or error code
     * @param <T> The type of the data payload (will be null for failures)
     * @return ApiResponse with success=false and timestamp set to current time
     */
    public static <T> ApiResponse<T> failure(String message, Object error) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .data(null)
            .error(error)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
