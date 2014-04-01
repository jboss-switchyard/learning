package org.jboss.example.homeloan;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;
import org.jboss.example.homeloan.data.LoanApplication;
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
        exclude = {"jms", "sql"})
public class Step1Test {

    @ServiceOperation("CreditService")
    private Invoker creditService;
    
    @ServiceOperation("LoanEvaluationService")
    private Invoker loanEvalService;
    
    
    @Test
    public void checkCredit() throws Exception {

        Applicant applicant = new Applicant().setSsn("555-123-4567");
        CreditInfo credit = creditService
                .sendInOut(applicant)
                .getContent(CreditInfo.class);
        
        Assert.assertNotNull(credit);
        Assert.assertEquals(555, credit.getScore());
    }
    
    @Test
    public void qualifyLoan() throws Exception {
        
        LoanApplication loan = MockApplication.good();
        // Invoke service
        loanEvalService
            .operation("preQualify")
            .sendInOut(loan);

        // validate the results
        Assert.assertTrue(loan.isApproved());
        Assert.assertEquals(0, loan.getInsuranceCost());
    }
}
