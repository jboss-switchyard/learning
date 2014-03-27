package org.jboss.example.homeloan.data;


public class CreditInfo {
	
	private Applicant applicant;
	private int score;

	public int getScore() {
		return score;
	}

	public CreditInfo setScore(int score) {
		this.score = score;
		return this;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public CreditInfo setApplicant(Applicant applicant) {
		this.applicant = applicant;
		return this;
	}
	
}
