package de.slag.invest.accounting;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class AccountingNotesTest {

	@Test
	public void it() {
		AccountingNotes accountingNotes = new AccountingNotes();
		accountingNotes.add(bd(5000));
		accountingNotes.add("ABC", 50, bd(2500));
		Assert.assertTrue(bd(2500).equals(accountingNotes.getCash()));
		Assert.assertTrue(accountingNotes.getIsinMap().get("ABC").equals(50));

		accountingNotes.sub("ABC", 25, bd(1500));
		Assert.assertTrue(bd(4000).equals(accountingNotes.getCash()));
		Assert.assertTrue(accountingNotes.getIsinMap().get("ABC").equals(25));
		
		accountingNotes.add("DEF", 200, bd(4500));
		Assert.assertTrue(bd(-500).equals(accountingNotes.getCash()));
		Assert.assertTrue(accountingNotes.getIsinMap().get("ABC").equals(25));
		Assert.assertTrue(accountingNotes.getIsinMap().get("DEF").equals(200));
		
		accountingNotes.sub("ABC",50,bd(2000));
		Assert.assertTrue(bd(1500).equals(accountingNotes.getCash()));
		Assert.assertTrue(accountingNotes.getIsinMap().get("ABC").equals(-25));
		Assert.assertTrue(accountingNotes.getIsinMap().get("DEF").equals(200));
	}

	private BigDecimal bd(int val) {
		return BigDecimal.valueOf(val);
	}

}
