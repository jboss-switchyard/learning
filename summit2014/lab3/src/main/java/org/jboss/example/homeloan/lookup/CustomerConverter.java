package org.jboss.example.homeloan.lookup;

import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.jboss.example.homeloan.data.Customer;

@Converter
public class CustomerConverter {
	
	
	@Converter
    public static Customer from(List<Map<String, Object>> objects) {
        Customer customer = new Customer();
        if (objects != null && !objects.isEmpty()) {
        	Map<String, Object> customerData = objects.get(0);
            customer.setFirstName((String)customerData.get("firstName"));
            customer.setLastName((String)customerData.get("lastName"));
            customer.setSsn((String)customerData.get("ssn"));
            customer.setDob(customerData.get("dob").toString());
            customer.setStreetAddress(customerData.get("streetaddress").toString());
            customer.setCity(customerData.get("city").toString());
            customer.setState(customerData.get("state").toString());
            customer.setPostalCode(customerData.get("postalcode").toString());
            customer.setCheckingBalance(customerData.get("checkingbalance").toString());
            customer.setSavingsBalance(customerData.get("savingsBalance").toString());
        }
        
        return customer;
    }
}
