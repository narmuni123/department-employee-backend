# Design Document: API Wrapper Pattern

## Overview

This design implements a standardized API response/request wrapper pattern for a Spring Boot application managing employees and departments. The wrapper pattern provides consistent API responses across all endpoints by encapsulating response data, metadata, and error information in generic container classes.

The implementation follows an incremental approach:
1. Create new wrapper classes (ApiResponse, ApiRequest, RequestMeta)
2. Update GlobalExceptionHandler to use wrapped responses
3. Refactor existing controllers (DepartmentController, EmployeeController, ReportController)
4. Centralize messages in constants classes

This design preserves all existing business logic, endpoint paths, HTTP methods, and status codes while only changing the response structure.

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Application                       │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            │ HTTP Request/Response
                            │ (wrapped in ApiResponse)
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                   │
│                                                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Controllers Layer                         │ │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐  │ │
│  │  │ Department   │ │  Employee    │ │   Report     │  │ │
│  │  │ Controller   │ │  Controller  │ │  Controller  │  │ │
│  │  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘  │ │
│  └─────────┼────────────────┼────────────────┼──────────┘ │
│            │                │                │              │
│            │ Returns ApiResponse<T>          │              │
│            ▼                ▼                ▼              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Services Layer                            │ │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐  │ │
│  │  │ Department   │ │  Employee    │ │ Department   │  │ │
│  │  │   Service    │ │   Service    │ │Report Service│  │ │
│  │  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘  │ │
│  └─────────┼────────────────┼────────────────┼──────────┘ │
│            │                │                │              │
│            │ Returns DTOs/Entities           │              │
│            ▼                ▼                ▼              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Data Layer (JPA)                          │ │
│  │  ┌──────────────┐ ┌──────────────┐                    │ │
│  │  │  Department  │ │   Employee   │                    │ │
│  │  │   Entity     │ │    Entity    │                    │ │
│  │  └──────────────┘ └──────────────┘                    │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         Exception Handling (Cross-cutting)             │ │
│  │  ┌──────────────────────────────────────────────────┐ │ │
│  │  │      GlobalExceptionHandler                      │ │ │
│  │  │  Returns ResponseEntity<ApiResponse<?>>          │ │ │
│  │  └──────────────────────────────────────────────────┘ │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         Common Package (New Components)                │ │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐  │ │
│  │  │ ApiResponse  │ │  ApiRequest  │ │ RequestMeta  │  │ │
│  │  └──────────────┘ └──────────────┘ └──────────────┘  │ │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐  │ │
│  │  │ApiConstants  │ │ErrorMessages │ │Success       │  │ │
│  │  │              │ │              │ │Messages      │  │ │
│  │  └──────────────┘ └──────────────┘ └──────────────┘  │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Design Principles

1. **Separation of Concerns**: Wrapper classes are isolated in the common package, keeping them separate from business logic
2. **Generic Design**: ApiResponse<T> and ApiRequest<T> work with any data type
3. **Incremental Migration**: New components are added first, then existing code is refactored
4. **Backward Compatibility**: All existing endpoints, paths, and HTTP methods remain unchanged
5. **Centralized Constants**: All messages are managed in dedicated constants classes

## Components and Interfaces

### 1. ApiResponse<T>

Generic wrapper class for all API responses.

```java
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
    
    // Static factory method for success responses
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .error(null)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    // Static factory method for failure responses
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
```

**Key Design Decisions**:
- Uses Lombok's `@Data`, `@Builder`, and `@AllArgsConstructor` to minimize boilerplate
- Generic type `T` allows wrapping any data type (DTOs, entities, collections)
- Static factory methods provide convenient API for creating responses
- Timestamp is automatically set to current time in factory methods
- Error field is Object type to accommodate different error structures

### 2. RequestMeta

Metadata class for request tracking and authentication.

```java
package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestMeta {
    private String requestId;
    private String source;
    private String authToken;  // Optional field
}
```

**Key Design Decisions**:
- Simple POJO with three fields
- authToken is optional (can be null)
- Uses Lombok annotations for clean code

### 3. ApiRequest<T>

Generic wrapper class for API requests with metadata.

```java
package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiRequest<T> {
    private RequestMeta meta;
    private T payload;
}
```

**Key Design Decisions**:
- Generic type `T` allows wrapping any request payload
- Separates metadata from actual payload
- Uses Lombok annotations for clean code

### 4. Constants Classes

#### ApiConstants

```java
package com.munikiran.emplyeeDepartment.common.constants;

public class ApiConstants {
    // HTTP Status Messages
    public static final String STATUS_OK = "OK";
    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_NOT_FOUND = "NOT_FOUND";
    public static final String STATUS_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
    
    // Common field names
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    
    private ApiConstants() {
        // Prevent instantiation
    }
}
```

