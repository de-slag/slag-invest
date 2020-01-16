package de.slag.invest.filerepo;

import java.util.ArrayList;

public class PortfolioTransactionDataValidatorTestUtils {

	public static String getMessage(PortfolioTransactionDataValidator validator) {
		return new ArrayList<>(validator.getValidateIssues()).get(0);
	}

}
