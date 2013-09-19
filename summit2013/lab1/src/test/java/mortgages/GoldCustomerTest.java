
package mortgages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.transform.config.model.TransformSwitchYardScanner;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        scanners = TransformSwitchYardScanner.class,
        mixins = {CDIMixIn.class, HTTPMixIn.class})
public class GoldCustomerTest {

    private HTTPMixIn httpMixIn;

    @Test
    public void invokeOrderWebService() throws Exception {
        
        httpMixIn.postResource(
                "http://localhost:18080/loans/LoanProcessing", 
                "/xml/loan-request-gold.xml");
    }
}

