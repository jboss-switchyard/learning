package org.jboss.example.homeloan.status;

import org.jboss.example.homeloan.data.Qualification;

public interface StatusService {

	Qualification status(String ssn);
}
