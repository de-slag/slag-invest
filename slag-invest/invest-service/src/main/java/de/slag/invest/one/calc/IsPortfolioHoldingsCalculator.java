package de.slag.invest.one.calc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.slag.common.base.BaseException;
import de.slag.common.util.DateUtils;
import de.slag.invest.one.model.IsPortfolio;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsTransaction;
import de.slag.invest.one.model.IsTransactionType;

public class IsPortfolioHoldingsCalculator implements Calculator<Map<IsSecurity, Integer>> {

	private IsPortfolio portfolio;

	private LocalDate date;

	public IsPortfolioHoldingsCalculator(IsPortfolio portfolio, LocalDate date) {
		super();
		this.portfolio = portfolio;
		this.date = date;
	}

	@Override
	public Map<IsSecurity, Integer> calculate() throws Exception {
		List<IsTransaction> relevantTransactions = new ArrayList<IsTransaction>(portfolio.getTransactions()
				.stream()
				.filter(t -> DateUtils.isEqualOrBefore(date, t.getDate()))
				.collect(Collectors.toList()));
		Collections.sort(relevantTransactions, new Comparator<IsTransaction>() {

			@Override
			public int compare(IsTransaction arg0, IsTransaction arg1) {
				final int compareTo = arg0.getDate().compareTo(arg1.getDate());
				if (compareTo != 0) {
					return compareTo;
				}
				return arg0.getTransactionType().compareTo(arg1.getTransactionType());
			}
		});

		Map<IsSecurity, Integer> holdings = new HashMap<>();
		for (IsTransaction transaction : relevantTransactions) {
			final IsTransactionType transactionType = Objects.requireNonNull(transaction.getTransactionType());
			final int count = transaction.getCount();

			final IsSecurity security = transaction.getSecurity();
			if (!holdings.containsKey(security)) {
				holdings.put(security, 0);
			}
			final int countBefore = holdings.get(security);
			final int countAfter;

			if (IsTransactionType.BUY == transactionType) {
				countAfter = countBefore + count;
			} else if (IsTransactionType.SELL == transactionType) {
				countAfter = countBefore - count;
			} else {
				throw new BaseException("not supported: " + transactionType);
			}
			if (countAfter < 0) {
				throw new BaseException(String.format("count < 0 for %s at transaction %s", security, transaction));
			}
			holdings.put(security, countAfter);
		}
		return holdings;
	}

}
