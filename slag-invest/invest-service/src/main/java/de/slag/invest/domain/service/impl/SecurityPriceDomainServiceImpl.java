package de.slag.invest.domain.service.impl;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.core.dao.Dao;
import de.slag.invest.domain.model.security.Security;
import de.slag.invest.domain.model.security.SecurityPrice;
import de.slag.invest.domain.service.api.SecurityPriceDomainService;
import de.slag.invest.persist.api.SecurityPriceDao;

@Service
public class SecurityPriceDomainServiceImpl extends AbstractDomainService<SecurityPrice>
		implements SecurityPriceDomainService {

	@Resource
	private SecurityPriceDao securityPriceDao;

	@Override
	protected Dao<SecurityPrice> getDao() {
		return securityPriceDao;
	}

	@Override
	public Collection<SecurityPrice> findBy(Security security) {
		return securityPriceDao.findAllBy(sp -> security.equals(sp.getSecurity()));
	}
}
