package de.slag.invest.domain.service.api;

import java.util.Collection;

import de.slag.invest.domain.model.security.Security;
import de.slag.invest.domain.model.security.SecurityPrice;

public interface SecurityPriceDomainService extends DomainService<SecurityPrice> {

	Collection<SecurityPrice> findBy(Security security);

}
