package mortgages;

import javax.jms.MessageConsumer;
import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = {CDIMixIn.class, HornetQMixIn.class})
@RunWith(SwitchYardRunner.class)
public class T4_AuditJMSTest {
    
    private static final String AUDIT_QUEUE = "AuditQueue";

    @ServiceOperation("Audit")
    private Invoker service;

    private HornetQMixIn hornetQ;
    
    @Test
    public void sendTextMessageToJMSQueue() throws Exception {
    	// Build the applicant
		Applicant request = new Applicant();
		request.setName("Joan Jones");
		request.setCreditScore(650);
		request.setApproved(true);
		 
		// Invoke the audit service
		service.inputType(new QName("java:mortgages.Applicant")).sendInOnly(request);
		
        // Did the auditor publish the message to the audit queue?
        final MessageConsumer consumer = hornetQ.getJMSSession().createConsumer(
        		HornetQMixIn.getJMSQueue(AUDIT_QUEUE));
        Assert.assertNotNull(consumer.receive(1000));
    }
}
