package de.slag.invest.appservice.dataimport;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.invest.dtomodel.StockValueDto;
import de.slag.invest.dtoservice.StockValueDtoService;
import de.slag.invest.model.StockValue;
import de.slag.invest.service.StockValueService;

@Service
public class DataImportServiceImpl implements DataImportSerivce {

	@Resource
	private StockValueService stockValueService;

	@Resource
	private StockValueDtoService stockValueDtoService;

	@Resource
	private AdmCache admCache;
	
	@Resource
	private DataImportFetchService dataImportFetchService;	

	public void importData() {

		final List<StockValueDto> allStockValueDtos = dataImportFetchService.fetchData();

		final List<StockValue> allStockValues = allStockValueDtos.stream()
				.map(dto -> stockValueDtoService.stockValueOf(dto)).collect(Collectors.toList());
		
		allStockValues.forEach(sv -> stockValueService.save(sv));

	}

	

}
