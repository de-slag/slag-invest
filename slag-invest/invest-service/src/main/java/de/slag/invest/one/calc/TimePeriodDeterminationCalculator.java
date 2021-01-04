package de.slag.invest.one.calc;

import java.time.LocalDate;

import de.slag.common.base.BaseException;
import de.slag.common.util.DateUtils;
import de.slag.invest.one.model.InvTimePeriod;
import de.slag.invest.one.model.InvTimePeriodType;

public class TimePeriodDeterminationCalculator implements Calculator<InvTimePeriod> {

	private InvTimePeriodType type;
	
	private LocalDate date;

	public TimePeriodDeterminationCalculator(InvTimePeriodType type, LocalDate date) {
		super();
		this.type = type;
		this.date = date;
	}


	@Override
	public InvTimePeriod calculate() {
		switch (type) {
		case QUATER:
			final LocalDate firstDayOfQuater = DateUtils.firstDayOfQuater(date);
			final LocalDate lastDayOfQuater = DateUtils.lastDayOfQuater(date);
			return new InvTimePeriod(firstDayOfQuater, lastDayOfQuater);
		default:
			throw new BaseException("type not supported: " + type);
		}
	}

}
