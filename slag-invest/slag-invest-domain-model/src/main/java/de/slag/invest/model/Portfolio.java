package de.slag.invest.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Portfolio extends DomainBean {

	@Basic
	private String portfolioNumber;

	@Basic
	private BigDecimal cash;

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	public void setPortfolioNumber(String portfolioNumber) {
		this.portfolioNumber = portfolioNumber;
	}

}
