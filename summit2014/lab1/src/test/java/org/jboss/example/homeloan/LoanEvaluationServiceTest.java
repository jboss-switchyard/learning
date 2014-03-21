package org.jboss.example.homeloan;

import java.util.Calendar;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.Bankruptcy;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { CDIMixIn.class },
		exclude = "jms")
public class LoanEvaluationServiceTest {

	@ServiceOperation("LoanEvaluationService")
	private Invoker service;

	@Test
	public void underageApplicant() throws Exception {
		// Build loan application
		LoanApplication loan = new LoanApplication();
		Applicant applicant = new Applicant();
		applicant.setFirstName("George");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1998, 1, 1);
		applicant.setDob(calendar.getTime());
		loan.setApproved(true);
		loan.setApplicant(applicant);
		
		// Invoke service
		service.operation("preQualify").sendInOut(loan);

		// validate the results
		Assert.assertFalse(loan.isApproved());
		Assert.assertEquals("Applicant is underage", loan.getExplanation());
	}

	@Test
	public void bankruptcyRecent() throws Exception {
		// Build loan application
		LoanApplication loan = new LoanApplication();
		Bankruptcy bankruptcy = new Bankruptcy();
		bankruptcy.setYearOfOccurrence(2010);
		loan.setBankruptcy(bankruptcy);
		
		// Invoke service
		service.operation("preQualify").sendInOut(loan);

		// validate the results
		Assert.assertFalse(loan.isApproved());
		Assert.assertEquals("has been bankrupt", loan.getExplanation());
	}
	
	/**
	 * Rule under test:
	 *   rule "Row 1 Pricing loans" 
	 */
	@Test
	public void row1Loan() throws Exception {
		// Build loan application
		LoanApplication loan = new LoanApplication();
		Applicant applicant = new Applicant();
		applicant.setSsn("711-555-5555");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1981, 1, 1);
		applicant.setDob(calendar.getTime());
		applicant.setCreditScore(711);
		loan.setApplicant(applicant);
		IncomeSource income = new IncomeSource();
		income.setSelfEmployed(false);
		loan.setAmount(15000);
		loan.setLengthYears(20);
		loan.setDeposit(1500);
		loan.setIncome(income);
		
		// Invoke service
		service.operation("preQualify").sendInOut(loan);

		// validate the results
		Assert.assertTrue(loan.isApproved());
		Assert.assertEquals(0, loan.getInsuranceCost());
	}
	
}
