package de.slag.invest.one.model;

import java.math.BigDecimal;

public class IsSecurityPosition {
	
	private final IsSecurity security;
	
	private final Integer count;
	
	private final BigDecimal totalValue;

	public IsSecurityPosition(IsSecurity security, Integer count, BigDecimal totalValue) {
		super();
		this.security = security;
		this.count = count;
		this.totalValue = totalValue;
	}

	public IsSecurity getSecurity() {
		return security;
	}

	public Integer getCount() {
		return count;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	

}
