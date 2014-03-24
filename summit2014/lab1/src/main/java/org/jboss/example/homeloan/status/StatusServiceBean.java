package org.jboss.example.homeloan.status;

import org.jboss.example.homeloan.data.Qualification;
import org.jboss.example.homeloan.prequal.LoanStatus;

public class StatusServiceBean implements StatusService {

	@Override
	public Qualification status(String ssn) {
		return LoanStatus.getStatus(ssn);
	}

}
