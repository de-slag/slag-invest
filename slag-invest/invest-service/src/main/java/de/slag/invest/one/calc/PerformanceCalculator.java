package de.slag.invest.one.calc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import de.slag.common.base.BaseException;

public class PerformanceCalculator implements Calculator {

	private LocalDate forDate;

	private LocalDate sinceDate;

	private Map<LocalDate, BigDecimal> performanceValues = new HashMap<>();

	private int dateToleranceDays = 5;

	PerformanceCalculator(LocalDate forDate, LocalDate sinceDate, Map<LocalDate, BigDecimal> performanceValues,
			int dateToleranceDays) {
		super();
		this.forDate = forDate;
		this.sinceDate = sinceDate;
		this.performanceValues = performanceValues;
		this.dateToleranceDays = dateToleranceDays;
	}

	public BigDecimal calculate() {
		final BigDecimal forPrice = findForPrice();
		final BigDecimal sincePrice = findSincePrice();
		final BigDecimal divide = forPrice.divide(sincePrice, InvCalcConstants.DEFAULT_MATH_CONTEXT);
		return divide.subtract(BigDecimal.ONE);
	}

	private BigDecimal findForPrice() {
		return findPriceOn(forDate);
	}

	private BigDecimal findSincePrice() {
		return findPriceOn(sinceDate);
	}

	private BigDecimal findPriceOn(final LocalDate d) {
		LocalDate date = d;
		for (int i = 0; i < dateToleranceDays; i++) {
			if (performanceValues.containsKey(date)) {
				return performanceValues.get(date);
			}
			date = d.minusDays(i);
		}
		throw new BaseException("day not found: " + d);
	}
}
