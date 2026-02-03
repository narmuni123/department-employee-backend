package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Generic wrapper class for API requests with metadata.
 * 
 * This class encapsulates API request payloads along with metadata for:
 * - Request tracking and identification
 * - Source identification
 * - Authentication information
 * 
 * The generic type T allows wrapping any request payload type (DTOs, entities, etc.)
 * while maintaining consistent metadata structure across all API requests.
 * 
 * Uses Lombok annotations to reduce boilerplate code and enable fluent builder pattern.
 * 
 * @param <T> The type of the request payload
 */
@Data
@Builder
@AllArgsConstructor
public class ApiRequest<T> {
    /**
     * Metadata containing request identification and authentication information.
     */
    private RequestMeta meta;
    
    /**
     * The actual request payload data.
     * Can be any type (DTO, entity, primitive, collection, etc.)
     */
    private T payload;
}
