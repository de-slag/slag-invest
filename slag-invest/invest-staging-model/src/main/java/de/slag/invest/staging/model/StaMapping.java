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
	private String XtsuNotationId;

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
		return XtsuNotationId;
	}

	public void setXtsuNotationId(String xtsuNotationId) {
		XtsuNotationId = xtsuNotationId;
	}

}
