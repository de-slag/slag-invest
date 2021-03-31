package de.slag.invest.staging.logic.fetch.xstu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;

class XstuSecurityPointFetcherTest {

	@Test
	void test() throws Exception {
		XstuSecurityPointFetcher xstuSecurityPointFetcher = new XstuSecurityPointFetcher();
		Collection<FetchSecurityPoint> fetchSecurityPoints = xstuSecurityPointFetcher.fetchSecurityPoints();
		
	}

}
