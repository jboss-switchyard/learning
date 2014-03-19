package org.jboss.example.homeloan.extra;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.bpm.config.model.BPMComponentImplementationModel;
import org.switchyard.component.bpm.config.model.v1.V1BPMComponentImplementationModel;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.test.BeforeDeploy;
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

	private CDIMixIn cdiMixIn;
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
	
	@BeforeDeploy
	public void disableBPMPersistence() {
		for (ComponentModel component : cdiMixIn.getTestKit().getConfigModel().getComposite().getComponents()) {
			if (component.getImplementation() instanceof BPMComponentImplementationModel) {
				V1BPMComponentImplementationModel bpm = (V1BPMComponentImplementationModel)component.getImplementation();
				bpm.setPersistent(false);
			}
		}
	}

}
