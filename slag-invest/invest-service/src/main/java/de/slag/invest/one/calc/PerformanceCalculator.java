package de.slag.invest.one.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import de.slag.common.base.BaseException;
import de.slag.invest.one.model.InvTimePeriod;
import de.slag.invest.one.model.InvTimePeriodType;

public class PerformanceCalculator implements Calculator<BigDecimal> {

	private static final MathContext DEFAULT_MATH_CONTEXT = InvCalcConstants.DEFAULT_MATH_CONTEXT;

	private LocalDate forDate;

	private LocalDate sinceDate;

	private Map<LocalDate, BigDecimal> performanceValues = new HashMap<>();

	private int dateToleranceDays = 5;

	public static PerformanceCalculator of(LocalDate date, InvTimePeriodType timePeriodType,
			Map<LocalDate, BigDecimal> performanceValues, int dateToleranceDays) {
		
		final TimePeriodDeterminationCalculator timePeriodDeterminationCalculator = new TimePeriodDeterminationCalculator(
				timePeriodType, date);
		final InvTimePeriod period = timePeriodDeterminationCalculator.calculate();
		return new PerformanceCalculator(period.getEnd(), period.getBegin(), performanceValues,
				dateToleranceDays);
	}

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
		final BigDecimal divide = forPrice.divide(sincePrice, DEFAULT_MATH_CONTEXT);
		return divide.subtract(BigDecimal.ONE, DEFAULT_MATH_CONTEXT);
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
