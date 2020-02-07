package de.slag.invest.iface.av;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import de.slag.common.base.AdmCache;

@RunWith(MockitoJUnitRunner.class)
public class AvDataFetchServiceImplTest {
	
	@InjectMocks
	AvDataFetchServiceImpl avDataFetchServiceImpl = new AvDataFetchServiceImpl();
	
	@Mock
	DataImportFetchService dataImportFetchService;

	@Mock
	AdmCache admCache;

	Map<String, String> admMap = new HashMap<>();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
//		Mockito.when(dataImportFetchService.fetchData()).thenCallRealMethod();
		Mockito.when(admCache.getValue(Mockito.anyString())).thenCallRealMethod();
		Mockito.when(dataImportFetchService.fetchData()).thenCallRealMethod();
	}

	@Test
	public void testFetchData() {
		Assert.assertNotNull(avDataFetchServiceImpl.fetchData());
	}

}
