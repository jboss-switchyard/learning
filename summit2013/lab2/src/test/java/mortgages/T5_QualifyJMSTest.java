package mortgages;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = {CDIMixIn.class, HornetQMixIn.class})
@RunWith(SwitchYardRunner.class)
public class T5_QualifyJMSTest {
    
    private static final String REQUEST_QUEUE = "QualifyRequest";
    private static final String RESPONSE_QUEUE = "QualifyResponse";

    private SwitchYardTestKit testKit;
    private HornetQMixIn hornetQ;
    
    @Test
    public void sendTextMessageToJMSQueue() throws Exception {
    	// Mock the qualify service to verify it's invoked
    	MockHandler qualifyMock = testKit.replaceService("QualificationService");
    	
    	// Create and send a JMS message with the request XML payload
        MessageProducer producer = hornetQ.getJMSSession().createProducer(
        		HornetQMixIn.getJMSQueue(REQUEST_QUEUE));
        TextMessage message = hornetQ.getJMSSession().createTextMessage();
        message.setText(testKit.readResourceString("xml/applicant-before.xml"));
        producer.send(message);
        
        // Verify that we received the message in the service
        qualifyMock.waitForOKMessage();
        Assert.assertEquals(1, qualifyMock.getMessages().size());
        
        // Did we get a reply?
        MessageConsumer consumer = hornetQ.getJMSSession().createConsumer(
        		HornetQMixIn.getJMSQueue(RESPONSE_QUEUE));
        Assert.assertNotNull(consumer.receive(1000));
    }
}
