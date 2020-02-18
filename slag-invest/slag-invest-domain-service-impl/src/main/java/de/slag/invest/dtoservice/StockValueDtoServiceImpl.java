package de.slag.invest.dtoservice;

import org.springframework.stereotype.Service;

import de.slag.invest.dtomodel.StockValueDto;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.StockValue;

@Service
public class StockValueDtoServiceImpl implements StockValueDtoService {

	public StockValue stockValueOf(StockValueDto dto) {
		final StockValue stockValue = new StockValue(new Mandant());
		stockValue.setIsin(dto.getIsin());
		return stockValue;
	}

}
