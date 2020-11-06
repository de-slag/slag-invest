package de.slag.invest.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.core.dao.Dao;
import de.slag.invest.domain.model.kpi.KeyPerformanceIndicator;
import de.slag.invest.domain.service.api.KeyPerformanceIndicatorDomainService;
import de.slag.invest.persist.api.KeyPerformanceIndicatorDao;

@Service
public class KeyPerformanceIndicatorDomainServiceImpl extends AbstractDomainService<KeyPerformanceIndicator>
		implements KeyPerformanceIndicatorDomainService {

	@Resource
	private KeyPerformanceIndicatorDao keyPerformanceIndicatorDao;

	@Override
	protected Dao<KeyPerformanceIndicator> getDao() {
		return keyPerformanceIndicatorDao;
	}
}
