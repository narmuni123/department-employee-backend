package com.munikiran.emplyeeDepartment.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiRequestTest {

    @Test
    void testApiRequestCreationWithAllArgsConstructor() {
        // Arrange
        String requestId = "req-123";
        String source = "web-app";
        String payload = "test payload";
        
        // Act
        ApiRequest<String> request = new ApiRequest<>(requestId, source, payload);
        
        // Assert
        assertNotNull(request);
        assertEquals(requestId, request.getRequestId());
        assertEquals(source, request.getSource());
        assertEquals(payload, request.getPayload());
    }

    @Test
    void testApiRequestBuilderPattern() {
        // Arrange
        String requestId = "req-456";
        String source = "mobile-app";
        String payload = "builder payload";
        
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .requestId(requestId)
            .source(source)
            .payload(payload)
            .build();
        
        // Assert
        assertNotNull(request);
        assertEquals(requestId, request.getRequestId());
        assertEquals(source, request.getSource());
        assertEquals(payload, request.getPayload());
    }

    @Test
    void testApiRequestWithNullRequestId() {
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .requestId(null)
            .source("test-source")
            .payload("payload")
            .build();
        
        // Assert
        assertNull(request.getRequestId());
        assertEquals("test-source", request.getSource());
        assertEquals("payload", request.getPayload());
    }

    @Test
    void testApiRequestWithNullPayload() {
        // Arrange
        String requestId = "req-789";
        String source = "api-client";
        
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .requestId(requestId)
            .source(source)
            .payload(null)
            .build();
        
        // Assert
        assertEquals(requestId, request.getRequestId());
        assertEquals(source, request.getSource());
        assertNull(request.getPayload());
    }

    @Test
    void testApiRequestWithComplexPayloadType() {
        // Arrange
        class TestPayload {
            String name;
            int value;
            
            TestPayload(String name, int value) {
                this.name = name;
                this.value = value;
            }
        }
        
        String requestId = "req-complex";
        String source = "test-app";
        TestPayload payload = new TestPayload("test", 42);
        
        // Act
        ApiRequest<TestPayload> request = ApiRequest.<TestPayload>builder()
            .requestId(requestId)
            .source(source)
            .payload(payload)
            .build();
        
        // Assert
        assertNotNull(request);
        assertEquals(requestId, request.getRequestId());
        assertEquals(source, request.getSource());
        assertEquals(payload, request.getPayload());
        assertEquals("test", request.getPayload().name);
        assertEquals(42, request.getPayload().value);
    }

    @Test
    void testApiRequestSettersAndGetters() {
        // Arrange
        ApiRequest<String> request = new ApiRequest<>();
        String requestId = "req-set";
        String source = "setter-test";
        String payload = "setter payload";
        
        // Act
        request.setRequestId(requestId);
        request.setSource(source);
        request.setPayload(payload);
        
        // Assert
        assertEquals(requestId, request.getRequestId());
        assertEquals(source, request.getSource());
        assertEquals(payload, request.getPayload());
    }

    @Test
    void testApiRequestEqualsAndHashCode() {
        // Arrange
        ApiRequest<String> request1 = new ApiRequest<>("req-1", "app", "payload");
        ApiRequest<String> request2 = new ApiRequest<>("req-1", "app", "payload");
        ApiRequest<String> request3 = new ApiRequest<>("req-1", "app", "different");
        
        // Assert
        assertEquals(request1, request2); // Same values
        assertNotEquals(request1, request3); // Different payload
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testApiRequestToString() {
        // Arrange
        ApiRequest<String> request = new ApiRequest<>("req-toString", "test", "test payload");
        
        // Act
        String toString = request.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("ApiRequest"));
        assertTrue(toString.contains("requestId"));
        assertTrue(toString.contains("source"));
        assertTrue(toString.contains("payload"));
    }
}
