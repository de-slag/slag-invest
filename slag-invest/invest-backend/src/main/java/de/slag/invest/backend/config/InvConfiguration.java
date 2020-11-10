package de.slag.invest.backend.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class InvConfiguration {

	@Scheduled(fixedRate = 5000)
	public void runScheduled() {
		System.out.println("scheduled: " + LocalDateTime.now());
	}

}
