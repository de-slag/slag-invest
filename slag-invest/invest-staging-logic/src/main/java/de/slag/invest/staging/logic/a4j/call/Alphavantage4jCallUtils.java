package de.slag.invest.staging.logic.a4j.call;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.patriques.output.timeseries.data.StockData;

import de.slag.invest.a4j.call.Alphavantage4jCall;

public class Alphavantage4jCallUtils {

	public static Collection<Map<String, String>> call(Alphavantage4jCall call) {
		List<StockData> stockDatas;
		try {
			stockDatas = call.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stockDatas.stream().map(d -> {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			hashMap.put(AlphavantageStockDataModel.DATE,
					new SimpleDateFormat(AlphavantageStockDataModel.DEFAULT_DATE_FORMAT)
							.format(toDate(d.getDateTime())));
			hashMap.put(AlphavantageStockDataModel.CLOSE, Double.toString(d.getClose()));

			return hashMap;
		}).collect(Collectors.toList());
	}

	public static Date toDate(final LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}
