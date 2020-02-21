package de.slag.invest.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.slag.invest.model.Mandant;

@Service
public class MandantServiceImpl extends AbstractDomainServiceImpl<Mandant> implements MandantService {

	@Override
	protected Class<Mandant> getType() {
		return Mandant.class;
	}

	@Override
	public Optional<Mandant> loadBy(String name) {
		return loadBy(m -> m.getName().equals(name));
	}

}
