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
package org.overlord.rtgov.samples.policy.async;

import static org.junit.Assert.*;

import org.infinispan.manager.CacheContainer;
import org.junit.Test;
import org.overlord.rtgov.activity.model.soa.RequestReceived;
import org.overlord.rtgov.activity.model.soa.RequestSent;
import org.overlord.rtgov.activity.model.soa.ResponseReceived;
import org.overlord.rtgov.activity.model.soa.ResponseSent;
import org.overlord.rtgov.common.infinispan.InfinispanManager;
import org.overlord.rtgov.epn.Network;
import org.overlord.rtgov.epn.embedded.EmbeddedEPNManager;
import org.overlord.rtgov.epn.util.NetworkUtil;

public class EPNTest {

    @Test
    public void testSuspendCustomer() {
        EmbeddedEPNManager epnm=new EmbeddedEPNManager();
        
        // Load network
        Network network=null;
        
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("epn.json");
            
            byte[] b=new byte[is.available()];
            is.read(b);
            
            is.close();
            
            network = NetworkUtil.deserialize(b);
            
            epnm.register(network);
        } catch (Exception e) {
            fail("Failed to register network: "+e);
        }
        
        // Obtain Principals cache
        java.util.Map<Object,Object> cache=null;
        
        try {
            CacheContainer cc=InfinispanManager.getCacheContainer(null);
            
            cache = cc.getCache("Principals");
            
            cache.clear();
            
        } catch (Exception e) {
            fail("Failed to get default cache container: "+e);
        }
        
        java.util.List<java.io.Serializable> events=
                    new java.util.ArrayList<java.io.Serializable>();
        
