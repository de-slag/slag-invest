package de.slag.invest.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.invest.av.stock.AvStockCall;
import de.slag.invest.av.stock.AvStockResponse;

public class StockValueCallRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(StockValueCallRunner.class);

	private AvStockCall call;

	private AvStockResponse response;

	private Exception e;

	public StockValueCallRunner(AvStockCall call) {
		this.call = call;
	}

	@Override
	public void run() {
		LOG.info(String.format("call '%s'...", call));
		try {
			this.response = call.call();
		} catch (Exception e) {
			LOG.error("error calling StockValueCall", e);
			this.e = e;
			return;
		}
		LOG.info(String.format("call for symbol '%s' done. %s values recieved.", call, response.getStocks().size()));
	}

	public AvStockResponse getResponse() {
		if (e != null) {
			throw new BaseException("error while calling", e.getMessage());
		}
		return response;
	}

}
