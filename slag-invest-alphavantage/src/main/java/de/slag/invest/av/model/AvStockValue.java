package de.slag.invest.av.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AvStockValue {
	
	private String symbol;
	
	private LocalDate date;
	
	private BigDecimal open;
	
	private BigDecimal high;
	
	private BigDecimal low;
	
	private BigDecimal close;
	
	private Long volume;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "AvStockValue [symbol=" + symbol + ", date=" + date + ", open=" + open + ", high=" + high + ", low="
				+ low + ", close=" + close + ", volume=" + volume + "]";
	}
	
	

}
