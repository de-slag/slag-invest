package de.slag.invest.backend.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "de.slag" })
public class InvestBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestBackendApplication.class, args);
	}

}
