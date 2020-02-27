package de.slag.invest.webcommon;

import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.Builder;

public class WsResponseBuilder implements Builder<WsResponse<?>> {

	private Object responseValue;

	private Boolean success;
	
	private String message;
	
	public WsResponseBuilder withMessage(String message) {
		this.message = message;
		return this;
	}

	public WsResponseBuilder withValue(Object value) {
		this.responseValue = value;
		return this;
	}

	public WsResponseBuilder withSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	public WsResponse<?> build() {
		WsResponse<?> wsResponse;
		if (responseValue instanceof String || responseValue == null) {
			wsResponse = createStringResponse();
		} else {
			throw new RuntimeException("not supported type: " + responseValue);
		}
		wsResponse.setCreatedAt(new Date());
		wsResponse.setSuccessful(BooleanUtils.isTrue(success));	
		wsResponse.setMessage(message);
		
		return wsResponse;

	}

	public WsResponse<String> createStringResponse() {
		final WsStringResponse wsStringResponse = new WsStringResponse();
		wsStringResponse.setValue((String) responseValue);
		return wsStringResponse;		
	}

}
