package com.munikiran.emplyeeDepartment.common;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testSuccessFactoryMethod() {
        // Arrange
        String message = "Operation successful";
        String data = "test data";
        
        // Act
        ApiResponse<String> response = ApiResponse.success(message, data);
        
        // Assert
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testFailureFactoryMethod() {
        // Arrange
        String message = "Operation failed";
        String error = "Error details";
        
        // Act
        ApiResponse<String> response = ApiResponse.failure(message, error);
        
        // Assert
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(error, response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testTimestampIsSetToCurrentTime() {
        // Arrange
        LocalDateTime before = LocalDateTime.now();
        
        // Act
        ApiResponse<String> response = ApiResponse.success("Test", "data");
        
        // Assert
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(before.minusSeconds(1)));
        assertTrue(response.getTimestamp().isBefore(after.plusSeconds(1)));
    }

    @Test
    void testBuilderPattern() {
        // Arrange & Act
        ApiResponse<String> response = ApiResponse.<String>builder()
            .success(true)
            .message("Custom message")
            .data("Custom data")
            .error(null)
            .timestamp(LocalDateTime.now())
            .build();
        
        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Custom message", response.getMessage());
        assertEquals("Custom data", response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testSuccessWithNullData() {
        // Act
        ApiResponse<String> response = ApiResponse.success("Success", null);
        
        // Assert
        assertTrue(response.isSuccess());
        assertNull(response.getData());
    }

    @Test
    void testFailureWithNullError() {
        // Act
        ApiResponse<String> response = ApiResponse.failure("Failure", null);
        
        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getError());
    }

    @Test
    void testGenericTypeWithComplexObject() {
        // Arrange
        class TestData {
            String name;
            int value;
            
            TestData(String name, int value) {
                this.name = name;
                this.value = value;
            }
        }
        TestData testData = new TestData("test", 123);
        
        // Act
        ApiResponse<TestData> response = ApiResponse.success("Success", testData);
        
        // Assert
        assertTrue(response.isSuccess());
        assertEquals(testData, response.getData());
        assertEquals("test", response.getData().name);
        assertEquals(123, response.getData().value);
    }
}
