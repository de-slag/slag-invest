package de.slag.invest.appservice.dataimport;

import java.util.Arrays;
import java.util.List;

public interface FetchFile {
	
	String ISIN = "ISIN";
	
	String SYMBOL = "SYMBOL";
	
	List<String> HEADER = Arrays.asList(ISIN,SYMBOL);

}
