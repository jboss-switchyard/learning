package org.jboss.example.homeloan;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jboss.example.homeloan.data.Customer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.naming.NamingMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { CDIMixIn.class, NamingMixIn.class },
		exclude = "jms")
public class Step2Test {

    private Connection connection;
	private NamingMixIn namingMixIn;
	@ServiceOperation("CustomerLookup")
	private Invoker service;

	@Test
	public void customerExists() throws Exception {
		Customer customer = service.sendInOut("755-55-5555").getContent(Customer.class);
		Assert.assertEquals("Joseph", customer.getFirstName());
	}
	
	@Test
    public void customerDoesNotExist() throws Exception {
        Customer customer = service.sendInOut("755-55-1111").getContent(Customer.class);
        Assert.assertNull(customer.getSsn());
    }
	

	@After
    public void shutDown() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
        namingMixIn.uninitialize();
    }

	@Before
    public void createAndBind() throws Exception {
    	JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        connection = dataSource.getConnection();

        String createStatement = "CREATE TABLE CUSTOMER("
        	+ "SSN VARCHAR(11) PRIMARY KEY,"
        	+ "FIRSTNAME VARCHAR(50),"
        	+ "LASTNAME VARCHAR(50),"
        	+ "STREETADDRESS VARCHAR(255),"
        	+ "CITY VARCHAR(60),"
        	+ "STATE VARCHAR(2),"
        	+ "POSTALCODE VARCHAR(60),"
    		+ "DOB DATE,"
        	+ "CHECKINGBALANCE DECIMAL(14,2),"
        	+ "SAVINGSBALANCE DECIMAL(14,2));";
        
        String insertCustomer = "INSERT INTO CUSTOMER VALUES "
        		+ "('755-55-5555', 'Joseph', 'Smith', '123 Street', 'Elm', 'NC', '27808', '1970-01-01', 14000.40, 22000.99);";

        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS CUSTOMER");
        connection.createStatement().executeUpdate(createStatement);
        connection.createStatement().executeUpdate(insertCustomer);
        

        namingMixIn = new NamingMixIn();
        namingMixIn.initialize();
        bindDataSource(namingMixIn.getInitialContext(), "java:jboss/datasources/CustomerDS", dataSource);
    }

    private void bindDataSource(InitialContext context, String name, DataSource ds) throws Exception {
        try {
            context.bind(name, ds);
        } catch (NameAlreadyBoundException e) {
            e.getMessage(); // ignore
        }
    }
}
