package org.jboss.example.homeloan;

import org.jboss.example.homeloan.data.Qualification;
import org.jboss.example.homeloan.prequal.LoanStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class, HTTPMixIn.class },
        exclude = "jms")
public class Step7Test {

    private static final String STATUS_MSG = "Approved from Test";
    private static final String STATUS_URL = "http://localhost:18081/loanstatus/123";

    private HTTPMixIn httpClient;
    
    @Test
    public void checkStatus() throws Exception {
        
        // Seed a qualification status
        Qualification qual = new Qualification();
        qual.setStatus(STATUS_MSG);
        LoanStatus.updateStatus("123", qual);
        
        // Check status
        String output = httpClient.sendString(STATUS_URL, "", HTTPMixIn.HTTP_GET);
        Assert.assertTrue(output.contains(STATUS_MSG));
    }
    
    @BeforeDeploy
    public void setProperties() {
        System.setProperty("org.switchyard.component.resteasy.standalone.port", "18081");
        System.setProperty("org.switchyard.component.resteasy.standalone.path", "");
    }
}
