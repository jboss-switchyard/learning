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
package org.overlord.rtgov.samples.jbossas.slamonitor.monitor;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.overlord.rtgov.active.collection.ActiveCollectionManager;
import org.overlord.rtgov.active.collection.ActiveCollectionManagerAccessor;
import org.overlord.rtgov.active.collection.ActiveList;
import org.overlord.rtgov.active.collection.predicate.MVEL;
import org.overlord.rtgov.active.collection.predicate.Predicate;
import org.overlord.rtgov.analytics.service.ResponseTime;
import org.overlord.rtgov.analytics.situation.Situation;

/**
 * This is the custom event monitor that receives node notifications
 * from the EPN, and makes the events available via a REST API.
 *
 */
@Path("/monitor")
@ApplicationScoped
public class SLAMonitor {

    private static final String SERVICE_RESPONSE_TIMES = "ServiceResponseTimes";
    private static final String SITUATIONS = "Situations";

    private static final Logger LOG=Logger.getLogger(SLAMonitor.class.getName());
    
    private ActiveCollectionManager _acmManager=null;
    
    private ActiveList _serviceResponseTime=null;
    private ActiveList _situations=null;
    
    /**
     * This is the default constructor.
     */
    public SLAMonitor() {
        
        try {
            _acmManager = ActiveCollectionManagerAccessor.getActiveCollectionManager();

            _serviceResponseTime = (ActiveList)
                    _acmManager.getActiveCollection(SERVICE_RESPONSE_TIMES);
        
            _situations = (ActiveList)
                    _acmManager.getActiveCollection(SITUATIONS);
        
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to initialize active collection manager", e);
        }

    }
    
    /**
     * This method returns the list of response times.
     * 
     * @param serviceType The optional service type
     * @param operation The optional operation
     * @param fault The optional fault
     * @return The response times
     */
    @GET
    @Path("/responseTimes")
    @Produces("application/json")
    public java.util.List<ResponseTime> getResponseTimes(
    					@QueryParam("serviceType") String serviceType,
    					@QueryParam("operation") String operation,
    					@QueryParam("fault") String fault) {
        java.util.List<ResponseTime> ret=new java.util.ArrayList<ResponseTime>();

        ActiveList list=getResponseTimeList(serviceType, operation, fault);
        
        for (Object obj : list) {
            if (obj instanceof ResponseTime) {
                ret.add((ResponseTime)obj);
            }
        }
        
        return (ret);
    }

    /**
     * This method returns the active list for the response times
     * associated with the supplied query parameters.
     * 
     * @param serviceType The optional service type
     * @param operation The optional operation
     * @param fault The optional fault
     * @return The active list of response times
     */
    protected ActiveList getResponseTimeList(String serviceType, String operation, String fault) {
    	ActiveList ret=_serviceResponseTime;
    	
    	if (LOG.isLoggable(Level.FINE)) {
    	    LOG.fine("Get Response Time List: serviceType="+serviceType+" operation="
    	            +operation+" fault="+fault);
    	}
    	
    	if (serviceType != null || operation != null || fault != null) {
        	String alname="RespTime:"+serviceType+":"+operation+":"+fault;

        	ret = (ActiveList)_acmManager.getActiveCollection(alname);
        	
        	if (ret == null) {
        	    String expr=expressionBuilder(null, "serviceType", serviceType);
                expr = expressionBuilder(expr, "operation", operation);
                expr = expressionBuilder(expr, "fault", fault);
        	    
        		Predicate predicate=new MVEL(expr);        		
        		
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Create derived collection for: serviceType="+serviceType+" operation="
                            +operation+" fault="+fault);
                }
                
        		ret = (ActiveList)_acmManager.create(alname, _serviceResponseTime,
        		                    predicate, null);
        	}
    	}
    	
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Returning: serviceType="+serviceType+" operation="
                    +operation+" fault="+fault+" ret="+ret);
        }
        
    	return (ret);
    }
    
    /**
     * This method builds an expression based on the supplied property and value.
     * 
     * @param expr The initial expression
     * @param prop The property being built
     * @param value The optional property value
     * @return The new expression, taking into account the supplied property information if relevant
     */
    protected static String expressionBuilder(String expr, String prop, String value) {
        if (value != null) {
            String subexpr=prop+" == \""+value+"\"";
            if (expr == null) {
                expr = subexpr;
            } else {
                expr += " && "+subexpr;
            }
        }
        
        return (expr);
    }
    
    /**
     * This method returns the list of situations.
     * 
     * @return The situations
     */
    @GET
    @Path("/situations")
    @Produces("application/json")
    public java.util.List<Situation> getSituations() {
        java.util.List<Situation> ret=new java.util.ArrayList<Situation>();

        for (Object obj : _situations) {
            if (obj instanceof Situation) {
                ret.add((Situation)obj);
            }
        }
        
        return (ret);
    }
    
}
