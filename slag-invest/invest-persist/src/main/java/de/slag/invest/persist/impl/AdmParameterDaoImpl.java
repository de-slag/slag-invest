package de.slag.invest.persist.impl;

import de.slag.common.model.EntityBean;
import de.slag.common.model.beans.AdmParameter;
import de.slag.invest.persist.api.AdmParameterDao;

public class AdmParameterDaoImpl extends AbstractInvestDao<AdmParameter> implements AdmParameterDao {

	@Override
	protected Class<? extends EntityBean> getType() {
		return AdmParameter.class;
	}

}
