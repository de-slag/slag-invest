package de.slag.invest.av.response;

import java.util.ArrayList;
import java.util.Collection;

import de.slag.invest.av.AvResponse;
import de.slag.invest.av.model.AvStockValue;

public class AvStockValueResponse implements AvResponse {
	
	private final Collection<AvStockValue> values = new ArrayList<>();

	public Collection<AvStockValue> getValues() {
		return values;
	}

}
