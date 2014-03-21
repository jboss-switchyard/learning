package org.jboss.example.homeloan;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
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
	@ServiceOperation("IntakeService")
	private Invoker intake;
	
	@Test
	public void test() {
		MockHandler intakeHandler = testKit.replaceService("IntakeService");
		intake.inputType(APPLICATION_TYPE).sendInOnly(testKit.readResourceString("Victor.xml"));
		Assert.assertEquals(1, intakeHandler.getMessages().size());
	}
}
