package de.slag.invest.staging.logic;

import java.util.Optional;

import de.slag.common.logic.BusinessService;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.model.StaMapping;

public interface StaMappingBusinessService extends BusinessService<StaMapping> {

	Optional<StaMapping> loadByIsinWkn(IsinWkn isinWkn);

}
