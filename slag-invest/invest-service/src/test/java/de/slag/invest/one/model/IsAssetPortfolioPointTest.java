package de.slag.invest.one.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.base.pattern.BuildException;
import de.slag.common.util.CurrencyUtils;
import de.slag.invest.one.TestIsPortfolioProvider;
import de.slag.invest.one.TestIsSecurityPointProvider;
import de.slag.invest.one.TestIsSecurityProvider;
import de.slag.invest.one.TransactionLoadingTestSupport;
import de.slag.invest.one.portfolio.IsPortfolioPointBuilder;
import de.slag.invest.one.portfolio.IsPortfolioProvider;
import de.slag.invest.one.portfolio.IsSecurityPointProvider;
import de.slag.invest.one.portfolio.IsSecurityProvider;

class IsAssetPortfolioPointTest {

	IsClearingAccountPoint defaultClearingAccountPoint = new IsClearingAccountPoint(LocalDate.of(2010, 1, 4),
			CurrencyUtils.newAmount(5000));

	LocalDate defaultDate = LocalDate.of(2010, 1, 4);

	IsSecurityPointProvider securityPointProvider;

	IsSecurityProvider securityProvider;
	
	IsPortfolioProvider portfolioProvider;

	Map<String, Integer> defaultPortfolioContent = new HashMap<>();

	IsPortfolioPoint deaultPortfolioPoint;

	TransactionLoadingTestSupport transactionLoadingTestSupport;

	@BeforeEach
	public void setUp() throws Exception {
		securityProvider = TestIsSecurityProvider.build();
		securityPointProvider = TestIsSecurityPointProvider.buildWith(securityProvider);
		portfolioProvider = TestIsPortfolioProvider.buildWith(securityProvider);
		
		
		transactionLoadingTestSupport = new TransactionLoadingTestSupport(securityProvider);

		final Collection<IsTransaction> defaultTransactions = transactionLoadingTestSupport
				.loadTransactions("portfolio-001");

		final IsPortfolio isPortfolio = new IsPortfolio(defaultTransactions);
		deaultPortfolioPoint = IsPortfolioPointBuilder.of(securityPointProvider, isPortfolio, defaultDate).build();
		deaultPortfolioPoint.getSecurityPositions()
				.forEach(p -> defaultPortfolioContent.put(p.getSecurityPoint().getSecurity().getWknIsin(),
						p.getCount()));
	}

	@Test
	void testCreation() throws Exception {
		final IsClearingAccountPoint clearingAccountPoint = new IsClearingAccountPoint(defaultDate,
				CurrencyUtils.newAmount(5000));

		IsPortfolioPoint portfolioPoint = new IsPortfolioPointBuilder(securityPointProvider, defaultPortfolioContent,
				defaultDate).build();
		final IsAssetPortfolioPoint assetPortfolioPoint = IsAssetPortfolioPoint.buildWith(portfolioPoint,
				clearingAccountPoint);
		assertNotNull(assetPortfolioPoint);
	}

	@Test
	void testFailBuildWithNoParameters() throws Exception {
		assertThrows(BuildException.class, () -> IsAssetPortfolioPoint.buildWith(null, null));
		assertThrows(BuildException.class, () -> IsAssetPortfolioPoint.buildWith(null, defaultClearingAccountPoint));
		assertThrows(BuildException.class, () -> IsAssetPortfolioPoint.buildWith(deaultPortfolioPoint, null));

	}

}
