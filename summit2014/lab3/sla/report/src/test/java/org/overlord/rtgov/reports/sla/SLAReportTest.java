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
package org.overlord.rtgov.reports.sla;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityType;
import org.overlord.rtgov.activity.model.ActivityUnit;
import org.overlord.rtgov.activity.model.Origin;
import org.overlord.rtgov.activity.model.soa.RequestReceived;
import org.overlord.rtgov.activity.model.soa.ResponseSent;
import org.overlord.rtgov.activity.server.ActivityStore;
import org.overlord.rtgov.activity.server.QuerySpec;
import org.overlord.rtgov.activity.store.jpa.JPAActivityStore;
import org.overlord.rtgov.reports.MVELReportGenerator;
import org.overlord.rtgov.reports.ReportContext;
import org.overlord.rtgov.reports.model.Calendar;
import org.overlord.rtgov.reports.model.Report;
import org.overlord.rtgov.reports.model.Tabular;
import org.overlord.rtgov.reports.util.ReportsUtil;

public class SLAReportTest {
    
    private static final Logger LOG=Logger.getLogger(SLAReportTest.class.getName());

    private static EntityManager _em;
    private static JPAActivityStore _activityStore;
    
    private static final String OVERLORD_RTGOV_ACTIVITY_ORM="overlord-rtgov-activity-orm";
    
    private static final Interaction[] TEST1={
        new Interaction("st1", "op1", "p1", 100000, 100),
        new Interaction("st1", "op1", "p1", 100500, 500),
        new Interaction("st1", "op1", "p1", 100800, 100),
        new Interaction("st1", "op1", "p1", 100900, 100),
        new Interaction("st1", "op1", "p1", 101000, 150)
    };
    
    private static final Interaction[] TEST2={
        new Interaction("st2", "op2", "p2", 200000, 100),
        new Interaction("st2", "op2", "p2", 200500, 500),
        new Interaction("st2", "op2", "p2", 200800, 100),
        new Interaction("st2", "op2", "p2", 200900, 100),
        new Interaction("st2", "op2", "p2", 201000, 150)
    };
    
    private static final Interaction[] TEST3={
        new Interaction("st3", "op3", "p3", 300000, 100),
        new Interaction("st3", "op3", "p3", 300500, 500),
        new Interaction("st3", "op3", "p3", 300700, 400),
        new Interaction("st3", "op3", "p3", 300850, 350),
        new Interaction("st3", "op3", "p3", 301000, 150)
    };
    
    private static final Interaction[] TEST4={
        new Interaction("st4", "op4", "p4", 400000, 100),
        new Interaction("st4", "op4", "p4", 400500, 500),
        new Interaction("st4", "op4", "p4", 400700, 400),
        new Interaction("st4", "op4", "p4", 400850, 350),
        new Interaction("st4", "op4", "p4", 401000, 150),
        new Interaction("st4", "op4", "p4", 401100, 500),
        new Interaction("st4", "op4", "p4", 401250, 550),
        new Interaction("st4", "op4", "p4", 401450, 450),
        new Interaction("st4", "op4", "p4", 401700, 150)
    };
    
    @BeforeClass
    public static void initialiseEntityManager() throws Exception{
        _em = Persistence.createEntityManagerFactory(OVERLORD_RTGOV_ACTIVITY_ORM).createEntityManager();
        _activityStore = new JPAActivityStore();
        _activityStore.setEntityManager(_em);
    }
    
    @org.junit.Before
    public void clearDB()throws Exception {
        // Clear activity units
        
        /* TODO: Currently commit fails due to unitId being set to null of ActivityType.
         * The delete is not being propagated?
         *
        _em.getTransaction().begin();
        
        @SuppressWarnings("unchecked")
        java.util.List<ActivityUnit> results=_em.createQuery("SELECT au FROM ActivityUnit au").getResultList();

        for (ActivityUnit au : results) {
            // TODO: Currently has a referential integrity issue with unitId on activity type being null!!
            System.err.println("REMOVE AU="+au);
            
            _em.remove(au);
        }
        
        _em.getTransaction().commit();
        */
    }
    
