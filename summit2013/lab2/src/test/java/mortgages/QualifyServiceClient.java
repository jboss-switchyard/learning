package mortgages;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;

public class QualifyServiceClient {

    private static final String REQUEST_QUEUE = "QualifyRequest";
    private static final String RESPONSE_QUEUE = "QualifyResponse";
    
    private static final String USER = "guest";
    private static final String PASSWD = "guest-12";
    
    public static void main(final String[] args) throws Exception {
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                                    .setUser(USER)
                                    .setPassword(PASSWD);
        
        hqMixIn.initialize();

        try {
        	// JMS Client Setup
            Session session = hqMixIn.createJMSSession();
            MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(REQUEST_QUEUE));
            
            // Send the request message
            ObjectMessage request = (ObjectMessage)
            		hqMixIn.createJMSMessageFromResource("xml/applicant-before.xml");
            producer.send(request);
            System.out.println("Sent message [\n" + request.getObject() + "\n]");
            
            // Wait for response
            MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(RESPONSE_QUEUE));
            TextMessage response = (TextMessage)consumer.receive(1000);
            if (response != null) {
            	System.out.println("Received Response [\n" + response.getText() + "\n]");
            } else {
            	System.out.println("No response received from " + RESPONSE_QUEUE);
            }
        } finally {
            hqMixIn.uninitialize();
        }
    }

}
