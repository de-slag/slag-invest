package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.slag.invest.one.model.InvTimePeriod;
import de.slag.invest.one.model.InvTimePeriodType;

class TimePeriodDeterminationCalculatorTest {

	@Test
	void firstQuaterTest() {
		final TimePeriodDeterminationCalculator c = new TimePeriodDeterminationCalculator(InvTimePeriodType.QUATER,
				LocalDate.of(2010, 4, 1));
		final InvTimePeriod calculate = c.calculate();
		assertEquals(LocalDate.of(2010, 4, 1), calculate.getBegin());
		assertEquals(LocalDate.of(2010, 6, 30), calculate.getEnd());
	}

}
