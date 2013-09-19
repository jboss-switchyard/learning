package mortgages;

import mortgages.Applicant;

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
		mixins = CDIMixIn.class)
public class QualificationTest {

    @ServiceOperation("QualificationService")
    private Invoker service;

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

}
