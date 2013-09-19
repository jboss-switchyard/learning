package mortgages;

import javax.inject.Named;

@Named("AutoApproval")
public class AutoApproval {

    public void approve(Applicant applicant) {
        applicant.setApproved(true);
    }
}
