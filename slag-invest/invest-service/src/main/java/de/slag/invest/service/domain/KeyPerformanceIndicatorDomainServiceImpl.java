package de.slag.invest.service.domain;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.core.dao.Dao;
import de.slag.invest.domain.model.kpi.KeyPerformanceIndicator;
import de.slag.invest.persist.api.KeyPerformanceIndicatorDao;
import de.slag.invest.service.domain.api.KeyPerformanceIndicatorDomainService;

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
