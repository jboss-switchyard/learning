package org.jboss.example.homeloan.intake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.inject.Named;

import org.jboss.example.homeloan.data.Customer;
import org.jboss.example.homeloan.data.LoanApplication;

@Named("Loan")
public class LoanBean {

	public void customerUpdate(LoanApplication app, Customer customer) throws Exception {
		app.getApplicant().setFirstName(customer.getFirstName());
		app.getApplicant().setLastName(customer.getLastName());
		app.getApplicant().setDob(new SimpleDateFormat("yyyy-MM-dd").parse(customer.getDob()));
		app.getApplicant().setPostalCode(customer.getPostalCode());
		app.getApplicant().setStreetAddress(customer.getStreetAddress());
		app.getApplicant().setSavingsBalance(customer.getSavingsBalance());
		app.getApplicant().setCheckingBalance(customer.getCheckingBalance());
	}
	
	public void summary(LoanApplication app) {
		DecimalFormat df = new DecimalFormat("###,###.00");
		System.out.println(
				  "\n======== Loan Intake Summary ========="
				+ "\nSSN           : " + app.getApplicant().getSsn()
				+ "\nLoan Amount   : " + app.getAmount()
				+ "\nLoan Length   : " + app.getLengthYears()
				+ "\nIncome        : " + app.getIncome().getMonthlyAmount()
				+ "\nName          : " + app.getApplicant().getFirstName() + " " + app.getApplicant().getLastName()
				+ "\nAddress       : " + app.getApplicant().getStreetAddress()
				+ "\nPostal Code   : " + app.getApplicant().getPostalCode()
				+ "\nChecking Bal  : " + df.format(app.getApplicant().getCheckingBalance())
				+ "\nSavings Bal   : " + df.format(app.getApplicant().getSavingsBalance())
				+ "\n======================================\n");
	}
}
