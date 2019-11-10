package de.slag.invest.av.call;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.timeseries.TimeSeriesResponse;

import de.slag.invest.av.AvCall;
import de.slag.invest.av.model.AvStockValue;
import de.slag.invest.av.response.AvStockValueResponse;

public class StockValueCall implements AvCall {

	private String symbol;

	private TimeSeries stockTimeSeries;

	private Boolean outputSizeFull;

	StockValueCall() {

	}

	public AvStockValueResponse call() throws Exception {

		final OutputSize outputSize = BooleanUtils.isTrue(outputSizeFull) ? OutputSize.FULL : OutputSize.COMPACT;
		final TimeSeriesResponse timeSeriesResponse = stockTimeSeries.daily(symbol, outputSize);

		final AvStockValueResponse response = new AvStockValueResponse();
		response.getValues().addAll(timeSeriesResponse.getStockData().stream().map(stock -> {
			final AvStockValue avStockValue = new AvStockValue();

			avStockValue.setSymbol(symbol);
			avStockValue.setDate(stock.getDateTime().toLocalDate());
			avStockValue.setOpen(BigDecimal.valueOf(stock.getOpen()));
			avStockValue.setClose(BigDecimal.valueOf(stock.getClose()));
			avStockValue.setHigh(BigDecimal.valueOf(stock.getHigh()));
			avStockValue.setLow(BigDecimal.valueOf(stock.getLow()));
			avStockValue.setVolume(Long.valueOf(stock.getVolume()));

			return avStockValue;
		}).collect(Collectors.toList()));

		return response;
	}

	void setStockTimeSeries(TimeSeries stockTimeSeries) {
		this.stockTimeSeries = stockTimeSeries;
	}

	void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setOutputSizeFull(Boolean outputSizeFull) {
		this.outputSizeFull = outputSizeFull;
	}

}
