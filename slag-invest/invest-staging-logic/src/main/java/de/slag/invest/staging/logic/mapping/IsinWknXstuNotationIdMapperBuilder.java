package de.slag.invest.staging.logic.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.CsvUtils;

public class IsinWknXstuNotationIdMapperBuilder implements Builder<IsinWknXstuNotationIdMapper> {

	private String sourceFileName;

	public IsinWknXstuNotationIdMapperBuilder withSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
		return this;
	}

	@Override
	public IsinWknXstuNotationIdMapper build() {
		Collection<CSVRecord> records = CsvUtils.readRecords(sourceFileName);
		Map<String, String> map = new HashMap<>();
		records.forEach(rec -> {
			String isinWkn = rec.get("ISIN_WKN");
			String notation = rec.get("XSTU_NOTATION_ID");
			map.put(isinWkn, notation);
		});
		return new IsinWknXstuNotationIdMapper(map);
	}

}
