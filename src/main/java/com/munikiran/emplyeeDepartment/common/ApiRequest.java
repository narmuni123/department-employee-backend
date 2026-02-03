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
