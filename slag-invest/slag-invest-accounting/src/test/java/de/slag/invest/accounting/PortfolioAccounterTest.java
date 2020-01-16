package de.slag.invest.accounting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slag.common.base.BaseException;
import de.slag.invest.accounting.PortfolioAccounter.PortfolioAccounterResult;

public class PortfolioAccounterTest {

	private PortfolioAccounterTestGenerator testGenerator;

	@Before
	public void setUp() {
		testGenerator = new PortfolioAccounterTestGenerator();
	}
	
	@Test(expected = BaseException.class)
	public void testFailsOversold() {
		final PortfolioAccounter portfolioAccounter = new PortfolioAccounter(testGenerator.oversold());
		portfolioAccounter.setUp();
		portfolioAccounter.run();
		portfolioAccounter.getResult();
	}
	
	@Test
	public void testMedium() {
		final PortfolioAccounter portfolioAccounter = new PortfolioAccounter(testGenerator.medium());
		portfolioAccounter.setUp();
		portfolioAccounter.run();
		final PortfolioAccounterResult result = portfolioAccounter.getResult();
		Assert.assertTrue(result.getHoldings().get("ABC") == 10);
		Assert.assertTrue(result.getHoldings().get("DEF") == 50);
		Assert.assertTrue(result.getHoldings().get("HIJ") == 3000);
	}
	
	@Test
	public void testSimpleBuyAndSell() {
		final PortfolioAccounter portfolioAccounter = new PortfolioAccounter(testGenerator.simpleBuyAndSell());
		portfolioAccounter.setUp();
		portfolioAccounter.run();
		final PortfolioAccounterResult result = portfolioAccounter.getResult();
		Assert.assertTrue(result.getHoldings().get("ABC") == 10);
		Assert.assertTrue(result.getHoldings().get("DEF") == 50);
	}

	@Test
	public void testSimple() {
		final PortfolioAccounter portfolioAccounter = new PortfolioAccounter(testGenerator.simple());
		portfolioAccounter.setUp();
		portfolioAccounter.run();
		final PortfolioAccounterResult result = portfolioAccounter.getResult();
		Assert.assertTrue(result.getHoldings().get("ABC") == 30);
		Assert.assertTrue(result.getHoldings().get("DEF") == 50);
	}

	@Test
	public void testCashInOnly() {
		final PortfolioAccounter portfolioAccounter = new PortfolioAccounter(testGenerator.cashInOnly());
		portfolioAccounter.setUp();
		portfolioAccounter.run();
		final PortfolioAccounterResult result = portfolioAccounter.getResult();
		Assert.assertTrue(result.getHoldings().isEmpty());
	}
}
