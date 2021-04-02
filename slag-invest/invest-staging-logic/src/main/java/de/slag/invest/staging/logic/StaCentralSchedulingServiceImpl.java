package de.slag.invest.staging.logic;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StaCentralSchedulingServiceImpl implements StaCentralSchedulingService {

	private static final Log LOG = LogFactory.getLog(StaCentralSchedulingServiceImpl.class);

	private static final String CRON_EVERY_SIX_HOURS_ON_WORKDAYS = "0 */6 * * 1-5";

	private static final String CRON_EVERY_FIVE_MINUTES = "0/5 * * *";

//	@Resource
//	private StaAdmConfigBusinessService staAdmConfigBusinessService;
//
//	@Resource
//	private StaSecurityPointBusinessService staSecurityPointBusinessService;
	
	@PostConstruct
	public void init() {
		LOG.info("initialized");
	}

	@Scheduled(cron = "0 * * * * *")
	public void fetchData() {
		LOG.info("fetch data");
	}
}
