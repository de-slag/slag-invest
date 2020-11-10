package de.slag.invest.backend.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "de.slag" })
@EnableScheduling
public class InvestBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestBackendApplication.class, args);
	}

}
