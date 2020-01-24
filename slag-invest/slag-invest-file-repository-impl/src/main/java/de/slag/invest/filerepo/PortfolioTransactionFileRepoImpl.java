package de.slag.invest.filerepo;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;
import de.slag.common.utils.DateUtils;

@Repository
public class PortfolioTransactionFileRepoImpl implements PortfolioTransactionFileRepo {

	private static final Log LOG = LogFactory.getLog(PortfolioTransactionFileRepoImpl.class);

	private static final String PORTFOLIO = "PORTFOLIO_";
	@Resource
	private AdmCache admCache;

	@Override
	public Collection<PortfolioTransactionDto> findAll() {
		final String fileRepoPathName = admCache.getValue(FileRepoProperties.DE_SLAG_INVEST_FILEREPO)
				.orElseThrow(() -> new BaseException("not configured: " + FileRepoProperties.DE_SLAG_INVEST_FILEREPO));

		LOG.info("File-Repo Path-Name: " + fileRepoPathName);

		final File fileRepoPath = new File(fileRepoPathName);
		final List<File> portfolioFiles = Arrays.asList(fileRepoPath.listFiles()).stream()
				.filter(file -> file.getName().startsWith(PORTFOLIO)).filter(file -> file.getName().endsWith(".csv"))
				.collect(Collectors.toList());

		return portfolioFiles.stream().flatMap(file -> toTransactionDtos(file).stream()).collect(Collectors.toList());

	}

	private Collection<PortfolioTransactionDto> toTransactionDtos(File file) {
		final String portfolioNumber0 = file.getName().substring(PORTFOLIO.length());
		final String portfolioNumber = portfolioNumber0.substring(0, portfolioNumber0.length() - 4);

		Collection<CSVRecord> records = CsvUtils.getRecords(file.getAbsolutePath());

		Collection<Map<String, String>> recordsAsValueMaps = records.stream().map(rec -> rec.toMap())
				.collect(Collectors.toList());

		final PortfolioTransactionDataValidator portfolioTransactionDataValidator = new PortfolioTransactionDataValidator();
		if (!portfolioTransactionDataValidator.isValid(recordsAsValueMaps)) {
			LOG.error(String.format("* * *\ncontent not valid: %s , file: %s",
					portfolioTransactionDataValidator.getValidateIssues(), file));
			return Collections.emptyList();
		}

		return records.stream().map(rec -> {
			PortfolioTransactionDto dto = new PortfolioTransactionDto();
			dto.setPortfolioNumber(portfolioNumber);

			final LocalDateTime timestamp;
			if (!rec.isMapped("TIMESTAMP")) {
				if (!rec.isMapped("DATE")) {
					throw new BaseException("not mapped: DATE or TIMESTAMP, file " + file);
				}
				final String string = rec.get("DATE");
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(string);
				} catch (ParseException e) {
					throw new BaseException(e);
				}
				timestamp = DateUtils.toLocalDateTime(date);

			} else {
				final String string = rec.get("TIMESTAMP");
				final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
				Date parse;
				try {
					parse = simpleDateFormat.parse(string);
				} catch (ParseException e) {
					throw new BaseException(e);
				}
				timestamp = DateUtils.toLocalDateTime(parse);
			}

			dto.setDate(timestamp.toLocalDate());
			dto.setIsin(rec.get("ISIN_WKN"));

			final String countAsSTring = rec.get("COUNT");
			dto.setCount(StringUtils.isBlank(countAsSTring) ? null : Integer.valueOf(countAsSTring));

			final String totalPriceAsString = getOneOrTheOther(rec, file.getAbsolutePath(), "AMOUNT", "TOTAL_PRICE");
			dto.setTotalPrice(StringUtils.isBlank(totalPriceAsString) ? null
					: BigDecimal.valueOf(Double.valueOf(totalPriceAsString)));
			dto.setType(rec.get("TYPE"));
			return dto;
		}).collect(Collectors.toList());
	}

	private String getOneOrTheOther(CSVRecord rec, String filename, String one, String other) {
		if (rec.isMapped(one)) {
			return rec.get(one);
		}
		if (rec.isMapped(other)) {
			return rec.get(other);
		}
		throw new BaseException("file '%s' does not mapping '%s' nor '%s'", filename, one, other);

	}

}
