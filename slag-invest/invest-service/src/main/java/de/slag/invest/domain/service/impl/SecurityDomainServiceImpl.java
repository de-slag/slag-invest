package de.slag.invest.domain.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.common.core.dao.Dao;
import de.slag.invest.domain.model.security.Security;
import de.slag.invest.domain.service.api.SecurityDomainService;
import de.slag.invest.persist.api.SecurityDao;

@Service
public class SecurityDomainServiceImpl extends AbstractDomainService<Security> implements SecurityDomainService {

	@Resource
	private SecurityDao securityDao;

	@Override
	protected Dao<Security> getDao() {
		return securityDao;
	}

	@Override
	public Optional<Security> loadByIsin(String isin) {
		final Collection<Long> allIds = securityDao.findAllIds();
		final List<Security> collect = allIds.stream().map(id -> securityDao.loadById(id)).filter(o -> o.isPresent())
				.map(o -> o.get()).filter(s -> isin.equals(s.getIsin())).collect(Collectors.toList());
		
		if (collect.size() > 1) {
			throw new BaseException("more than one result found for: " + isin);
		}
		if (collect.isEmpty()) {
			return Optional.empty();
		}
		return collect.stream().findAny();
	}

}
