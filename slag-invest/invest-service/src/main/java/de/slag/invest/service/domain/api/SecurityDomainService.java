package de.slag.invest.service.domain.api;

import java.util.Optional;

import de.slag.invest.domain.model.security.Security;

public interface SecurityDomainService extends DomainService<Security> {

	Optional<Security> loadByIsin(String isin);

}
