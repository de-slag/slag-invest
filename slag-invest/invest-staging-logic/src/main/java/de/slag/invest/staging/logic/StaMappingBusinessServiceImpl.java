package de.slag.invest.staging.logic;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.data.PersistService;
import de.slag.common.logic.AbstractBusinessService;
import de.slag.invest.staging.data.StaMappingPersistService;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.model.StaMapping;

@Service
public class StaMappingBusinessServiceImpl extends AbstractBusinessService<StaMapping>
		implements StaMappingBusinessService {

	@Resource
	private StaMappingPersistService staMappingPersistService;

	@Override
	public StaMapping create() {
		return new StaMapping();
	}

	@Override
	protected PersistService<StaMapping> getPersistService() {
		return staMappingPersistService;
	}

	@Override
	public Optional<StaMapping> loadByIsinWkn(IsinWkn isinWkn) {
		return staMappingPersistService.loadBy(mapping -> isinWkn.getValue().equals(mapping.getIsinWkn()));
	}

}
