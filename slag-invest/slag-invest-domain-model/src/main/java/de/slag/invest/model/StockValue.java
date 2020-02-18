package de.slag.invest.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class StockValue extends MandantBean {
	

	@Basic
	private String isin;
	
	@Basic
	private String name;
	
	@Basic
	private String wkn;
	
	public StockValue(Mandant mandant) {
		super(mandant);
	}
	
	StockValue() {
		this(null);
	}

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
