
package mortgages;

import javax.xml.ws.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.mixins.PropertyMixIn;
import org.switchyard.transform.config.model.TransformSwitchYardScanner;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        scanners = TransformSwitchYardScanner.class,
        mixins = {CDIMixIn.class, HTTPMixIn.class, PropertyMixIn.class})
public class FancyCreditTest {

    private HTTPMixIn httpMixIn;
    private PropertyMixIn properties;
    
    @BeforeDeploy
    public void setTestProperties() {
    	properties.set("testPort", Integer.valueOf(18080));
    }
    
    @Before
    public void setUp() throws Exception {
        Endpoint.publish("http://localhost:18080/FancyCredit", new CreditWebService());
    }

    @Test
    public void invokeOrderWebService() throws Exception {
        httpMixIn.postResource(
                "http://localhost:18080/loans/LoanProcessing", 
                "/xml/fancycredit.xml");
        Thread.sleep(1000);
    }
}

