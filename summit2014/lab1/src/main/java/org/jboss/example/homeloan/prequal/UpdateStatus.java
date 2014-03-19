package org.jboss.example.homeloan.prequal;

import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Qualification;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class UpdateStatus implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		// Update loan status
		LoanApplication application = (LoanApplication)workItem.getParameter("application");
		Qualification qualification = (Qualification)workItem.getParameter("qualification");
		LoanStatus.updateStatus(application.getApplicant().getSsn(), qualification);
		
		// Print summary
		System.out.println(
				  "\n===== Prequalification Summary ======="
				+ "\nSSN            : " + application.getApplicant().getSsn()
				+ "\nName           : " + application.getApplicant().getFirstName() + " " + application.getApplicant().getLastName()
				+ "\nLoan Amount    : " + application.getAmount()
				+ "\nLoan Length    : " + application.getLengthYears()
				+ "\nStatus         : " + qualification.getStatus()
				+ "\nExplanation    : " + qualification.getExplanation()
				+ "\nInsurance Cost : " + qualification.getCost()
				+ "\nRate           : " + qualification.getRate()
				+ "\n======================================\n");
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// Nothing to do here
	}

}
