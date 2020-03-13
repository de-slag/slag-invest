package de.slag.invest.webservice.crud;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import de.slag.invest.model.Mandant;
import de.slag.invest.webservice.CredentialToken;
import de.slag.invest.webservice.IwsCredentialComponent;

public abstract class AbstractIwsCrudController<T> {

	@Resource
	private IwsCredentialComponent iwsCredentialComponent;

	protected abstract Long create0(Mandant mandant);

	protected abstract T load0(long id, Mandant mandant);

	protected abstract void save0(T t, Mandant mandant);

	protected abstract void delete0(long id, Mandant mandant);

	protected void validate(String token) {
		try {
			if (!isValid(CredentialToken.of(token))) {
				throw new IllegalAccessException("credentials not valid");
			}
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private boolean isValid(final CredentialToken of) {
		return iwsCredentialComponent.isValid(of);
	}

	protected Mandant getMandant(String token) {
		return iwsCredentialComponent.getMandant(CredentialToken.of(token));
	}

	@GetMapping("/load")
	public T load(@RequestParam("id") long id, @RequestParam(value = "token", required = false) String token) {
		validate(token);
		return load0(id, getMandant(token));
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON)
	public Response save(@RequestBody T t, @RequestParam(value = "token", required = false) String token) {
		validate(token);
		save0(t, getMandant(token));
		return Response.ok().build();
	}

	@DeleteMapping(path = "/delete")
	public Response delete(@RequestParam("id") long id, @RequestParam(value = "token", required = false) String token) {
		validate(token);
		delete0(id, getMandant(token));
		return Response.ok().build();
	}

	@GetMapping("/create")
	public Long create(@RequestParam(value = "token", required = false) String token) {
		validate(token);
		return create0(getMandant(token));
	}
	
	

}
