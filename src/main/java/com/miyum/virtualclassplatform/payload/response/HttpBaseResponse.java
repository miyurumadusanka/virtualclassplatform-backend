package com.miyum.virtualclassplatform.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpBaseResponse {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("body")
    private Object response;

    public HttpBaseResponse(boolean status, Object response) {
        this.status = status;
        this.response = response;
    }
}
