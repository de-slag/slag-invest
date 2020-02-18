package de.slag.invest.webservice.response;

import java.util.ArrayList;
import java.util.Collection;

import de.slag.invest.model.StockValue;

public class WsBeanResponse extends WsResponse {
	
	private Collection<StockValue> beans = new ArrayList<>();

	public Collection<StockValue> getBeans() {
		return beans;
	}

	public void setBeans(Collection<StockValue> beans) {
		this.beans = beans;
	}
	
	

}
