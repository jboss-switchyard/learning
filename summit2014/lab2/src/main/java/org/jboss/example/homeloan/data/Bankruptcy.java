package org.jboss.example.homeloan.data;

import java.io.Serializable;

public class Bankruptcy implements Serializable {
    private static final long serialVersionUID = 1L;

    private int amountOwed;
    private int yearOfOccurrence;

    /**
     * @return the amountOwed
     */
    public int getAmountOwed() {
        return amountOwed;
    }

    /**
     * @param amountOwed
     *            the amountOwed to set
     */
    public void setAmountOwed(final int amountOwed) {
        this.amountOwed = amountOwed;
    }

    /**
     * @return the yearOfOccurrence
     */
    public int getYearOfOccurrence() {
        return yearOfOccurrence;
    }

    /**
     * @param yearOfOccurrence
     *            the yearOfOccurrence to set
     */
    public void setYearOfOccurrence(final int yearOfOccurrence) {
        this.yearOfOccurrence = yearOfOccurrence;
    }

}
