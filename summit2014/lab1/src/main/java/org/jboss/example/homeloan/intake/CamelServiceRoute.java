package org.jboss.example.homeloan.intake;

import org.apache.camel.builder.RouteBuilder;

public class CamelServiceRoute extends RouteBuilder {

	public void configure() {
		from("switchyard://IntakeService")
			.setProperty("LoanApplication").simple("${body}")
			.setBody().simple("${body.applicant.ssn}")
			.to("switchyard://CustomerLookup")
			.filter(simple("${body} != null && ${body.size} > 0"))
					.beanRef("Loan", "customerUpdate(${property.LoanApplication}, ${body})")
					.setHeader("ExistingCustomer").constant(true)
			.end()
			.beanRef("Loan", "summary(${property.LoanApplication})")
			.setBody().property("LoanApplication")
			.to("switchyard://PreQualificationService");
	}

}
