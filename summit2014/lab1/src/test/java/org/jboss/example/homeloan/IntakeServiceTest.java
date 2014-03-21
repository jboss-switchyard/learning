package org.jboss.example.homeloan;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jboss.example.homeloan.data.Applicant;
import org.jboss.example.homeloan.data.IncomeSource;
import org.jboss.example.homeloan.data.LoanApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.naming.NamingMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { CDIMixIn.class, NamingMixIn.class },
		exclude = "jms")
public class IntakeServiceTest {


    private Connection connection;
	private SwitchYardTestKit testKit;
	private NamingMixIn namingMixIn;
	@ServiceOperation("IntakeService")
	private Invoker service;

	@Test
	public void testIntake() throws Exception {

		MockHandler lookup = new MockHandler().replyWithOut(null);
		testKit.replaceService("CustomerLookup", lookup);
		
		LoanApplication loan = createApplication();
		service.operation("intake").sendInOnly(loan);
		
		// validate the results
		Assert.assertTrue(loan.isApproved());
	}
	
	/*
	@Test
	public void lookupFound() throws Exception {
		ArrayList<Customer> results = new ArrayList<Customer>();
		results.add(new Customer().setName("George Bill III").setSsn("711-55-5555"));
		MockHandler lookup = new MockHandler().replyWithOut(results);
		testKit.replaceService("CustomerLookup", lookup);
		
		LoanApplication loan = createApplication();
		Assert.assertEquals("George Bill", loan.getApplicant().getName());
		
		service.operation("intake").sendInOnly(loan);
		Assert.assertEquals("George Bill III", loan.getApplicant().getName());
	}
	*/
	
	@Test
	public void testWithDB() throws Exception {
		createAndBind();
		LoanApplication loan = createApplication();
		service.operation("intake").sendInOnly(loan);
		shutDown();
	}

	LoanApplication createApplication() {
		LoanApplication loan = new LoanApplication();
		Applicant applicant = new Applicant();
		applicant.setFirstName("Joe");
		applicant.setSsn("715-55-5555");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1981, 1, 1);
		applicant.setDob(calendar.getTime());
		loan.setApplicant(applicant);
		IncomeSource income = new IncomeSource();
		income.setSelfEmployed(false);
		loan.setAmount(15000);
		loan.setLengthYears(20);
		loan.setDeposit(1500);
		loan.setIncome(income);
		
		return loan;
	}
	
    void createAndBind() throws Exception {
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
    
    public void shutDown() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
        namingMixIn.uninitialize();
    }

}
