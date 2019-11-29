package de.slag.invest.av.stock;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;

import de.slag.invest.av.AvException;


public class AvStockCallBuilder implements Builder<AvStockCall> {

	private static final int DEFAULT_TIME_OUT = 3000;

	private String apiKey;

	private String symbol;

	private Boolean outputSizeFull;

	private Integer readTimeOut;

	@Override
	public AvStockCall build() {
		if (StringUtils.isBlank(apiKey)) {
			throw new AvException("no apiKey configured");
		}
		if (StringUtils.isBlank(symbol)) {
			throw new AvException("no symbol configured");
		}
		return build0();
	}

	private AvStockCall build0() {
		final int currentReadTimeOut = readTimeOut != null ? readTimeOut : DEFAULT_TIME_OUT;
		final AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, currentReadTimeOut);
		final TimeSeries timeSeries = new TimeSeries(apiConnector);
		final OutputSize outputSize = BooleanUtils.isTrue(outputSizeFull) ? OutputSize.FULL : OutputSize.COMPACT;
		return new AvStockCallImpl(timeSeries, symbol, outputSize);
	}
	
	public AvStockCallBuilder apiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}
	
	public AvStockCallBuilder symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
	
	public AvStockCallBuilder outputSizeFull(boolean outputSizeFull) {
		this.outputSizeFull = outputSizeFull;
		return this;
	}
	
	public AvStockCallBuilder readTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
		return this;
	}

}
