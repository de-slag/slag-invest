package de.slag.invest.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.webcommon.DemoDto;

@RestController
@RequestMapping("/demo")
public class IwsDemoController extends AbstractIwsController {

	private static final Log LOG = LogFactory.getLog(IwsDemoController.class);

	private Collection<DemoDto> dtos = new ArrayList<>();

	@DeleteMapping(path = "/delete")
	public String delete(@RequestParam long id) {
		final DemoDto load = load(id);
		if (load == null) {
			return null;
		}
		dtos.remove(load);
		return "deletion OK";
	}

	@PostMapping(path = "/test", consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
	public String save(@RequestBody String string) {
		LOG.info(string);
		return "OK: " + string;
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON)
	public Response save(@RequestBody DemoDto demoDto) {
		final Long id = demoDto.getId();
		Objects.requireNonNull(id, "id is null");
		final DemoDto load = load(id);
		Objects.requireNonNull(load);
		dtos.remove(load);
		dtos.add(demoDto);
		return Response.ok().build();
	}

	@GetMapping("/load")
	public DemoDto load(@RequestParam long id) {
		final Optional<DemoDto> findAny = dtos.stream().filter(e -> e.getId().equals(id)).findAny();
		if (findAny.isEmpty()) {
			return null;
		}
		return findAny.get();
	}

	@GetMapping("/create")
	public Long create() {
		final DemoDto demoDto = new DemoDto();
		final Long id;
		synchronized (this) {
			if (dtos.isEmpty()) {
				id = 1L;
			} else {
				id = Collections.max(dtos.stream().map(e -> e.getId()).collect(Collectors.toList())) + 1;
			}
			demoDto.setId(id);
			dtos.add(demoDto);
		}
		return id;
	}

	// that is not working, exception occures
	public ArrayList<DemoDto> getDtoList() {
		final ArrayList<DemoDto> arrayList = new ArrayList<>();
		arrayList.add(createDemoDto());
		arrayList.add(createDemoDto());
		return arrayList;
	}

	// that is not working properties are NULL on client side
	public Response getDtoResponse() {
		return Response.ok(createDemoDto()).build();
	}

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
