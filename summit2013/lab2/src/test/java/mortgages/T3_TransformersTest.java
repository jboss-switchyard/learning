package mortgages;

import javax.xml.namespace.QName;

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
		mixins = CDIMixIn.class, 
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
		exclude = "jms")
public class T3_TransformersTest {

	@ServiceOperation("QualificationService")
	private Invoker service;
	
	private SwitchYardTestKit testKit;

	@Test
	public void applicantTransformsXML() throws Exception {
		// The payload type name for an XML applicant
		QName appType = new QName("urn:lab2:1.0", "applicant");
		// The applicant XML that needs to be transformed
		String request = testKit.readResourceString("xml/applicant-before.xml");
		// Invoke the service, trigger input and output transformers
		String result = service
				.inputType(appType)
				.expectedOutputType(appType)
				.sendInOut(request).getContent(String.class);
		
		// Verify our output was transformed correctly
		testKit.compareXMLToResource(result, "xml/applicant-after.xml");
	}

    @Before
    public void setUp() throws Exception {
    	// mock the audit service so this test doesn't fail once
    	// the invocation is added the bean
    	testKit.registerInOnlyService("Audit");
    }

}
