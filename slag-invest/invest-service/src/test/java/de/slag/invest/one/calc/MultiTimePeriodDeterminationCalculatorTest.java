package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.slag.invest.one.model.IsTimePeriod;
import de.slag.invest.one.model.IsTimePeriodType;

class MultiTimePeriodDeterminationCalculatorTest {

	@Test
	void tenYearsIntermediateTest() {
		final MultiTimePeriodDeterminationCalculator calculator = MultiTimePeriodDeterminationCalculator
				.of(LocalDate.of(2010, 1, 6), LocalDate.of(2019, 11, 11), IsTimePeriodType.QUATER);
		final Collection<IsTimePeriod> result = calculator.calculate();
		assertEquals(40, result.size());
		List<IsTimePeriod> resultList = new ArrayList<>(result);
		assertEquals(LocalDate.of(2010, 1, 1), resultList.get(0).getBegin());
		assertEquals(LocalDate.of(2010, 3, 31), resultList.get(0).getEnd());
		assertEquals(LocalDate.of(2010, 4, 1), resultList.get(1).getBegin());
		assertEquals(LocalDate.of(2010, 6, 30), resultList.get(1).getEnd());
		assertEquals(LocalDate.of(2010, 7, 1), resultList.get(2).getBegin());
		assertEquals(LocalDate.of(2010, 9, 30), resultList.get(2).getEnd());
		
		assertEquals(LocalDate.of(2013, 4, 1), resultList.get(13).getBegin());
		assertEquals(LocalDate.of(2013, 6, 30), resultList.get(13).getEnd());
		
		assertEquals(LocalDate.of(2019, 10, 1), resultList.get(39).getBegin());
		assertEquals(LocalDate.of(2019, 12, 31), resultList.get(39).getEnd());
	}
	
	@Test
	void firstThreeQuatersIntermediateTest() {
		final MultiTimePeriodDeterminationCalculator calculator = MultiTimePeriodDeterminationCalculator
				.of(LocalDate.of(2010, 2, 12), LocalDate.of(2010, 8, 28), IsTimePeriodType.QUATER);
		final Collection<IsTimePeriod> result = calculator.calculate();
		assertEquals(3, result.size());
		List<IsTimePeriod> resultList = new ArrayList<>(result);
		assertEquals(LocalDate.of(2010, 1, 1), resultList.get(0).getBegin());
		assertEquals(LocalDate.of(2010, 3, 31), resultList.get(0).getEnd());
		assertEquals(LocalDate.of(2010, 4, 1), resultList.get(1).getBegin());
		assertEquals(LocalDate.of(2010, 6, 30), resultList.get(1).getEnd());
		assertEquals(LocalDate.of(2010, 7, 1), resultList.get(2).getBegin());
		assertEquals(LocalDate.of(2010, 9, 30), resultList.get(2).getEnd());
	}

	@Test
	void firstTwoQuatersTest() {
		final MultiTimePeriodDeterminationCalculator calculator = MultiTimePeriodDeterminationCalculator
				.of(LocalDate.of(2010, 1, 1), LocalDate.of(2010, 6, 30), IsTimePeriodType.QUATER);
		final Collection<IsTimePeriod> result = calculator.calculate();
		assertEquals(2, result.size());
		List<IsTimePeriod> resultList = new ArrayList<>(result);
		assertEquals(LocalDate.of(2010, 1, 1), resultList.get(0).getBegin());
		assertEquals(LocalDate.of(2010, 3, 31), resultList.get(0).getEnd());
		assertEquals(LocalDate.of(2010, 4, 1), resultList.get(1).getBegin());
		assertEquals(LocalDate.of(2010, 6, 30), resultList.get(1).getEnd());

	}

	@Test
	void firstQuaterIntermediateTest() {
		final Collection<IsTimePeriod> result = MultiTimePeriodDeterminationCalculator
				.of(LocalDate.of(2010, 1, 25), LocalDate.of(2010, 3, 2), IsTimePeriodType.QUATER)
				.calculate();
		assertEquals(1, result.size());
		List<IsTimePeriod> resultList = new ArrayList<>(result);
		assertEquals(LocalDate.of(2010, 1, 1), resultList.get(0).getBegin());
		assertEquals(LocalDate.of(2010, 3, 31), resultList.get(0).getEnd());
	}

	@Test
	void firstQuaterTest() {
		final Collection<IsTimePeriod> result = MultiTimePeriodDeterminationCalculator
				.of(LocalDate.of(2010, 1, 1), LocalDate.of(2010, 3, 31), IsTimePeriodType.QUATER)
				.calculate();
		assertEquals(1, result.size());
		List<IsTimePeriod> resultList = new ArrayList<>(result);
		assertEquals(LocalDate.of(2010, 1, 1), resultList.get(0).getBegin());
		assertEquals(LocalDate.of(2010, 3, 31), resultList.get(0).getEnd());
	}

}
