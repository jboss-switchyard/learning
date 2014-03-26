package org.jboss.example.homeloan.credit;

import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.CreditInfo;
import org.switchyard.component.bean.Service;

@Service(CreditService.class)
public class CreditServiceBean implements CreditService {

	@Override
	public CreditInfo creditCheck(Applicant applicant) {
		String creditScore = "000";
		if (applicant != null && applicant.getSsn() != null) {
			creditScore = applicant.getSsn().substring(0, 3);
		}

		CreditInfo credit = new CreditInfo();
		credit.setApplicant(applicant);
		credit.setScore(Integer.parseInt(creditScore));
		return credit;
	}

}
