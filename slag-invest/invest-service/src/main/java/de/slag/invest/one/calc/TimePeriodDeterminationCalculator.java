package de.slag.invest.one.calc;

import java.time.LocalDate;

import de.slag.common.base.BaseException;
import de.slag.common.util.DateUtils;
import de.slag.invest.one.model.IsTimePeriod;
import de.slag.invest.one.model.IsTimePeriodType;

public class TimePeriodDeterminationCalculator implements Calculator<IsTimePeriod> {

	private IsTimePeriodType type;
	
	private LocalDate date;

	public TimePeriodDeterminationCalculator(IsTimePeriodType type, LocalDate date) {
		super();
		this.type = type;
		this.date = date;
	}


	@Override
	public IsTimePeriod calculate() {
		switch (type) {
		case QUATER:
			final LocalDate firstDayOfQuater = DateUtils.firstDayOfQuater(date);
			final LocalDate lastDayOfQuater = DateUtils.lastDayOfQuater(date);
			return new IsTimePeriod(firstDayOfQuater, lastDayOfQuater);
		default:
			throw new BaseException("type not supported: " + type);
		}
	}

}
