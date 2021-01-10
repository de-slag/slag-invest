package de.slag.invest.one.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.money.MonetaryAmount;

import de.slag.common.base.BaseException;
import de.slag.common.base.pattern.BuildException;
import de.slag.common.util.CurrencyUtils;

public class IsAssetPortfolioPoint implements IsPoint {

	private IsPortfolioPoint portfolioPoint;

	private IsClearingAccountPoint clearingAccountPoint;

	private LocalDate pointDate;

	private MonetaryAmount pointAmount;

	public static IsAssetPortfolioPoint buildWith(IsPortfolioPoint portfolioPoint,
			IsClearingAccountPoint clearingAccountPoint) throws Exception {

		if (portfolioPoint == null) {
			throw new BuildException("portfolioPoint not setted");
		}
		
		if (clearingAccountPoint == null) {
			throw new BuildException("clearingAccountPoint not setted");
		}

		final LocalDate pointDate = Objects.requireNonNull(portfolioPoint.getPointDate());
		if (!pointDate.equals(clearingAccountPoint.getPointDate())) {
			throw new BuildException("point dates are not equal");
		}
		final MonetaryAmount pointAmount = CurrencyUtils.add(portfolioPoint.getPointAmount(),
				clearingAccountPoint.getPointAmount());
		return new IsAssetPortfolioPoint(portfolioPoint, clearingAccountPoint, pointDate, pointAmount);
	}

	private IsAssetPortfolioPoint(IsPortfolioPoint portfolioPoint, IsClearingAccountPoint clearingAccountPoint,
			LocalDate pointDate, MonetaryAmount pointAmount) {
		super();
		this.portfolioPoint = portfolioPoint;
		this.clearingAccountPoint = clearingAccountPoint;
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

	public IsPortfolioPoint getPortfolioPoint() {
		return portfolioPoint;
	}

	public IsClearingAccountPoint getClearingAccountPoint() {
		return clearingAccountPoint;
	}

}
