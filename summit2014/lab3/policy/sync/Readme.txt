Synchronous Policy Enforcement
==============================

Example of a business policy that has been defined to run synchronously
within the execution environment. The decisions taken by this policy are immediately actionable, allowing
them to be used to block the business transaction. In this specific example, if the customer issues multiple
requests too quickly, the requests will be rejected, so providing a crude form of throttling.

You must also deploy the Order Management Application and Information Processor.

To deploy the quickstart, after the server has been started, run:

	mvn jboss-as:deploy

To undeploy the quickstart, run:

	mvn jboss-as:undeploy


