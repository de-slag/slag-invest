package de.slag.invest.staging.logic.fetch;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.logic.AdmParameterBusinessService;
import de.slag.common.logic.XiDataBusinessService;
import de.slag.common.model.beans.AdmParameter;
import de.slag.common.model.beans.XiData;
import de.slag.invest.staging.logic.StaMappingBusinessService;
import de.slag.invest.staging.logic.StaSecurityPointBusinessService;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapperBuilder;
import de.slag.invest.staging.model.StaMapping;
import de.slag.invest.staging.model.StaSecurityBasePoint;

@Service
public class StaFetchServiceImpl implements StaFetchService {

	private static final Log LOG = LogFactory.getLog(StaFetchServiceImpl.class);

	@Resource
	private AdmParameterBusinessService admParameterBusinessService;

	@Resource
	private StaMappingBusinessService staMappingBusinessService;

	@Resource
	private StaSecurityPointBusinessService staSecurityPointBusinessService;

	@Resource
	private XiDataBusinessService xiDataBusinessService;

	@Override
	public void fetchAll() {
		final IsinWknSybmolMapper sybmolMapper = createIsinWknSymbolMapper();
		final IsinWknXstuNotationIdMapper xstuNotationIdMapper = createIsinWknXstuNotationIdMapper();
		final IsinWknOvNotationIdMapper isinWknOvNotationIdMapper = createIsinWknOvNotationMapper();

		Function<String, String> configurationProvider = key -> {
			Optional<AdmParameter> parameterOptional = admParameterBusinessService.loadByKey(key);
			if (parameterOptional.isEmpty()) {
				return null;
			}
			return parameterOptional.get().getConfigValue();

		};

		Consumer<XiData> securityPointPersister = data -> xiDataBusinessService.save(data);
		Supplier<XiData> newSecurityPointSupplier = () -> xiDataBusinessService.create();
		new FetchingRunner(configurationProvider, securityPointPersister, newSecurityPointSupplier, sybmolMapper,
				xstuNotationIdMapper, isinWknOvNotationIdMapper);

	}

	private IsinWknOvNotationIdMapper createIsinWknOvNotationMapper() {
		Function<String, Optional<String>> provider = isinWkn -> {
			Optional<StaMapping> loadByIsinWkn = staMappingBusinessService.loadByIsinWkn(IsinWkn.of(isinWkn));
			return Optional.of(
					loadByIsinWkn.orElseThrow(() -> new RuntimeException("not found: " + isinWkn)).getOvNotationId());
		};
		return new IsinWknOvNotationIdMapperBuilder().withProvider(provider).build();
	}

	private IsinWknXstuNotationIdMapper createIsinWknXstuNotationIdMapper() {

		Function<String, Optional<String>> provider = isinWkn -> {
			Optional<StaMapping> loadByIsinWkn = staMappingBusinessService.loadByIsinWkn(IsinWkn.of(isinWkn));
			return Optional.of(
					loadByIsinWkn.orElseThrow(() -> new RuntimeException("not found: " + isinWkn)).getXtsuNotationId());
		};
		return new IsinWknXstuNotationIdMapperBuilder().withProvider(provider).build();
	}

	private IsinWknSybmolMapper createIsinWknSymbolMapper() {

		final Function<String, Optional<String>> provider = isinWkn -> {
			final Optional<StaMapping> mappingOptional = staMappingBusinessService.loadByIsinWkn(IsinWkn.of(isinWkn));
			return Optional
					.of(mappingOptional.orElseThrow(() -> new RuntimeException("not found: " + isinWkn)).getSymbol());

		};
		return new IsinWknSybmolMapperBuilder().withProvider(provider).build();
	}

}
