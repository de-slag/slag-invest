package de.slag.invest.accounting.model;

import java.time.LocalDate;

public class AccPortfolioStatement {
	
	private final AccPortfolio portfolio;
	
	private final LocalDate from;
	
	private final LocalDate to;

	public AccPortfolioStatement(AccPortfolio portfolio, LocalDate from, LocalDate to) {
		super();
		this.portfolio = portfolio;
		this.from = from;
		this.to = to;
	}

	public AccPortfolio getPortfolio() {
		return portfolio;
	}

	public LocalDate getFrom() {
		return from;
	}

	public LocalDate getTo() {
		return to;
	}
	
	
	
	

}
