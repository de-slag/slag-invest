package de.slag.invest.one.model;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;

public class IsSecurityPosition {
	
	private final IsSecurityPoint securityPoint;
	
	private final Integer count;
	
	private final MonetaryAmount totalValue;

	public IsSecurityPosition(IsSecurityPoint securityPoint, Integer count, MonetaryAmount totalValue) {
		super();
		this.securityPoint = securityPoint;
		this.count = count;
		this.totalValue = totalValue;
	}

	public IsSecurityPoint getSecurityPoint() {
		return securityPoint;
	}

	public Integer getCount() {
		return count;
	}

	public MonetaryAmount getTotalValue() {
		return totalValue;
	}

	

}
