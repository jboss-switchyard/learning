package org.jboss.example.homeloan.data;

import java.io.Serializable;

public class IncomeSource implements Serializable {
    private static final long serialVersionUID = 1L;

    private int monthlyAmount;
    private boolean selfEmployed;

    /**
     * @return the monthlyAmount
     */
    public int getMonthlyAmount() {
        return monthlyAmount;
    }

    /**
     * @param monthlyAmount
     *            the monthlyAmount to set
     */
    public void setMonthlyAmount(final int monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    /**
     * @return the selfEmployed
     */
    public boolean isSelfEmployed() {
        return selfEmployed;
    }

    /**
     * @param selfEmployed
     *            the selfEmployed to set
     */
    public void setSelfEmployed(final boolean selfEmployed) {
        this.selfEmployed = selfEmployed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": monthlyAmount=" + monthlyAmount + " selfEmployed=" + selfEmployed;
    }

}