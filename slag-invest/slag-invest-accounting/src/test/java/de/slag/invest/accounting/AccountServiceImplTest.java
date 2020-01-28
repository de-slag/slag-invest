package de.slag.invest.accounting;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slag.common.testsupport.MatchersProvider;

public class AccountServiceImplTest implements MatchersProvider {

	private AccountServiceImpl accountServiceImpl;

	@Before
	public void setUp() {
		accountServiceImpl = new AccountServiceImpl();
	}

	@Test
	public void test() {
		Assert.assertThat(accountServiceImpl.determineBegin(AccountingPeriod.QUATERLY, LocalDate.of(2010, 02, 17)),
				is(LocalDate.of(2010, 01, 01)));

		Assert.assertThat(accountServiceImpl.determineBegin(AccountingPeriod.QUATERLY, LocalDate.of(2010, 01, 1)),
				is(LocalDate.of(2010, 01, 01)));

		Assert.assertThat(accountServiceImpl.determineBegin(AccountingPeriod.QUATERLY, LocalDate.of(2010, 6, 30)),
				is(LocalDate.of(2010, 04, 01)));
	}

}
