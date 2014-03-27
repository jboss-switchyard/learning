
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
import org.overlord.rtgov.quickstarts.demos.orders.Order;
import org.switchyard.Exchange;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.ExchangePhase;
import org.switchyard.HandlerException;
import org.switchyard.Message;
import org.switchyard.Property;

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
            
            if (p != null && p.getValue().toString().equals(
                            "java:org.jboss.example.homeloan.data.LoanApplication")) {

                String ssn=getApplicant(mesg);
                       
                if (ssn != null) {
                    if (_principals.containsKey(ssn)) {
                        
                        @SuppressWarnings("unchecked")
                        java.util.Map<String,java.io.Serializable> props=
                                (java.util.Map<String,java.io.Serializable>)
                                        _principals.get(ssn);
                        
                        // Check if customer is suspended
                        if (props.containsKey("suspended")
                                && props.get("suspended").equals(Boolean.TRUE)) {                            
                            throw new HandlerException("Customer '"+ssn
                                            +"' has been suspended");
                        }
                    }
                    
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("*********** Policy Enforcer: customer '"
                                +ssn+"' has not been suspended");
                        LOG.fine("*********** Principal: "+_principals.get(ssn));
                    }
                } else {
                    LOG.warning("Unable to find ssn");
                }
            }
        }
    }

    /**
     * This method returns the applicant ssn associated with the
     * exchange.
     * 
     * @param msg The message
     * @return The applicant ssn
     */
    protected String getApplicant(Message msg) {
        String ssn=null;

        Object content=msg.getContent();
        
        if (content instanceof LoanApplication) {
            ssn = ((Applicant)content).getApplicant().getSsn();
        }
        
        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("Customer="+ssn);
        }
        
        return (ssn);
    }
    
    @Override
    public List<String> getTargets() {
        return Arrays.asList(PROVIDER);
    }
    
}
