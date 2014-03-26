package org.jboss.example.homeloan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.Customer;
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
        exclude = "jms")
public class Step4Test {

    @ServiceOperation("IntakeService")
    private Invoker service;
    
    private SwitchYardTestKit testKit;
    
    @Test
    public void customerUpdate() throws Exception {

        MockHandler lookup = new MockHandler().replyWithOut(createCustomer());
        testKit.replaceService("CustomerLookup", lookup);
        testKit.replaceService("PreQualificationService");
        
        LoanApplication loan = createApplication();
        service.operation("intake").sendInOnly(loan);
        
        // validate the results
        Assert.assertEquals("William", loan.getApplicant().getFirstName());
    }
    
    private List<Customer> createCustomer() {
        Customer customer = new Customer();
        customer.setSsn("711-555-5555");
        customer.setFirstName("William");
        customer.setLastName("Lumbergh");
        customer.setStreetAddress("PO Box 100");
        customer.setPostalCode("11111");
        customer.setCheckingBalance(1234.56);
        customer.setSavingsBalance(6543.21);
        List<Customer> customerList = new ArrayList<Customer>();
        customerList.add(customer);
        return customerList;
    }
    
    private LoanApplication createApplication() {
        LoanApplication loan = new LoanApplication();
        Applicant applicant = new Applicant();
        applicant.setFirstName("Bill");
        applicant.setLastName("Lumbergh");
        applicant.setStreetAddress("PO Box 100");
        applicant.setPostalCode("11111");
        applicant.setSsn("711-555-5555");
        applicant.setCheckingBalance(1234.56);
        applicant.setSavingsBalance(6543.21);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1981, 1, 1);
        applicant.setDob(calendar.getTime());
        loan.setApplicant(applicant);
        IncomeSource income = new IncomeSource();
        income.setSelfEmployed(false);
        income.setMonthlyAmount(5000);
        loan.setAmount(15000);
        loan.setLengthYears(20);
        loan.setDeposit(1500);
        loan.setIncome(income);
        return loan;
    }
}
