package org.jboss.example.homeloan;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class, HTTPMixIn.class },
        exclude = "jms")
public class Step7Test {
    
    private static final String URL = "http://localhost:18080/homeloan/IntakeService";

    private HTTPMixIn restClient;
    private SwitchYardTestKit testKit;
    
    @Test
    public void sendREST() throws Exception {
        
    }
}
