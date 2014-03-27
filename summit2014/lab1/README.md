####Required Environment

* JBDS 6/7 or Eclipse Kepler
* EAP 6.1
* SY 1.0.0 Tooling 
* SY 1.0.0.Final Runtime


####Runtime Installation

Installer for the runtime can be found [here.](http://www.redhat.com/j/elqNow/elqRedir.htm?ref=http://downloads.jboss.org/switchyard/releases/v1.0.Final/switchyard-installer-1.0.0.Final.zip)

Instructions [here.](https://docs.jboss.org/author/display/SWITCHYARD/Installing+SwitchYard)


####Database Setup

I used the H2 db embedded in EAP for my testing.  Feel free to choose another DB and document the process.

Launch the H2 db and console via the h2 jar in your EAP install:
```
java -jar modules/system/layers/base/com/h2database/h2/main/h2-1.3.168-redhat-2.jar 
```

In the console, create a table:
```
CREATE TABLE CUSTOMER(
    SSN VARCHAR(11) PRIMARY KEY,
    FIRSTNAME VARCHAR(50),
    LASTNAME VARCHAR(50),
    STREETADDRESS VARCHAR(255),
    CITY VARCHAR(60),
    STATE VARCHAR(2),
    POSTALCODE VARCHAR(60),
    DOB DATE,
    CHECKINGBALANCE DECIMAL(14,2),
    SAVINGSBALANCE DECIMAL(14,2));
```

And insert some test data:
```
INSERT INTO CUSTOMER VALUES 
    ('800559876', 'Joe', 'Deeppockets-existing', '345 Pine Ave', 'Springfield', 'MO', '65810', '1966-07-04', 14000.40, 22000.99);
INSERT INTO CUSTOMER VALUES 
    ('610761010', 'Sally', 'Shortchange-existing', '456 Larch Lane', 'Springfield', 'MA', '99999', '1966-08-05', 9100.10, 2750.75);
INSERT INTO CUSTOMER VALUES 
    ('680777098', 'Barbara', 'Borderline-existing', '567 Poplar Pkwy', 'Worcester', 'MA', '01604', '1976-09-06', 300.41, 11.01);
    
```

Add the following datasource definition to standalone-full.xml:
```
<datasource jndi-name="java:jboss/datasources/CustomerDS" pool-name="CustomerDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:h2:tcp://localhost/~/test</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
</datasource>
```


####Server Setup

Add a JMS user:
```
${EAP_HOME}/bin/add-user.sh
  - Application User
  - Application Realm
  - Username : guest
  - Password : guestp.1
  - Roles : guest
```
  
Start the server:
```
bin/standalone.sh -c standalone-full.xml
```

In a separate terminal window, add a JMS queue:
```
bin/jboss-cli.sh --connect --command="jms-queue add --queue-address=LoanIntake --entries=LoanIntake"
```

####Running the Demo

Inside the homeloan directory ...

Build the app:
```
mvn clean package
```

Deploy the app:
```
mvn jboss-as:deploy
```

Test Automatic Approval with SOAP message:
```
mvn -Psoap
```

Check status via the status web page - http://localhost:8080/loanstatus/740123456 .

Test Manual Approval with JMS message:
```
mvn -Pjms -Dexec.args="Joe"
```

Check status page and see that the application is in "Pending" status - http://localhost:8080/loanstatus/800559876 .

Go to the evaluation page and enter rate, explanation, and set status to Rejected/Approved - http://localhost:8080/loans/evaluation.jsf .

Check status page and see that the application status matches what you entered above.
