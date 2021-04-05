package de.slag.invest.staging.webapp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "de.slag")
@EnableScheduling
@EntityScan(basePackages = "de.slag")
public class InvestStagingWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestStagingWebappApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		InitialContext initCtx;
		try {
			initCtx = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		Context envCtx;
		try {
			envCtx = (Context) initCtx.lookup("java:comp/env");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		Object lookup;
		try {
			lookup = envCtx.lookup("jdbc/default");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		return (DataSource) lookup;
	}

	@Bean
	public PhysicalNamingStrategy physicalNamingStrategy() {
		return new CustomPhysicalNamingStrategy();
	}

}
