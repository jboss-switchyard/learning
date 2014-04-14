/*
 * 2012-3 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.example.homeloan.interceptors;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.overlord.rtgov.active.collection.ActiveMap;
import org.overlord.rtgov.client.CollectionManager;
import org.overlord.rtgov.client.DefaultCollectionManager;
import org.switchyard.Exchange;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.ExchangePhase;
import org.switchyard.HandlerException;
import org.switchyard.Message;
import org.switchyard.Property;

import org.jboss.example.homeloan.data.LoanApplication;
import org.jboss.example.homeloan.data.Applicant;

@Named("PolicyEnforcer")
public class PolicyEnforcer implements ExchangeInterceptor {
    
    private static final String PRINCIPALS = "Principals";

    private static final Logger LOG=Logger.getLogger(PolicyEnforcer.class.getName());
    
    private CollectionManager _collectionManager=new DefaultCollectionManager();
    
    private ActiveMap _principals=null;
    
    private boolean _initialized=false;
    
    private static final ObjectMapper MAPPER=new ObjectMapper();

    static {
        SerializationConfig config=MAPPER.getSerializationConfig()
                .withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL)
                .withSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
        
        MAPPER.setSerializationConfig(config);
    }

    protected void init() {
                        
                
        if (_collectionManager != null) {
            _principals = _collectionManager.getMap(PRINCIPALS);
        }
        
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("*********** Policy Enforcer Initialized with acm="
                        +_collectionManager+", principals map="+_principals);
        }
        
        _initialized = true;
        
        System.out.println("KENNY----------->"+_principals);
    }

    /**
     * {@inheritDoc}
     */
    public void after(String call, Exchange exch) throws HandlerException {
    }
    
    /**
     * {@inheritDoc}
     */
    public void before(String call, Exchange exch) throws HandlerException {

        ExchangePhase phase=exch.getPhase();  

        if (phase != ExchangePhase.IN) {
            return;
        }

        if (!_initialized) {
            init();
        }
        
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("********* Exchange="+exch);
        }
        
        if (_principals != null) {            
            org.switchyard.Message mesg=exch.getMessage();
            
            if (mesg == null) {
                LOG.severe("Could not obtain message for phase ("+phase+") and exchange: "+exch);
                return;
            }

            org.switchyard.Context context=exch.getContext();
            
            java.util.Set<Property> contextProps=context.getProperties(
                    org.switchyard.Scope.MESSAGE);

            Property p=null;
            
            for (Property prop : contextProps) {
                if (prop.getName().equals("org.switchyard.contentType")) {
                    p = prop;
                    break;
                }
            }
            
            if (LOG.isLoggable(Level.FINER)) {
                LOG.finer("Content type="+(p==null?null:p.getValue()));
            }
            if (p != null)
            {
	       System.out.println("KENNY----------->"p.getValue().toString());
            }
            if (p != null && p.getValue().toString().equals(
                            "java:org.jboss.example.homeloan.data.LoanApplication")) {

                String customer=getCustomer(mesg);
                       
                if (customer != null) {
                    if (_principals.containsKey(customer)) {
                        
                        @SuppressWarnings("unchecked")
                        java.util.Map<String,java.io.Serializable> props=
                                (java.util.Map<String,java.io.Serializable>)
                                        _principals.get(customer);
                        
                        // Check if customer is suspended
                        if (props.containsKey("suspended")
                                && props.get("suspended").equals(Boolean.TRUE)) {                            
                            throw new HandlerException("Customer '"+customer
                                            +"' has been suspended");
                        }
                    }
                    
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("*********** Policy Enforcer: customer '"
                                +customer+"' has not been suspended");
                        LOG.fine("*********** Principal: "+_principals.get(customer));
                    }
                } else {
                    LOG.warning("Unable to find customer name");
                }
            }
        }
    }

    /**
     * This method returns the customer associated with the
     * exchange.
     * 
     * @param msg The message
     * @return The customer
     */
    protected String getCustomer(Message msg) {
        String customer=null;

        Object content=msg.getContent();
        
        if (content instanceof LoanApplication) {
            customer = ((LoanApplication)content).getApplicant().getSsn();
        }
        
        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("Customer="+customer);
        }
        System.out.println("KENNY----------->customer"+customer);
          
        return (customer);
    }
    
    @Override
    public List<String> getTargets() {
        return Arrays.asList(PROVIDER);
    }
    
}
