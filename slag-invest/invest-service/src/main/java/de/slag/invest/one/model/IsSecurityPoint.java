package de.slag.invest.one.model;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

public class IsSecurityPoint implements IsPoint {
	
	private IsSecurity security;

	private LocalDate pointDate;

	private MonetaryAmount pointAmount;

	public IsSecurityPoint(IsSecurity security, MonetaryAmount pointAmount, LocalDate pointDate) {
		super();
		this.security = security;
		this.pointAmount = pointAmount;
		this.pointDate = pointDate;
	}

	public IsSecurity getSecurity() {
		return security;
	}

	@Override
	public LocalDate getPointDate() {
		return pointDate;
	}

	@Override
	public MonetaryAmount getPointAmount() {
		return pointAmount;
	}

}