#### ErrorMessages

```java
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
        // Prevent instantiation
    }
}
```

#### SuccessMessages

```java
package com.munikiran.emplyeeDepartment.common.constants;

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
    
    private SuccessMessages() {
        // Prevent instantiation
    }
}
```

**Key Design Decisions**:
- All constants are `public static final String`
- Private constructors prevent instantiation
- Organized by domain (API, Errors, Success)
- Message templates allow concatenation with dynamic values

### 5. Updated GlobalExceptionHandler

```java
package com.munikiran.emplyeeDepartment.exception;

import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        ApiResponse<?> response = ApiResponse.failure(
            ex.getMessage(),
            ErrorMessages.RESOURCE_NOT_FOUND
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        ApiResponse<?> response = ApiResponse.failure(
            ErrorMessages.INTERNAL_SERVER_ERROR,
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

**Key Design Decisions**:
- Returns `ResponseEntity<ApiResponse<?>>` instead of plain `ResponseEntity`
- Uses wildcard `?` for ApiResponse since error responses don't have typed data
- Maintains original HTTP status codes (404 for not found, 500 for server errors)
- Uses constants from ErrorMessages class
- Exception message is passed to ApiResponse.failure()

### 6. Controller Refactoring Pattern

Example refactoring for DepartmentController:

**Before:**
```java
@GetMapping("/{id}")
public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
    DepartmentDTO department = departmentService.getDepartmentById(id);
    return ResponseEntity.ok(department);
}
```

**After:**
```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<DepartmentDTO>> getDepartmentById(@PathVariable Long id) {
    DepartmentDTO department = departmentService.getDepartmentById(id);
    ApiResponse<DepartmentDTO> response = ApiResponse.success(
        SuccessMessages.DEPARTMENT_RETRIEVED,
        department
    );
    return ResponseEntity.ok(response);
}
```

**Key Design Decisions**:
- Return type changes from `ResponseEntity<T>` to `ResponseEntity<ApiResponse<T>>`
- Service layer calls remain unchanged (no business logic changes)
- Use `ApiResponse.success()` factory method for successful responses
- Use constants from SuccessMessages class
- HTTP status code remains the same (200 OK)

## Data Models

### ApiResponse Structure

```json
{
  "success": true,
  "message": "Department retrieved successfully",
  "data": {
    "id": 1,
    "name": "Engineering",
    "description": "Engineering Department"
  },
  "error": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

### ApiResponse Error Structure

```json
{
  "success": false,
  "message": "Department not found with id: 999",
  "data": null,
  "error": "RESOURCE_NOT_FOUND",
  "timestamp": "2024-01-15T10:30:00"
}
```

### ApiRequest Structure

```json
{
  "meta": {
    "requestId": "req-12345",
    "source": "web-app",
    "authToken": "Bearer token123"
  },
  "payload": {
    "name": "Engineering",
    "description": "Engineering Department"
  }
}
```

### Response Transformation Examples

#### Single Entity Response

**Before:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

**After:**
```json
{
  "success": true,
  "message": "Employee retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "error": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Collection Response

**Before:**
```json
[
  {"id": 1, "name": "Engineering"},
  {"id": 2, "name": "Sales"}
]
```

**After:**
```json
{
  "success": true,
  "message": "Departments retrieved successfully",
  "data": [
    {"id": 1, "name": "Engineering"},
    {"id": 2, "name": "Sales"}
  ],
  "error": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

## Data Flow

### Successful Request Flow

```
1. Client sends HTTP request
   ↓
2. Spring DispatcherServlet routes to Controller
   ↓
3. Controller method receives request parameters
   ↓
4. Controller calls Service layer method
   ↓
5. Service performs business logic, returns DTO/Entity
   ↓
6. Controller wraps result in ApiResponse.success()
   ↓
7. Controller returns ResponseEntity<ApiResponse<T>>
   ↓
8. Spring serializes ApiResponse to JSON
   ↓
9. Client receives wrapped response with metadata
```

### Error Request Flow

```
1. Client sends HTTP request
   ↓
2. Spring DispatcherServlet routes to Controller
   ↓
3. Controller calls Service layer method
   ↓
4. Service throws exception (e.g., ResourceNotFoundException)
   ↓
5. GlobalExceptionHandler catches exception
   ↓
6. Handler wraps error in ApiResponse.failure()
   ↓
7. Handler returns ResponseEntity<ApiResponse<?>>
   ↓
8. Spring serializes ApiResponse to JSON
   ↓
9. Client receives wrapped error response with metadata
```



## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Success Factory Method Correctness

*For any* message string and data object, calling `ApiResponse.success(message, data)` should return an ApiResponse where success is true, the message field equals the provided message, the data field equals the provided data, the error field is null, and the timestamp is not null.

**Validates: Requirements 1.3, 1.5**

### Property 2: Failure Factory Method Correctness

*For any* message string and error object, calling `ApiResponse.failure(message, error)` should return an ApiResponse where success is false, the message field equals the provided message, the data field is null, the error field equals the provided error, and the timestamp is not null.

**Validates: Requirements 1.4, 1.5**

### Property 3: Timestamp Freshness

*For any* ApiResponse created via factory methods, the timestamp should be within 1 second of the current time.

**Validates: Requirements 1.5**

### Property 4: Controller Response Wrapping

*For any* controller endpoint (DepartmentController, EmployeeController, ReportController), the response type should be `ResponseEntity<ApiResponse<T>>` and successful responses should have success=true.

**Validates: Requirements 5.1, 5.2, 5.3, 5.4, 4.1**

### Property 5: Exception Handler Response Wrapping

*For any* exception handled by GlobalExceptionHandler, the response type should be `ResponseEntity<ApiResponse<?>>` and the ApiResponse should have success=false.

**Validates: Requirements 4.1, 4.4**

### Property 6: HTTP Status Code Preservation

*For any* endpoint operation, the HTTP status code should match the semantic meaning: 200 for successful retrieval/update/delete, 201 for creation, 404 for ResourceNotFoundException, and 500 for generic exceptions.

**Validates: Requirements 7.1, 7.2, 7.3, 7.4, 7.5, 4.4**

### Property 7: Endpoint Backward Compatibility

*For any* endpoint that existed before refactoring, the URL path, HTTP method, and request parameter names should remain unchanged after refactoring.

**Validates: Requirements 5.5, 8.1, 8.2, 8.3**

### Property 8: Response Data Structure Preservation

*For any* endpoint response, the data field in the ApiResponse should contain the same structure and values as the original unwrapped response.

**Validates: Requirements 8.4**

### Property 9: No Hardcoded Strings in Controllers

*For any* controller method or exception handler method, message strings should reference constants from SuccessMessages or ErrorMessages classes, not hardcoded string literals.

**Validates: Requirements 6.4**

### Property 10: Builder Pattern Functionality

*For any* valid combination of fields, the @Builder annotation should enable fluent construction of ApiResponse, ApiRequest, and RequestMeta objects.

**Validates: Requirements 10.4**

## Error Handling

### Exception Handling Strategy

The wrapper pattern integrates with Spring's existing exception handling mechanism through GlobalExceptionHandler:

1. **ResourceNotFoundException**: 
   - Wrapped in `ApiResponse.failure()`
   - HTTP 404 status
   - Error field contains "RESOURCE_NOT_FOUND"
   - Message contains specific resource details

2. **Generic Exception**:
   - Wrapped in `ApiResponse.failure()`
   - HTTP 500 status
   - Error field contains exception message
   - Message contains generic error text

3. **Future Exception Types**:
   - Follow same pattern: wrap in ApiResponse.failure()
   - Set appropriate HTTP status code
   - Provide meaningful error details

### Error Response Structure

All error responses follow this structure:

```json
{
  "success": false,
  "message": "Human-readable error message",
  "data": null,
  "error": "Error details or error code",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Validation Errors

For future validation error handling:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiResponse<?>> handleValidationException(
        MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> 
        errors.put(error.getField(), error.getDefaultMessage())
    );
    
    ApiResponse<?> response = ApiResponse.failure(
        "Validation failed",
        errors
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

## Testing Strategy

### Dual Testing Approach

This feature requires both unit tests and property-based tests to ensure comprehensive coverage:

- **Unit tests**: Verify specific examples, edge cases, and integration points
- **Property tests**: Verify universal properties across all inputs

Together, these approaches provide comprehensive coverage where unit tests catch concrete bugs and property tests verify general correctness.

### Unit Testing

**Focus Areas**:
1. **Wrapper Class Structure**: Verify ApiResponse, ApiRequest, and RequestMeta have correct fields
2. **Factory Methods**: Test specific examples of success() and failure() methods
3. **Exception Handling**: Test specific exception scenarios (ResourceNotFoundException, generic Exception)
4. **Controller Integration**: Test specific endpoint responses are properly wrapped
5. **Constants Classes**: Verify constants exist and have expected values

**Example Unit Tests**:

```java
@Test
void testApiResponseSuccessMethod() {
    String message = "Operation successful";
    String data = "test data";
    
    ApiResponse<String> response = ApiResponse.success(message, data);
    
    assertTrue(response.isSuccess());
    assertEquals(message, response.getMessage());
    assertEquals(data, response.getData());
    assertNull(response.getError());
    assertNotNull(response.getTimestamp());
}

@Test
void testGlobalExceptionHandlerResourceNotFound() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Department not found");
    
    ResponseEntity<ApiResponse<?>> response = 
        globalExceptionHandler.handleResourceNotFoundException(ex);
    
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertFalse(response.getBody().isSuccess());
    assertEquals("Department not found", response.getBody().getMessage());
}

@Test
void testDepartmentControllerGetById() {
    Long departmentId = 1L;
    DepartmentDTO mockDepartment = new DepartmentDTO(1L, "Engineering", "Eng Dept");
    when(departmentService.getDepartmentById(departmentId)).thenReturn(mockDepartment);
    
    ResponseEntity<ApiResponse<DepartmentDTO>> response = 
        departmentController.getDepartmentById(departmentId);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().isSuccess());
    assertEquals(mockDepartment, response.getBody().getData());
}
```

### Property-Based Testing

**Library**: Use JUnit-Quickcheck or jqwik for Java property-based testing

**Configuration**: Each property test should run minimum 100 iterations

**Tag Format**: Each test must reference its design property:
```java
// Feature: api-wrapper-pattern, Property 1: Success Factory Method Correctness
```

**Focus Areas**:
1. **Factory Method Properties**: Test success() and failure() with random inputs
2. **Timestamp Properties**: Verify timestamps are always fresh
3. **Controller Wrapping Properties**: Test all controller methods return wrapped responses
4. **HTTP Status Properties**: Verify status codes match semantic meanings
5. **Backward Compatibility Properties**: Verify endpoints remain unchanged

**Example Property Tests**:

```java
@Property
// Feature: api-wrapper-pattern, Property 1: Success Factory Method Correctness
void successFactoryMethodAlwaysReturnsSuccessTrue(
        @ForAll String message, 
        @ForAll String data) {
    ApiResponse<String> response = ApiResponse.success(message, data);
    
    assertTrue(response.isSuccess());
    assertEquals(message, response.getMessage());
    assertEquals(data, response.getData());
    assertNull(response.getError());
    assertNotNull(response.getTimestamp());
}

