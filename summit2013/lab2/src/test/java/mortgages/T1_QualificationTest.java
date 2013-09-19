package mortgages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
		mixins = CDIMixIn.class,
		exclude = "jms")
public class T1_QualificationTest {

    @ServiceOperation("QualificationService")
    private Invoker service;
    
    private SwitchYardTestKit testKit;
    
    @Test
    public void qualifySuccess() {
        Applicant request = new Applicant();
        request.setName("Joan Jones");
        request.setCreditScore(650);
        
        Applicant reply = service.operation("qualify").sendInOut(request)
                .getContent(Applicant.class);

        // validate the results
        Assert.assertTrue(reply.isApproved());
    }
    
    @Test
    public void qualifyFail() {
        Applicant request = new Applicant();
        request.setName("Bill Smith");
        request.setCreditScore(450);
        
        Applicant reply = service.operation("qualify").sendInOut(request)
                .getContent(Applicant.class);

        // validate the results
        Assert.assertFalse(reply.isApproved());
    }

    @Before
    public void setUp() throws Exception {
    	// mock the audit service so this test doesn't fail once
    	// the invocation is added the bean
    	testKit.registerInOnlyService("Audit");
    }

}
