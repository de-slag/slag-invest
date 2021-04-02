package de.slag.invest.staging.logic.fetch.ov;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.DateUtils;

public class OvUrlStringBuilder implements Builder<String> {

	private String formatUrl = "https://www.onvista.de/onvista/boxes/historicalquote/export.csv?notationId=%s&dateStart=%s&interval=%s";

	private ChronoUnit chronoUnit = ChronoUnit.YEARS;

	private LocalDate fromDate = LocalDate.now();

	private String notationId;

	public OvUrlStringBuilder withFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public OvUrlStringBuilder withNotationId(String notationId) {
		this.notationId = notationId;
		return this;
	}

	@Override
	public String build() {
		String dateStart = getDateString();
		String interval = getInterval();
		return String.format(formatUrl, notationId, dateStart, interval);
	}

	private String getInterval() {
		switch (chronoUnit) {
		case MONTHS:
			return "M1";
		case YEARS:
			return "Y1";
		default:
			throw new UnsupportedOperationException(chronoUnit.toString());
		}
	}

	private String getDateString() {
		return new SimpleDateFormat("dd.MM.yyyy").format(getDate());
	}

	private Date getDate() {
		return DateUtils.toDate(getLocalDate());
	}

	private LocalDate getLocalDate() {
		switch (chronoUnit) {
		case MONTHS:
			return fromDate.minus(Period.ofMonths(1));

		case YEARS:
			return fromDate.minus(Period.ofYears(1));
		default:
			throw new UnsupportedOperationException(chronoUnit.toString());
		}
	}

}
