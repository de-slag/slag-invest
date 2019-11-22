package de.slag.invest.appservice.dataimport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.common.reflect.engine.SimpleReflectionEngine;
import de.slag.invest.dtomodel.StockValueDto;
import de.slag.invest.dtoservice.StockValueDtoService;
import de.slag.invest.iface.av.api.AvDataFetchService;
import de.slag.invest.iface.av.api.AvStockValueDto;
import de.slag.invest.imp.cache.ImportCacheService;
import de.slag.invest.imp.cache.ImportCacheStockValueDto;
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
	private AvDataFetchService avDataFetchService;

	@Resource
	private ImportCacheService importCacheService;

	@Resource
	private DataImportConsolidateService dataImportConsolidateService;

	public void importData() {

		final Collection<ImpStockValueDto> dtos = new ArrayList<>();

		Collection<ImpStockValueDto> fetchDataFromInterface = fetchDataFromInterface();
		dtos.addAll(fetchDataFromInterface);
		dtos.addAll(fetchDataFromCache());

		storeToCache(fetchDataFromInterface);

		Collection<ImpStockValueDto> newest = dataImportConsolidateService.newestForThisDay(dtos);

		// TODO: filter doubles from database

		saveAll(newest);
	}

	private void storeToCache(Collection<ImpStockValueDto> fetchDataFromInterface) {
		importCacheService.storeData(fetchDataFromInterface.stream().map(dto -> {
			final ImportCacheStockValueDto toDto = new ImportCacheStockValueDto();
			
			new SimpleReflectionEngine().mapValues(dto, toDto);
			

			return toDto;
		}).collect(Collectors.toList()));

	}

	private void saveAll(final Collection<ImpStockValueDto> dtos) {
		List<StockValueDto> collect2 = dtos.stream().map(dto -> of(dto)).collect(Collectors.toList());

		final List<StockValue> allStockValues = collect2.stream().map(dto -> stockValueDtoService.stockValueOf(dto))
				.collect(Collectors.toList());

		allStockValues.forEach(sv -> stockValueService.save(sv));
	}

	private Collection<ImpStockValueDto> fetchDataFromCache() {
		Collection<ImportCacheStockValueDto> cachedData = importCacheService.fetchData();
		return cachedData.stream().map(dto -> of(dto)).collect(Collectors.toList());
	}

	private Collection<ImpStockValueDto> fetchDataFromInterface() {
		final Collection<AvStockValueDto> fetchData = avDataFetchService.fetchData();
		return fetchData.stream().map(dto -> of(dto)).collect(Collectors.toList());
	}

	private ImpStockValueDto of(AvStockValueDto dto) {
		ImpStockValueDto toDto = new ImpStockValueDto();
		new SimpleReflectionEngine().mapValues(dto, toDto);
		return toDto;
	}

	private StockValueDto of(ImpStockValueDto dto) {
		StockValueDto toDto = new StockValueDto();
		new SimpleReflectionEngine().mapValues(dto, toDto);
		return toDto;
	}

	private ImpStockValueDto of(ImportCacheStockValueDto dto) {
		ImpStockValueDto toDto = new ImpStockValueDto();
		new SimpleReflectionEngine().mapValues(dto, toDto);
		return toDto;
	}

}
