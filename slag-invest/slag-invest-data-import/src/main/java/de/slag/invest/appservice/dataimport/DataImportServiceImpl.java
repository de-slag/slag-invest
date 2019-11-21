package de.slag.invest.appservice.dataimport;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.service.StockValueService;

@Service
public class DataImportServiceImpl implements DataImportSerivce {
	
	@Resource
	private StockValueService stockValueService;

}
