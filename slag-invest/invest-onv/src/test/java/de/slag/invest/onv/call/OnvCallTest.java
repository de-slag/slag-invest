package de.slag.invest.onv.call;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.slag.invest.onv.model.OnvStockData;

class OnvCallTest {

	@Test
	void it() throws Exception {
		OnvCall call = new OnvCall(
				"https://www.onvista.de/onvista/boxes/historicalquote/export.csv?notationId=20735&dateStart=04.11.2019&interval=Y1");
		List<OnvStockData> onvStockData = call.call();
		onvStockData.size();
	}

}
