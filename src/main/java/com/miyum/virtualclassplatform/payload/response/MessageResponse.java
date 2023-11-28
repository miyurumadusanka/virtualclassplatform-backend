package com.miyum.virtualclassplatform.payload.response;

import lombok.Data;

@Data
public class MessageResponse {

    private String message;

	public MessageResponse(String message) {
		super();
		this.message = message;
	}
}
