package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Metadata class for API requests containing request identification and authentication information.
 * 
 * This class encapsulates metadata that can be sent with API requests to provide:
 * - Request tracking via requestId
 * - Source identification via source
 * - Optional authentication via authToken
 * 
 * Uses Lombok annotations to reduce boilerplate code.
 */
@Data
@Builder
@AllArgsConstructor
public class RequestMeta {
    /**
     * Unique identifier for the request, used for tracking and correlation.
     */
    private String requestId;
    
    /**
     * Source of the request (e.g., "web-app", "mobile-app", "api-client").
     */
    private String source;
    
    /**
     * Optional authentication token for the request.
     * Can be null if authentication is not required or handled elsewhere.
     */
    private String authToken;
}
