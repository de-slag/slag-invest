package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slag.invest.one.model.InvTimePeriod;
import de.slag.invest.one.model.InvTimePeriodType;

class PerformanceCalculatorTest {

	static CalculatorTestSupport calculatorTestSupport;

	@BeforeAll
	static void setUp() throws IOException {
		calculatorTestSupport = new CalculatorTestSupport();
		calculatorTestSupport.loadResourceFileToCache("dax-2010-2014.csv");
	}

	@Test
	void thirdQuarterWithPeriodBuilderTest() throws IOException {
		Map<LocalDate, BigDecimal> performanceValues = calculatorTestSupport.valuesOf("dax-2010-2014.csv");
		int dateToleranceDays = 5;

		final InvTimePeriod period = new InvTimePeriod(LocalDate.of(2010, 7, 1), LocalDate.of(2010, 9, 30));
		final PerformanceCalculator performanceCalculator = PerformanceCalculator.of(period, performanceValues,
				dateToleranceDays);

		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(0.063439, result.doubleValue());
	}

	@Test
	void secondQuarterWithBuilderTest() throws IOException {
		Map<LocalDate, BigDecimal> performanceValues = calculatorTestSupport.valuesOf("dax-2010-2014.csv");
		int dateToleranceDays = 5;

		final PerformanceCalculator performanceCalculator = PerformanceCalculator.of(LocalDate.of(2010, 4, 1),
				InvTimePeriodType.QUATER, performanceValues, dateToleranceDays);

		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(-0.0433065, result.doubleValue());
	}

	@Test
	void secondQuarterTest() throws IOException {
		LocalDate forDate = LocalDate.of(2010, 6, 30);
		LocalDate sinceDate = LocalDate.of(2010, 4, 1);
		Map<LocalDate, BigDecimal> performanceValues = calculatorTestSupport.valuesOf("dax-2010-2014.csv");
		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(-0.0433065, result.doubleValue());
	}

	@Test
	void firstQuarterTest() throws IOException {
		LocalDate forDate = LocalDate.of(2010, 3, 31);
		LocalDate sinceDate = LocalDate.of(2010, 1, 1);
		Map<LocalDate, BigDecimal> performanceValues = calculatorTestSupport.valuesOf("dax-2010-2014.csv");
		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(0.032920, result.doubleValue());
	}

	@Test
	void firstMonthTest() throws IOException {
		LocalDate forDate = LocalDate.of(2010, 1, 31);
		LocalDate sinceDate = LocalDate.of(2010, 1, 1);
		Map<LocalDate, BigDecimal> performanceValues = calculatorTestSupport.valuesOf("dax-2010-2014.csv");
		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(-0.0585219, result.doubleValue());
	}

	@Test
	void simpleTest() {
		LocalDate forDate = LocalDate.of(2010, 1, 31);
		LocalDate sinceDate = LocalDate.of(2010, 1, 1);

		Map<LocalDate, BigDecimal> performanceValues = new HashMap<>();
		performanceValues.put(LocalDate.of(2009, 12, 30), BigDecimal.valueOf(5957.430));
		performanceValues.put(LocalDate.of(2010, 01, 29), BigDecimal.valueOf(5608.79));

		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal result = performanceCalculator.calculate();
		assertEquals(-0.0585219, result.doubleValue());
	}

}
