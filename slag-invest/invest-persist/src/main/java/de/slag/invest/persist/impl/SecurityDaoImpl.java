package de.slag.invest.persist.impl;

import org.springframework.stereotype.Repository;

import de.slag.common.model.EntityBean;
import de.slag.invest.domain.model.security.Security;
import de.slag.invest.persist.api.SecurityDao;

@Repository
public class SecurityDaoImpl extends AbstractInvestDao<Security> implements SecurityDao {

	@Override
	protected Class<? extends EntityBean> getType() {
		return Security.class;
	}

}
