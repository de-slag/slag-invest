package de.slag.invest.iface.av.api;

import java.util.Collection;

public interface AvDataFetchService {
	
	Collection<AvStockValueDto> fetchData();

}
