package de.slag.invest.one.model;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

public class IsClearingAccountPoint implements IsPoint {
	
	private LocalDate pointDate;
	
	private MonetaryAmount pointAmount;

	@Override
	public LocalDate getPointDate() {
		return pointDate;
	}

	@Override
	public MonetaryAmount getPointAmount() {
		return pointAmount;
	}

	public IsClearingAccountPoint(LocalDate pointDate, MonetaryAmount pointAmount) {
		super();
		this.pointDate = pointDate;
		this.pointAmount = pointAmount;
	}

}
