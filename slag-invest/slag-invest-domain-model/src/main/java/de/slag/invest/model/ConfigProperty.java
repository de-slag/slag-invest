package de.slag.invest.model;

import javax.persistence.Entity;

@Entity
public class ConfigProperty extends MandantBean {

	public ConfigProperty() {
		super(null);
	}
	
	public ConfigProperty(Mandant mandant) {
		super(mandant);
	}
	
	private String key;
	
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
