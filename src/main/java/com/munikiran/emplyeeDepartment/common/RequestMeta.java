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
    

    private String authToken;
}
