package org.jboss.example.homeloan.lookup;

import org.jboss.example.homeloan.data.Customer;

public interface CustomerLookup {

	Customer lookup(String ssn);
}
