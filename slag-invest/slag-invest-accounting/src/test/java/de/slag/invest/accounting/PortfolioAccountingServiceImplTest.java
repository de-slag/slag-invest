package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

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
	public void testAccount() {
		Portfolio portfolio = new Portfolio();
		Collection<PortfolioTransaction> transactions = portfolio.getTransactions();

		transactions.add(createTransaction(portfolio, "", LocalDate.of(2010, 01, 01), 1, BigDecimal.valueOf(5000),
				PortfolioTransactionType.CASH_IN));
		transactions.add(
				createTransaction(portfolio, "ISIN_A", LocalDate.of(2010, 01, 02), 50, BigDecimal.valueOf(2500), BUY));

		portfolioAccountingServiceImpl.account(portfolio);
	}

	private PortfolioTransaction createTransaction(Portfolio portfolio, String isin, LocalDate localDate, Integer count,
			BigDecimal totalPrice, PortfolioTransactionType type) {
		PortfolioTransaction portfolioTransaction = new PortfolioTransaction();
		portfolioTransaction.setPortfolio(portfolio);
		portfolioTransaction.setIsin(isin);
		portfolioTransaction.setDate(localDate);
		portfolioTransaction.setCount(count);
		portfolioTransaction.setType(type);
		portfolioTransaction.setTotalPrice(totalPrice);
		return portfolioTransaction;
	}

}
