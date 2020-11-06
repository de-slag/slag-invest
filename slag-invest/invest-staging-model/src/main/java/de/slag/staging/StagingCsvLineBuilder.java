package de.slag.staging;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;

import de.slag.staging.model.SecurityCsv;

public class StagingCsvLineBuilder implements Builder<List<String>> {

	private StagingStockData stockData;

	private Date fetchTimestamp;

	private String wknIsin;
	
	private String sourceInfo;
	
	public StagingCsvLineBuilder withSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
		return this;
	}

	public StagingCsvLineBuilder withWknIsin(String wknIsin) {
		this.wknIsin = wknIsin;
		return this;
	}

	public StagingCsvLineBuilder withFetchTimestamp(Date fetchTimestamp) {
		this.fetchTimestamp = fetchTimestamp;
		return this;
	}

	public StagingCsvLineBuilder withStockData(StagingStockData stockData) {
		this.stockData = stockData;
		return this;
	}

	@Override
	public List<String> build() {
		Objects.requireNonNull(fetchTimestamp, "fetchTimestamp not setted");
		Objects.requireNonNull(wknIsin, "wknIsin not setted");
		Objects.requireNonNull(stockData, "stockData not setted");
		Objects.requireNonNull(sourceInfo, "sourceInfo not setted");

		List<String> result = new ArrayList<String>();

		result.add(new SimpleDateFormat(SecurityCsv.FETCH_TS_FORMAT).format(fetchTimestamp));
		result.add(wknIsin);
		result.add(new SimpleDateFormat(SecurityCsv.DATE_FORMAT).format(of(stockData.getDateTime())));
		result.add(Double.toString(stockData.getOpen()));
		result.add(Double.toString(stockData.getClose()));
		result.add(Double.toString(stockData.getHigh()));
		result.add(Double.toString(stockData.getLow()));
		result.add(Long.toString(stockData.getVolume()));
		result.add("(DEFAULT)");
		result.add(sourceInfo);
		return result;
	}

	private Date of(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		long epoch = localDateTime.atZone(zoneId).toEpochSecond();

		return new Date(epoch * 1000);
	}
	
	

}
