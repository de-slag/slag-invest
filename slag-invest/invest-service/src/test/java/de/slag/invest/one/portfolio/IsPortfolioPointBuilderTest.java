package de.slag.invest.one.portfolio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slag.common.util.CurrencyUtils;
import de.slag.invest.one.model.IsPortfolioPoint;

class IsPortfolioPointBuilderTest {

	static IsSecurityPointProvider securityPointProvider;

	@BeforeAll
	static void setUp() {
		securityPointProvider = new IsSecurityOpportunisticTestProvider();
	}

	@Test
	void simpleTest() throws Exception {
		Map<String, Integer> portfolioContent = new HashMap<>();
		portfolioContent.put("846900", 10);
		portfolioContent.put("555700", 125);

		final IsPortfolioPointBuilder isPortfolioPointBuilder = new IsPortfolioPointBuilder(securityPointProvider, portfolioContent,
				LocalDate.now());
		
		final IsPortfolioPoint portfolioPoint = isPortfolioPointBuilder.build();
		assertNotNull(portfolioPoint);
		assertEquals(CurrencyUtils.newAmount(24080), portfolioPoint.getPointAmount());
	}

}
