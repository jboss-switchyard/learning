package org.jboss.example.homeloan.prequal;

import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Qualification;

public interface PreQualificationService {

	Qualification prequalify(LoanApplication application);
}
