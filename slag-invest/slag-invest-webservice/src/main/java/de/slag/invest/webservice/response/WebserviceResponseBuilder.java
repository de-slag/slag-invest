package de.slag.invest.webservice.response;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import de.slag.invest.model.DomainBean;

public class WebserviceResponseBuilder implements Builder<WebserviceResponse> {

	private Collection<DomainBean> responseObjects = new ArrayList<>();

	public WebserviceResponseBuilder with(Collection<DomainBean> beans) {
		this.responseObjects.addAll(beans);
		return this;
	}

	@Override
	public WebserviceResponse build() {
		 
		
		// TODO Auto-generated method stub
		return null;
	}

}
