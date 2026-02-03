package com.munikiran.emplyeeDepartment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest<T> {
    private String requestId;
    private String source;
    private T payload;
}
