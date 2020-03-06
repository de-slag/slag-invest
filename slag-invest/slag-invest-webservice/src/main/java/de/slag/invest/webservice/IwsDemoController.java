package de.slag.invest.webservice;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.webcommon.DemoDto;

@RestController
@RequestMapping("/demo")
public class IwsDemoController extends AbstractIwsController {

	private static final Log LOG = LogFactory.getLog(IwsDemoController.class);

	@GetMapping("/dto")
	public DemoDto getDto() {
		return createDemoDto();
	}

	@GetMapping("/response")
	public Response getResponse() {
		return Response.ok().build();
	}

	@GetMapping
	public String get() {
		return "demo";
	}

	private DemoDto createDemoDto() {
		final DemoDto demoDto = new DemoDto();
		demoDto.setId(1L);
		demoDto.setName("Demo!");
		return demoDto;
	}
}
