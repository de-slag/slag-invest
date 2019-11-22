package de.slag.invest.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PortfolioPosition extends DomainBean {

	@ManyToOne
	private Portfolio portfolio;

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	
	

}
