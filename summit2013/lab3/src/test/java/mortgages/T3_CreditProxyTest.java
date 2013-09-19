
package mortgages;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;
import org.switchyard.transform.config.model.TransformSwitchYardScanner;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        scanners = TransformSwitchYardScanner.class,
        mixins = {CDIMixIn.class, HTTPMixIn.class})
public class T3_CreditProxyTest {

    private HTTPMixIn httpMixIn;
    private SwitchYardTestKit testKit;

    @Test
    public void invokeOrderWebService() throws Exception {
    	MockHandler creditService = testKit.replaceService("CreditService");
    	httpMixIn.setDumpMessages(true);
        httpMixIn.postResource(
                "http://localhost:18080/CreditProxy/CreditService", 
                "/xml/credit-request.xml");
        
        creditService.setWaitTimeout(400);
        creditService.waitForOKMessage();
        Assert.assertEquals(1, creditService.getMessages().size());
    }
}

