package de.slag.invest.one.calc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.slag.invest.one.model.IsTimePeriod;
import de.slag.invest.one.model.IsTimePeriodType;

public class MultiTimePeriodDeterminationCalculator implements Calculator<Collection<IsTimePeriod>> {

	private LocalDate begin;
	private LocalDate end;
	private IsTimePeriodType type;

	public static MultiTimePeriodDeterminationCalculator of(LocalDate begin, LocalDate end, IsTimePeriodType type) {
		return new MultiTimePeriodDeterminationCalculator(begin, end, type);
	}

	private MultiTimePeriodDeterminationCalculator(LocalDate begin, LocalDate end, IsTimePeriodType type) {
		super();
		this.begin = begin;
		this.end = end;
		this.type = type;
	}

	@Override
	public Collection<IsTimePeriod> calculate() {
		List<IsTimePeriod> result = new ArrayList<>();
		final TimePeriodDeterminationCalculator timePeriodDeterminationCalculator = new TimePeriodDeterminationCalculator(
				type, begin);
		result.add(timePeriodDeterminationCalculator.calculate());

		while (!isComplete(result)) {
			final LocalDate lastDate = lastDate(result);
			final LocalDate plusDays = lastDate.plusDays(1);
			final TimePeriodDeterminationCalculator calculator = new TimePeriodDeterminationCalculator(type, plusDays);
			final IsTimePeriod period = calculator.calculate();
			result.add(period);
		}
		return result;
	}

	private boolean isComplete(List<IsTimePeriod> result) {
		final LocalDate lastDate = lastDate(result);
		if (end.isEqual(lastDate)) {
			return true;
		}
		return end.isBefore(lastDate);
	}

	private LocalDate lastDate(List<IsTimePeriod> result) {
		return result.stream().map(period -> period.getEnd()).max((a, b) -> a.compareTo(b)).get();
	}

}
