package de.slag.invest.one.calc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.slag.invest.one.model.InvTimePeriod;
import de.slag.invest.one.model.InvTimePeriodType;

public class MultiTimePeriodDeterminationCalculator implements Calculator<Collection<InvTimePeriod>> {

	private LocalDate begin;
	private LocalDate end;
	private InvTimePeriodType type;

	public static MultiTimePeriodDeterminationCalculator of(LocalDate begin, LocalDate end, InvTimePeriodType type) {
		return new MultiTimePeriodDeterminationCalculator(begin, end, type);
	}

	private MultiTimePeriodDeterminationCalculator(LocalDate begin, LocalDate end, InvTimePeriodType type) {
		super();
		this.begin = begin;
		this.end = end;
		this.type = type;
	}

	@Override
	public Collection<InvTimePeriod> calculate() {
		List<InvTimePeriod> result = new ArrayList<>();
		final TimePeriodDeterminationCalculator timePeriodDeterminationCalculator = new TimePeriodDeterminationCalculator(
				type, begin);
		result.add(timePeriodDeterminationCalculator.calculate());

		while (!isComplete(result)) {
			final LocalDate lastDate = lastDate(result);
			final LocalDate plusDays = lastDate.plusDays(1);
			final TimePeriodDeterminationCalculator calculator = new TimePeriodDeterminationCalculator(type, plusDays);
			final InvTimePeriod period = calculator.calculate();
			result.add(period);
		}
		return result;
	}

	private boolean isComplete(List<InvTimePeriod> result) {
		final LocalDate lastDate = lastDate(result);
		if (end.isEqual(lastDate)) {
			return true;
		}
		return end.isBefore(lastDate);
	}

	private LocalDate lastDate(List<InvTimePeriod> result) {
		return result.stream().map(period -> period.getEnd()).max((a, b) -> a.compareTo(b)).get();
	}

}
