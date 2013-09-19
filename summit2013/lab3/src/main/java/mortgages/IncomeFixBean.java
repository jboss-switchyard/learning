package mortgages;

import org.switchyard.component.bean.Service;

@Service(value = IncomeAdjustment.class, name = "IncomeFix")
public class IncomeFixBean implements IncomeAdjustment {

	@Override
	public Applicant adjust(Applicant applicant) {
		double incomeFloor = applicant.getLoanAmount() / 3;
        if (applicant.getIncome() < incomeFloor) {
        	System.out.println("Adjusting income to guarantee approval");
        	applicant.setIncome(incomeFloor + 1);
        }
        return applicant;
	}

}
