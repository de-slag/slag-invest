package de.slag.invest.staging.logic.scheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.slag.common.data.SysLogPersistService;
import de.slag.common.logic.AdmParameterBusinessService;
import de.slag.common.logic.XiBusinessService;
import de.slag.common.model.beans.SysLog;
import de.slag.common.model.beans.SysLog.Severity;
import de.slag.invest.staging.logic.StaSecurityPointBusinessService;
import de.slag.common.model.beans.XiData;

@Service
public class StaCentralSchedulingServiceImpl implements StaCentralSchedulingService {

	private static final Log LOG = LogFactory.getLog(StaCentralSchedulingServiceImpl.class);

	private static final String CRON_EVERY_SIX_HOURS_ON_WORKDAYS = "0 */6 * * 1-5";

	private static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * * * *";

	@Resource
	private AdmParameterBusinessService admParameterBusinessService;

	@Resource
	private StaSecurityPointBusinessService staSecurityPointBusinessService;

	@Resource
	private SysLogPersistService sysLogPersistService;

	@Resource
	private XiBusinessService xiBusinessService;

	@PostConstruct
	public void init() {
		LOG.info("initialized");
	}

	@Scheduled(cron = CRON_EVERY_FIVE_MINUTES)
	public void fetchData() {
		LOG.info("fetch data");

		SysLog syslog = new SysLog();
		syslog.setSeverity(Severity.INFO);
		syslog.setInfo("data fetched");
		sysLogPersistService.save(syslog);

		XiData xiData = xiBusinessService.create();
		xiData.setType("FETCHED_SECURITY_DATA");
		xiBusinessService.addValue(xiData, "test", "test-value");
		xiBusinessService.save(xiData);

		LOG.info("data fetched");
	}
}
