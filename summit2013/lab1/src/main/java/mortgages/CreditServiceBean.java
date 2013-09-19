package mortgages;

import org.switchyard.component.bean.Service;

@Service(CreditService.class)
public class CreditServiceBean implements CreditService {
	
	@Override
    public Applicant assignScore(Applicant applicant) {
        int score = applicant.getAge() * 20; 
        applicant.setCreditScore(score);
        return applicant;
    }
}
