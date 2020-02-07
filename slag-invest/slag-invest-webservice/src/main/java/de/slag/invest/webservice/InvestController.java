package de.slag.invest.webservice;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.slag.common.base.AdmCache;

@RestController
public class InvestController {

	private static final Log LOG = LogFactory.getLog(InvestController.class);

	@Resource
	private AdmCache admCache;

	@PostConstruct
	public void setUp() {
		System.out.println("Controller created!");
		System.out.println(String.format("admCache: %s", admCache));
		LOG.info("test log");
	}

	@GetMapping("/invest")
	public Collection<String> sayHello() {
		return IntStream.range(0, 10).mapToObj(i -> "Hello1 number " + i).collect(Collectors.toList());
	}

}
