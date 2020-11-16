package de.slag.invest.one;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.model.EntityBean;
import de.slag.common.util.SleepUtils;
import de.slag.invest.domain.model.security.Security;
import de.slag.invest.one.api.EntityType;
import de.slag.invest.one.api.InvOneService;
import de.slag.invest.service.adm.api.InvAdmParameter;
import de.slag.invest.service.adm.api.InvAdmService;
import de.slag.invest.service.domain.api.DomainService;
import de.slag.invest.service.domain.api.ReportDomainService;
import de.slag.invest.service.domain.api.SecurityDomainService;

@Service
public class InvOneServiceImpl implements InvOneService {

	private static final String REPORT = "REPORT";

	private static final String CALC = "CALC";

	private static final String IMPORT = "IMPORT";

	private static final Log LOG = LogFactory.getLog(InvOneServiceImpl.class);

	private static final List<String> CONTROL_COMMANDS = Arrays.asList(IMPORT, CALC, REPORT);

	private static final Map<Class<?>, EntityType> CLASS_TYPE_MAP = new HashMap<>();

	@Resource
	private InvAdmService invAdmService;

	@Resource
	private SecurityDomainService securityDomainService;

	@Resource
	private ReportDomainService reportDomainService;
	
	@PostConstruct
	public void init() {
		CLASS_TYPE_MAP.put(Security.class, EntityType.SECURITY);
	}

	@Override
	public void runScheduled() {
		final String schedulerControlDirectory = invAdmService.getValue(InvAdmParameter.SCHEDULER_CONTROL_DIRECTORY);
		if (StringUtils.isBlank(schedulerControlDirectory)) {
			LOG.warn(String.format("parameter '%s' not configured, skipping.",
					InvAdmParameter.SCHEDULER_CONTROL_DIRECTORY));
			return;
		}
		final List<Runnable> commandsToRun = CONTROL_COMMANDS.stream()
				.filter(c -> fileOf(schedulerControlDirectory, c).exists()).peek(c -> LOG.info("Task to run: " + c))
				.peek(c -> fileOf(schedulerControlDirectory, c).delete()).map(c -> runnableOf(c))
				.collect(Collectors.toList());

		if (commandsToRun.isEmpty()) {
			LOG.info("nothing to do");
			return;
		}

		commandsToRun.forEach(Runnable::run);
		LOG.info("all tasks done.");
	}

	private File fileOf(String directory, String filename) {
		return new File(directory + "/" + filename);
	}

	private Runnable runnableOf(String command) {
		switch (command) {
		case IMPORT:
			return () -> runImport();
		case CALC:
			return () -> runCalculations();
		case REPORT:
			return () -> runReporting();
		}
		throw new UnsupportedOperationException("command not supported: " + command);
	}

	private void runImport() {
		LOG.info("run import...");
		SleepUtils.sleepFor(5000);
		LOG.info("import done.");
	}

	private void runCalculations() {
		LOG.info("run calc...");
		SleepUtils.sleepFor(5000);
		LOG.info("calc done.");

	}

	private void runReporting() {
		LOG.info("run reporting...");
		SleepUtils.sleepFor(5000);
		LOG.info("import reporting.");
	}

	@Override
	public EntityBean loadOrCreate(EntityType type, Long id) {
		final DomainService<? extends EntityBean> domainService = determineService(type);
		if (id == null) {
			return domainService.create();
		}

		final Optional<? extends EntityBean> load = domainService.load(id);
		if (load.isEmpty()) {
			throw new RuntimeException(String.format("entity not found. type: %s, id: %s", type, id));
		}
		return load.get();
	}

	private DomainService<? extends EntityBean> determineService(EntityType type) {
		switch (type) {
		case SECURITY:
			return securityDomainService;
		case REPORT:
			return reportDomainService;
		default:
			throw new RuntimeException("not supported: " + type);
		}
	}

	@Override
	public void save(EntityBean bean) {
		determineService(CLASS_TYPE_MAP.get(bean.getClass())).saveBean(bean);
	}
}
