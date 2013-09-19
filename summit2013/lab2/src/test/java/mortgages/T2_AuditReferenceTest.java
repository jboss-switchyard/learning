package mortgages;

import org.junit.Assert;
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
		exclude = "jms")
public class T2_AuditReferenceTest {

	@ServiceOperation("QualificationService")
	private Invoker service;
	
	private SwitchYardTestKit testKit;

	@Test
	public void testQualify() throws Exception {
		// Build the request object
		Applicant request = new Applicant();
        request.setName("Joan Jones");
        request.setCreditScore(650);
        
        // Mock the service we are calling
        MockHandler mockService = testKit.registerInOnlyService("Audit");
        
        // Invoke the service
        service.operation("qualify").sendInOut(request);

		// Verify that the Audit service was called
        mockService.setWaitTimeout(200);
        mockService.waitForOKMessage();
		Assert.assertEquals(1, mockService.getMessages().size());
	}

}
