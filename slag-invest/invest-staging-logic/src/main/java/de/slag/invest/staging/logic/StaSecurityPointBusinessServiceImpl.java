package de.slag.invest.staging.logic;

import javax.annotation.Resource;

import de.slag.common.data.PersistService;
import de.slag.common.logic.AbstractBusinessService;
import de.slag.invest.staging.data.StaSecurityPointPersistService;
import de.slag.invest.staging.model.StaSecurityPoint;

public class StaSecurityPointBusinessServiceImpl extends AbstractBusinessService<StaSecurityPoint>
		implements StaSecurityPointBusinessService {

	@Resource
	private StaSecurityPointPersistService staSecurityPointPersistService;

	@Override
	public StaSecurityPoint create() {
		return new StaSecurityPoint();
	}

	@Override
	protected PersistService<StaSecurityPoint> getPersistService() {
		return staSecurityPointPersistService;
	}

}
