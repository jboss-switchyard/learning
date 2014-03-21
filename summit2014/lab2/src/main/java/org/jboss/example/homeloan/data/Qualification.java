package org.jboss.example.homeloan.data;

import java.io.Serializable;

public class Qualification implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String rate;
	private String explanation;
	private int cost;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRate() {
		return rate;
	}
	
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	public String getExplanation() {
		return explanation;
	}
	
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String toString() {
		return "Qualification :: " 
				+ "status=" + status 
				+ "|rate=" + rate
				+ "|explanation=" + explanation
				+ "|cost=" + cost;
				
	}
}
