Service Level Agreement - Monitor
=================================

This quickstart provides an example of a RESTful service that internally accesses the Active Collection
mechanism to obtain service response time and situation information.

For this quickstart to function correctly, you must also deploy the Order Management Application and
Information Processor, as well as the SLA Event Processor Network.

To deploy the quickstart, after the server has been started, run:

	mvn jboss-as:deploy

To run the example, generate some situations (e.g. run the example 'order3' request in the ordermgmt/app
folder). Then using a REST client, issue the following GET:

http://localhost:8080/slamonitor-monitor/monitor/situations

This will return the situations currently available in the 'Situations' active collection. NOTE: These situations only remain in this collection for a short period of time, so no entries are returned, you will need to re-run the 'order3' request in the ordermgmt/app folder.

To undeploy the quickstart, run:

	mvn jboss-as:undeploy


NOTE: This quickstart can be deployed with the profile(s): server

