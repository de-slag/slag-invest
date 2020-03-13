package de.slag.invest.webcommon.mapping;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import de.slag.invest.webcommon.model.CommonDto;

class VersaValueMappingRunnerTest {

	@Test
	void test() {
		CommonDto dto = new CommonDto();
		final Map<String, Object> dtoValues = dto.getValues();
		dtoValues.put("NAME", "versa-test");
		dtoValues.put("name", "versa-test-false");
		dtoValues.put("TITLE", "title-test");
		dtoValues.put("ID", 47L);
		
		MappingTestEntity object = new MappingTestEntity(1L);
		final VersaValueMappingRunner versaValueMappingRunner = new VersaValueMappingRunner(dto, object);
		versaValueMappingRunner.prepare();
		versaValueMappingRunner.run();
		assertEquals("versa-test", object.getName());
		assertEquals(1l, object.getId());
	}

}
