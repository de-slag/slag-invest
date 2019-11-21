package de.slag.invest.appservice.dataimport;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.model.StockValue;
import de.slag.invest.service.StockValueService;

@Service
public class DataImportServiceImpl implements DataImportSerivce {
	
	@Resource
	private StockValueService stockValueService;

	public void importData() {
		
		testWiseGenerateData();

	}

	private void testWiseGenerateData() {
		stockValueService.save(createStockValue("abc"));
		stockValueService.save(createStockValue("def"));
		stockValueService.save(createStockValue("ghi"));		
	}

	private StockValue createStockValue(String isin) {
		StockValue stockValue = new StockValue();
		stockValue.setIsin(isin);
		return stockValue;
	}

}