    public ActivityUnit createTestActivityUnit(Interaction i) {
        ActivityUnit act=new ActivityUnit();
        String id=UUID.randomUUID().toString();
        
        act.setId(id);

        Origin origin=new Origin();
        origin.setPrincipal(i.getPrincipal());
        act.setOrigin(origin);

        RequestReceived me1=new RequestReceived();
        me1.setUnitId(id);
        me1.setUnitIndex(0);
        
        me1.setTimestamp(i.getStartTime());
        me1.setOperation(i.getOperation());
        me1.setServiceType(i.getServiceType());
        me1.setMessageId(UUID.randomUUID().toString());
        
        act.getActivityTypes().add(me1);
        
        ResponseSent me2=new ResponseSent();
        me2.setUnitId(id);
        me2.setUnitIndex(1);
        
        me2.setTimestamp(i.getStartTime()+i.getResponseTime());
        me2.setOperation(i.getOperation());
        me2.setServiceType(i.getServiceType());
        me2.setMessageId(UUID.randomUUID().toString());
        me2.setReplyToId(me1.getMessageId());
        
        act.getActivityTypes().add(me2);
        
        return (act);
    }
    
    public void record(Interaction[] interactions) {
        _em.getTransaction().begin();
        
        for (Interaction i : interactions) {
            ActivityUnit au=createTestActivityUnit(i);
            _em.persist(au);
        }
        
        _em.flush();
        
        _em.getTransaction().commit();
    }
    
