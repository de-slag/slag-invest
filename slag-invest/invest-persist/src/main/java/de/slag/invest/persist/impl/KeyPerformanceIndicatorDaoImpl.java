package de.slag.invest.persist.impl;

import org.springframework.stereotype.Repository;

import de.slag.common.model.EntityBean;
import de.slag.invest.domain.model.kpi.KeyPerformanceIndicator;
import de.slag.invest.persist.api.KeyPerformanceIndicatorDao;

@Repository
public class KeyPerformanceIndicatorDaoImpl extends AbstractInvestDao<KeyPerformanceIndicator>
		implements KeyPerformanceIndicatorDao {

	@Override
	protected Class<? extends EntityBean> getType() {
		return KeyPerformanceIndicator.class;
	}

}
