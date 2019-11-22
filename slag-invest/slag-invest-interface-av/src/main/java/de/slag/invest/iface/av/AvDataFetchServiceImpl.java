package de.slag.invest.iface.av;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.iface.av.api.AvDataFetchService;
import de.slag.invest.iface.av.api.AvStockValueDto;

@Service
public class AvDataFetchServiceImpl implements AvDataFetchService {
	
	@Resource
	private DataImportFetchService dataImportFetchService;

	@Override
	public Collection<AvStockValueDto> fetchData() {
		List<AvStockValueDto> fetchData = dataImportFetchService.fetchData();
		return fetchData;
	}

}
