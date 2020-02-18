package de.slag.invest.accounting.gen;

import java.math.BigDecimal;
import java.util.Collection;

import de.slag.invest.facades.PortfolioFacade;
import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

public class TestCaseGenerator {

	private static final PortfolioTransactionType BUY = PortfolioTransactionType.BUY;
	private static final PortfolioTransactionType CASH_IN = PortfolioTransactionType.CASH_IN;

	public PortfolioFacade generateCase01() {
		final Portfolio portfolio = new Portfolio(null);
		portfolio.setPortfolioNumber("testcase01");

		final PortfolioFacade portfolioFacade = new PortfolioFacade(portfolio);
		final Collection<PortfolioTransaction> transactions = portfolioFacade.getTransactions();

		transactions.add(cashTransaction(bigDecimalOf(5000), CASH_IN, portfolio.getPortfolioNumber()));
		transactions.add(cashTransaction(bigDecimalOf(5000), CASH_IN, portfolio.getPortfolioNumber()));
		transactions.add(transaction("4711-0815", 50, bigDecimalOf(750), BUY, portfolio.getPortfolioNumber()));
		return portfolioFacade;

	}

	private BigDecimal bigDecimalOf(final int value) {
		return BigDecimal.valueOf(value);
	}

	private PortfolioTransaction cashTransaction(BigDecimal total, PortfolioTransactionType type,
			String portfolioNumber) {
		return transaction(null, 1, total, type, portfolioNumber);
	}

	private PortfolioTransaction transaction(String isin, Integer count, BigDecimal totalPrice,
			PortfolioTransactionType type, String portfolioNumber) {
		final PortfolioTransaction portfolioTransaction = new PortfolioTransaction(null);
		portfolioTransaction.setIsin(isin);
		portfolioTransaction.setCount(count);
		portfolioTransaction.setTotalPrice(totalPrice);
		portfolioTransaction.setType(type);
		portfolioTransaction.setPortfolioNumber(portfolioNumber);
		return portfolioTransaction;
	}
}
