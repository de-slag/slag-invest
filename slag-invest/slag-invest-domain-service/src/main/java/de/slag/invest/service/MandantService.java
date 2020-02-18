package de.slag.invest.service;

import java.util.Optional;

import de.slag.invest.model.Mandant;

public interface MandantService extends DomainService<Mandant> {
	
	Optional<Mandant> loadBy(String name);

}
