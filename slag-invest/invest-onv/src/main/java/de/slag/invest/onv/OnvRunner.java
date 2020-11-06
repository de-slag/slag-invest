package de.slag.invest.onv;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.util.DateUtils;
import de.slag.invest.onv.call.OnvCall;
import de.slag.invest.onv.call.OnvCallBuilder;
import de.slag.invest.onv.model.OnvStockData;
import de.slag.staging.StagingCsvLineBuilder;
import de.slag.staging.StagingModelUtils;
import de.slag.staging.StagingStockData;

public class OnvRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(OnvRunner.class);

	private static final String DATE_INFO_FORMAT = "dd.MM.yyyy";

	private String baseUrl;
	private List<String> notations = new ArrayList<>();
	private Map<String, String> notationWknIsinMap = new HashMap<>();
	private String outputFolder;

	OnvRunner(String baseUrl, List<String> notations, Map<String, String> notationWknIsinMap, String outputFolder) {
		this.baseUrl = baseUrl;
		this.notations.addAll(notations);
		this.notationWknIsinMap.putAll(notationWknIsinMap);
		this.outputFolder = outputFolder;
		LOG.info(this);
	}

	@Override
	public String toString() {
		return "OnvRunner [baseUrl=" + baseUrl + ", notations=" + notations + ", notationWknIsinMap="
				+ notationWknIsinMap + ", outputFolder=" + outputFolder + "]";
	}

	@Override
	public void run() {
		final Date fetchTimestamp = new Date();
		final List<List<String>> csvLines = new ArrayList<>();
		final String dateInfo = new SimpleDateFormat(DATE_INFO_FORMAT)
				.format(DateUtils.toDate(LocalDate.now().minus(1, ChronoUnit.YEARS)));

		for (String notation : notations) {
			final OnvCall onvCall = new OnvCallBuilder().withBaseUrl(baseUrl).withNotationIn(notation)
					.withDateInfo(dateInfo).build();
			final List<OnvStockData> call;
			try {
				call = onvCall.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			call.forEach(stockData -> {
				final List<String> csvLine = new StagingCsvLineBuilder().withFetchTimestamp(fetchTimestamp)
						.withStockData(stagingStockDataOf(stockData)).withWknIsin(notationWknIsinMap.get(notation))
						.withSourceInfo("onv:" + notation).build();
				csvLines.add(csvLine);
			});
		}
		LOG.info(String.format("%s dataset fetched", csvLines.size()));
		StagingModelUtils.writeOut(csvLines, outputFolder);

	}

	private StagingStockData stagingStockDataOf(OnvStockData stockData) {
		final LocalDateTime dateTime = DateUtils.toLocalDateTime(DateUtils.toDate(stockData.getDatum()));
		return new StagingStockData() {

			@Override
			public long getVolume() {
				return stockData.getVolume();
			}

			@Override
			public double getOpen() {
				return stockData.getEroeffnung();
			}

			@Override
			public double getLow() {
				return stockData.getTief();
			}

			@Override
			public double getHigh() {
				return stockData.getHoch();
			}

			@Override
			public LocalDateTime getDateTime() {
				return dateTime;
			}

			@Override
			public double getClose() {
				return stockData.getSchluss();
			}
		};
	}

}
