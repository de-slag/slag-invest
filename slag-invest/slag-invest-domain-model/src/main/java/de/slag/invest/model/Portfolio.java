package de.slag.invest.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Portfolio extends DomainBean {
	
	@Basic
	private String portfolioNumber;
	
	@OneToMany(mappedBy = "portfolio")
	private final Collection<PortfolioTransaction> transactions = new ArrayList<>();

	public Collection<PortfolioTransaction> getTransactions() {
		return transactions;
	}

}