@Property
// Feature: api-wrapper-pattern, Property 2: Failure Factory Method Correctness
void failureFactoryMethodAlwaysReturnsSuccessFalse(
        @ForAll String message, 
        @ForAll String error) {
    ApiResponse<String> response = ApiResponse.failure(message, error);
    
    assertFalse(response.isSuccess());
    assertEquals(message, response.getMessage());
    assertNull(response.getData());
    assertEquals(error, response.getError());
    assertNotNull(response.getTimestamp());
}

@Property
// Feature: api-wrapper-pattern, Property 3: Timestamp Freshness
void timestampIsAlwaysFresh(@ForAll String message, @ForAll String data) {
    LocalDateTime before = LocalDateTime.now();
    ApiResponse<String> response = ApiResponse.success(message, data);
    LocalDateTime after = LocalDateTime.now();
    
    assertTrue(response.getTimestamp().isAfter(before.minusSeconds(1)));
    assertTrue(response.getTimestamp().isBefore(after.plusSeconds(1)));
}

@Property
// Feature: api-wrapper-pattern, Property 6: HTTP Status Code Preservation
void httpStatusCodesMatchSemantics(@ForAll("exceptionTypes") Exception ex) {
    ResponseEntity<ApiResponse<?>> response = 
        globalExceptionHandler.handleException(ex);
    
    if (ex instanceof ResourceNotFoundException) {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    } else {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    assertFalse(response.getBody().isSuccess());
}
```

### Integration Testing

**Focus Areas**:
1. **End-to-End Flows**: Test complete request-response cycles with wrapped responses
2. **Exception Flows**: Test that exceptions are properly caught and wrapped
3. **Multiple Controllers**: Verify all controllers work consistently with wrapper pattern

**Example Integration Test**:

```java
@SpringBootTest
@AutoConfigureMockMvc
class ApiWrapperIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testDepartmentEndpointReturnsWrappedResponse() throws Exception {
        mockMvc.perform(get("/api/departments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.error").isEmpty())
            .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    void testResourceNotFoundReturnsWrappedError() throws Exception {
        mockMvc.perform(get("/api/departments/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
```

### Test Coverage Goals

- **Unit Tests**: 80%+ code coverage for wrapper classes and refactored controllers
- **Property Tests**: All 10 correctness properties implemented
- **Integration Tests**: All existing endpoints tested with wrapped responses
- **Regression Tests**: Verify all existing functionality still works after refactoring

### Testing During Incremental Implementation

After each implementation step:
1. Run all existing tests to ensure no regression
2. Run new tests for newly added components
3. Verify application starts successfully
4. Test sample endpoints manually if needed

This ensures the application remains functional throughout the incremental migration.
