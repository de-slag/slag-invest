package de.slag.invest.dtoservice;

import de.slag.invest.dtomodel.StockValueDto;
import de.slag.invest.model.StockValue;

public interface StockValueDtoService {
	
	StockValue stockValueOf(StockValueDto dto); 

}
