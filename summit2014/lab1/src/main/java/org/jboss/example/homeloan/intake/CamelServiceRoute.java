package org.jboss.example.homeloan.intake;

import org.apache.camel.builder.RouteBuilder;

public class CamelServiceRoute extends RouteBuilder {

    public void configure() {
        from("switchyard://IntakeService")
            .setProperty("LoanApplication").simple("${body}")
            .setBody().simple("${body.applicant.ssn}")
            .to("switchyard://CustomerLookup")
            // BEGIN - additional routing logic

            // END - additional routing logic
            .setBody().property("LoanApplication")
            .to("switchyard://PreQualificationService");
    }

}
