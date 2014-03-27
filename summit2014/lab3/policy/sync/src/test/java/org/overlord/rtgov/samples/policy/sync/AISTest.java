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
package org.overlord.rtgov.samples.policy.sync;

import static org.junit.Assert.*;

import org.infinispan.manager.CacheContainer;
import org.junit.Test;
import org.overlord.rtgov.activity.model.soa.RequestReceived;
import org.overlord.rtgov.activity.util.ActivityValidatorUtil;
import org.overlord.rtgov.activity.validator.ActivityValidator;
import org.overlord.rtgov.common.infinispan.InfinispanManager;

public class AISTest {

    @Test
    public void testAcceptSpacedOutRequests() {
        ActivityValidator av=null;
        
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("av.json");
            
            byte[] b=new byte[is.available()];
            is.read(b);
            
            is.close();
            
            java.util.List<ActivityValidator> avs=ActivityValidatorUtil.deserializeActivityValidatorList(b);
            
            if (avs.size() != 1) {
            	fail("Expecting 1 activity validator, but got: "+avs.size());
            }
            
            av = avs.get(0);
            
            av.init();
            
        } catch (Exception e) {
            fail("Failed to load activity validator: "+e);
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
        
        RequestReceived rq1=new RequestReceived();
        rq1.setOperation("submitOrder");
        rq1.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rq1.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        rq1.getProperties().put("customer", "Fred");
        
        try {
        	av.validate(rq1);
            
            synchronized (this) {
                wait(3000);
            }
        } catch (Exception e) {
            fail("Failed to validate 1st event: "+e);
        }
        
        try {
        	av.validate(rq1);
        } catch (Exception e) {       	
            fail("Failed to validate 2nd event: "+e);
        }
    }
    
    @Test
    public void testRejectRushedSecondRequest() {
        ActivityValidator av=null;
        
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("av.json");
            
            byte[] b=new byte[is.available()];
            is.read(b);
            
            is.close();
            
            java.util.List<ActivityValidator> avs=ActivityValidatorUtil.deserializeActivityValidatorList(b);
            
            if (avs.size() != 1) {
            	fail("Expecting 1 activity validator, but got: "+avs.size());
            }
            
            av = avs.get(0);
            
            av.init();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to load activity validator: "+e);
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
        
        RequestReceived rq1=new RequestReceived();
        rq1.setOperation("submitOrder");
        rq1.setServiceType("{urn:switchyard-quickstart-demo:orders:0.1.0}OrderService");
        rq1.setContent("<Order><customer>Fred</customer><total>100</total></Order>");
        rq1.getProperties().put("customer", "Fred");
        
        try {
        	av.validate(rq1);
            
            synchronized (this) {
                wait(500);
            }
        } catch (Exception e) {
            fail("Failed to validate event: "+e);
        }
        
        try {
        	av.validate(rq1);
        	
        	fail("Should have failed");
        } catch (Exception e) {
        }
    }
}
