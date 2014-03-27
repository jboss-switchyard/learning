package org.jboss.example.homeloan.data;


public class Customer {

	private String firstName = "";
	private String lastName = "";
	private String ssn;
	private String dob;
	private String streetAddress;
	private String city;
	private String state;
	private String postalCode;
	private double checkingBalance;
	private double savingsBalance;

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

	public String getSsn() {
		return ssn;
	}

	public Customer setSsn(String ssn) {
		this.ssn = ssn;
		return this;
	}

	public String getDob() {
		return dob;
	}

	public Customer setDob(String dob) {
		this.dob = dob;
		return this;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public Customer setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Customer setCity(String city) {
		this.city = city;
		return this;
	}

	public String getState() {
		return state;
	}

	public Customer setState(String state) {
		this.state = state;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Customer setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public double getCheckingBalance() {
		return checkingBalance;
	}

	public Customer setCheckingBalance(double checkingBalance) {
		this.checkingBalance = checkingBalance;
		return this;
	}
	
	public Customer setCheckingBalance(String checkingBalance) {
		this.checkingBalance = Double.valueOf(checkingBalance);
		return this;
	}

	public double getSavingsBalance() {
		return savingsBalance;
	}

	public Customer setSavingsBalance(double savingsBalance) {
		this.savingsBalance = savingsBalance;
		return this;
	}

	public Customer setSavingsBalance(String savingsBalance) {
		this.savingsBalance = Double.valueOf(savingsBalance);
		return this;
	}

}
