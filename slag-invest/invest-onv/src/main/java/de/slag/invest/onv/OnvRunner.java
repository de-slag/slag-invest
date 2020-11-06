package de.slag.invest.onv;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.slag.common.util.DateUtils;
import de.slag.invest.onv.call.OnvCall;
import de.slag.invest.onv.call.OnvCallBuilder;
import de.slag.invest.onv.model.OnvStockData;
import de.slag.staging.StagingCsvLineBuilder;
import de.slag.staging.StagingModelUtils;
import de.slag.staging.StagingStockData;

public class OnvRunner implements Runnable {

	private String baseUrl;
	private List<String> notations = new ArrayList<>();
	private Map<String, String> notationWknIsinMap = new HashMap<>();
	private String outputFolder;

	OnvRunner(String baseUrl, List<String> notations, Map<String, String> notationWknIsinMap, String outputFolder) {
		this.baseUrl = baseUrl;
		this.notations.addAll(notations);
		this.notationWknIsinMap.putAll(notationWknIsinMap);
		this.outputFolder = outputFolder;
	}

	@Override
	public void run() {
		Date fetchTimestamp = new Date();
		List<List<String>> csvLines = new ArrayList<>();

		for (String notation : notations) {
			final OnvCall onvCall = new OnvCallBuilder().withBaseUrl(baseUrl).withNotationIn(notation).build();
			List<OnvStockData> call;
			try {
				call = onvCall.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			call.forEach(stockData -> {
				List<String> csvLine = new StagingCsvLineBuilder()
						.withFetchTimestamp(fetchTimestamp)
						.withStockData(stagingStockDataOf(stockData))
						.withWknIsin(notationWknIsinMap.get(notation))
						.withSourceInfo("onv:" + notation).build();
				csvLines.add(csvLine);
			});
		}
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
