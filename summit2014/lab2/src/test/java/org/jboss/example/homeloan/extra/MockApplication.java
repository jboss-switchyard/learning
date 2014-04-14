package org.jboss.example.homeloan.extra;

import java.util.Calendar;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;

public class MockApplication {
    
    public static LoanApplication good() {
        return createApplication();
    }
    
    public static LoanApplication bad() {
        LoanApplication app = createApplication();
        app.getApplicant().setSsn("580-555-1234");
        return app;
    }

    public static LoanApplication createApplication() {
        // Build loan application
        LoanApplication loan = new LoanApplication();
        Applicant applicant = new Applicant();
        applicant.setSsn("711-555-5555");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1981, 1, 1);
        applicant.setDob(calendar.getTime());
        applicant.setCreditScore(711);
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
