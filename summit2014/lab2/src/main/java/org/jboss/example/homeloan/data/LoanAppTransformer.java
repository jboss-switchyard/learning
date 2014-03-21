package org.jboss.example.homeloan.data;

import java.text.SimpleDateFormat;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class LoanAppTransformer {
	
	private static final String APP_NS = 
			"http://jboss.com/demo/products/soa-p/5.2/Application.xsd";

	@Transformer(from = "{http://jboss.com/demo/products/soa-p/5.2/Application.xsd}Application")
	public LoanApplication transformApplicationToLoanApplication(Element from) throws Exception {
		LoanApplication loanApp = parseApplication(from);
		IncomeSource income = parseIncome(getElement(from, "Employment"));
		Applicant applicant = parseApplicant(getElement(from, "Borrower"));
		
		if (income != null) {
			loanApp.setIncome(income);
		}
		if (applicant != null) {
			loanApp.setApplicant(applicant);
		}
		return loanApp;
		
	}
	
	private LoanApplication parseApplication(Element element) {
		LoanApplication app = new LoanApplication();
		String amount = getElementValue(element, "Amount");
		if (amount != null) {
			app.setAmount(Integer.parseInt(amount));
		}
		app.setApprovedRate(getElementValue(element, "Interest_Rate"));
		String months = getElementValue(element, "Number_Of_Months");
		if (months != null) {
			app.setLengthYears(Integer.parseInt(months) / 12);
		}
		Element cashDeposit = getElement(element, "Cash_Deposit");
		if (cashDeposit != null) {
			String depositAmount = getElementValue(cashDeposit, "Amount");
			if (amount != null) {
				app.setDeposit(Integer.parseInt(depositAmount));
			}
		}
		
		return app;
	}
	
	private IncomeSource parseIncome(Element element) {
		if (element == null) {
			return null;
		}
		IncomeSource income = new IncomeSource();
		String monthlyAmount = getElementValue(element, "Monthly_Income");
		if (monthlyAmount != null) {
			income.setMonthlyAmount(Integer.parseInt(monthlyAmount));
		}
		String selfEmployed = getElementValue(element, "Is_Self_Employed");
		income.setSelfEmployed(Boolean.parseBoolean(selfEmployed));
		return income;
	}
	
	private Applicant parseApplicant(Element element) throws Exception {
		if (element == null) {
			return null;
		}
		Applicant applicant = new Applicant();
		String dob = getElementValue(element, "DOB");
		if (dob != null) {
			applicant.setDob(new SimpleDateFormat("MM/dd/yyyy").parse(dob));
		}
		applicant.setFirstName(getElementValue(element, "First_Name"));
		applicant.setLastName(getElementValue(element, "Last_Name"));
		applicant.setStreetAddress(getElementValue(element, "Address_1"));
		applicant.setPostalCode(getElementValue(element, "Postal_Code"));
		applicant.setCity(getElementValue(element, "City"));
		applicant.setState(getElementValue(element, "State"));
		applicant.setSsn(getElementValue(element, "SSN"));
		return applicant;
		
	}

    private String getElementValue(Element parent, String elementName) {
        String value = null;
        NodeList nodes = parent.getElementsByTagNameNS(APP_NS, elementName);
        if (nodes.getLength() > 0) {
            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return value;
    }
    
    private Element getElement(Element parent, String elementName) {
        Element element = null;
        NodeList nodes = parent.getElementsByTagNameNS(APP_NS, elementName);
        if (nodes.getLength() > 0) {
            element = (Element)nodes.item(0);
        }
        return element;
    }
}
