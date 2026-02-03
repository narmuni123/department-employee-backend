package com.munikiran.emplyeeDepartment.common.constants;

/**
 * Constants class for API-related constant values.
 * This class contains common HTTP status messages and field names used across the API.
 */
public class ApiConstants {
    
    // HTTP Status Messages
    public static final String STATUS_OK = "OK";
    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_NOT_FOUND = "NOT_FOUND";
    public static final String STATUS_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
    
    // Common field names
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
