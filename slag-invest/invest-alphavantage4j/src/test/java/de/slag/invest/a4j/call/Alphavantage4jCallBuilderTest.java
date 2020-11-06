package de.slag.invest.a4j.call;

import org.junit.jupiter.api.Test;

class Alphavantage4jCallBuilderTest {

	@Test
	void integrationTest() throws Exception {
		Alphavantage4jCallBuilder alphavantage4jCallBuilder = new Alphavantage4jCallBuilder();
		alphavantage4jCallBuilder.withApiKey("SEOG69AIA6X9PGR2");
		alphavantage4jCallBuilder.withSymbol("MSFT");
		Alphavantage4jCall a4jCall = alphavantage4jCallBuilder.build();
		a4jCall.call();
	}

}
