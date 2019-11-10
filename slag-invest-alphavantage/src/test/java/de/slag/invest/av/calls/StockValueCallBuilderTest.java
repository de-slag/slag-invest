package de.slag.invest.av.calls;

import org.junit.Before;
import org.junit.Test;

import de.slag.invest.av.AvException;
import de.slag.invest.av.call.StockValueCallBuilder;

public class StockValueCallBuilderTest {
	
	StockValueCallBuilder builder;
	
	@Before
	public void setUp() {
		builder = new StockValueCallBuilder();
	}
	
	@Test
	public void test() throws Exception {
		builder.build();
	}

	@Test(expected = AvException.class)
	public void testFailsNoConfig() {
		builder.build();
	}

}
