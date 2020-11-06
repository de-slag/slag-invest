package de.slag.invest.a4j.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.Daily;
import org.patriques.output.timeseries.data.StockData;

public class Alphavantage4jCall implements Callable<List<StockData>> {

	private String apiKey;

	private String symbol;

	private boolean verbose = false;

	Alphavantage4jCall(String apiKey, String symbol) {
		this.apiKey = apiKey;
		this.symbol = symbol;
	}

	@Override
	public List<StockData> call() throws Exception {
//		String apiKey = "50M3AP1K3Y";
		int timeout = 3000;
		AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
		TimeSeries stockTimeSeries = new TimeSeries(apiConnector);

		List<StockData> resultList = new ArrayList<>();

		try {
//			"MSFT"
			Daily response = stockTimeSeries.daily(symbol, OutputSize.COMPACT);
			Map<String, String> metaData = response.getMetaData();
			System.out.println("Information: " + metaData.get("1. Information"));
			System.out.println("Stock: " + metaData.get("2. Symbol"));

			List<StockData> stockData = response.getStockData();

			if (verbose) {
				stockData.forEach(stock -> {
					System.out.println("date:   " + stock.getDateTime());
					System.out.println("open:   " + stock.getOpen());
					System.out.println("high:   " + stock.getHigh());
					System.out.println("low:    " + stock.getLow());
					System.out.println("close:  " + stock.getClose());
					System.out.println("volume: " + stock.getVolume());
				});
			}
			resultList.addAll(stockData);
		} catch (AlphaVantageException e) {
			throw new RuntimeException(e);
		}
		return resultList;
	}
}
