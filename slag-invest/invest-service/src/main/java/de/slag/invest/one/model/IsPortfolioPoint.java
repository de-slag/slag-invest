package de.slag.invest.one.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.money.MonetaryAmount;

public class IsPortfolioPoint implements IsPoint {

	private List<IsSecurityPosition> securityPositions = new ArrayList<>();
	private LocalDate pointDate;
	private MonetaryAmount pointAmount;

	public IsPortfolioPoint(List<IsSecurityPosition> securityPositions, LocalDate pointDate,
			MonetaryAmount pointAmount) {
		super();
		this.securityPositions.addAll(securityPositions);
		this.pointDate = pointDate;
		this.pointAmount = pointAmount;
	}

	@Override
	public LocalDate getPointDate() {
		return pointDate;
	}

	@Override
	public MonetaryAmount getPointAmount() {
		return pointAmount;
	}

	public List<IsSecurityPosition> getSecurityPositions() {
		return securityPositions;
	}

}
