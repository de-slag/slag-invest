package de.slag.invest.webservice;

import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.webservice.response.WebserviceResponse2;

@RestController
@RequestMapping("/test")
public class IwsTestController extends AbstractIwsController {

	@GetMapping
	public Object get(@RequestParam(required = false) String param) {
		if ("response".equalsIgnoreCase(param)) {
			return Response.SC_OK;
		}
		if ("wsresponse".equalsIgnoreCase(param)) {
			final WebserviceResponse2 response = new WebserviceResponse2();
			response.setSuccessful(true);
			response.setMessage("param: " + param);
			return response;
		}
		return "test, param: " + param;
	}

}
