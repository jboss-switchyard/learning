package mortgages;

import org.apache.camel.builder.RouteBuilder;

public class CamelServiceRoute extends RouteBuilder {

	/**
	 * The Camel route is configured via this method.  The from:
	 * endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		from("switchyard://LoanProcessing")
        .log("Request for LoanProcessing : ${body}")
        .choice()
            .when(header("{urn:mortgages:1.0}status").isEqualTo("gold"))
                .to("bean://AutoApproval")
            .otherwise()
                .filter().simple("${body.creditScore} == 0")
                    .to("switchyard://CreditService")
                    .end()
                .to("switchyard://QualificationService")
                .end()
            .end()
        .log("Result of LoanProcessing: ${body}");
	}

}
