package de.slag.invest.iface.av;

import java.util.Arrays;
import java.util.List;

public interface FetchFile {
	
	String ISIN = "ISIN";
	
	String SYMBOL = "SYMBOL";
	
	List<String> HEADER = Arrays.asList(ISIN,SYMBOL);

}
