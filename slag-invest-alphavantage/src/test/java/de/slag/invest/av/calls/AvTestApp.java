package de.slag.invest.av.calls;

import java.util.Collection;
import java.util.Objects;

import de.slag.common.base.BaseException;
import de.slag.invest.av.AvProperties;
import de.slag.invest.av.call.StockValueCall;
import de.slag.invest.av.call.StockValueCallBuilder;
import de.slag.invest.av.model.AvStockValue;
import de.slag.invest.av.response.AvStockValueResponse;

public class AvTestApp {

	public static void main(String[] args) throws Exception {

		AvTestAdmSupport testSupport = new AvTestAdmSupport();

		String apiKey = testSupport.getProperty(AvProperties.API_KEY)
				.orElseThrow(() -> new BaseException(AvProperties.API_KEY + " not configured"));
		
		Objects.requireNonNull(apiKey, "apiKey");

		StockValueCall stockValueCall = new StockValueCallBuilder().apiKey(apiKey).symbol("^GDAXI").build();
		AvStockValueResponse call = stockValueCall.call();
		Collection<AvStockValue> values = call.getValues();
		values.forEach(System.out::println);

	}

}
