package de.slag.invest.model;

public abstract class MandantBean extends DomainBean {

	private Mandant mandant;

	public MandantBean(Mandant mandant) {
		this.mandant = mandant;
	}

	public Mandant getMandant() {
		return mandant;
	}
}
