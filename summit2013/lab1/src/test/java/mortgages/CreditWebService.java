package mortgages;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import mortgages.Applicant;

@WebService(targetNamespace="http://lab2.mortgages/")
public class CreditWebService {

    @WebMethod()
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "applicant")
    public Applicant assignScore(@WebParam(name = "applicant") Applicant applicant) {
        System.out.println("==========> INSIDE CreditWebService");
        int creditScore = applicant.getName().length() * 50;
        applicant.setCreditScore(creditScore);
        return applicant;
    }
}
