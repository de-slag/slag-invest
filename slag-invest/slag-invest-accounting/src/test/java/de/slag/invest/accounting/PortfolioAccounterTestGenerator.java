package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

public class PortfolioAccounterTestGenerator {

	private static final PortfolioTransactionType CASH_IN = PortfolioTransactionType.CASH_IN;
	private static final PortfolioTransactionType CASH_OUT = PortfolioTransactionType.CASH_OUT;

	public Collection<PortfolioTransaction> medium() {
		Collection<PortfolioTransaction> transactions = new ArrayList<>();

		transactions.add(cashTransaction(LocalDate.of(2010, 01, 02).atStartOfDay(), CASH_IN, 10000));
		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 03).atStartOfDay(), 30, PortfolioTransactionType.BUY, 3000));
		transactions.add(transaction("DEF", LocalDate.of(2010, 01, 04).atStartOfDay(), 50, PortfolioTransactionType.BUY, 5000));
		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 05).atStartOfDay(), 20, PortfolioTransactionType.SELL, 2000));
		transactions.add(transaction("HIJ", LocalDate.of(2010, 01, 06).atStartOfDay(), 3000, PortfolioTransactionType.BUY, 300));
		transactions.add(cashTransaction(LocalDate.of(2010, 01, 07).atStartOfDay(), CASH_OUT, 3000));

		return transactions;
	}

	public Collection<PortfolioTransaction> simpleBuyAndSell() {
		Collection<PortfolioTransaction> transactions = new ArrayList<>();

		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 03).atStartOfDay(), 30, PortfolioTransactionType.BUY, 3000));
		transactions.add(transaction("DEF", LocalDate.of(2010, 01, 04).atStartOfDay(), 50, PortfolioTransactionType.BUY, 5000));
		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 05).atStartOfDay(), 20, PortfolioTransactionType.SELL, 2000));

		return transactions;
	}
	
	public Collection<PortfolioTransaction> oversold() {
		Collection<PortfolioTransaction> transactions = new ArrayList<>();

		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 03).atStartOfDay(), 30, PortfolioTransactionType.BUY, 3000));
		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 04).atStartOfDay(), 50, PortfolioTransactionType.SELL, 5000));

		return transactions;
	}

	public Collection<PortfolioTransaction> simple() {
		Collection<PortfolioTransaction> transactions = new ArrayList<>();

		transactions.add(transaction("ABC", LocalDate.of(2010, 01, 03).atStartOfDay(), 30, PortfolioTransactionType.BUY, 3000));
		transactions.add(transaction("DEF", LocalDate.of(2010, 01, 04).atStartOfDay(), 50, PortfolioTransactionType.BUY, 5000));

		return transactions;
	}

	public Collection<PortfolioTransaction> cashInOnly() {
		Collection<PortfolioTransaction> transactions = new ArrayList<>();

		transactions.add(cashTransaction(LocalDate.of(2010, 01, 02).atStartOfDay(), CASH_IN, 5000));

		return transactions;
	}

	private PortfolioTransaction cashTransaction(LocalDateTime date, PortfolioTransactionType type, Integer totalPrice) {
		return transaction(null, date, null, type, totalPrice);
	}

	private PortfolioTransaction transaction(String isin, LocalDateTime date, Integer count, PortfolioTransactionType type,
			Integer totalPrice) {
		final PortfolioTransaction t = new PortfolioTransaction(null);

		t.setIsin(isin);
		t.setType(type);
		t.setCount(count);
		t.setTimestamp(date);
		t.setIsin(isin);
		t.setTotalPrice(BigDecimal.valueOf(totalPrice));

		return t;
	}

}