    @Test
    public void testSLAReport1NoDuration() {
        MVELReportGenerator gen=new MVELReportGenerator();
        gen.setScriptLocation("SLAReport.mvel");
        
        try {
            gen.init();
        } catch (Exception e) {
            fail("Failed to initialize: "+e);
        }
        
        TestReportContext context=new TestReportContext();
        
        context.getServices().put(ActivityStore.class, _activityStore);
        
        // Create test data
        record(TEST1);
        
        try {
            QuerySpec qs=new QuerySpec().setExpression("SELECT at FROM ActivityType at WHERE at.timestamp >= "+
                    TEST1[0].getStartTime()+" AND at.timestamp <= "+
                    (TEST1[TEST1.length-1].getStartTime()+TEST1[TEST1.length-1].getResponseTime())).
                    setFormat("jpql");
            
            java.util.List<ActivityType> list=_activityStore.query(qs);
            
            if (list.size() != TEST1.length*2) {
                fail("Expecting "+(TEST1.length*2)+" records, but got: "+list.size());
            }
        } catch (Exception e) {
            fail("Failed to obtain activity types from db: "+e);
        }
        
        // Generate report
        try {
            java.util.Map<String,Object> properties=new java.util.HashMap<String, Object>();
            properties.put("maxResponseTime", "400");
            properties.put("start", TEST1[0].getStartTime());
            properties.put("end", TEST1[TEST1.length-1].getStartTime()+TEST1[TEST1.length-1].getResponseTime());
            
            Report report=gen.generate(context, properties);
            
            if (report == null) {
                fail("Report is null");
            }
            
            if (!report.getTitle().equals("SLA Report")) {
                fail("Title incorrect: "+report.getTitle());
            }
            
            if (report.getSections().size() != 1) {
                fail("Expecting 1 section: "+report.getSections().size());
            }
            
            System.out.println("REPORT="+new String(ReportsUtil.serializeReport(report)));
            
            Tabular table=(Tabular)report.getSections().get(0);
            
            if (table.getRows().size() != 1) {
                fail("Should be 1 row: "+table.getRows().size());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to generate: "+e);
        }
    }
    
    @Test
    public void testSLAReport2WithDuration() {
        MVELReportGenerator gen=new MVELReportGenerator();
        gen.setScriptLocation("SLAReport.mvel");
        
        try {
            gen.init();
        } catch (Exception e) {
            fail("Failed to initialize: "+e);
        }
        
        TestReportContext context=new TestReportContext();
        
        context.getServices().put(ActivityStore.class, _activityStore);
        
        // Create test data
        record(TEST2);
        
        try {
            QuerySpec qs= new QuerySpec().setExpression("SELECT at FROM ActivityType at WHERE at.timestamp >= "+
                    TEST2[0].getStartTime()+" AND at.timestamp <= "+
                    (TEST2[TEST2.length-1].getStartTime()+TEST2[TEST2.length-1].getResponseTime())).
                    setFormat("jpql");
            
            java.util.List<ActivityType> list = _activityStore.query(qs);
            
            if (list.size() != TEST2.length*2) {
                fail("Expecting "+(TEST2.length*2)+" records, but got: "+list.size());
            }
        } catch (Exception e) {
            fail("Failed to obtain activity types from db: "+e);
        }
        
        // Generate report
        try {
            java.util.Map<String,Object> properties=new java.util.HashMap<String, Object>();
            properties.put("maxResponseTime", "400");
            properties.put("averagedDuration", "450");
            properties.put("start", TEST2[0].getStartTime());
            properties.put("end", TEST2[TEST2.length-1].getStartTime()+TEST2[TEST2.length-1].getResponseTime());
            
            Report report=gen.generate(context, properties);
            
            if (report == null) {
                fail("Report is null");
            }
            
            if (!report.getTitle().equals("SLA Report")) {
                fail("Title incorrect: "+report.getTitle());
            }

            if (report.getSections().size() != 1) {
                fail("Expecting 1 section: "+report.getSections().size());
            }
            
            System.out.println("REPORT="+new String(ReportsUtil.serializeReport(report)));
            
            Tabular table=(Tabular)report.getSections().get(0);
            
            if (table.getRows().size() != 0) {
                fail("Should be no rows: "+table.getRows().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to generate: "+e);
        }
    }
    
    @Test
    public void testSLAReport3WithDuration() {
        MVELReportGenerator gen=new MVELReportGenerator();
        gen.setScriptLocation("SLAReport.mvel");
        
        try {
            gen.init();
        } catch (Exception e) {
            fail("Failed to initialize: "+e);
        }
        
        TestReportContext context=new TestReportContext();
        
        context.getServices().put(ActivityStore.class, _activityStore);
        
        // Create test data
        record(TEST3);
        
        try {
            QuerySpec qs= new QuerySpec().setExpression("SELECT at FROM ActivityType at WHERE at.timestamp >= "+
                    TEST3[0].getStartTime()+" AND at.timestamp <= "+
                    (TEST3[TEST3.length-1].getStartTime()+500)).
                    setFormat("jpql");
            
            java.util.List<ActivityType> list = _activityStore.query(qs);
            
            if (list.size() != TEST3.length*2) {
                fail("Expecting "+(TEST3.length*2)+" records, but got: "+list.size());
            }
        } catch (Exception e) {
            fail("Failed to obtain activity types from db: "+e);
        }
        
        // Generate report
        try {
            java.util.Map<String,Object> properties=new java.util.HashMap<String, Object>();
            properties.put("maxResponseTime", "400");
            properties.put("averagedDuration", "450");
            properties.put("start", TEST3[0].getStartTime());
            properties.put("end", TEST3[TEST3.length-1].getStartTime()+500);
            
            Report report=gen.generate(context, properties);
            
            if (report == null) {
                fail("Report is null");
            }
            
            if (!report.getTitle().equals("SLA Report")) {
                fail("Title incorrect: "+report.getTitle());
            }

            if (report.getSections().size() != 1) {
                fail("Expecting 1 section: "+report.getSections().size());
            }
            
            System.out.println("REPORT="+new String(ReportsUtil.serializeReport(report)));
            
            Tabular table=(Tabular)report.getSections().get(0);
            
            if (table.getRows().size() != 1) {
                fail("Should be 1 row: "+table.getRows().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to generate: "+e);
        }
    }
    
    @Test
    public void testSLAReport4WithDurationAndCalendar() {
        MVELReportGenerator gen=new MVELReportGenerator();
        gen.setScriptLocation("SLAReport.mvel");
        
        try {
            gen.init();
        } catch (Exception e) {
            fail("Failed to initialize: "+e);
        }
        
        TestCalendar cal=new TestCalendar(401000, 401700);
        
        TestReportContext context=new TestReportContext();
        context.setCalendar(cal);
        
        context.getServices().put(ActivityStore.class, _activityStore);
        
        // Create test data
        record(TEST4);
        
        try {
            QuerySpec qs= new QuerySpec().setExpression("SELECT at FROM ActivityType at WHERE at.timestamp >= "+
                    TEST4[0].getStartTime()+" AND at.timestamp <= "+
                    (TEST4[TEST4.length-1].getStartTime()+500)).
                    setFormat("jpql");
            
            java.util.List<ActivityType> list = _activityStore.query(qs);
            
            if (list.size() != TEST4.length*2) {
                fail("Expecting "+(TEST4.length*2)+" records, but got: "+list.size());
            }
        } catch (Exception e) {
            fail("Failed to obtain activity types from db: "+e);
        }
        
        // Generate report
        try {
            java.util.Map<String,Object> properties=new java.util.HashMap<String, Object>();
            properties.put("maxResponseTime", "400");
            properties.put("averagedDuration", "450");
            properties.put("start", TEST4[0].getStartTime());
            properties.put("end", TEST4[TEST4.length-1].getStartTime()+500);
            properties.put("timezone", "EST");
            
            Report report=gen.generate(context, properties);
            
            if (report == null) {
                fail("Report is null");
            }
            
            if (!report.getTitle().equals("SLA Report")) {
                fail("Title incorrect: "+report.getTitle());
            }

            if (report.getSections().size() != 1) {
                fail("Expecting 1 section: "+report.getSections().size());
            }
            
            System.out.println("REPORT="+new String(ReportsUtil.serializeReport(report)));
            
            Tabular table=(Tabular)report.getSections().get(0);
            
            if (table.getRows().size() != 1) {
                fail("Should be 1 row: "+table.getRows().size());
            }

            if (table.getSummary() == null) {
                fail("No summary");
            }
            
            if (table.getSummary().getValues().size() != 2) {
                fail("Summary should have 2 values: "+table.getSummary().getValues().size());
            }
            
            if (!table.getSummary().getValues().get(1).equals(new Long(450))) {
                fail("Value 1 should be 450: "+table.getSummary().getValues().get(1));
            }
            
            if (table.getSummary().getProperties().size() != 1) {
                fail("Expecting 1 property: "+table.getSummary().getProperties().size());
            }
            
            if (!table.getSummary().getProperties().containsKey("ViolationPercentage")) {
                fail("Property 'ViolationPercentage' not found");
            }
            
            if (!table.getSummary().getProperties().get("ViolationPercentage").equals(20.45)) {
                fail("Value 3 should be 20.45: "+table.getSummary().getValues().get(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to generate: "+e);
        }
    }
    
    public static class Interaction {
        private String _serviceType=null;
        private String _operation=null;
        private String _principal=null;
        private long _startTime=0;
        private long _responseTime=0;
        
        public Interaction(String servType, String op, String p, long st, long rt) {
            _serviceType = servType;
            _operation = op;
            _principal = p;
            _startTime = st;
            _responseTime = rt;
        }
        
        public String getServiceType() {
            return (_serviceType);
        }
        
        public String getOperation() {
            return (_operation);
        }
        
        public String getPrincipal() {
            return (_principal);
        }
        
        public long getStartTime() {
            return (_startTime);
        }
        
        public long getResponseTime() {
            return (_responseTime);
        }
    }
    
    public static class TestCalendar extends Calendar {
        
        private long _start=0;
        private long _end=0;
        
        public TestCalendar(long start, long end) {
            _start = start;
            _end = end;
        }
        
        public void setStart(long ts) {
            _start = ts;
        }
        
        public void setEnd(long ts) {
            _end = ts;
        }
        
        public boolean isWorkingDateTime(long timestamp) {
            return (timestamp >= _start && timestamp <= _end);
        }

        public long getWorkingDuration(long from, long to) {
            return (to-from);
        }
    }
    
    public static class TestReportContext implements ReportContext {
        
        private java.util.List<String> _errors=new java.util.ArrayList<String>();
        private java.util.Map<Class<?>, Object> _services=new java.util.HashMap<Class<?>, Object>();
        private Calendar _calendar=null;
        
        public java.util.Map<Class<?>, Object> getServices() {
            return (_services);
        }

        public Object getService(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        public Object getService(Class<?> cls) {
            return (_services.get(cls));
        }

        public void logError(String mesg) {
            LOG.severe(mesg);
            _errors.add(mesg);
        }
        
        public java.util.List<String> getErrors() {
            return (_errors);
        }

        public Calendar getCalendar(String name, String timezone) {
            return (_calendar);
        }
        
        public void setCalendar(Calendar cal) {
            _calendar = cal;
        }

        public void logWarning(String mesg) {
            LOG.warning(mesg);
        }

        public void logInfo(String mesg) {
            LOG.info(mesg);
        }

        public void logDebug(String mesg) {
            LOG.finest(mesg);
        }
        
    }
}
