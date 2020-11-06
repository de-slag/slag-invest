package de.slag.invest.persist.impl;

import org.springframework.stereotype.Repository;

import de.slag.common.model.EntityBean;
import de.slag.invest.domain.model.security.SecurityPrice;
import de.slag.invest.persist.api.SecurityPriceDao;

@Repository
public class SecurityPriceDaoImpl extends AbstractInvestDao<SecurityPrice> implements SecurityPriceDao {

	@Override
	protected Class<? extends EntityBean> getType() {
		return SecurityPrice.class;
	}

}
