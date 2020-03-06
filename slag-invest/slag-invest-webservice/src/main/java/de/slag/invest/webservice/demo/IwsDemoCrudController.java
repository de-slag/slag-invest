package de.slag.invest.webservice.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.webcommon.demo.DemoDto;
import de.slag.invest.webservice.crud.AbstractIwsCrudController;

@RestController
@RequestMapping("/democrud")
public class IwsDemoCrudController extends AbstractIwsCrudController<DemoDto> {

	private Long seq = 0L;

	private Map<Long, DemoDto> map = new HashMap<>();

	@Override
	public Long create0() {
		final Long id = ++seq;
		final DemoDto value = new DemoDto();
		value.setId(id);
		map.put(id, value);
		return id;
	}

	@Override
	protected DemoDto load0(long id) {
		return map.get(id);
	}

	@Override
	protected void save0(DemoDto t) {
		map.put(t.getId(), t);

	}

	@Override
	protected void delete0(long id) {
		map.remove(id);
	}

	@Override
	protected void validate(String token) {
		// nothing to do at Demo
	}

}
