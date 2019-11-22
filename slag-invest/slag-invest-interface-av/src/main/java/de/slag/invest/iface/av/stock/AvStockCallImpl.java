package de.slag.invest.iface.av.stock;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.timeseries.TimeSeriesResponse;
import org.patriques.output.timeseries.data.StockData;

import de.slag.common.base.BaseException;
import de.slag.common.utils.DateUtils;

class AvStockCallImpl implements AvStockCall {

	private static final Log LOG = LogFactory.getLog(AvStockCallImpl.class);

	private final TimeSeries timeSeries;
	private final String symbol;
	private final OutputSize outputSize;

	AvStockCallImpl(TimeSeries timeSeries, String symbol, OutputSize outputSize) {
		this.timeSeries = timeSeries;
		this.symbol = symbol;
		this.outputSize = outputSize;
	}

	@Override
	public AvStockResponse call() throws Exception {
		LOG.info("call av..." + symbol);
		final TimeSeriesResponse timeSeriesResponse = timeSeries.daily(symbol, outputSize);
		LOG.info("call av...done.");
		final MetaData metaData = metadataOf(timeSeriesResponse.getMetaData());

		List<StockData> stockData = timeSeriesResponse.getStockData();
		LOG.info(stockData.size() + " datasets recieved.");
		List<AvStock> stocks = stockData.stream().map(sd -> avStockOf(sd, metaData)).collect(Collectors.toList());

		return new AvStockResponse() {

			@Override
			public Collection<AvStock> getStocks() {
				return stocks;
			}
		};
	}

	private AvStock avStockOf(StockData sd, MetaData metaData) {
		final LocalDateTime dateTime = sd.getDateTime();
		final BigDecimal close = BigDecimal.valueOf(sd.getClose());
		final BigDecimal open = BigDecimal.valueOf(sd.getOpen());
		final BigDecimal high = BigDecimal.valueOf(sd.getHigh());
		final BigDecimal low = BigDecimal.valueOf(sd.getLow());
		final long volume = sd.getVolume();

		return new AvStock() {

			@Override
			public long getVolume() {
				return volume;
			}

			@Override
			public String getSymbol() {
				return metaData.getSymbol();
			}

			@Override
			public BigDecimal getOpen() {
				return open;
			}

			@Override
			public BigDecimal getLow() {
				return low;
			}

			@Override
			public BigDecimal getHigh() {

				return high;
			}

			@Override
			public BigDecimal getClose() {
				return close;
			}

			@Override
			public LocalDate getDate() {
				return dateTime.toLocalDate();
			}
		};
	}

	private MetaData metadataOf(Map<String, String> metaData) {
		String dateString = metaData.get("3. Last Refreshed");
		String sym = metaData.get("2. Symbol");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse;
		try {
			parse = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
		LocalDate localDate = DateUtils.toLocalDate(parse);

		return new MetaData() {

			@Override
			public String getSymbol() {
				return sym;
			}

			@Override
			public LocalDate getLastRefreshed() {
				return localDate;
			}
		};
	}

	private interface MetaData {
		String getSymbol();

		LocalDate getLastRefreshed();
	}

	@Override
	public String toString() {
		return "AvStockCallImpl [symbol=" + symbol + "]";
	}

}
