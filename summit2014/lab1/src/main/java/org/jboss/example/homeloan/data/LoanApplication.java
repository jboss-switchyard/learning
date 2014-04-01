package org.jboss.example.homeloan.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LoanApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	private int amount;
	private boolean approved;
	private int deposit;
	private String approvedRate;
	private int lengthYears;
	private String explanation;
	private int insuranceCost;
	private Applicant applicant;
	private Bankruptcy bankruptcy;
	private IncomeSource income;

	public LoanApplication() {
		this.setApproved(false);
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * @param approved
	 *            the approved to set
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return the deposit
	 */
	public int getDeposit() {
		return deposit;
	}

	/**
	 * @param deposit
	 *            the deposit to set
	 */
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	/**
	 * @return the approvedRate
	 */
	public String getApprovedRate() {
		return approvedRate;
	}

	/**
	 * @param approvedRate
	 *            the approvedRate to set
	 */
	public void setApprovedRate(String approvedRate) {
		this.approvedRate = approvedRate;
	}

	/**
	 * @return the lengthYears
	 */
	public int getLengthYears() {
		return lengthYears;
	}

	/**
	 * @param lengthYears
	 *            the lengthYears to set
	 */
	public void setLengthYears(int lengthYears) {
		this.lengthYears = lengthYears;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return the insuranceCost
	 */
	public int getInsuranceCost() {
		return insuranceCost;
	}

	/**
	 * @param insuranceCost
	 *            the insuranceCost to set
	 */
	public void setInsuranceCost(int insuranceCost) {
		this.insuranceCost = insuranceCost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": Approved=" + this.approved
				+ " Explanation=" + this.explanation + " Amount=" + this.amount
				+ " Deposit=" + this.deposit + " ApprovedRate="
				+ this.approvedRate + " lengthYears=" + this.lengthYears
				+ " InsuranceCost=" + this.insuranceCost
				+ " Applicant=" + applicant;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public Bankruptcy getBankruptcy() {
		return bankruptcy;
	}

	public void setBankruptcy(Bankruptcy bankruptcy) {
		this.bankruptcy = bankruptcy;
	}

	public IncomeSource getIncome() {
		return income;
	}

	public void setIncome(IncomeSource income) {
		this.income = income;
	}
	
	public static LoanApplication fromElement(Element element) {
	    LoanApplication loanApp = parseApplication(element);
        IncomeSource income = parseIncome(getElement(element, "Employment"));
        Applicant applicant = parseApplicant(getElement(element, "Borrower"));
        
        if (income != null) {
            loanApp.setIncome(income);
        }
        if (applicant != null) {
            loanApp.setApplicant(applicant);
        }
        return loanApp;
	}
	
	private static final String APP_NS = 
            "http://jboss.com/demo/products/fsw/6.0/Application.xsd";
    
    private static LoanApplication parseApplication(Element element) {
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
    
    private static IncomeSource parseIncome(Element element) {
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
    
    private static Applicant parseApplicant(Element element) {
        if (element == null) {
            return null;
        }
        Applicant applicant = new Applicant();
        String dob = getElementValue(element, "DOB");
        if (dob != null) {
            try {
                applicant.setDob(new SimpleDateFormat("MM/dd/yyyy").parse(dob));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
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

    private static String getElementValue(Element parent, String elementName) {
        String value = null;
        NodeList nodes = parent.getElementsByTagNameNS(APP_NS, elementName);
        if (nodes.getLength() > 0) {
            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return value;
    }
    
    private static Element getElement(Element parent, String elementName) {
        Element element = null;
        NodeList nodes = parent.getElementsByTagNameNS(APP_NS, elementName);
        if (nodes.getLength() > 0) {
            element = (Element)nodes.item(0);
        }
        return element;
    }
}
