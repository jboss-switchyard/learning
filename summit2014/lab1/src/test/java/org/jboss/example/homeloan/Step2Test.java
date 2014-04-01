package org.jboss.example.homeloan;

import org.jboss.example.homeloan.data.Qualification;
import org.jboss.example.homeloan.extra.MockApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class },
        exclude = "jms")
public class Step2Test {

    @ServiceOperation("PreQualificationService")
    private Invoker service;
    
    @Test
    public void existingCustomer() throws Exception {
        
        Qualification result = service
                .operation("qualify")
                .property("existingcustomer", true)
                .sendInOut(MockApplication.good())
                .getContent(Qualification.class);
        
        // validate the results
        Assert.assertEquals("Approved", result.getStatus());
    }
    
    @Test
    public void newCustomer() throws Exception {
        
        Qualification result = service
                .operation("qualify")
                .sendInOut(MockApplication.bad())
                .getContent(Qualification.class);
        
        // validate the results
        Assert.assertEquals("Rejected", result.getStatus());
    }
}
