package de.slag.invest.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountingNotesValidator {

	private final List<String> issues = new ArrayList<>();

	public boolean isValid(AccountingNotes notes) {
		issues.clear();
		BigDecimal cash = notes.getCash();
		if (BigDecimal.ZERO.compareTo(cash) > 0) {
			issues.add("cash negative");
		}

		Map<String, Integer> isinMap = notes.getIsinMap();
		isinMap.keySet().forEach(isin -> {
			if (isinMap.get(isin) < 0) {
				issues.add(isin + " negative");
			}
		});

		return issues.isEmpty();
	}

	public List<String> getIssues() {
		return issues;
	}

	@Override
	public String toString() {
		return issues.isEmpty() ? "VALID" : ("INVALID: " + String.join("; ", issues));
	}

}
