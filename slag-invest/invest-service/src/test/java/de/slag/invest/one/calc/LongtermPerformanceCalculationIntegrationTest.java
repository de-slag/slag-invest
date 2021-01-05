package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slag.invest.one.model.IsTimePeriod;
import de.slag.invest.one.model.IsTimePeriodType;

class LongtermPerformanceCalculationIntegrationTest {

	private static final String SLAG_001_CSV = "slag-001.csv";
	private static final String DAX_2010_2014_CSV = "dax-2010-2014.csv";
	static CalculatorTestSupport calculatorTestSupport;

	@BeforeAll
	static void setUp() throws IOException {
		calculatorTestSupport = new CalculatorTestSupport();
		calculatorTestSupport.loadResourceFileToCache(DAX_2010_2014_CSV);
		calculatorTestSupport.loadResourceFileToCache(SLAG_001_CSV);
	}

	@Test
	void test() throws IOException {
		final Map<LocalDate, BigDecimal> valuesOf = calculatorTestSupport.valuesOf(DAX_2010_2014_CSV);
		LocalDate begin = LocalDate.of(2010, 1, 1);
		LocalDate end = LocalDate.of(2014, 12, 31);
		final MultiTimePeriodDeterminationCalculator multiTimePeriodDeterminationCalculator = MultiTimePeriodDeterminationCalculator
				.of(begin, end, IsTimePeriodType.QUATER);
		final Collection<IsTimePeriod> timePeriods = multiTimePeriodDeterminationCalculator.calculate();

		assertEquals(20, timePeriods.size());

		final List<IsTimePeriod> timePeriodList = new ArrayList<>(timePeriods);

		final IsTimePeriod invTimePeriod0 = timePeriodList.get(0);
		final PerformanceCalculator performanceCalculator0 = PerformanceCalculator.of(invTimePeriod0, valuesOf, 5);
		final BigDecimal performanceResult0 = performanceCalculator0.calculate();

		assertEquals(0.032920, performanceResult0.doubleValue());

		final IsTimePeriod invTimePeriod2 = timePeriodList.get(2);
		final PerformanceCalculator performanceCalculator2 = PerformanceCalculator.of(invTimePeriod2, valuesOf, 5);
		final BigDecimal performanceResult2 = performanceCalculator2.calculate();

		assertEquals(0.063439, performanceResult2.doubleValue());

		final IsTimePeriod invTimePeriod11 = timePeriodList.get(11);
		final PerformanceCalculator performanceCalculator11 = PerformanceCalculator.of(invTimePeriod11, valuesOf, 5);
		final BigDecimal performanceResult11 = performanceCalculator11.calculate();

		double result2012Q4 = 0.038989;
		assertEquals(result2012Q4, performanceResult11.doubleValue());
	}

	@Test
	void test2() throws IOException {
		final Map<LocalDate, BigDecimal> valuesOf = calculatorTestSupport.valuesOf(SLAG_001_CSV);
		LocalDate begin = LocalDate.of(2018, 10, 1);
		LocalDate end = LocalDate.of(2020, 9, 30);
		final MultiTimePeriodDeterminationCalculator multiTimePeriodDeterminationCalculator = MultiTimePeriodDeterminationCalculator
				.of(begin, end, IsTimePeriodType.QUATER);
		final Collection<IsTimePeriod> timePeriods = multiTimePeriodDeterminationCalculator.calculate();
		StringBuilder sb = new StringBuilder();
		timePeriods.forEach(period -> {
			final PerformanceCalculator performanceCalculator = PerformanceCalculator.of(period, valuesOf, 5);
			final BigDecimal performanceResult = performanceCalculator.calculate();
			sb.append("Quaterly perfomance (");
			sb.append(period.getEnd());
			sb.append("): ");
			sb.append(performanceResult.multiply(BigDecimal.valueOf(100)));
			sb.append("\n");
		});
		final Path createTempFile = Files.createTempFile("QUATERLY-PERFORMANCE-slag-001", ".txt");
		final FileOutputStream fileOutputStream = new FileOutputStream(createTempFile.toFile());
		fileOutputStream.write(sb.toString().getBytes());
		fileOutputStream.close();

	}

}
