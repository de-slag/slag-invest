package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slag.common.base.BaseException;
import de.slag.invest.one.model.IsPortfolio;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsTransaction;
import de.slag.invest.one.model.IsTransactionType;

class IsPortfolioHoldingsCalculatorTest {

	private static final LocalDate DATE_2010_07_01 = LocalDate.of(2010, 7, 1);
	private static final LocalDate DATE_2010_01_04 = LocalDate.of(2010, 1, 4);
	private static final LocalDate DATE_2010_02_01 = LocalDate.of(2010, 2, 1);
	private static final LocalDate DATE_2010_03_01 = LocalDate.of(2010, 3, 1);
	private static final LocalDate DATE_2010_03_31 = LocalDate.of(2010, 3, 31);
	private static final LocalDate DATE_2010_04_05 = LocalDate.of(2010, 4, 5);
	private static final LocalDate DATE_2010_06_30 = LocalDate.of(2010, 6, 30);

	static IsSecurity security_dax;
	static IsSecurity security_hdax;
	static IsSecurity security_se600;
	static IsSecurity security_msci_world;

	@BeforeAll
	static void setUp() {
		security_dax = new IsSecurity("846900", "DAX Index");
		security_hdax = new IsSecurity("846901", "HDAX Index");
		security_se600 = new IsSecurity("EU0009658202", "Stoxx Europe 600");
		security_msci_world = new IsSecurity("CH0001693230", "MSCI World");
	}

	@Test
	void mixedTestWithCalculationBetween() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();

		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_hdax, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_se600, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_msci_world, DATE_2010_01_04, 10));

		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_04_05, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_hdax, DATE_2010_04_05, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_se600, DATE_2010_04_05, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_msci_world, DATE_2010_04_05, 10));

		final IsPortfolioHoldingsCalculator firstQuarterCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_03_31);

		final IsPortfolioHoldingsCalculator secondQuarterCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_06_30);

		final Map<IsSecurity, Integer> firstQuarterResult = firstQuarterCalculator.calculate();
		final Map<IsSecurity, Integer> secondQuarterResult = secondQuarterCalculator.calculate();

		assertEquals(4, firstQuarterResult.size());
		assertEquals(10, firstQuarterResult.get(security_dax));
		assertEquals(10, firstQuarterResult.get(security_hdax));
		assertEquals(10, firstQuarterResult.get(security_se600));
		assertEquals(10, firstQuarterResult.get(security_msci_world));
		
		assertEquals(4, secondQuarterResult.size());
		assertEquals(5, secondQuarterResult.get(security_dax));
		assertEquals(5, secondQuarterResult.get(security_hdax));
		assertEquals(5, secondQuarterResult.get(security_se600));
		assertEquals(0, secondQuarterResult.get(security_msci_world));

	}

	@Test
	void mixedTestWithCompleteResold() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();

		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_hdax, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_se600, DATE_2010_01_04, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_msci_world, DATE_2010_01_04, 10));

		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_02_01, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_hdax, DATE_2010_02_01, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_se600, DATE_2010_02_01, 5));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_msci_world, DATE_2010_02_01, 10));

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertEquals(4, calculate.size());
		assertEquals(5, calculate.get(security_dax));
		assertEquals(5, calculate.get(security_hdax));
		assertEquals(5, calculate.get(security_se600));
		assertEquals(0, calculate.get(security_msci_world));
	}

	@Test
	void sameDayTest() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();

		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_01_04, 3));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 5));

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertEquals(1, calculate.size());
		assertEquals(2, calculate.get(security_dax));
	}

	@Test
	void lesserSimpleTest() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();

		transactions.add(new IsTransaction(IsTransactionType.SELL, security_hdax, DATE_2010_03_01, 4));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 1));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_03_01, 5));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_02_01, 10));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_hdax, DATE_2010_02_01, 8));

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertEquals(2, calculate.size());
		assertEquals(6, calculate.get(security_dax));
		assertEquals(4, calculate.get(security_hdax));
	}

	@Test
	void failsSellMoreThanHoldedTest() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 1));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_03_01, 5));

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		assertThrows(BaseException.class, () -> portfolioHoldingsCalculator.calculate());
	}

	@Test
	void lessSimpleTest() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 1));
		transactions.add(new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_02_01, 10));
		transactions.add(new IsTransaction(IsTransactionType.SELL, security_dax, DATE_2010_03_01, 5));

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertEquals(1, calculate.size());
		assertEquals(6, calculate.get(security_dax));
	}

	@Test
	void simpleTest() throws Exception {
		Collection<IsTransaction> transactions = new ArrayList<>();
		final IsTransaction buyDax = new IsTransaction(IsTransactionType.BUY, security_dax, DATE_2010_01_04, 1);
		transactions.add(buyDax);

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_07_01);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertEquals(1, calculate.size());
		assertEquals(1, calculate.get(security_dax));
	}

	@Test
	void testWithNoTransactions() throws Exception {

		Collection<IsTransaction> transactions = new ArrayList<>();

		final IsPortfolioHoldingsCalculator portfolioHoldingsCalculator = new IsPortfolioHoldingsCalculator(
				buildWith(transactions), DATE_2010_01_04);

		final Map<IsSecurity, Integer> calculate = portfolioHoldingsCalculator.calculate();
		assertTrue(calculate.isEmpty());
	}

	private IsPortfolio buildWith(Collection<IsTransaction> transactions) {
		return new IsPortfolio(transactions);
	}

}
