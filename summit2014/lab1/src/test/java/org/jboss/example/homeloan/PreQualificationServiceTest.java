package org.jboss.example.homeloan;

import java.util.Calendar;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Qualification;
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
public class PreQualificationServiceTest {

	@ServiceOperation("PreQualificationService")
	private Invoker service;

	@Test
	public void testPrequalify() throws Exception {
		
		LoanApplication loan = new LoanApplication();
		Applicant applicant = new Applicant();
		applicant.setSsn("711-555-5555");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1981, 1, 1);
		applicant.setDob(calendar.getTime());
		loan.setApplicant(applicant);
		IncomeSource income = new IncomeSource();
		income.setSelfEmployed(false);
		loan.setAmount(15000);
		loan.setLengthYears(20);
		loan.setDeposit(1500);
		loan.setIncome(income);
		Qualification result = service.operation("qualify")
				.sendInOut(loan).getContent(Qualification.class);
		
		// validate the results
		Assert.assertEquals("Approved", result.getStatus());
	}
}
