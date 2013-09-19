package mortgages;

import org.switchyard.component.bean.Property;
import org.switchyard.component.bean.Service;

@Service(QualificationService.class)
public class QualificationServiceBean implements QualificationService {
    
    private static final String EASY = "easy";
    
    @Property(name = "creditTerms")
    private String creditTerms;

    @Override
    public Applicant qualify(Applicant applicant) {
        if (EASY.equals(creditTerms)) {
            applicant.setApproved(true);
        } else {
            applicant.setApproved(applicant.getCreditScore() >= 600);
        }
        
        return applicant;
    }

}