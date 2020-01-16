package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.slag.common.base.BaseException;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

public class PortfolioAccounter {

	private Collection<PortfolioTransaction> transactions = new ArrayList<PortfolioTransaction>();

	private BigDecimal cash = BigDecimal.ZERO;

	private boolean readyToRun = false;

	private boolean done = false;

	private Map<String, Integer> holdings = new HashMap<>();

	public PortfolioAccounter(Collection<PortfolioTransaction> transactions) {
		Objects.requireNonNull(transactions, "transactions not setted");
		this.transactions.addAll(transactions);
	}

	public void setUp() {
		assert !transactions.isEmpty();
		readyToRun = true;
	}

	public void run() {
		if (!readyToRun) {
			throw new BaseException("not ready to run, call 'setUp' first");
		}

		transactions.forEach(t -> {
			if (isCashTransaction(t)) {
				return;
			}
			final String isin = t.getIsin();
			if (!holdings.containsKey(isin)) {
				holdings.put(isin, 0);
			}

			switch (t.getType()) {
			case BUY:
				holdings.put(isin, holdings.get(isin) + t.getCount());
				break;
			case SELL:
				holdings.put(isin, holdings.get(isin) - t.getCount());
				break;

			default:
				throw new BaseException("not supported: " + t.getType());
			}
		});

		done = true;
	}

	private void validate() {
		holdings.keySet().forEach(isin -> {
			final Integer count = holdings.get(isin);
			if (count < 0) {
				throw new BaseException("not valid: " + isin + ", count " + count);
			}
		});
	}

	private boolean isCashTransaction(PortfolioTransaction t) {
		return Arrays.asList(PortfolioTransactionType.CASH_OUT, PortfolioTransactionType.CASH_IN,
				PortfolioTransactionType.YIELD, PortfolioTransactionType.COST).contains(t.getType());
	}

	public PortfolioAccounterResult getResult() {
		if (!done) {
			throw new BaseException("not done, call 'run' first");
		}
		validate();

		return new PortfolioAccounterResult() {

			@Override
			public Map<String, Integer> getHoldings() {
				return holdings;
			}

			@Override
			public BigDecimal getCash() {
				return cash;
			}
		};
	}

	public interface PortfolioAccounterResult {

		BigDecimal getCash();

		Map<String, Integer> getHoldings();

	}

}
