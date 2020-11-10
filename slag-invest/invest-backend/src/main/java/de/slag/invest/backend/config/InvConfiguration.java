package de.slag.invest.backend.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import de.slag.invest.one.api.InvOneService;

@Configuration
public class InvConfiguration {
	
	@Resource
	private InvOneService invOneService;

	@Scheduled(cron = "0 * * * * *")
	public void runScheduled() {
		invOneService.runScheduled();
	}
}
