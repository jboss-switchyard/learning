Service Level Agreement - Report
================================

This quickstart provides an example of a deployable report definition, configured to provide a Service
Level Agreement report.

For this quickstart to function correctly, activity events need to be recorded in the activity store.
Therefore initially it is recommended that you deploy the Order Management Application and
Information Processor quickstarts. However any SwitchYard application could be used.

To deploy the quickstart, after the server has been started, run:

	mvn jboss-as:deploy


To run the example, generate some activity (e.g. run some of the example requests in the ordermgmt/app
folder). Then using a REST client, issue the following GET:

http://localhost:8080/overlord-rtgov/report/generate?report=SLAReport&startDay=1&startMonth=1&startYear=2013&endDay=31&endMonth=12&endYear=2013&maxResponseTime=400&averagedDuration=450&calendar=Default

using appropriate basic authentication credentials, e.g. username 'admin' with the password specified during installation.

This will generate an example report. Current the report is based on all activities, but will eventually
be updated to filter based on service type and optionally operation name and principal. Please see
the quickstart guide for more information on this example, and the developer guide for information on
how to specify new reports.

To undeploy the quickstart, run:

	mvn jboss-as:undeploy


NOTE: This quickstart can be deployed with the profile(s): server

