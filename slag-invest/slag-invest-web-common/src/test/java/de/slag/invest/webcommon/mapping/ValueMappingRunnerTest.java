package de.slag.invest.webcommon.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import de.slag.invest.webcommon.model.CommonDto;

class ValueMappingRunnerTest {

	@Test
	void test() {
		final CommonDto to = new CommonDto();
		final MappingTestEntity from = new MappingTestEntity(1L);
		from.setName("test-name");
		final ValueMappingRunner valueMappingRunner = new ValueMappingRunner(from, to);
		valueMappingRunner.prepare();
		valueMappingRunner.run();
		
		final Map<String, Object> dtoValues = to.getValues();
		assertEquals("test-name", dtoValues.get("NAME"));
		assertEquals(1L, dtoValues.get("ID"));
	}

	

}
