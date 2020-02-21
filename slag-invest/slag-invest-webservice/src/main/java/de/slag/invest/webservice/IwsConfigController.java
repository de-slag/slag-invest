package de.slag.invest.webservice;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.model.Mandant;
import de.slag.invest.service.ConfigPropertyService;
import de.slag.invest.service.MandantService;
import de.slag.invest.webservice.response.WebserviceResponse2;

@RestController
@RequestMapping("/config")
public class IwsConfigController extends AbstractIwsController {

	@Resource
	private MandantService mandantService;

	@Resource
	private ConfigPropertyService configPropertyService;

	@GetMapping("/read")
	public WebserviceResponse2 read(@RequestParam String token, @RequestParam String key) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}
		final Mandant mandant = getCredentialsComponent().getMandant(CredentialToken.of(token));
		if (mandant == null) {
			return null;
		}
		final Optional<ConfigProperty> loadBy = configPropertyService.loadBy(key, mandant);
		if (loadBy.isEmpty()) {
			return null;
		}
		final ConfigProperty configProperty = loadBy.get();
		
		
		return null;
	}

	@GetMapping("/save")
	public WebserviceResponse2 save(@RequestParam String token, @RequestParam String key) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}
		final Mandant mandant = getCredentialsComponent().getMandant(CredentialToken.of(token));
		if (mandant == null) {
			return null;
		}
		return null;
	}

	@GetMapping("/delete")
	public WebserviceResponse2 delete(@RequestParam String token, @RequestParam String key) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}
		final Mandant mandant = getCredentialsComponent().getMandant(CredentialToken.of(token));
		if (mandant == null) {
			return null;
		}
		return null;
	}

	@GetMapping("/import")
	public WebserviceResponse2 importConfig(@RequestParam String token) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}
		final Mandant mandant = getCredentialsComponent().getMandant(CredentialToken.of(token));
		if (mandant == null) {
			return null;
		}
		return null;
	}

	@GetMapping("/export")
	public WebserviceResponse2 exportConfig(@RequestParam String token) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}
		final Mandant mandant = getCredentialsComponent().getMandant(CredentialToken.of(token));
		if (mandant == null) {
			return null;
		}
		return null;
	}

}
