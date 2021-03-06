package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slag.invest.facades.PortfolioFacade;
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
		Portfolio portfolio = new Portfolio(null);
		final PortfolioFacade portfolioFacade = new PortfolioFacade(portfolio);
		final Collection<PortfolioTransaction> transactions = portfolioFacade.getTransactions();

		transactions.add(createTransaction(portfolio, "", date(1, 1).atStartOfDay(), 1, 5000, PortfolioTransactionType.CASH_IN));
		transactions.add(createTransaction(portfolio, "ISIN_A", date(1, 2).atStartOfDay(), 50, 2500, BUY));

		portfolioAccountingServiceImpl.account(portfolioFacade);
		Assert.assertTrue(BigDecimal.valueOf(2500).equals(portfolio.getCash()));
	}

	private PortfolioTransaction createTransaction(Portfolio portfolio, String isin, LocalDateTime localDate, Integer count,
			Integer totalPrice, PortfolioTransactionType type) {
		PortfolioTransaction portfolioTransaction = new PortfolioTransaction(null);
		portfolioTransaction.setIsin(isin);
		portfolioTransaction.setTimestamp(localDate);
		portfolioTransaction.setCount(count);
		portfolioTransaction.setType(type);
		portfolioTransaction.setTotalPrice(BigDecimal.valueOf(totalPrice));
		return portfolioTransaction;
	}

	private LocalDate date(int month, int dayOfMonth) {
		return LocalDate.of(2010, month, dayOfMonth);
	}

}
