package de.slag.invest.one.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.slag.common.base.BaseException;
import de.slag.common.util.DateUtils;

public class IsPortfolio {

	private List<IsTransaction> transactions = new ArrayList<>();

	private IsPortfolio(Collection<IsTransaction> transactions) {
		super();
		this.transactions.addAll(transactions);
	}

	public Collection<IsTransaction> getTransactions() {
		return transactions;
	}

	// TODO refactor this to another logic class
	public Map<IsSecurity, Integer> getHoldingsFor(LocalDate date) {
		List<IsTransaction> relevantTransactions = new ArrayList<IsTransaction>(transactions.stream()
				.filter(t -> DateUtils.isEqualOrBefore(date, t.getDate()))
				.collect(Collectors.toList()));
		Collections.sort(relevantTransactions, new Comparator<IsTransaction>() {

			@Override
			public int compare(IsTransaction arg0, IsTransaction arg1) {
				return arg0.getDate().compareTo(arg1.getDate());
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
		}
		return holdings;
	}
}
