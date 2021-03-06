package de.slag.invest.onv.call;

import org.apache.commons.lang3.builder.Builder;

public class OnvCallBuilder implements Builder<OnvCall> {

	private String baseUrl;

	private String notationId;

	private String dateInfo;

	public OnvCallBuilder withBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public OnvCallBuilder withNotationIn(String notationId) {
		this.notationId = notationId;
		return this;
	}

	public OnvCallBuilder withDateInfo(String dateInfo) {
		this.dateInfo = dateInfo;
		return this;
	}

	@Override
	public OnvCall build() {
		return new OnvCall(String.format(baseUrl, notationId, dateInfo));
	}

}