        RequestSent rqs=new RequestSent();
        rqs.setOperation("submitOrder");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        RequestReceived rqr=new RequestReceived();
        rqr.setOperation("submitOrder");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        ResponseSent rps=new ResponseSent();
        rps.setOperation("submitOrder");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"total\":100,\"customer\":\"Fred\"}");
        
        rps.getProperties().put("total", "100");
        rps.getProperties().put("customer", "Fred");
        
        ResponseReceived rpr=new ResponseReceived();
        rpr.setOperation("submitOrder");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rpr.setContent("{\"total\":100,\"customer\":\"Fred\"}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Fred")) {
            fail("Principal is not Fred: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props1=(java.util.Map<String,Object>)cache.get("Fred");
        
        if (props1.containsKey("suspended")) {
            fail("Fred should not have a 'suspended' property yet: "+props1.get("suspended"));
        }
        
        // Make second purchase that takes the customer above the level
        events=new java.util.ArrayList<java.io.Serializable>();
    
        rqs=new RequestSent();
        rqs.setOperation("submitOrder");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        rqr=new RequestReceived();
        rqr.setOperation("submitOrder");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        rps=new ResponseSent();
        rps.setOperation("submitOrder");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"customer\":\"Fred\",\"total\":100}");
        
        rps.getProperties().put("total", "100");
        rps.getProperties().put("customer", "Fred");
        
        rpr=new ResponseReceived();
        rpr.setOperation("submitOrder");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rpr.setContent("{\"customer\":\"Fred\",\"total\":100}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Fred")) {
            fail("Principal is not Fred: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props2 = (java.util.Map<String,Object>)cache.get("Fred");
        
        if (props2.get("suspended") != Boolean.TRUE) {
            fail("Fred is not suspended");
        }
    }

    @Test
    public void testCustomerIsolation() {
        EmbeddedEPNManager epnm=new EmbeddedEPNManager();
        
        // Load network
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("epn.json");
            
            byte[] b=new byte[is.available()];
            is.read(b);
            
            is.close();
            
            Network network=NetworkUtil.deserialize(b);
            
            epnm.register(network);
        } catch (Exception e) {
            fail("Failed to register network: "+e);
        }
        
        // Obtain Principals cache
        java.util.Map<Object,Object> cache=null;
        
        try {
            CacheContainer cc=InfinispanManager.getCacheContainer(null);
            
            cache = cc.getCache("Principals");
            
            cache.clear();
            
        } catch (Exception e) {
            fail("Failed to get default cache container: "+e);
        }
        
        java.util.List<java.io.Serializable> events=
                    new java.util.ArrayList<java.io.Serializable>();
        
        RequestSent rqs=new RequestSent();
        rqs.setOperation("submitOrder");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        RequestReceived rqr=new RequestReceived();
        rqr.setOperation("submitOrder");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        
        ResponseSent rps=new ResponseSent();
        rps.setOperation("submitOrder");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"total\":100,\"customer\":\"Fred\"}");
        
        rps.getProperties().put("total", "100");
        rps.getProperties().put("customer", "Fred");
        
        ResponseReceived rpr=new ResponseReceived();
        rpr.setOperation("submitOrder");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rpr.setContent("{\"total\":100,\"customer\":\"Fred\"}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Fred")) {
            fail("Principal is not Fred: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props1=(java.util.Map<String,Object>)cache.get("Fred");
        
        if (props1.containsKey("suspended")) {
            fail("Fred should not have a 'suspended' property yet: "+props1.get("suspended"));
        }
        
        // Make second purchase but for different customer
        events=new java.util.ArrayList<java.io.Serializable>();
    
        rqs=new RequestSent();
        rqs.setOperation("submitOrder");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Order><customer>Joe</customer><total>100</total></Order>");
        
        rqr=new RequestReceived();
        rqr.setOperation("submitOrder");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Order><customer>Joe</customer><total>100</total></Order>");
        
        rps=new ResponseSent();
        rps.setOperation("submitOrder");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"total\":100,\"customer\":\"Joe\"}");
        
        rps.getProperties().put("total", "100");
        rps.getProperties().put("customer", "Joe");
        
        rpr=new ResponseReceived();
        rpr.setOperation("submitOrder");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rpr.setContent("{\"total\":100,\"customer\":\"Joe\"}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Joe")) {
            fail("Principal is not Joe: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props2=(java.util.Map<String,Object>)cache.get("Joe");
        
        if (props2.containsKey("suspended")) {
            fail("Joe should not have a 'suspended' property yet: "+props2.get("suspended"));
        }
        
    }

    @Test
    public void testUnsuspendCustomer() {
        EmbeddedEPNManager epnm=new EmbeddedEPNManager();
        
        // Load network
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("epn.json");
            
            byte[] b=new byte[is.available()];
            is.read(b);
            
            is.close();
            
            Network network=NetworkUtil.deserialize(b);
            
            epnm.register(network);
        } catch (Exception e) {
            fail("Failed to register network: "+e);
        }
        
        // Obtain Principals cache
        java.util.Map<Object,Object> cache=null;
        
        try {
            CacheContainer cc=InfinispanManager.getCacheContainer(null);
            
            cache = cc.getCache("Principals");
            
            cache.clear();
            
        } catch (Exception e) {
            fail("Failed to get default cache container: "+e);
        }
        
        java.util.List<java.io.Serializable> events=
                    new java.util.ArrayList<java.io.Serializable>();
        
        RequestSent rqs=new RequestSent();
        rqs.setOperation("submitOrder");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Order><customer>Fred</customer><total>200</total></Order>");
        
        RequestReceived rqr=new RequestReceived();
        rqr.setOperation("submitOrder");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Order><customer>Fred</customer><total>200</total></Order>");
        
        ResponseSent rps=new ResponseSent();
        rps.setOperation("submitOrder");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"total\":200,\"customer\":\"Fred\"}");
        
        rps.getProperties().put("total", "200");
        rps.getProperties().put("customer", "Fred");
        
        ResponseReceived rpr=new ResponseReceived();
        rpr.setOperation("submitOrder");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"total\":200,\"customer\":\"Fred\"}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Fred")) {
            fail("Principal is not Fred: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props1=(java.util.Map<String,Object>)cache.get("Fred");
        
        if (!props1.containsKey("suspended")) {
            fail("Fred should have a 'suspended' property");
        }
        
        if (props1.get("suspended") != Boolean.TRUE) {
            fail("Fred should be suspended");
        }
        
        // Make second purchase that takes the customer above the level
        events=new java.util.ArrayList<java.io.Serializable>();
    
        rqs=new RequestSent();
        rqs.setOperation("makePayment");
        rqs.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqs.setContent("<Payment><customer>Fred</customer><amount>170</amount></Payment>");
        
        rqr=new RequestReceived();
        rqr.setOperation("makePayment");
        rqr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rqr.setContent("<Payment><customer>Fred</customer><amount>170</amount></Payment>");
        
        rps=new ResponseSent();
        rps.setOperation("makePayment");
        rps.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rps.setContent("{\"amount\":170,\"customer\":\"Fred\"}");
        
        rps.getProperties().put("total", "170");
        rps.getProperties().put("customer", "Fred");
        
        rpr=new ResponseReceived();
        rpr.setOperation("makePayment");
        rpr.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rpr.setContent("{\"amount\":170,\"customer\":\"Fred\"}");
        
        events.add(rqs);
        events.add(rqr);
        events.add(rps);
        events.add(rpr);
        
        try {
            epnm.publish("SOAEvents", events);
            
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to publish events: "+e);
        }
        
        if (!cache.containsKey("Fred")) {
            fail("Principal is not Fred: "+cache);
        }
        
        @SuppressWarnings("unchecked")
        java.util.Map<String,Object> props2=(java.util.Map<String,Object>)cache.get("Fred");
        
        if (!props2.containsKey("suspended")) {
            fail("Fred should have a 'suspended' property");
        }
        
        if (props2.get("suspended") != Boolean.FALSE) {
            fail("Fred should be unsuspended");
        }
    }
}
