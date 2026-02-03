# Implementation Plan: API Wrapper Pattern

## Overview

This implementation plan breaks down the API wrapper pattern feature into incremental, testable steps. The approach follows a safe migration path: create new components first, then integrate them into existing code one piece at a time. Each task builds on previous work and includes validation through tests.

## Tasks

- [ ] 1. Create wrapper classes and constants
  - [x] 1.1 Create ApiResponse<T> generic class with factory methods
    - Create class in `com.munikiran.emplyeeDepartment.common` package
    - Add fields: success, message, data, error, timestamp
    - Use Lombok annotations: @Data, @Builder, @AllArgsConstructor
    - Implement static method `success(String message, T data)`
    - Implement static method `failure(String message, Object error)`
    - Both factory methods should set timestamp to LocalDateTime.now()
    - _Requirements: 1.1, 1.3, 1.4, 1.5, 10.1_
  
  - [ ]* 1.2 Write property test for ApiResponse factory methods
    - **Property 1: Success Factory Method Correctness**
    - **Property 2: Failure Factory Method Correctness**
    - **Property 3: Timestamp Freshness**
    - **Validates: Requirements 1.3, 1.4, 1.5**
  
  - [x] 1.3 Create RequestMeta class
    - Create class in `com.munikiran.emplyeeDepartment.common` package
    - Add fields: requestId, source, authToken (all String)
    - Use Lombok annotations: @Data, @Builder, @AllArgsConstructor
    - _Requirements: 2.2, 10.3_
  
  - [x] 1.4 Create ApiRequest<T> generic class
    - Create class in `com.munikiran.emplyeeDepartment.common` package
    - Add fields: meta (RequestMeta), payload (T)
    - Use Lombok annotations: @Data, @Builder, @AllArgsConstructor
    - _Requirements: 2.1, 10.2_
  
  - [ ]* 1.5 Write unit tests for wrapper class structure
    - Test ApiResponse has all required fields
    - Test ApiRequest has all required fields
    - Test RequestMeta has all required fields
    - Test Lombok builder pattern works for all classes
    - _Requirements: 1.1, 2.1, 2.2, 10.4_

- [ ] 2. Create constants classes
  - [x] 2.1 Create ApiConstants class
    - Create class in `com.munikiran.emplyeeDepartment.common.constants` package
    - Add constants: STATUS_OK, STATUS_CREATED, STATUS_NOT_FOUND, STATUS_INTERNAL_ERROR
    - Add constants: FIELD_ID, FIELD_NAME
    - Make constructor private to prevent instantiation
    - _Requirements: 6.1, 6.5_
  
  - [x] 2.2 Create ErrorMessages class
    - Create class in `com.munikiran.emplyeeDepartment.common.constants` package
    - Add constants: DEPARTMENT_NOT_FOUND, EMPLOYEE_NOT_FOUND, RESOURCE_NOT_FOUND
    - Add constants: INTERNAL_SERVER_ERROR, INVALID_REQUEST
    - Make constructor private to prevent instantiation
    - _Requirements: 6.2, 6.5_
  
  - [x] 2.3 Create SuccessMessages class
    - Create class in `com.munikiran.emplyeeDepartment.common.constants` package
    - Add department operation constants: DEPARTMENT_CREATED, DEPARTMENT_RETRIEVED, DEPARTMENT_UPDATED, DEPARTMENT_DELETED, DEPARTMENTS_RETRIEVED
    - Add employee operation constants: EMPLOYEE_CREATED, EMPLOYEE_RETRIEVED, EMPLOYEE_UPDATED, EMPLOYEE_DELETED, EMPLOYEES_RETRIEVED
    - Add report constant: REPORT_GENERATED
    - Make constructor private to prevent instantiation
    - _Requirements: 6.3, 6.5_
  
  - [ ]* 2.4 Write unit tests for constants classes
    - Verify ApiConstants has expected constants
    - Verify ErrorMessages has expected constants
    - Verify SuccessMessages has expected constants
    - Verify constructors are private
    - _Requirements: 6.1, 6.2, 6.3, 6.5_

