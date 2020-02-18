package de.slag.invest.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Mandant extends DomainBean {
	
	@Basic
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
