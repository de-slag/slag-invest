package de.slag.invest.service;

import org.springframework.stereotype.Service;

import de.slag.invest.model.StockValue;

@Service
public class StockValueServiceImpl extends AbstractDomainServiceImpl<StockValue> implements StockValueService {

	@Override
	protected Class<StockValue> getType() {
		return StockValue.class;
	}

}