- [x] 3. Checkpoint - Verify wrapper classes compile and tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Update GlobalExceptionHandler
  - [x] 4.1 Refactor handleResourceNotFoundException method
    - Change return type to `ResponseEntity<ApiResponse<?>>`
    - Use `ApiResponse.failure()` to wrap error response
    - Use ErrorMessages.RESOURCE_NOT_FOUND constant
    - Maintain HTTP 404 status code
    - _Requirements: 4.1, 4.2, 4.4, 6.4, 7.3_
  
  - [x] 4.2 Refactor handleGenericException method
    - Change return type to `ResponseEntity<ApiResponse<?>>`
    - Use `ApiResponse.failure()` to wrap error response
    - Use ErrorMessages.INTERNAL_SERVER_ERROR constant
    - Maintain HTTP 500 status code
    - _Requirements: 4.1, 4.3, 4.4, 6.4, 7.4_
  
  - [ ]* 4.3 Write property test for exception handler
    - **Property 5: Exception Handler Response Wrapping**
    - **Property 6: HTTP Status Code Preservation** (exception cases)
    - **Validates: Requirements 4.1, 4.4, 7.3, 7.4**
  
  - [ ]* 4.4 Write unit tests for exception handler
    - Test ResourceNotFoundException returns wrapped response with 404
    - Test generic Exception returns wrapped response with 500
    - Test response has success=false
    - Test error messages use constants
    - _Requirements: 4.2, 4.3, 4.4, 6.4_

- [x] 5. Checkpoint - Verify exception handling works correctly
  - Ensure all tests pass, ask the user if questions arise.

- [x] 6. Refactor DepartmentController
  - [x] 6.1 Update all GET endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use SuccessMessages constants for messages
    - Maintain HTTP 200 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.1, 5.4, 5.5, 6.4, 7.1, 8.1, 8.2, 8.4_
  
  - [x] 6.2 Update POST endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use SuccessMessages.DEPARTMENT_CREATED constant
    - Maintain HTTP 201 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.1, 5.4, 5.5, 6.4, 7.2, 8.1, 8.2, 8.4_
  
  - [x] 6.3 Update PUT and DELETE endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use appropriate SuccessMessages constants
    - Maintain HTTP 200 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.1, 5.4, 5.5, 6.4, 7.1, 8.1, 8.2, 8.4_
  
  - [ ]* 6.4 Write property tests for DepartmentController
    - **Property 4: Controller Response Wrapping** (DepartmentController)
    - **Property 6: HTTP Status Code Preservation** (DepartmentController)
    - **Property 7: Endpoint Backward Compatibility** (DepartmentController)
    - **Property 8: Response Data Structure Preservation** (DepartmentController)
    - **Property 9: No Hardcoded Strings in Controllers** (DepartmentController)
    - **Validates: Requirements 5.1, 5.4, 5.5, 6.4, 7.1, 7.2, 7.5, 8.1, 8.2, 8.3, 8.4**
  
  - [ ]* 6.5 Write unit tests for DepartmentController endpoints
    - Test GET by ID returns wrapped response
    - Test GET all returns wrapped response
    - Test POST returns wrapped response with 201
    - Test PUT returns wrapped response
    - Test DELETE returns wrapped response
    - Test all responses use constants for messages
    - _Requirements: 5.1, 5.4, 6.4, 7.1, 7.2_

