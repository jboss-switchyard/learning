package mortgages;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace="http://mortgages/")
public class CreditWebService {

    @WebMethod()
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "applicant")
    public Applicant assignScore(@WebParam(name = "applicant") Applicant applicant) {
    	int creditScore = applicant.getName().length() * 50;
        applicant.setCreditScore(creditScore);
        
        System.out.println("CreditWebService :: assigned score of " 
        		+ creditScore + " for " + applicant.getName());

        return applicant;
    }
}