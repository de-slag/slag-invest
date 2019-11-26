package de.slag.invest.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class PortfolioTransaction extends DomainBean {
	
	@ManyToOne
	private Portfolio portfolio;

	@Basic
	private String isin;

	@Basic
	private LocalDate date;

	@Basic
	private BigDecimal totalPrice;

	@Basic
	private Integer count;

	@Enumerated(EnumType.STRING)
	private PortfolioTransactionType type;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public PortfolioTransactionType getType() {
		return type;
	}

	public void setType(PortfolioTransactionType type) {
		this.type = type;
	}
	
	public enum PortfolioTransactionType {
		BUY,

		SELL,

		CASH_IN,

		CASH_OUT,

		COST,

		YIELD;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

}