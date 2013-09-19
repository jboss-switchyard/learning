package mortgages;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Applicant implements Serializable {
    private static final long serialVersionUID = 1L;

    private int age;
    private Date applicationDate;
    private int creditScore;
    private String name;
    private boolean approved;

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age age to set
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * @return the applicationDate
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * @param applicationDate
     *            the applicationDate to set
     */
    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
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
    public void setApproved(final boolean approved) {
        this.approved = approved;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": Name=" + getName() + " Age=" + getAge() + " CreditScore=" + creditScore + " ApplicationDate="
                + applicationDate + " Approved=" + approved;
    }

}
