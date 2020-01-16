package de.slag.invest.filerepo;

import java.util.ArrayList;
import java.util.HashMap;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PortfolioTransactionDataValidatorTest {

	private HashMap<String, String> dataMap;

	@Before
	public void setUp() {
		dataMap = new HashMap<>();
	}
	
	@Test
	public void testPrice() {
		dataMap.put("PRICE", "250.8");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validatePrice(dataMap);

		Assert.assertTrue(validator.isValid0());
	}
	
	@Test
	public void testPriceNotPositiveFail() {
		dataMap.put("PRICE", "0");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validatePrice(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("Column PRICE: value is not positive: 0.0"));
	}

	@Test
	public void testPriceNoValidDoubleFail() {
		dataMap.put("PRICE", "u");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validatePrice(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("Column: PRICE:'u' is not a valid double value"));
	}

	@Test
	public void testPriceNull() {
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validatePrice(dataMap);
	}

	@Test
	public void testAmount() {
		dataMap.put("AMOUNT", "250.17");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateAmount(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testAmountNegativeFail() {
		dataMap.put("AMOUNT", "-8");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateAmount(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("Column AMOUNT: value is not positive: -8.0"));
	}

	@Test
	public void testAmountNotValidDoubleFail() {
		dataMap.put("AMOUNT", "c");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateAmount(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("Column: AMOUNT:'c' is not a valid double value"));
	}

	@Test
	public void testAmountNull() {
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateAmount(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testAmountEmpty() {
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateAmount(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testIsinWknEmpty() {
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateIsinWkn(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testIsin() {
		dataMap.put("ISIN_WKN", "DE000BAY0017");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateIsinWkn(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testWkn() {
		dataMap.put("ISIN_WKN", "846900");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateIsinWkn(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testIsinWknFail() {
		dataMap.put("ISIN_WKN", "ABC-1234");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateIsinWkn(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("'ABC-1234' is not a valid ISIN or WKN"));
	}

	@Test
	public void testCount() {
		dataMap.put("COUNT", "5");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateCount(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testCountFail() {
		dataMap.put("COUNT", "A");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateCount(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator), is("Column: COUNT: 'A' is not an Integer"));
	}

	@Test
	public void testTimestampFail() {
		dataMap.put("TIMESTAMP", "xyz");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateTimestamp(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertThat(getMessage(validator),
				is("Column: TIMESTAMP: 'xyz' is not in pattern 'yyyy-MM-dd_hh-mm-ss'"));
	}

	@Test
	public void testTimestampEmptyFail() {
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateTimestamp(dataMap);

		Assert.assertFalse(validator.isValid0());
		Assert.assertTrue(validator.getValidateIssues().size() == 1);
		Assert.assertTrue("Column: TIMESTAMP is empty".equals(getMessage(validator)));
	}

	@Test
	public void testTimestamp() {
		dataMap.put("TIMESTAMP", "2010-01-01_17-59-59");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		validator.validateTimestamp(dataMap);

		Assert.assertTrue(validator.isValid0());
	}

	@Test
	public void testEmpty() {
		Assert.assertFalse(new PortfolioTransactionDataValidator().isValid(new HashMap<>()));
	}
	
	@Test
	public void testCrossNullAmountOnInFails() {
		dataMap.put("TYPE", "IN");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type 'IN' amount must not be null"));
	}
	
	@Test
	public void testCrossNullAmountOnOutFails() {
		dataMap.put("TYPE", "OUT");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type 'OUT' amount must not be null"));
	}
	
	@Test
	public void testCrossNullCountOnBuyFails() {
		dataMap.put("TYPE", "BUY");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type 'BUY' count must not be null"));
	}
	
	@Test
	public void testCrossNullCountOnSellFails() {
		dataMap.put("TYPE", "SELL");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type 'SELL' count must not be null"));
	}
	
	@Test
	public void testCrossNullAmountOrPriceOnBuyFails() {
		dataMap.put("TYPE", "BUY");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		dataMap.put("COUNT", "50");
		
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type BUY amount or price must not be null"));
	}
	
	@Test
	public void testCrossNullAmountOrPriceOnSellFails() {
		dataMap.put("TYPE", "SELL");
		dataMap.put("TIMESTAMP", "2010-03-01_08-10-00");
		dataMap.put("COUNT", "50");
		
		final PortfolioTransactionDataValidator validator = new PortfolioTransactionDataValidator();
		Assert.assertFalse(validator.isValid(dataMap));
		Assert.assertThat(getMessage(validator), is("on type SELL amount or price must not be null"));
	}

	private String getMessage(PortfolioTransactionDataValidator validator) {
		return new ArrayList<>(validator.getValidateIssues()).get(0);
	}

	private Matcher<String> is(String string) {
		return new BaseMatcher<String>() {

			@Override
			public boolean matches(Object item) {
				return string.equals(item);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText(string);

			}
		};
	}

}
