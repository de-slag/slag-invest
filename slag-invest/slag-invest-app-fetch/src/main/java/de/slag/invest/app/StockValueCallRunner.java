package de.slag.invest.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.invest.av.call.StockValueCall;
import de.slag.invest.av.response.AvStockValueResponse;

public class StockValueCallRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(StockValueCallRunner.class);

	private StockValueCall call;

	private AvStockValueResponse response;

	private Exception e;

	public StockValueCallRunner(StockValueCall call) {
		this.call = call;
	}

	@Override
	public void run() {
		final String symbol = call.getSymbol();
		LOG.info(String.format("call for symbol '%s'...", symbol));
		try {
			this.response = call.call();
		} catch (Exception e) {
			LOG.error("error calling StockValueCall", e);
			this.e = e;
			return;
		}
		LOG.info(String.format("call for symbol '%s' done. %s values recieved.", symbol, response.getValues().size()));
	}

	public AvStockValueResponse getResponse() {
		if (e != null) {
			throw new BaseException("error while calling", e.getMessage());
		}
		return response;
	}

	public String getSymbol() {
		return call.getSymbol();
	}

}
