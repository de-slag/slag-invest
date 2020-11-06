package de.slag.invest.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.invest.domain.model.security.Security;
import de.slag.invest.domain.model.security.SecurityPrice;
import de.slag.invest.domain.service.api.SecurityDomainService;
import de.slag.invest.domain.service.api.SecurityPriceDomainService;
import de.slag.invest.service.adm.InvAdmParameter;
import de.slag.invest.service.api.DataImportService;
import de.slag.invest.service.api.InvAdmService;
import de.slag.staging.model.SecurityCsv;

@Service
public class DataImportServiceImpl implements DataImportService {

	private static final Log LOG = LogFactory.getLog(DataImportServiceImpl.class);

	@Resource
	private SecurityDomainService securityDomainService;

	@Resource
	private SecurityPriceDomainService securityPriceDomainService;

	@Resource
	private InvAdmService invAdmService;

	@Override
	public void importData() {
		importSecuritiesFromStagingArea();
	}

	private void importSecuritiesFromStagingArea() {
		List<String> fileNames = getFileNames("securities");
		for (String fileName : fileNames) {
			try {
				importSecuritiesFromFile(fileName);
			} catch (IOException e) {
				throw new RuntimeException("file name: " + fileName, e);
			}
		}
	}

	private void importSecuritiesFromFile(String fileName) throws IOException {
		Collection<CSVRecord> records = CsvUtils.getRecords(fileName, SecurityCsv.STRUCTURE);
		for (CSVRecord csvRecord : records) {
			importSecurityFromRecord(csvRecord);
		}
	}

	private void importSecurityFromRecord(CSVRecord csvRecord) {
		final String isin = isinOf(csvRecord.get(SecurityCsv.WKN_ISIN));
		final Security security = loadOrCreateSecurityBy(isin);

		final Date fetchTs = fetchTsOf(csvRecord);
		final Date dateOf = dateOf(csvRecord);

		// FIXME - load SecurityPrice for the day not for fetch TS
		Predicate<SecurityPrice> sameDay = s -> s.getRetrievedAt().equals(dateOf);

		Predicate<SecurityPrice> sameSecurity = s -> security.equals(s.getSecurity());

		Predicate<SecurityPrice> predicate = sameSecurity.and(sameDay);
		final Optional<SecurityPrice> loadBy = securityPriceDomainService.loadBy(predicate);

		final SecurityPrice price;
		if (loadBy.isEmpty()) {
			price = createPrice(security, fetchTs);
		} else {
			price = loadBy.get();
			if (price.getRetrievedAt().after(fetchTs)) {
				LOG.debug(String.format("%s %s, data in system is more up-to-date than the imported, skipping", isin,
						dateOf));
				return;
			}
		}
		price.setRetrievedAt(null);
		price.setHigh(null);
		price.setLow(null);
		price.setOpen(null);
		price.setClose(null);
		price.setVolume(null);
		securityPriceDomainService.save(price);
	}

	private SecurityPrice createPrice(Security security, Date fetchTs) {
		final SecurityPrice securityPrice = new SecurityPrice();
		securityPrice.setSecurity(security);
		securityPrice.setRetrievedAt(fetchTs);
		securityPriceDomainService.save(securityPrice);
		return securityPrice;
	}

	private Date dateOf(CSVRecord csvRecord) {
		final String dateAsString = csvRecord.get(SecurityCsv.DATE);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SecurityCsv.DATE_FORMAT);
		try {
			return simpleDateFormat.parse(dateAsString);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
	}

	private Date fetchTsOf(CSVRecord csvRecord) {
		final String string = csvRecord.get(SecurityCsv.FETCH_TS);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SecurityCsv.FETCH_TS_FORMAT);
		try {
			return simpleDateFormat.parse(string);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
	}

	private Security loadOrCreateSecurityBy(String isin) {
		Optional<Security> securityOptional = securityDomainService.loadByIsin(isin);
		if (securityOptional.isPresent()) {
			return securityOptional.get();
		}
		final Security newSecurity = new Security();
		newSecurity.setIsin(isin);
		securityDomainService.save(newSecurity);
		return newSecurity;
	}

	private String isinOf(String wknIsin) {
		if (isIsin(wknIsin)) {
			return wknIsin;
		}
		return determineIsinByWkn(wknIsin);
	}

	private String determineIsinByWkn(String wknIsin) {
		// FIXME
		return wknIsin;
	}

	private boolean isIsin(String wknIsin) {
		// FIXME
		return false;
	}

	private List<String> getFileNames(String dataAreaName) {
		final String value = invAdmService.getValue(InvAdmParameter.STAGINGAREA_DIRECTORY);
		return Arrays.asList(new File(value).listFiles()).stream()
				.filter(file -> file.getName().startsWith(dataAreaName)).filter(file -> file.getName().endsWith(".csv"))
				.map(file -> file.getAbsolutePath()).collect(Collectors.toList());

	}

}
