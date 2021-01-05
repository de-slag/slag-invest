package de.slag.invest.one.model;

import java.math.BigDecimal;

public class IsSecurity {
	
	private final String wknIsin;
	
	private final String description;
	
	private final BigDecimal price;

	private IsSecurity(String wknIsin, String description, BigDecimal price) {
		super();
		this.wknIsin = wknIsin;
		this.description = description;
		this.price = price;
	}

	public String getWknIsin() {
		return wknIsin;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
