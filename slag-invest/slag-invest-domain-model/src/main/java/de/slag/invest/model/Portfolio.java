package de.slag.invest.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.slag.invest.DomainBeanValidFilter;

@Entity
public class Portfolio extends DomainBean {
	
	@Basic
	private String portfolioNumber;
	
	@Basic
	private BigDecimal cash;
	
	@OneToMany(mappedBy = "portfolio")
	private final Collection<PortfolioTransaction> transactions = new ArrayList<>();
	
	@OneToMany(mappedBy = "portfolio")
	private final Collection<PortfolioPosition> positions = new ArrayList<>();

	public Collection<PortfolioTransaction> getTransactions() {
		return transactions.stream().filter(new DomainBeanValidFilter()).collect(Collectors.toList());
	}
	
	public void add(PortfolioTransaction t) {
		assert t.getPortfolio() == null;
		t.setPortfolio(this);
		transactions.add(t);
	}
	
	public void add(PortfolioPosition p) {
		assert p.getPortfolio() == null;
		p.setPortfolio(this);
		positions.add(p);
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

}
