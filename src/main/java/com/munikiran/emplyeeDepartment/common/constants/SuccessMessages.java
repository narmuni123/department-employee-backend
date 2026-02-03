package com.munikiran.emplyeeDepartment.common.constants;

/**
 * Constants class for success messages used across the API.
 * This class contains all success message constants for consistent response messaging.
 */
public class SuccessMessages {
    
    // Department operations
    public static final String DEPARTMENT_CREATED = "Department created successfully";
    public static final String DEPARTMENT_RETRIEVED = "Department retrieved successfully";
    public static final String DEPARTMENT_UPDATED = "Department updated successfully";
    public static final String DEPARTMENT_DELETED = "Department deleted successfully";
    public static final String DEPARTMENTS_RETRIEVED = "Departments retrieved successfully";
    
    // Employee operations
    public static final String EMPLOYEE_CREATED = "Employee created successfully";
    public static final String EMPLOYEE_RETRIEVED = "Employee retrieved successfully";
    public static final String EMPLOYEE_UPDATED = "Employee updated successfully";
    public static final String EMPLOYEE_DELETED = "Employee deleted successfully";
    public static final String EMPLOYEES_RETRIEVED = "Employees retrieved successfully";
    
    // Report operations
    public static final String REPORT_GENERATED = "Report generated successfully";
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SuccessMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
