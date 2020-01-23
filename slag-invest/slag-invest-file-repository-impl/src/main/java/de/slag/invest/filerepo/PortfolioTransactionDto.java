package de.slag.invest.filerepo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioTransactionDto {
	
	private String portfolioNumber;
	
	private String isin;
	
	private LocalDate date;
	
	private BigDecimal totalPrice;
	
	private Integer count;
	
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	public void setPortfolioNumber(String portfolioNumber) {
		this.portfolioNumber = portfolioNumber;
	}

	@Override
	public String toString() {
		return "PortfolioTransactionDto [portfolioNumber=" + portfolioNumber + ", isin=" + isin + ", date=" + date
				+ ", totalPrice=" + totalPrice + ", count=" + count + ", type=" + type + "]";
	}
	
	

}
