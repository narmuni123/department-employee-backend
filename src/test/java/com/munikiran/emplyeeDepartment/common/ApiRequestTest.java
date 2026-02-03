package com.munikiran.emplyeeDepartment.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiRequestTest {

    @Test
    void testApiRequestCreationWithAllArgsConstructor() {
        // Arrange
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-123")
            .source("web-app")
            .authToken("token-abc")
            .build();
        String payload = "test payload";
        
        // Act
        ApiRequest<String> request = new ApiRequest<>(meta, payload);
        
        // Assert
        assertNotNull(request);
        assertEquals(meta, request.getMeta());
        assertEquals(payload, request.getPayload());
    }

    @Test
    void testApiRequestBuilderPattern() {
        // Arrange
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-456")
            .source("mobile-app")
            .authToken("token-xyz")
            .build();
        String payload = "builder payload";
        
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .meta(meta)
            .payload(payload)
            .build();
        
        // Assert
        assertNotNull(request);
        assertEquals(meta, request.getMeta());
        assertEquals(payload, request.getPayload());
        assertEquals("req-456", request.getMeta().getRequestId());
        assertEquals("mobile-app", request.getMeta().getSource());
        assertEquals("token-xyz", request.getMeta().getAuthToken());
    }

    @Test
    void testApiRequestWithNullMeta() {
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .meta(null)
            .payload("payload")
            .build();
        
        // Assert
        assertNull(request.getMeta());
        assertEquals("payload", request.getPayload());
    }

    @Test
    void testApiRequestWithNullPayload() {
        // Arrange
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-789")
            .source("api-client")
            .build();
        
        // Act
        ApiRequest<String> request = ApiRequest.<String>builder()
            .meta(meta)
            .payload(null)
            .build();
        
        // Assert
        assertNotNull(request.getMeta());
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
        
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-complex")
            .source("test-app")
            .authToken("test-token")
            .build();
        TestPayload payload = new TestPayload("test", 42);
        
        // Act
        ApiRequest<TestPayload> request = ApiRequest.<TestPayload>builder()
            .meta(meta)
            .payload(payload)
            .build();
        
        // Assert
        assertNotNull(request);
        assertEquals(meta, request.getMeta());
        assertEquals(payload, request.getPayload());
        assertEquals("test", request.getPayload().name);
        assertEquals(42, request.getPayload().value);
    }

    @Test
    void testApiRequestSettersAndGetters() {
        // Arrange
        ApiRequest<String> request = new ApiRequest<>(null, null);
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-set")
            .source("setter-test")
            .build();
        String payload = "setter payload";
        
        // Act
        request.setMeta(meta);
        request.setPayload(payload);
        
        // Assert
        assertEquals(meta, request.getMeta());
        assertEquals(payload, request.getPayload());
    }

    @Test
    void testApiRequestEqualsAndHashCode() {
        // Arrange
        RequestMeta meta1 = RequestMeta.builder()
            .requestId("req-1")
            .source("app")
            .authToken("token")
            .build();
        RequestMeta meta2 = RequestMeta.builder()
            .requestId("req-1")
            .source("app")
            .authToken("token")
            .build();
        
        ApiRequest<String> request1 = new ApiRequest<>(meta1, "payload");
        ApiRequest<String> request2 = new ApiRequest<>(meta2, "payload");
        ApiRequest<String> request3 = new ApiRequest<>(meta1, "different");
        
        // Assert
        assertEquals(request1, request2); // Same values
        assertNotEquals(request1, request3); // Different payload
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testApiRequestToString() {
        // Arrange
        RequestMeta meta = RequestMeta.builder()
            .requestId("req-toString")
            .source("test")
            .build();
        ApiRequest<String> request = new ApiRequest<>(meta, "test payload");
        
        // Act
        String toString = request.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("ApiRequest"));
        assertTrue(toString.contains("meta"));
        assertTrue(toString.contains("payload"));
    }
}
