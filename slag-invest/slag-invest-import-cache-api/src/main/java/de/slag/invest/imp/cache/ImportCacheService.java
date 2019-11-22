package de.slag.invest.imp.cache;

import java.util.Collection;

public interface ImportCacheService {

	Collection<ImportCacheStockValueDto> fetchData();
	
	void storeData(Collection<ImportCacheStockValueDto> data);

}
