package org.jboss.example.homeloan.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Applicant implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date dob;
    private int creditScore;
    private String firstName = "";
    private String lastName = "";
    private String ssn;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String state;
    private double checkingBalance;
    private double savingsBalance;

    /**
     * @return the calculated age from the DOB
     */
    public int getAge() {

        final Calendar dob = Calendar.getInstance();
        dob.setTime(getDob());
        final Calendar now = Calendar.getInstance();

        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        // IF THE CURRENT MONTH IS LESS THAN THE DOB MONTH
        // THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
        // BIRTHDAY YET THIS YEAR
        if (now.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age = age - 1;
        }

        // IF THE MONTH IN THE DOB IS EQUAL TO THE CURRENT MONTH
        // THEN CHECK THE DAY TO FIND OUT IF THEY HAVE HAD THEIR
        // BIRTHDAY YET. IF THE CURRENT DAY IS LESS THAN THE DAY OF THE DOB
        // THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
        // BIRTHDAY YET THIS YEAR
        if ((now.get(Calendar.MONTH) == dob.get(Calendar.MONTH)) && (now.get(Calendar.DATE) < dob.get(Calendar.DATE))) {
            age = age - 1;
        }

        // THE AGE VARIBALE WILL NOW CONTAIN THE CORRECT AGE
        // DERIVED FROMTHE GIVEN DOB

        return age;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob
     *            the dob to set
     */
    public void setDob(final Date dob) {
        this.dob = dob;
    }

    /**
     * @return the creditScore
     */
    public int getCreditScore() {
        return creditScore;
    }

    /**
     * @param creditScore
     *            the creditScore to set
     */
    public void setCreditScore(final int creditScore) {
        this.creditScore = creditScore;
    }

    /**
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the name to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the name to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    

    public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public double getCheckingBalance() {
		return checkingBalance;
	}

	public void setCheckingBalance(double checkingBalance) {
		this.checkingBalance = checkingBalance;
	}
	

	public double getSavingsBalance() {
		return savingsBalance;
	}

	public void setSavingsBalance(double savingsBalance) {
		this.savingsBalance = savingsBalance;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": Name=" + getFirstName() + " " + getLastName() 
        		+ " Age=" + getAge() + " CreditScore=" + creditScore + " StreetAddress="
                + streetAddress  + " SSN=" + ssn;
    }


	public String getSsn() {
		return ssn;
	}


	public Applicant setSsn(String ssn) {
		this.ssn = ssn;
		return this;
	}

}
