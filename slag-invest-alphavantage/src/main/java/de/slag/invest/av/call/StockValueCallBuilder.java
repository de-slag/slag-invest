package de.slag.invest.av.call;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;

import de.slag.invest.av.AvException;

public class StockValueCallBuilder implements Builder<StockValueCall> {

	private static final int TIME_OUT = 3000;

	private String apiKey;
	
	private String symbol;
	
	private Boolean outputSizeFull;

	public StockValueCall build() {
		if (StringUtils.isBlank(apiKey)) {
			throw new AvException("apiKey configured");
		}
		if (StringUtils.isBlank(symbol)) {
			throw new AvException("symbol configured");
		}
		
		return build0();
	}

	private StockValueCall build0() {
		final AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, TIME_OUT);

		final StockValueCall stockValueCall = new StockValueCall();
		stockValueCall.setStockTimeSeries(new TimeSeries(apiConnector));
		stockValueCall.setSymbol(symbol);
		stockValueCall.setOutputSizeFull(outputSizeFull);

		return stockValueCall;
	}
	
	public StockValueCallBuilder outputSizeFull(boolean outputSizeFull) {
		this.outputSizeFull = outputSizeFull;
		return this;
	}
	
	public StockValueCallBuilder symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public StockValueCallBuilder apiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

}
