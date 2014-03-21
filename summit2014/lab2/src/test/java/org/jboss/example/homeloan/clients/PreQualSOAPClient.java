package org.jboss.example.homeloan.clients;

import org.switchyard.component.test.mixins.http.HTTPMixIn;

public class PreQualSOAPClient {
	
	private static final String URL = "http://localhost:8080/homeloan/IntakeService";
    private static final String SUFFIX = "-soap.xml";
    private static final String PREFIX = "src/test/resources/";

    
    public static void main(final String[] args) throws Exception {

        HTTPMixIn soapMixIn = new HTTPMixIn();
        soapMixIn.initialize();
        
        String request = PREFIX + "Tina" + SUFFIX;
        if (args.length == 1) {
        	request = PREFIX + args[0] + SUFFIX;
        }

        try {
        	soapMixIn.setDumpMessages(true);
            soapMixIn.postFile(URL, request);
        } finally {
            soapMixIn.uninitialize();
        }
    }
}
