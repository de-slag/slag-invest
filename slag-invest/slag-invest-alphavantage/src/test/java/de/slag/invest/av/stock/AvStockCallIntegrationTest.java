package de.slag.invest.av.stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slag.common.base.BaseException;
import de.slag.invest.av.AvProperties;
import de.slag.invest.av.stock.AvStock;
import de.slag.invest.av.stock.AvStockCall;
import de.slag.invest.av.stock.AvStockCallImpl;
import de.slag.invest.av.stock.AvStockCallBuilder;
import de.slag.invest.av.stock.AvStockResponse;

public class AvStockCallIntegrationTest {

	private static final Log LOG = LogFactory.getLog(AvStockCallIntegrationTest.class);

	private Properties properties;

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		String userHome = SystemUtils.USER_HOME;
		String pathname = userHome + "/slag.properties";
		File file = new File(pathname);
		if (!file.exists()) {
			throw new BaseException("properties file not found: " + pathname);
		}
		properties = new Properties();
		properties.load(new FileInputStream(file));
		properties.keySet().stream()
			.map(key -> (String) key)
			.forEach(key -> LOG.info(key + "=" + properties.getProperty(key)));
	}

	@Test
	public void test() throws Exception {
		final AvStockCallBuilder avStockCallBuilder = new AvStockCallBuilder();
		avStockCallBuilder.apiKey(properties.getProperty(AvProperties.API_KEY));
		avStockCallBuilder.symbol("^GDAXI");		
		final AvStockCall avStockCall = avStockCallBuilder.build();
		
		final AvStockResponse response = avStockCall.call();
		Collection<AvStock> stocks = response.getStocks();
		Assert.assertTrue(stocks.size() == 100);
	}

}
