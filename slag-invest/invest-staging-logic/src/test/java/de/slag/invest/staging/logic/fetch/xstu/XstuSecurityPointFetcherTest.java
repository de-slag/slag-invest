package de.slag.invest.staging.logic.fetch.xstu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapperBuilder;

class XstuSecurityPointFetcherTest {

	private IsinWknXstuNotationIdMapper mapper;
	
	private List<IsinWkn> isinWkns;

	@BeforeEach
	void setUp() {
		String sourceFileName = ResourceUtils.getFileFromResources("mapping.csv").toString();
		List<String> singletonList = Collections.singletonList("IE00B5BMR087");
		
		mapper = new IsinWknXstuNotationIdMapperBuilder().withSourceFileName(sourceFileName).build();
		isinWkns = singletonList.stream().map(e -> IsinWkn.of(e)).collect(Collectors.toList());
	}

	@Test
	void test() throws Exception {
		
		XstuSecurityPointFetcher xstuSecurityPointFetcher = new XstuSecurityPointFetcherBuilder().withMapper(mapper)
				.withIsinWkns(isinWkns).build();
		Collection<FetchSecurityPoint> fetchSecurityPoints = xstuSecurityPointFetcher.fetchSecurityPoints();
		assertTrue(!fetchSecurityPoints.isEmpty());

	}

}
