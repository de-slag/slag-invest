package de.slag.invest.staging.logic;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.slag.invest.staging.logic.fetch.FetchingRunner;
import de.slag.invest.staging.model.StaSecurityPoint;

@Service
public class StaCentralSchedulingServiceImpl implements StaCentralSchedulingService {

	private static final Log LOG = LogFactory.getLog(StaCentralSchedulingServiceImpl.class);

	private static final String CRON_EVERY_SIX_HOURS_ON_WORKDAYS = "0 */6 * * 1-5";

	private static final String CRON_EVERY_FIVE_MINUTES = "0/5 * * *";

	@Resource
	private StaAdmConfigBusinessService staAdmConfigBusinessService;

	@Resource
	private StaSecurityPointBusinessService staSecurityPointBusinessService;

	@Scheduled(cron = CRON_EVERY_FIVE_MINUTES)
	public void fetchData() {

		Function<String, String> configurationProvider = key -> {
			return staAdmConfigBusinessService.loadBy(key).orElseThrow(() -> new NoSuchElementException(key))
					.getValue();
		};
		Consumer<StaSecurityPoint> securityPointPersiter = point -> staSecurityPointBusinessService.save(point);
		Supplier<StaSecurityPoint> newSecurityPointSupplier = () -> staSecurityPointBusinessService.create();

		FetchingRunner fetchingRunner = new FetchingRunner(configurationProvider, securityPointPersiter,
				newSecurityPointSupplier);

		fetchingRunner.run();
	}
}
