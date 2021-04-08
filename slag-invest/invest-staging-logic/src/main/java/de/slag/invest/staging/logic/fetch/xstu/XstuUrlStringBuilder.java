package de.slag.invest.staging.logic.fetch.xstu;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.DateUtils;

public class XstuUrlStringBuilder implements Builder<String> {

	private String baseUrl = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive?notationId=%s&timeFrom=%s&timeTo=%s&configId=%s";

	private String configId = "649e0402-3e80-426b-b89c-2fcc3f522c2a";

	private String notationId;

	private LocalDateTime timeFrom;

	private LocalDateTime timeTo;

	public XstuUrlStringBuilder withTimeFrom(LocalDateTime timeFrom) {
		this.timeFrom = timeFrom;
		return this;
	}

	public XstuUrlStringBuilder withTimeTo(LocalDateTime timeTo) {
		this.timeTo = timeTo;
		return this;
	}

	public XstuUrlStringBuilder withNotationId(String notationId) {
		this.notationId = notationId;
		return this;
	}
	
	public XstuUrlStringBuilder withConfigId(String configId) {
		this.configId = configId;
		return this;
	}
	
	public XstuUrlStringBuilder withBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}	

	@Override
	public String build() {
		String formattedTimeFrom = formatDateTime(timeFrom);
		String formattedTimeTo = formatDateTime(timeTo);

		return String.format(baseUrl, notationId, formattedTimeFrom, formattedTimeTo, configId);
	}

	private String formatDateTime(LocalDateTime localDateTime) {
		Date date = DateUtils.toDate(localDateTime);

		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
		String formatDay = sdfDay.format(date);
		String formatTime = sdftime.format(date);
		return formatDay + "T" + formatTime + ".000Z";
	}

}
