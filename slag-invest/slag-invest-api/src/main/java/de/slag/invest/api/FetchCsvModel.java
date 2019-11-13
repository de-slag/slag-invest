package de.slag.invest.api;

import java.util.Arrays;
import java.util.List;

public interface FetchCsvModel {
	
	public static final String FETCHED = "FETCHED";
	public static final String VOLUME = "VOLUME";
	public static final String CLOSE = "CLOSE";
	public static final String LOW = "LOW";
	public static final String HIGH = "HIGH";
	public static final String OPEN = "OPEN";
	public static final String DATE = "DATE";
	public static final String ISIN = "ISIN";
	public static final List<String> HEADER = Arrays.asList(ISIN, DATE, OPEN, HIGH, LOW, CLOSE, VOLUME, FETCHED);

}
