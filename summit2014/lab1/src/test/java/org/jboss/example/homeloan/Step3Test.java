package org.jboss.example.homeloan;

import org.jboss.example.homeloan.extra.MockApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class },
        exclude = {"jms", "sql"})
public class Step3Test {

    @ServiceOperation("IntakeService")
    private Invoker service;
    
    private SwitchYardTestKit testKit;
    
    @Test
    public void checkStatus() throws Exception {

        // Mock providers for services called from IntakeService
        MockHandler lookUpService = testKit
            .registerInOutService("CustomerLookup")
            .replyWithOut(null);
        MockHandler preQualService = testKit
            .replaceService("PreQualificationService")
            .forwardInToOut();
        
        service
            .operation("intake")
            .sendInOnly(MockApplication.good());
        
        // validate that our downstream service references were called
        Assert.assertEquals(1, lookUpService.getMessages().size());
        Assert.assertEquals(1, preQualService.getMessages().size());
    }
    
}
