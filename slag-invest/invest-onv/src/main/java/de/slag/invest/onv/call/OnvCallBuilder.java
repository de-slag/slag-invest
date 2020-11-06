package de.slag.invest.onv.call;

import org.apache.commons.lang3.builder.Builder;

public class OnvCallBuilder implements Builder<OnvCall> {
	
	private String baseUrl;
	
	private String notationId;
	
	public OnvCallBuilder withBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}
	
	public OnvCallBuilder withNotationIn(String notationId) {
		this.notationId = notationId;
		return this;
	}

	@Override
	public OnvCall build() {
		return new OnvCall(String.format(baseUrl, notationId));
	}

}
