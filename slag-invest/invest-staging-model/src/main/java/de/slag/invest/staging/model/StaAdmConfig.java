package de.slag.invest.staging.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

import de.slag.common.model.EntityBean;

@Entity
public class StaAdmConfig extends EntityBean {

	@Basic
	private String key;

	@Basic
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
