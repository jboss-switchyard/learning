package mortgages;

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
		mixins = CDIMixIn.class, 
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
		exclude = "soap")
public class T2_IncomeFixRoutedTest {

	@ServiceOperation("CreditService")
	private Invoker service;
	
	private SwitchYardTestKit testKit;

	@Test
	public void routeToFancyCredit() throws Exception {
		String request = testKit.readResourceString("xml/income-none.xml");
		testKit.registerInOutService("FancyCredit"); 
		MockHandler incomeFix = testKit.replaceService("IncomeFix"); 
		
		service.operation("assignScore").sendInOut(request);
		
		incomeFix.setWaitTimeout(300);
		incomeFix.waitForOKMessage();
		Assert.assertEquals(1, incomeFix.getMessages().size());
	}

}
