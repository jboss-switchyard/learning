package org.jboss.example.homeloan;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import org.jboss.example.homeloan.data.LoanApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class, HornetQMixIn.class })
public class Step6Test {

    private static final String REQUEST_QUEUE = "LoanIntake";
    
    private HornetQMixIn hornetQ;
    private SwitchYardTestKit testKit;
    
    @Test
    public void sendTextMessageToJMSQueue() throws Exception {
        // Mock the qualify service to verify it's invoked
        MockHandler qualifyMock = testKit.replaceService("IntakeJMS");
        
        // Create and send a JMS message with the request XML payload
        MessageProducer producer = hornetQ.getJMSSession().createProducer(
                HornetQMixIn.getJMSQueue(REQUEST_QUEUE));
        ObjectMessage message = hornetQ.getJMSSession().createObjectMessage();
        message.setObject(new LoanApplication());
        producer.send(message);
        
        // Verify that we received the message in the service
        qualifyMock.waitForOKMessage();
        Assert.assertEquals(1, qualifyMock.getMessages().size());
    }
}
