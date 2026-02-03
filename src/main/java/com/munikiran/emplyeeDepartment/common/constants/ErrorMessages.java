package com.munikiran.emplyeeDepartment.common.constants;

public class ErrorMessages {
    
    // Resource not found errors
    public static final String DEPARTMENT_NOT_FOUND = "Department not found with id: ";
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found with id: ";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    
    // Generic errors
    public static final String INTERNAL_SERVER_ERROR = "An internal server error occurred";
    public static final String INVALID_REQUEST = "Invalid request";

    private ErrorMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
