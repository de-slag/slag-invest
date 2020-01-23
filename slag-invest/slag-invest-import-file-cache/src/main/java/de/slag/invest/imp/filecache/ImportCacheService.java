package de.slag.invest.imp.filecache;

import java.util.Collection;

public interface ImportCacheService {

	Collection<ImportCacheStockValueDto> fetchData();
	
	void storeData(Collection<ImportCacheStockValueDto> data);

}
