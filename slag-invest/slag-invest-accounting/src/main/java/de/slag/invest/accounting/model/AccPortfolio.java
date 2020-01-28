package de.slag.invest.accounting.model;

import java.util.ArrayList;
import java.util.List;

import de.slag.invest.model.PortfolioTransaction;

public class AccPortfolio {
	
	private final String number;
	
	private final List<PortfolioTransaction> transactions = new ArrayList<>();

	public AccPortfolio(String number) {
		super();
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public List<PortfolioTransaction> getTransactions() {
		return transactions;
	}

}
