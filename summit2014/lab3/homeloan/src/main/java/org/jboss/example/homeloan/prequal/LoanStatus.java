package org.jboss.example.homeloan.prequal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Qualification;

public class LoanStatus {

	// Recipe for memory leak:
	// 1 - create static map with strong references
	// 2 - never clean up
	private static Map<String, LoanApplication> applications = 
			new ConcurrentHashMap<String, LoanApplication>();
	
	private static Map<String, Qualification> qualifications = 
			new ConcurrentHashMap<String, Qualification>();
	
	public static void addApplication(String ssn, LoanApplication application) {
		applications.put(ssn, application);
	}
	
	public static void updateStatus(String ssn, Qualification qualification) {
		qualifications.put(ssn, qualification);
	}
	
	public static Qualification getStatus(String ssn) {
		return qualifications.get(ssn);
	}
}
