package de.slag.invest.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class PortfolioTransaction extends MandantBean {


	@Basic
	private String portfolioNumber;

	@Basic
	private String isin;

	@Basic
	private LocalDateTime timestamp;

	@Basic
	private BigDecimal totalPrice;

	@Basic
	private Integer count;

	@Enumerated(EnumType.STRING)
	private PortfolioTransactionType type;

	public PortfolioTransaction(Mandant mandant) {
		super(mandant);
	}
	
	PortfolioTransaction() {
		this(null);
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
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

		@Deprecated
		CASH_IN,

		@Deprecated
		CASH_OUT,

		COST,

		YIELD,

		IN,

		OUT;
	}

	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	public void setPortfolioNumber(String portfolioNumber) {
		this.portfolioNumber = portfolioNumber;
	}


	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "PortfolioTransaction [portfolioNumber=" + portfolioNumber + ", isin=" + isin + ", timestamp="
				+ timestamp + ", totalPrice=" + totalPrice + ", count=" + count + ", type=" + type + "]";
	}

}
