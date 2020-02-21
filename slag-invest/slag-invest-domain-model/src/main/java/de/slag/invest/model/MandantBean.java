package de.slag.invest.model;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class MandantBean extends DomainBean {

	@ManyToOne
	private Mandant mandant;

	public MandantBean(Mandant mandant) {
		this.mandant = mandant;
	}

	public Mandant getMandant() {
		return mandant;
	}
	
}
