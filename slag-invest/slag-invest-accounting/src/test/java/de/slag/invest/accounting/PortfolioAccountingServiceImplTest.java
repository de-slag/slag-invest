package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

public class PortfolioAccountingServiceImplTest {

	
	private static final PortfolioTransactionType BUY = PortfolioTransactionType.BUY;
	private PortfolioAccountingServiceImpl portfolioAccountingServiceImpl;

	@Before
	public void setUp() {
		portfolioAccountingServiceImpl = new PortfolioAccountingServiceImpl();
	}

	@Test
	public void it() {
		Portfolio portfolio = new Portfolio();
		

		portfolio.add(createTransaction(portfolio, "", date(1, 1), 1, 5000,
				PortfolioTransactionType.CASH_IN));
		portfolio.add(
				createTransaction(portfolio, "ISIN_A", date(1, 2), 50, 2500, BUY));

		portfolioAccountingServiceImpl.account(portfolio);
		Assert.assertTrue(BigDecimal.valueOf(2500).equals(portfolio.getCash()));
	}

	private PortfolioTransaction createTransaction(Portfolio portfolio, String isin, LocalDate localDate, Integer count,
			Integer totalPrice, PortfolioTransactionType type) {
		PortfolioTransaction portfolioTransaction = new PortfolioTransaction();
		portfolioTransaction.setPortfolio(portfolio);
		portfolioTransaction.setIsin(isin);
		portfolioTransaction.setDate(localDate);
		portfolioTransaction.setCount(count);
		portfolioTransaction.setType(type);
		portfolioTransaction.setTotalPrice(BigDecimal.valueOf(totalPrice));
		return portfolioTransaction;
	}
	
	private LocalDate date(int month, int dayOfMonth) {
		return LocalDate.of(2010, month, dayOfMonth);
	}

}
