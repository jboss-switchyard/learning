package mortgages;

import org.switchyard.component.test.mixins.http.HTTPMixIn;

/**
 * Simple client to send a SOAP message.
 */
public final class CreditServiceClient {

    private static final String URL = "http://localhost:8080/CreditProxy/CreditService";
    private static final String GOOD_XML = "src/test/resources/xml/income-good.xml";
    private static final String NOT_SO_GOOD_XML = "src/test/resources/xml/income-not-so-good.xml";


    /**
     * Only execution point for this application.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] ignored) throws Exception {

        HTTPMixIn soapMixIn = new HTTPMixIn();
        soapMixIn.initialize();

        try {
        	System.out.println("Sending a batch of applications ...");
        	for (int i = 0; i < 50; i++) {
        		soapMixIn.postFile(URL, GOOD_XML);
        	}
        	for (int i = 0; i < 50; i++) {
        		soapMixIn.postFile(URL, NOT_SO_GOOD_XML);
        	}
            System.out.println("Application submission complete.");
        } finally {
            soapMixIn.uninitialize();
        }
    }
}
