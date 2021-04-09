package de.slag.invest.staging.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

import de.slag.common.model.EntityBean;

@Entity
public class StaMapping extends EntityBean {
	
	@Basic
	private String isinWkn;
	
	@Basic
	private String symbol;
	
	@Basic
	private String xtsuNotationId;
	
	@Basic
	private String ovNotationId;

	public String getIsinWkn() {
		return isinWkn;
	}

	public void setIsinWkn(String isinWkn) {
		this.isinWkn = isinWkn;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getXtsuNotationId() {
		return xtsuNotationId;
	}

	public void setXtsuNotationId(String xtsuNotationId) {
		this.xtsuNotationId = xtsuNotationId;
	}

	public String getOvNotationId() {
		return ovNotationId;
	}

	public void setOvNotationId(String ovNotationId) {
		this.ovNotationId = ovNotationId;
	}

}
