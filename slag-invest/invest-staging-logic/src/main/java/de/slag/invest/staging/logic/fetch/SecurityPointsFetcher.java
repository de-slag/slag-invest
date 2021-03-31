package de.slag.invest.staging.logic.fetch;

import java.util.Collection;
import java.util.concurrent.Callable;

import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;

public interface SecurityPointsFetcher extends Callable<Collection<FetchSecurityPoint>> {

	@Override
	default Collection<FetchSecurityPoint> call() throws Exception {
		return fetchSecurityPoints();
	}

	Collection<FetchSecurityPoint> fetchSecurityPoints() throws Exception;

}
