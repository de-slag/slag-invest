package de.slag.invest.filerepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.validation.Validator;

public class PortfolioTransactionDataValidator implements Validator<Collection<Map<String, String>>> {

	private static final String COL_PRICE = "PRICE";
	private static final String MSG_NOT_A_VALID_DOUBLE_VALUE = "Column: %s:'%s' is not a valid double value";
	private static final String COL_AMOUNT = "AMOUNT";
	private static final String COL_ISIN_WKN = "ISIN_WKN";
	private static final String COL_COUNT = "COUNT";
	private static final String COL_TYPE = "TYPE";
	private static final String COL_TIMESTAMP = "TIMESTAMP";

	private static final Collection<String> VALID_TYPE_STRINGS = Arrays.asList("BUY", "SELL", "IN", "OUT");
	private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_hh-mm-ss";

	private static final String MSG_NOT_AN_INTEGER = "Column: %s: '%s' is not an Integer";
	private static final String MSG_NOT_TIMESTAMP_PATTERN = "Column: %s: '%s' is not in pattern 'yyyy-MM-dd_hh-mm-ss'";
	private static final String MSG_EMPTY = "Column: %s is empty";
	private static final String MSG_NOT_A_VALID_ISIN_WKN = "'%s' is not a valid ISIN or WKN";

	private static final Log LOG = LogFactory.getLog(PortfolioTransactionDataValidator.class);

	private final Collection<String> validateIssues = new ArrayList<>();

	boolean isValid(Map<String, String> dataMap) {
		validateAllFields(dataMap);
		validateCrossFields(dataMap);
		return isValid0();
	}

	private void validateCrossFields(Map<String, String> dataMap) {
		final String typeValue = dataMap.get(COL_TYPE);
		if (Arrays.asList("IN", "OUT").contains(typeValue)) {
			if (isEmpty(dataMap.get(COL_AMOUNT))) {
				validateIssues.add(String.format("on type '%s' amount must not be null", typeValue));
				return;
			}
		}
		if (Arrays.asList("BUY", "SELL").contains(typeValue)) {
			if (isEmpty(dataMap.get(COL_COUNT))) {
				validateIssues.add(String.format("on type '%s' count must not be null", typeValue));
				return;
			}
			if (isEmpty(dataMap.get(COL_AMOUNT)) && isEmpty(dataMap.get(COL_PRICE))) {
				addIssue("on type %s amount or price must not be null", typeValue);
			}

		}
	}

	private void validateAllFields(Map<String, String> dataMap) {
		validateTimestamp(dataMap);
		validateType(dataMap);
		validateCount(dataMap);
		validateIsinWkn(dataMap);
	}

	void validateIsinWkn(Map<String, String> dataMap) {
		final String value = dataMap.get(COL_ISIN_WKN);
		if (StringUtils.isEmpty(value)) {
			return;
		}
		final int valueLenght = value.length();
		if (valueLenght != 12 && valueLenght != 6) {
			validateIssues.add(String.format(MSG_NOT_A_VALID_ISIN_WKN, value));
			return;
		}
	}

	void validateCount(Map<String, String> dataMap) {
		final String string = dataMap.get(COL_COUNT);
		if (StringUtils.isEmpty(string)) {
			return;
		}
		try {
			Integer.valueOf(string);
		} catch (NumberFormatException e) {
			LOG.trace(e);
			validateIssues.add(String.format(MSG_NOT_AN_INTEGER, COL_COUNT, string));
		}
	}

	private void validateType(Map<String, String> dataMap) {
		final String string = dataMap.get(COL_TYPE);
		if (StringUtils.isEmpty(string)) {
			validateIssues.add(COL_TYPE + " is empty");
		}
		if (!VALID_TYPE_STRINGS.contains(string)) {
			validateIssues.add(COL_TYPE + " not in " + VALID_TYPE_STRINGS);
		}
	}

	void validateTimestamp(Map<String, String> dataMap) {
		final String timestampString = dataMap.get(COL_TIMESTAMP);
		if (StringUtils.isEmpty(timestampString)) {
			validateIssues.add(String.format(MSG_EMPTY, COL_TIMESTAMP));
			return;
		}
		try {
			new SimpleDateFormat(TIMESTAMP_PATTERN).parse(timestampString);
		} catch (ParseException e) {
			validateIssues.add(String.format(MSG_NOT_TIMESTAMP_PATTERN, COL_TIMESTAMP, timestampString));
			LOG.trace(e);
		}
	}

	@Override
	public boolean isValid(Collection<Map<String, String>> t) {
		t.forEach(e -> validateAllFields(e));
		return isValid0();
	}

	boolean isValid0() {
		if (validateIssues.isEmpty()) {
			return true;
		}
		LOG.info(validateIssues);
		return false;
	}

	public Collection<String> getValidateIssues() {
		return validateIssues;
	}

	void validateAmount(HashMap<String, String> dataMap) {
		final String valueString = dataMap.get(COL_AMOUNT);
		if (StringUtils.isEmpty(valueString)) {
			return;
		}
		double value;
		try {
			value = Double.valueOf(valueString);
		} catch (NumberFormatException e) {
			LOG.trace(e.getMessage(), e);
			validateIssues.add(String.format(MSG_NOT_A_VALID_DOUBLE_VALUE, COL_AMOUNT, valueString));
			return;
		}
		if (0 >= value) {
			validateIssues.add(String.format("Column %s: value is not positive: %s", COL_AMOUNT, value));
		}

	}

	public void validatePrice(HashMap<String, String> dataMap) {
		final String valueString = dataMap.get(COL_PRICE);
		if (StringUtils.isEmpty(valueString)) {
			return;
		}
		double value;
		try {
			value = Double.valueOf(valueString);
		} catch (NumberFormatException e) {
			LOG.trace(e.getMessage(), e);
			validateIssues.add(String.format(MSG_NOT_A_VALID_DOUBLE_VALUE, COL_PRICE, valueString));
			return;
		}
		if (0 >= value) {
			validateIssues.add(String.format("Column %s: value is not positive: %s", COL_PRICE, value));
		}

	}

	private boolean isEmpty(Object o) {
		if (o instanceof CharSequence) {
			return StringUtils.isEmpty((CharSequence) o);
		}
		return o == null;
	}

	private void addIssue(String format, Object... args) {
		validateIssues.add(String.format(format, args));
	}

}
