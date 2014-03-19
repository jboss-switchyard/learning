package org.jboss.example.homeloan;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.bpm.config.model.BPMComponentImplementationModel;
import org.switchyard.component.bpm.config.model.v1.V1BPMComponentImplementationModel;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { CDIMixIn.class },
		exclude = {"jms", "soap"})
public class ValidationTest {
	
	private static final QName APPLICATION_TYPE = new QName(
			"http://jboss.com/demo/products/soa-p/5.2/Application.xsd", "Application");
	
	private SwitchYardTestKit testKit;
	private CDIMixIn cdiMixIn;
	@ServiceOperation("IntakeService")
	private Invoker intake;
	
	@Test
	public void test() {
		MockHandler intakeHandler = testKit.replaceService("IntakeService");
		intake.inputType(APPLICATION_TYPE).sendInOnly(testKit.readResourceString("Victor.xml"));
		Assert.assertEquals(1, intakeHandler.getMessages().size());
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
