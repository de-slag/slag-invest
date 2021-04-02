package de.slag.invest.staging.logic.fetch.ov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapperBuilder;

class OvSecurityPointFetcherTest {

	Collection<IsinWkn> isinWkns;
	IsinWknOvNotationIdMapper notationIdMapper;
	OvSecurityPointFetcher fetcher;

	@BeforeEach
	void setUp() {
		isinWkns = Collections.singletonList(IsinWkn.of("DE0008469008"));
		final String sourceFileName = ResourceUtils.getFileFromResources("mapping.csv").toString();
		notationIdMapper = new IsinWknOvNotationIdMapperBuilder().withSourceFileName(sourceFileName).build();

		fetcher = new OvSecurityPointFetcherBuilder().withIsinWkns(isinWkns).withNotationIdMapper(notationIdMapper)
				.build();
	}

	@Test
	void test() throws Exception {
		Collection<FetchSecurityPoint> fetchSecurityPoints = fetcher.fetchSecurityPoints();
		assertEquals(253, fetchSecurityPoints.size());
	}

}
