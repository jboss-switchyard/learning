package org.jboss.example.homeloan;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;
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
public class CreditServiceTest {

	@ServiceOperation("CreditService")
	private Invoker service;

	@Test
	public void testAssignScore() throws Exception {
		
		Applicant applicant = new Applicant();
		applicant.setSsn("789-123-4567");
		CreditInfo result = service.operation("assignScore").sendInOut(applicant)
				.getContent(CreditInfo.class);

		// validate the results
		Assert.assertEquals(789, result.getScore());
	}

}
