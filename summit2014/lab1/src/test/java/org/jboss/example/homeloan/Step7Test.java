package org.jboss.example.homeloan;

import java.util.Calendar;

import junit.framework.Assert;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;
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
public class Step7Test {

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
        
        service.operation("intake").sendInOnly(createApplication());
        
        // validate that our downstream service references were called
        Assert.assertEquals(1, lookUpService.getMessages().size());
        Assert.assertEquals(1, preQualService.getMessages().size());
    }
    
    private LoanApplication createApplication() {
        LoanApplication loan = new LoanApplication();
        Applicant applicant = new Applicant();
        applicant.setSsn("711-555-5555");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1981, 1, 1);
        applicant.setDob(calendar.getTime());
        loan.setApplicant(applicant);
        IncomeSource income = new IncomeSource();
        income.setSelfEmployed(false);
        loan.setAmount(15000);
        loan.setLengthYears(20);
        loan.setDeposit(1500);
        loan.setIncome(income);
        return loan;
    }
}
