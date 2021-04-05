package de.slag.invest.staging.logic;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.slag.common.data.SysLogPersistService;
import de.slag.common.model.beans.SysLog;
import de.slag.common.model.beans.SysLog.Severity;
import de.slag.invest.staging.model.StaAdmConfig;

@Service
public class StaCentralSchedulingServiceImpl implements StaCentralSchedulingService {

	private static final Log LOG = LogFactory.getLog(StaCentralSchedulingServiceImpl.class);

	private static final String CRON_EVERY_SIX_HOURS_ON_WORKDAYS = "0 */6 * * 1-5";

	private static final String CRON_EVERY_FIVE_MINUTES = "0/5 * * *";

	@Resource
	private StaAdmConfigBusinessService staAdmConfigBusinessService;

	@Resource
	private StaSecurityPointBusinessService staSecurityPointBusinessService;

	@Resource
	private SysLogPersistService sysLogPersistService;
	
	
	@PostConstruct
	public void init() {
		LOG.info("initialized");
	}

	@Scheduled(cron = "0 * * * * *")
	public void fetchData() {
		LOG.info("fetch data");

		long currentTimeMillis = System.currentTimeMillis();

		StaAdmConfig config = staAdmConfigBusinessService.create();
		config.setConfigKey("fetch.data." + currentTimeMillis);
		config.setConfigValue("OK");

		staAdmConfigBusinessService.save(config);

		SysLog syslog = new SysLog();
		syslog.setSeverity(Severity.INFO);
		syslog.setInfo("data fetched");
		sysLogPersistService.save(syslog);
		
		LOG.info("data fetched");
	}
}
