package com.munikiran.emplyeeDepartment.exception;

import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFoundException_ReturnsWrappedResponse() {
        // Arrange
        String errorMessage = "Department not found with id: 123";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleResourceNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(errorMessage, body.getMessage());
        assertNull(body.getData());
        assertEquals(ErrorMessages.RESOURCE_NOT_FOUND, body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleResourceNotFoundException_MaintainsHttp404Status() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleResourceNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHandleResourceNotFoundException_UsesErrorMessagesConstant() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleResourceNotFoundException(exception);

        // Assert
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorMessages.RESOURCE_NOT_FOUND, body.getError());
    }

    @Test
    void testHandleResourceNotFoundException_ReturnsApiResponseType() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleResourceNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testHandleGenericException_ReturnsWrappedResponse() {
        // Arrange
        String errorMessage = "Something went wrong";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ErrorMessages.INTERNAL_SERVER_ERROR, body.getMessage());
        assertNull(body.getData());
        assertEquals(errorMessage, body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleGenericException_MaintainsHttp500Status() {
        // Arrange
        Exception exception = new Exception("Generic error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testHandleGenericException_UsesErrorMessagesConstant() {
        // Arrange
        Exception exception = new Exception("Test error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleGenericException(exception);

        // Assert
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorMessages.INTERNAL_SERVER_ERROR, body.getMessage());
    }

    @Test
    void testHandleGenericException_ReturnsApiResponseType() {
        // Arrange
        Exception exception = new Exception("Test error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testHandleGenericException_SuccessIsFalse() {
        // Arrange
        Exception exception = new Exception("Test error");

        // Act
        ResponseEntity<ApiResponse<?>> response = 
            globalExceptionHandler.handleGenericException(exception);

        // Assert
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
}
