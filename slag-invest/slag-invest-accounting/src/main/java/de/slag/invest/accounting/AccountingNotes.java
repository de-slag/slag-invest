package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import de.slag.common.base.BaseException;

public class AccountingNotes {

	private BigDecimal cash = BigDecimal.ZERO;

	private Map<String, Integer> isinMap = new HashMap<>();

	public void sub(BigDecimal cash) {
		this.cash = this.cash.subtract(cash);
	}

	public void add(BigDecimal cash) {
		this.cash = this.cash.add(cash);
	}

	public void sub(String isin, Integer count, BigDecimal cash) {
		assertIsin(isin);
		isinMap.put(isin, isinMap.get(isin) - count);
		add(cash);
	}

	public void add(String isin, Integer count, BigDecimal cash) {
		assertIsin(isin);
		isinMap.put(isin, isinMap.get(isin) + count);
		sub(cash);
	}

	private void assertIsin(String isin) {
		if (isinMap.containsKey(isin)) {
			return;
		}
		isinMap.put(isin, 0);
	}

	public BigDecimal getCash() {
		return cash;
	}

	public Map<String, Integer> getIsinMap() {
		return isinMap;
	}

}
