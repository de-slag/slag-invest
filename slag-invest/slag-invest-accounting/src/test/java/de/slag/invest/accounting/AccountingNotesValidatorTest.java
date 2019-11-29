package de.slag.invest.accounting;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class AccountingNotesValidatorTest {

	@Test
	public void it() {
		AccountingNotes accountingNotes = new AccountingNotes();
		AccountingNotesValidator accountingNotesValidator = new AccountingNotesValidator();
		Assert.assertTrue(accountingNotesValidator.isValid(accountingNotes));

		accountingNotes.add(BigDecimal.valueOf(5000));

		Assert.assertTrue(accountingNotesValidator.isValid(accountingNotes));
		accountingNotes.add("abc", 70, BigDecimal.valueOf(3500));
		Assert.assertTrue(accountingNotesValidator.isValid(accountingNotes));
		
		accountingNotes.sub(BigDecimal.valueOf(5000));
		Assert.assertFalse(accountingNotesValidator.isValid(accountingNotes));
		Assert.assertTrue("INVALID: cash negative".equals(accountingNotesValidator.toString()));
		
		accountingNotes.sub("abc", 140, BigDecimal.valueOf(3000));
		Assert.assertFalse(accountingNotesValidator.isValid(accountingNotes));
		Assert.assertTrue("INVALID: cash negative; abc negative".equals(accountingNotesValidator.toString()));

	}

}
