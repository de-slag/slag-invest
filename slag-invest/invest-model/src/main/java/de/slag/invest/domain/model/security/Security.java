package de.slag.invest.domain.model.security;

import javax.persistence.Basic;
import javax.persistence.Entity;

import de.slag.common.model.EntityBean;

@Entity
public class Security extends EntityBean {

	@Basic
	private String name;

	@Basic
	private String isin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

}
