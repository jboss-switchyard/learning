package org.jboss.example.homeloan.clients;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;

public class PreQualJMSClient {

    private static final String REQUEST_QUEUE = "LoanIntake";
    
    private static final String USER = "guest";
    private static final String PASSWD = "guestp.1";
    private static final String DEFAULT_REQUEST = "Tina";
    private static final String SUFFIX = ".xml";
    
    public static void main(final String[] args) throws Exception {
    	
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                                    .setUser(USER)
                                    .setPassword(PASSWD);
        
        hqMixIn.initialize();

        String requestFile = DEFAULT_REQUEST + SUFFIX;
        if (args.length == 1) {
        	requestFile = args[0] + SUFFIX;
        }

        try {
        	// JMS Client Setup
            Session session = hqMixIn.createJMSSession();
            MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(REQUEST_QUEUE));
            
            // Send the request message
            ObjectMessage request = (ObjectMessage)
            		hqMixIn.createJMSMessageFromResource(requestFile);
            producer.send(request);
            System.out.println("Sent message [\n" + request.getObject() + "\n]");
            
        } finally {
            hqMixIn.uninitialize();
        }
    }

}
