package org.jboss.example.homeloan;

import java.util.Calendar;

import junit.framework.Assert;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Qualification;
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
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = { CDIMixIn.class },
        exclude = "jms")
public class Step3Test {

    @ServiceOperation("PreQualificationService")
    private Invoker service;
    
    private SwitchYardTestKit testKit;
    
    @Test
    public void existingCustomer() throws Exception {
        
        Qualification result = service
                .operation("qualify")
                .property("existingcustomer", true)
                .sendInOut(createApplication())
                .getContent(Qualification.class);
        
        // validate the results
        Assert.assertEquals("Approved", result.getStatus());
    }
    
    @Test
    public void newCustomer() throws Exception {
        
        Qualification result = service
                .operation("qualify")
                .sendInOut(createApplication())
                .getContent(Qualification.class);
        
        // validate the results
        Assert.assertEquals("Rejected", result.getStatus());
    }
    

    @Before
    public void mockCreditService() {
        testKit
            .registerInOutService("CreditSerivce")
            .replyWithOut(new CreditInfo().setScore(300));
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
