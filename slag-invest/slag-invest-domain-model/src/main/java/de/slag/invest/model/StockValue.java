package de.slag.invest.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;

import de.slag.common.model.EntityBean;

@Entity
public class StockValue extends DomainBean {
	
	@Basic
	private String isin;
	
	@Basic
	private String name;
	
	@Basic
	private String wkn;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWkn() {
		return wkn;
	}

	public void setWkn(String wkn) {
		this.wkn = wkn;
	}

}
