package de.slag.invest.webservice.crud;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

public abstract class AbstractIwsCrudController<T> {

	protected abstract Long create0();

	protected abstract T load0(long id);

	protected abstract void save0(T t);

	protected abstract void delete0(long id);

	@GetMapping("/load")
	public T load(@RequestParam long id) {
		return load0(id);
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON)
	public Response save(@RequestBody T t) {
		save0(t);
		return Response.ok().build();
	}

	@DeleteMapping(path = "/delete")
	public Response delete(@RequestParam long id) {
		delete0(id);
		return Response.ok().build();
	}

	@GetMapping("/create")
	public Long create() {
		return create0();
	}

}
