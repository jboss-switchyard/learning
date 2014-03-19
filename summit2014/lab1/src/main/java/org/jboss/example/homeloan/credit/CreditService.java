package org.jboss.example.homeloan.credit;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;


public interface CreditService {

	CreditInfo creditCheck(Applicant applicant);
}
