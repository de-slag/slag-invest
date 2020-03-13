package de.slag.invest.webcommon.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import de.slag.invest.webcommon.model.CommonDto;

public class MappingIntegrationTest {

	private static final MappingTestEntityType TYPE = MappingTestEntityType.TWO;
	private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
	private static final int NUMBER = 5;
	private static final String TITLE = "test-title";
	private static final String NAME = "integration-test";

	@Test
	public void test() {
		final MappingTestEntity mappingTestEntity = new MappingTestEntity(1L);
		mappingTestEntity.setName(NAME);
		mappingTestEntity.setTitle(TITLE);
		mappingTestEntity.setNumber(NUMBER);
		mappingTestEntity.setTimestamp(TIMESTAMP);
		mappingTestEntity.setType(TYPE);

		final CommonDto commonDto = new CommonDto();
		final ValueMappingRunner valueMappingRunner = new ValueMappingRunner(mappingTestEntity, commonDto);
		valueMappingRunner.prepare();
		valueMappingRunner.run();
		commonDto.getValues().put("some-key", "some-value");

		final MappingTestEntity mappingTestEntity2 = new MappingTestEntity(2L);
		final VersaValueMappingRunner versaValueMappingRunner = new VersaValueMappingRunner(commonDto,
				mappingTestEntity2);
		versaValueMappingRunner.prepare();
		versaValueMappingRunner.run();

		assertEquals(NAME, mappingTestEntity2.getName());
		assertEquals(TITLE, mappingTestEntity2.getTitle());
		assertEquals(NUMBER, mappingTestEntity2.getNumber());
		assertEquals(TIMESTAMP, mappingTestEntity2.getTimestamp());
		assertEquals(TYPE, mappingTestEntity2.getType());
		assertEquals(2l, mappingTestEntity2.getId());

	}

}