- [x] 7. Refactor EmployeeController
  - [x] 7.1 Update all GET endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use SuccessMessages constants for messages
    - Maintain HTTP 200 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.2, 5.4, 5.5, 6.4, 7.1, 8.1, 8.2, 8.4_
  
  - [x] 7.2 Update POST endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use SuccessMessages.EMPLOYEE_CREATED constant
    - Maintain HTTP 201 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.2, 5.4, 5.5, 6.4, 7.2, 8.1, 8.2, 8.4_
  
  - [x] 7.3 Update PUT and DELETE endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use appropriate SuccessMessages constants
    - Maintain HTTP 200 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.2, 5.4, 5.5, 6.4, 7.1, 8.1, 8.2, 8.4_
  
  - [ ]* 7.4 Write property tests for EmployeeController
    - **Property 4: Controller Response Wrapping** (EmployeeController)
    - **Property 6: HTTP Status Code Preservation** (EmployeeController)
    - **Property 7: Endpoint Backward Compatibility** (EmployeeController)
    - **Property 8: Response Data Structure Preservation** (EmployeeController)
    - **Property 9: No Hardcoded Strings in Controllers** (EmployeeController)
    - **Validates: Requirements 5.2, 5.4, 5.5, 6.4, 7.1, 7.2, 7.5, 8.1, 8.2, 8.3, 8.4**
  
  - [ ]* 7.5 Write unit tests for EmployeeController endpoints
    - Test GET by ID returns wrapped response
    - Test GET all returns wrapped response
    - Test POST returns wrapped response with 201
    - Test PUT returns wrapped response
    - Test DELETE returns wrapped response
    - Test all responses use constants for messages
    - _Requirements: 5.2, 5.4, 6.4, 7.1, 7.2_

- [~] 8. Refactor ReportController
  - [x] 8.1 Update all report endpoints to return wrapped responses
    - Change return types to `ResponseEntity<ApiResponse<T>>`
    - Wrap responses using `ApiResponse.success()`
    - Use SuccessMessages.REPORT_GENERATED constant
    - Maintain HTTP 200 status code
    - Preserve all endpoint paths and HTTP methods
    - _Requirements: 5.3, 5.4, 5.5, 6.4, 7.1, 8.1, 8.2, 8.4_
  
  - [ ]* 8.2 Write property tests for ReportController
    - **Property 4: Controller Response Wrapping** (ReportController)
    - **Property 6: HTTP Status Code Preservation** (ReportController)
    - **Property 7: Endpoint Backward Compatibility** (ReportController)
    - **Property 8: Response Data Structure Preservation** (ReportController)
    - **Property 9: No Hardcoded Strings in Controllers** (ReportController)
    - **Validates: Requirements 5.3, 5.4, 5.5, 6.4, 7.1, 7.5, 8.1, 8.2, 8.3, 8.4**
  
  - [ ]* 8.3 Write unit tests for ReportController endpoints
    - Test report endpoints return wrapped responses
    - Test all responses use constants for messages
    - Test HTTP status codes are correct
    - _Requirements: 5.3, 5.4, 6.4, 7.1_

- [x] 9. Checkpoint - Verify all controllers work correctly
  - Ensure all tests pass, ask the user if questions arise.

- [ ]* 10. Write integration tests
  - [ ]* 10.1 Write end-to-end integration tests for wrapped responses
    - Test complete request-response cycle for department endpoints
    - Test complete request-response cycle for employee endpoints
    - Test complete request-response cycle for report endpoints
    - Verify response structure matches ApiResponse format
    - _Requirements: 8.4, 9.5_
  
  - [ ]* 10.2 Write integration tests for error scenarios
    - Test ResourceNotFoundException returns wrapped error with 404
    - Test generic exceptions return wrapped error with 500
    - Verify error response structure matches ApiResponse format
    - _Requirements: 4.2, 4.3, 4.4, 9.5_
  
  - [ ]* 10.3 Write backward compatibility integration tests
    - Test all original endpoint paths still work
    - Test all original HTTP methods still work
    - Test request parameters are still accepted
    - Verify only response structure changed (wrapped in ApiResponse)
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 9.5_

- [x] 11. Final checkpoint - Run full test suite and verify application
  - Run all unit tests, property tests, and integration tests
  - Verify application starts successfully
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation after major changes
- Property tests validate universal correctness properties across all inputs
- Unit tests validate specific examples and edge cases
- Integration tests verify end-to-end functionality
- Service layer code is never modified - only controllers and exception handler
- All existing business logic is preserved throughout refactoring
