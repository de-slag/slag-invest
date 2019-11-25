package de.slag.invest.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class PortfolioPosition extends DomainBean {

	@Basic
	private String portfolioNumber;

	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	public void setPortfolioNumber(String portfolioNumber) {
		this.portfolioNumber = portfolioNumber;
	}

}
