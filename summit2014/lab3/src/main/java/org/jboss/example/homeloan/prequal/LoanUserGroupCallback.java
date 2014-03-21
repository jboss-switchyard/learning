package org.jboss.example.homeloan.prequal;

import java.util.Properties;

import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;

public class LoanUserGroupCallback extends JBossUserGroupCallbackImpl {
	
	private static Properties userGroups = new Properties();
    static {
    	userGroups.setProperty("admin", "approvers, users");
    	userGroups.setProperty("bill", "users");
    	userGroups.setProperty("mrbill", "approvers");
    }


	public LoanUserGroupCallback() {
		super(userGroups);
	}
}
