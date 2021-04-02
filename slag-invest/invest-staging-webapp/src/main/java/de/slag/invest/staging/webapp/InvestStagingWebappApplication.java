package de.slag.invest.staging.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "de.slag")
@EnableScheduling
public class InvestStagingWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestStagingWebappApplication.class, args);
	}

}
