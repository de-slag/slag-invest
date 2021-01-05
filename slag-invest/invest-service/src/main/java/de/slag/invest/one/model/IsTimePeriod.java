package de.slag.invest.one.model;

import java.time.LocalDate;

public class IsTimePeriod {
	
	private LocalDate begin;
	
	private LocalDate end;
	
	public IsTimePeriod(LocalDate begin, LocalDate end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

}
