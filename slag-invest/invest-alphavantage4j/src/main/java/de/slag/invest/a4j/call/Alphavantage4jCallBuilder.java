package de.slag.invest.a4j.call;

import java.util.Objects;

public class Alphavantage4jCallBuilder {
	
	private String apiKey;

	private String symbol;
	
	public Alphavantage4jCallBuilder withSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	
	public Alphavantage4jCallBuilder withApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	public Alphavantage4jCall build() {
		Objects.requireNonNull(apiKey,"apiKey not setted");
		Objects.requireNonNull(symbol, "symbol not setted");
		return new Alphavantage4jCall(apiKey,symbol);
	}

}
