package de.slag.invest.staging.webapp;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.slag.invest.staging.logic.StaCentralSchedulingService;

@Controller
public class GreetingController {

	@Resource
	private StaCentralSchedulingService staCentralSchedulingService;
	
	@PostConstruct
	public void init() {
		this.getClass();
	}
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	

}
