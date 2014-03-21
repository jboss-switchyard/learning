package org.jboss.example.homeloan.eval;

import org.jboss.example.homeloan.data.LoanApplication;

public interface LoanEvaluationService {

	LoanApplication qualify(LoanApplication application);
}
