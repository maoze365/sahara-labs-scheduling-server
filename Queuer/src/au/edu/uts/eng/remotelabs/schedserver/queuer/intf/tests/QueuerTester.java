/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Michael Diponio (mdiponio)
 * @date 28th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.intf.tests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilitiesId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.InQueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueRequestType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserNSNameSequence;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserQueueType;

/**
 * Tests the {@link Queuer} class.
 */
public class QueuerTester extends TestCase
{
    /** Object of class under test. */ 
    private Queuer queuer;

    public QueuerTester(String name) throws Exception
    {
        super(name);
        
        /* Set up the logger. */
        Field f = LoggerActivator.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(null, new SystemErrLogger());
        
        /* Set up the SessionFactory. */
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        props.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/sahara");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        props.setProperty("hibernate.connection.username", "sahara");
        props.setProperty("hibernate.connection.password", "saharapasswd");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        props.setProperty("hibernate.use_sql_comments", "true");
        props.setProperty("hibernate.generate_statistics", "true");
        cfg.setProperties(props);
        cfg.addAnnotatedClass(AcademicPermission.class);
        cfg.addAnnotatedClass(Config.class);
        cfg.addAnnotatedClass(MatchingCapabilities.class);
        cfg.addAnnotatedClass(MatchingCapabilitiesId.class);
        cfg.addAnnotatedClass(RequestCapabilities.class);
        cfg.addAnnotatedClass(ResourcePermission.class);
        cfg.addAnnotatedClass(Rig.class);
        cfg.addAnnotatedClass(RigCapabilities.class);
        cfg.addAnnotatedClass(RigType.class);
        cfg.addAnnotatedClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserAssociation.class);
        cfg.addAnnotatedClass(UserAssociationId.class);
        cfg.addAnnotatedClass(UserClass.class);
        cfg.addAnnotatedClass(UserLock.class);
        
        f = DataAccessActivator.class.getDeclaredField("sessionFactory");
        f.setAccessible(true);
        f.set(null, cfg.buildSessionFactory());
    }

    @Override
    @Before
    protected void setUp() throws Exception
    {
        super.setUp();
        this.queuer = new Queuer();
        
        Queue q = Queue.getInstance();
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        f.set(q, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        f.set(q, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        f.set(q, new HashMap<Long, InnerQueue>());
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueue()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(false);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
        
        /* Request parameters. */
        AddUserToQueue request = new AddUserToQueue();
        QueueRequestType qu = new QueueRequestType();
        request.setAddUserToQueue(qu);
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ':' + user.getName());
        qu.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(p1.getId().intValue());
        qu.setPermissionID(pid);
        
        AddUserToQueueResponse resp = this.queuer.addUserToQueue(request);
        
        SessionDao dao = new SessionDao();
        Session ent = dao.findActiveSession(user);
        dao.delete(ent);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(ent);
        assertTrue(ent.isActive());
        assertFalse(ent.isReady());
        assertEquals(uc1.getPriority(), ent.getPriority());
        assertEquals(p1.getAllowedExtensions(), ent.getExtensions());
        assertNull(ent.getCodeReference());
        assertEquals("RIG", ent.getResourceType());
        assertEquals(r.getId(), ent.getRequestedResourceId());
        assertEquals(r.getName(), ent.getRequestedResourceName());
        assertEquals(p1.getId(), ent.getResourcePermission().getId());
        assertEquals(user.getId(), ent.getUser().getId());
        assertEquals(user.getName(), ent.getUserName());
        assertEquals(user.getNamespace(), ent.getUserNamespace());
        
        assertNotNull(resp);
        InQueueType in = resp.getAddUserToQueueResponse();
        assertNotNull(in);
        
        assertTrue(in.getQueueSuccessful());
        assertTrue(in.getInQueue());
        assertFalse(in.getInSession());
        assertNull(in.getAssignedResource());
        ResourceIDType queud = in.getQueuedResouce();
        assertNotNull(queud);
        assertEquals("RIG", queud.getType());
        assertEquals(r.getName(), queud.getResourceName());
        assertEquals(r.getId().intValue(), queud.getResourceID());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueueRes()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(false);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
        
        /* Request parameters. */
        AddUserToQueue request = new AddUserToQueue();
        QueueRequestType qu = new QueueRequestType();
        request.setAddUserToQueue(qu);
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ':' + user.getName());
        qu.setUserID(uid);
        ResourceIDType res = new ResourceIDType();
        res.setType("RIG");
        res.setResourceID(r.getId().intValue());
        qu.setResourceID(res);
        
        AddUserToQueueResponse resp = this.queuer.addUserToQueue(request);
        
        SessionDao dao = new SessionDao();
        Session ent = dao.findActiveSession(user);
        dao.delete(ent);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(ent);
        assertTrue(ent.isActive());
        assertFalse(ent.isReady());
        assertEquals(uc1.getPriority(), ent.getPriority());
        assertEquals(p1.getAllowedExtensions(), ent.getExtensions());
        assertEquals(Math.floor(now.getTime() / 10000), Math.floor(ent.getActivityLastUpdated().getTime() / 10000));
        assertNull(ent.getCodeReference());
        assertEquals(Math.floor(now.getTime() / 10000), Math.floor(ent.getRequestTime().getTime() / 10000));
        assertEquals("RIG", ent.getResourceType());
        assertEquals(r.getId(), ent.getRequestedResourceId());
        assertEquals(r.getName(), ent.getRequestedResourceName());
        assertEquals(p1.getId(), ent.getResourcePermission().getId());
        assertEquals(user.getId(), ent.getUser().getId());
        assertEquals(user.getName(), ent.getUserName());
        assertEquals(user.getNamespace(), ent.getUserNamespace());
        
        assertNotNull(resp);
        InQueueType in = resp.getAddUserToQueueResponse();
        assertNotNull(in);
        
        assertTrue(in.getQueueSuccessful());
        assertTrue(in.getInQueue());
        assertFalse(in.getInSession());
        assertNull(in.getAssignedResource());
        ResourceIDType queud = in.getQueuedResouce();
        assertNotNull(queud);
        assertEquals("RIG", queud.getType());
        assertEquals(r.getName(), queud.getResourceName());
        assertEquals(r.getId().intValue(), queud.getResourceID());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueueResName()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(false);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
        
        /* Request parameters. */
        AddUserToQueue request = new AddUserToQueue();
        QueueRequestType qu = new QueueRequestType();
        request.setAddUserToQueue(qu);
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ':' + user.getName());
        qu.setUserID(uid);
        ResourceIDType res = new ResourceIDType();
        res.setType("RIG");
        res.setResourceName(r.getName());
        qu.setResourceID(res);
        
        AddUserToQueueResponse resp = this.queuer.addUserToQueue(request);
        
        SessionDao dao = new SessionDao();
        Session ent = dao.findActiveSession(user);
        dao.delete(ent);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(ent);
        assertTrue(ent.isActive());
        assertFalse(ent.isReady());
        assertEquals(uc1.getPriority(), ent.getPriority());
        assertEquals(p1.getAllowedExtensions(), ent.getExtensions());
        assertEquals(Math.floor(now.getTime() / 10000), Math.floor(ent.getActivityLastUpdated().getTime() / 10000));
        assertNull(ent.getCodeReference());
        assertEquals(Math.floor(now.getTime() / 10000), Math.floor(ent.getRequestTime().getTime() / 10000));
        assertEquals("RIG", ent.getResourceType());
        assertEquals(r.getId(), ent.getRequestedResourceId());
        assertEquals(r.getName(), ent.getRequestedResourceName());
        assertEquals(p1.getId(), ent.getResourcePermission().getId());
        assertEquals(user.getId(), ent.getUser().getId());
        assertEquals(user.getName(), ent.getUserName());
        assertEquals(user.getNamespace(), ent.getUserNamespace());
        
        assertNotNull(resp);
        InQueueType in = resp.getAddUserToQueueResponse();
        assertNotNull(in);
        
        assertTrue(in.getQueueSuccessful());
        assertTrue(in.getInQueue());
        assertFalse(in.getInSession());
        assertNull(in.getAssignedResource());
        ResourceIDType queud = in.getQueuedResouce();
        assertNotNull(queud);
        assertEquals("RIG", queud.getType());
        assertEquals(r.getName(), queud.getResourceName());
        assertEquals(r.getId().intValue(), queud.getResourceID());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueueInQueue()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
        
        /* Request parameters. */
        AddUserToQueue request = new AddUserToQueue();
        QueueRequestType qu = new QueueRequestType();
        request.setAddUserToQueue(qu);
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ':' + user.getName());
        qu.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(p1.getId().intValue());
        qu.setPermissionID(pid);
        
        AddUserToQueueResponse resp = this.queuer.addUserToQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(resp);
        InQueueType in = resp.getAddUserToQueueResponse();
        assertNotNull(in);
        
        assertFalse(in.getQueueSuccessful());
        assertTrue(in.getInQueue());
        assertFalse(in.getInSession());
        assertNull(in.getAssignedResource());
        ResourceIDType queud = in.getQueuedResouce();
        assertNotNull(queud);
        assertEquals("RIG", queud.getType());
        assertEquals(r.getName(), queud.getResourceName());
        assertEquals(r.getId().intValue(), queud.getResourceID());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueueCantQueue()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);

        db.beginTransaction();
        User user = new User("qperm1", "testns2", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(false);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        db.getTransaction().commit();
        db.flush();
        db.close();
        
        /* Request parameters. */
        AddUserToQueue request = new AddUserToQueue();
        QueueRequestType qu = new QueueRequestType();
        request.setAddUserToQueue(qu);
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ':' + user.getName());
        qu.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(p1.getId().intValue());
        qu.setPermissionID(pid);
        
        AddUserToQueueResponse resp = this.queuer.addUserToQueue(request);
        
        db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(resp);
        InQueueType in = resp.getAddUserToQueueResponse();
        assertNotNull(in);
        
        assertFalse(in.getQueueSuccessful());
        assertFalse(in.getInQueue());
        assertFalse(in.getInSession());
        assertNull(in.getAssignedResource());
        assertNull(in.getQueuedResouce());
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#removeUserFromQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue)}.
     */
    public void testRemoveUserFromQueue()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setRig(r);
        db.persist(ses);
        db.close();
        
        /* Request parameters. */
        RemoveUserFromQueue request = new RemoveUserFromQueue();
        UserIDType uId = new UserIDType();
        request.setRemoveUserFromQueue(uId);
        uId.setUserQName(user.getNamespace() + ":" + user.getName());
        
        RemoveUserFromQueueResponse resp = this.queuer.removeUserFromQueue(request);
        
        db = DataAccessActivator.getNewSession();
        ses = (Session) db.get(Session.class, ses.getId());
       
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(resp);
        InQueueType in = resp.getRemoveUserFromQueueResponse();
        assertNotNull(in);
        
        assertFalse(ses.isActive());
        assertNotNull(ses.getRemovalReason());
        assertNotNull(ses.getRemovalTime());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#removeUserFromQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue)}.
     */
    public void testRemoveUserFromQueueID()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date after = new Date(System.currentTimeMillis() + 1000000);
        Date now = new Date();
        db.beginTransaction();
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setRig(r);
        db.persist(ses);
        db.close();
        
        /* Request parameters. */
        RemoveUserFromQueue request = new RemoveUserFromQueue();
        UserIDType uId = new UserIDType();
        request.setRemoveUserFromQueue(uId);
        uId.setUserID(String.valueOf(user.getId()));
        
        RemoveUserFromQueueResponse resp = this.queuer.removeUserFromQueue(request);
        
        db = DataAccessActivator.getNewSession();
        ses = (Session) db.get(Session.class, ses.getId());
       
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        db.close();
        
        assertNotNull(resp);
        InQueueType in = resp.getRemoveUserFromQueueResponse();
        assertNotNull(in);
        
        assertFalse(ses.isActive());
        assertNotNull(ses.getRemovalReason());
        assertNotNull(ses.getRemovalTime());
    }


    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailability() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test_perm_avail1");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityAllOffline() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(false);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(false);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(false);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityAllInSession() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(false);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        type.setCodeAssignable(true);
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(true);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertFalse(queue.getIsQueueable());
        assertTrue(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityNoRigs() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNull(targets);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRig() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test_perm_test");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRigOffline() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(false);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRigInSession() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        type.setCodeAssignable(true);
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertTrue(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityCaps() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        RigCapabilitiesDao dao = new RigCapabilitiesDao(db);
        RigCapabilities a = dao.addCapabilities("a");
        RigCapabilities ab = dao.addCapabilities("a,b");
        RigCapabilities abc = dao.addCapabilities("a,b,c");
        RequestCapabilitiesDao reqCapsDao = new RequestCapabilitiesDao(db);
        RequestCapabilities ra = reqCapsDao.addCapabilities("a");
        
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(a);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(ab);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(false);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(abc);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type);
        rig4.setRigCapabilities(abc);
        db.persist(rig4);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("CAPABILITY");
        perm.setRequestCapabilities(ra);
        db.persist(perm);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        dao.delete(a);
        dao.delete(ab);
        dao.delete(abc);
        reqCapsDao.delete(ra);
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(ra.getId().intValue(), res.getResourceID());
        assertEquals(ra.getCapabilities(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(4, targets.length);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityCapsFree() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        RigCapabilitiesDao dao = new RigCapabilitiesDao(db);
        RigCapabilities a = dao.addCapabilities("a");
        RigCapabilities ab = dao.addCapabilities("a,b");
        RigCapabilities abc = dao.addCapabilities("a,b,c");
        RigCapabilities df = dao.addCapabilities("d,f");
        RequestCapabilitiesDao reqCapsDao = new RequestCapabilitiesDao(db);
        RequestCapabilities ra = reqCapsDao.addCapabilities("a");
        
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(a);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(true);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(ab);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(abc);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type);
        rig4.setRigCapabilities(df);
        db.persist(rig4);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("CAPABILITY");
        perm.setRequestCapabilities(ra);
        db.persist(perm);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        dao.delete(a);
        dao.delete(ab);
        dao.delete(abc);
        dao.delete(df);
        reqCapsDao.delete(ra);
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(ra.getId().intValue(), res.getResourceID());
        assertEquals(ra.getCapabilities(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckResourceAvailability() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigType type2 = new RigType();
        type2.setName("rig_type_res_test1");
        db.persist(type2);
        RigCapabilities caps = new RigCapabilities("ad,de,df");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(false);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(false);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type2);
        rig4.setRigCapabilities(caps);
        db.persist(rig4);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("TYPE");
        resReq.setResourceID(type.getId().intValue());
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(caps);
        db.delete(type);
        db.delete(type2);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());

        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        
        targ = targets[2];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName()); 

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);

        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckResourceAvailabilityName() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigType type2 = new RigType();
        type2.setName("rig_type_res_test1");
        db.persist(type2);
        RigCapabilities caps = new RigCapabilities("ad,de,df");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(false);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(false);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type2);
        rig4.setRigCapabilities(caps);
        db.persist(rig4);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("TYPE");
        resReq.setResourceName(type.getName());
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(caps);
        db.delete(type);
        db.delete(type2);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());

        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        
        targ = targets[2];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName()); 

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);

        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckResourceAvailabilityRig() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("ad,de,df");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("RIG");
        resReq.setResourceID(rig1.getId().intValue());
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());

        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);

        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckResourceAvailabilityRigName() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("ad,de,df");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(false);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("RIG");
        resReq.setResourceName(rig1.getName());
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());

        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);

        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckResourceAvailabilityCaps() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        RigCapabilitiesDao dao = new RigCapabilitiesDao(db);
        RigCapabilities a = dao.addCapabilities("a");
        RigCapabilities ab = dao.addCapabilities("a,b");
        RigCapabilities abc = dao.addCapabilities("a,b,c");
        RigCapabilities df = dao.addCapabilities("d,f");
        RequestCapabilitiesDao reqCapsDao = new RequestCapabilitiesDao(db);
        RequestCapabilities ra = reqCapsDao.addCapabilities("a");
        
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(a);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(true);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(ab);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(abc);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type);
        rig4.setRigCapabilities(df);
        db.persist(rig4);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("CAPABILITY");
        perm.setRequestCapabilities(ra);
        db.persist(perm);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("CAPABILITY");
        resReq.setResourceID(ra.getId().intValue());
        
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        dao.delete(a);
        dao.delete(ab);
        dao.delete(abc);
        dao.delete(df);
        reqCapsDao.delete(ra);
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(ra.getId().intValue(), res.getResourceID());
        assertEquals(ra.getCapabilities(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityCapsName() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        RigCapabilitiesDao dao = new RigCapabilitiesDao(db);
        RigCapabilities a = dao.addCapabilities("a");
        RigCapabilities ab = dao.addCapabilities("a,b");
        RigCapabilities abc = dao.addCapabilities("a,b,c");
        RigCapabilities df = dao.addCapabilities("d,f");
        RequestCapabilitiesDao reqCapsDao = new RequestCapabilitiesDao(db);
        RequestCapabilities ra = reqCapsDao.addCapabilities("a");
        
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(a);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(true);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(ab);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(abc);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type);
        rig4.setRigCapabilities(df);
        db.persist(rig4);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("CAPABILITY");
        perm.setRequestCapabilities(ra);
        db.persist(perm);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckResourceAvailability request = new CheckResourceAvailability();
        ResourceIDType resReq = new ResourceIDType();
        request.setCheckResourceAvailability(resReq);
        resReq.setType("CAPABILITY");
        resReq.setResourceName(ra.getCapabilities());
        
        CheckResourceAvailabilityResponse resp = this.queuer.checkResourceAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        dao.delete(a);
        dao.delete(ab);
        dao.delete(abc);
        dao.delete(df);
        reqCapsDao.delete(ra);
        db.close();
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckResourceAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(ra.getId().intValue(), res.getResourceID());
        assertEquals(ra.getCapabilities(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#getUserQueuePosition(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition)}.
     */
    public void testGetUserQueuePosition() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        db.getTransaction().commit();
        
        RequestCapabilitiesDao dao = new RequestCapabilitiesDao(db);
        RequestCapabilities rCaps1 = dao.addCapabilities("rig,test");
        RequestCapabilities rCaps2 = dao.addCapabilities("type");
        RequestCapabilities rCaps3 = dao.addCapabilities("perm,rig");
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5work", "testns", "USER");
        db.persist(user5);
        User user6 = new User("qperm6", "testns", "USER");
        db.persist(user6);
        User user7 = new User("qperm7", "testns", "USER");
        db.persist(user7);
        User user8 = new User("qperm8", "testns", "USER");
        db.persist(user8);
        User user9 = new User("qperm9", "testns", "USER");
        db.persist(user9);
        User user10 = new User("qperm10", "testns", "USER");
        db.persist(user10);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        UserAssociation ass6 = new UserAssociation(new UserAssociationId(user6.getId(), uc1.getId()), uc1, user6);
        db.persist(ass6);
        UserAssociation ass7 = new UserAssociation(new UserAssociationId(user7.getId(), uc1.getId()), uc1, user7);
        db.persist(ass7);
        UserAssociation ass8 = new UserAssociation(new UserAssociationId(user8.getId(), uc1.getId()), uc1, user8);
        db.persist(ass8);
        UserAssociation ass9 = new UserAssociation(new UserAssociationId(user9.getId(), uc1.getId()), uc1, user9);
        db.persist(ass9);
        UserAssociation ass10 = new UserAssociation(new UserAssociationId(user10.getId(), uc1.getId()), uc1, user10);
        db.persist(ass10);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type1");
        rt.setCodeAssignable(false);
        db.persist(rt);

        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig2");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rCaps1.getId());
        ses3.setRequestedResourceName(rCaps1.getCapabilities());
        ses3.setResourceType("CAPABILITY");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt.getId());
        ses4.setRequestedResourceName(rt.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(before);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 300000));
        ses5.setRequestedResourceId(r.getId());
        ses5.setRequestedResourceName(r.getName());
        ses5.setResourceType("RIG");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        Session ses6 = new Session();
        ses6.setActive(true);
        ses6.setReady(true);
        ses6.setActivityLastUpdated(now);
        ses6.setExtensions((short) 5);
        ses6.setPriority((short) 6);
        ses6.setRequestTime(now);
        ses6.setRequestedResourceId(r.getId());
        ses6.setRequestedResourceName(r.getName());
        ses6.setResourceType("RIG");
        ses6.setResourcePermission(p1);
        ses6.setUser(user6);
        ses6.setUserName(user6.getName());
        ses6.setUserNamespace(user6.getNamespace());
        db.persist(ses6);
        Session ses7 = new Session();
        ses7.setActive(true);
        ses7.setReady(true);
        ses7.setActivityLastUpdated(now);
        ses7.setExtensions((short) 5);
        ses7.setPriority((short) 6);
        ses7.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses7.setRequestedResourceId(rCaps1.getId());
        ses7.setRequestedResourceName(rCaps1.getCapabilities());
        ses7.setResourceType("CAPABILITY");
        ses7.setResourcePermission(p1);
        ses7.setUser(user7);
        ses7.setUserName(user7.getName());
        ses7.setUserNamespace(user7.getNamespace());
        db.persist(ses7);
        Session ses8 = new Session();
        ses8.setActive(true);
        ses8.setReady(true);
        ses8.setActivityLastUpdated(now);
        ses8.setExtensions((short) 5);
        ses8.setPriority((short) 6);
        ses8.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses8.setRequestedResourceId(rCaps2.getId());
        ses8.setRequestedResourceName(rCaps2.getCapabilities());
        ses8.setResourceType("CAPABILITY");
        ses8.setResourcePermission(p1);
        ses8.setUser(user8);
        ses8.setUserName(user8.getName());
        ses8.setUserNamespace(user8.getNamespace());
        db.persist(ses8);
        Session ses9 = new Session();
        ses9.setActive(true);
        ses9.setReady(true);
        ses9.setActivityLastUpdated(now);
        ses9.setExtensions((short) 5);
        ses9.setPriority((short) 6);
        ses9.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses9.setRequestedResourceId(rt.getId());
        ses9.setRequestedResourceName(rt.getName());
        ses9.setResourceType("TYPE");
        ses9.setResourcePermission(p1);
        ses9.setUser(user9);
        ses9.setUserName(user9.getName());
        ses9.setUserNamespace(user9.getNamespace());
        db.persist(ses9);
        Session ses10 = new Session();
        ses10.setActive(true);
        ses10.setReady(true);
        ses10.setActivityLastUpdated(now);
        ses10.setExtensions((short) 5);
        ses10.setPriority((short) 6);
        ses10.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses10.setRequestedResourceId(rCaps3.getId());
        ses10.setRequestedResourceName(rCaps3.getCapabilities());
        ses10.setResourceType("CAPABILITY");
        ses10.setResourcePermission(p1);
        ses10.setUser(user10);
        ses10.setUserName(user10.getName());
        ses10.setUserNamespace(user10.getNamespace());
        db.persist(ses10);
        db.getTransaction().commit();
        db.close();
        
//        db.beginTransaction();
//        db.refresh(r);
//        db.refresh(rt);
//        db.refresh(caps);
//        db.refresh(rCaps1);
//        db.refresh(rCaps2);
//        db.refresh(rCaps3);
//        db.refresh(user5);
//        db.getTransaction().commit();
        
        db = DataAccessActivator.getNewSession();
        ses1 = (Session)db.merge(ses1);
        ses2 = (Session)db.merge(ses2);
        ses3 = (Session)db.merge(ses3);
        ses4 = (Session)db.merge(ses4);
        ses5 = (Session)db.merge(ses5);
        ses6 = (Session)db.merge(ses6);
        ses7 = (Session)db.merge(ses7);
        ses8 = (Session)db.merge(ses8);
        ses9 = (Session)db.merge(ses9);
        ses10 = (Session)db.merge(ses10);
        
        Queue.getInstance().addEntry(ses1, db);
        Queue.getInstance().addEntry(ses2, db);
        Queue.getInstance().addEntry(ses3, db);
        Queue.getInstance().addEntry(ses4, db);
        Queue.getInstance().addEntry(ses5, db);
        Queue.getInstance().addEntry(ses6, db);
        Queue.getInstance().addEntry(ses7, db);
        Queue.getInstance().addEntry(ses8, db);
        Queue.getInstance().addEntry(ses9, db);
        Queue.getInstance().addEntry(ses10, db);
        
        /* Request parameters. */
        
        GetUserQueuePosition request = new GetUserQueuePosition();
        UserIDType uid = new UserIDType();
        request.setGetUserQueuePosition(uid);
        uid.setUserQName(user5.getNamespace() + ':' + user5.getName());
        
        GetUserQueuePositionResponse resp = this.queuer.getUserQueuePosition(request);
        
        r = (Rig) db.get(Rig.class, r.getId());
        rt = (RigType) db.get(RigType.class, rt.getId());
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(ses6);
        db.delete(ses7);
        db.delete(ses8);
        db.delete(ses9);
        db.delete(ses10);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(ass6);
        db.delete(ass7);
        db.delete(ass8);
        db.delete(ass9);
        db.delete(ass10);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.delete(user6);
        db.delete(user7);
        db.delete(user8);
        db.delete(user9);
        db.delete(user10);
        db.getTransaction().commit();
        
        dao = new RequestCapabilitiesDao(db);
        rCaps1 = (RequestCapabilities)db.merge(rCaps1);
        rCaps2 = (RequestCapabilities)db.merge(rCaps2);
        rCaps3 = (RequestCapabilities)db.merge(rCaps3);
        dao.delete(rCaps1);
        dao.delete(rCaps2);
        dao.delete(rCaps3);
        db.beginTransaction();
        caps = (RigCapabilities)db.merge(caps);
        db.delete(caps);
        db.getTransaction().commit();
        db.close();
        
        UserQueueType q = resp.getGetUserQueuePositionResponse();
        assertNotNull(q);
        assertTrue(q.getInQueue());
        assertFalse(q.getInSession());
        assertFalse(q.getQueueSuccessful());
        assertNull(q.getAssignedResource());
        assertEquals(1, q.getPosition());
        int time = Math.round((System.currentTimeMillis() - ses5.getRequestTime().getTime()) / 60000);
        assertEquals(time, q.getTime());
        
        ResourceIDType res = q.getQueuedResouce();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        QueueType qt = q.getQueue();
        assertNotNull(qt);
        assertTrue(qt.getIsQueueable());
        assertTrue(qt.getViable());
        assertFalse(qt.getHasFree());
        assertFalse(qt.getIsCodeAssignable());
        
        res = qt.getQueuedResource();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        QueueTargetType targets[] = qt.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        
        assertTrue(targets[0].getViable());
        assertFalse(targets[0].getIsFree());
        res = targets[0].getResource();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        OMElement ele = resp.getOMElement(GetUserQueuePositionResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#getUserQueuePosition(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition)}.
     */
    public void testGetUserQueuePositionAssigned() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm12", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);

        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        rt.setCodeAssignable(false);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("foo,bar,baz");
        db.persist(caps);

        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        db.refresh(r);
        db.refresh(rt);
        db.refresh(caps);
        
        Queue.getInstance().addEntry(ses1, db);
        
        /* Request parameters. */
        
        GetUserQueuePosition request = new GetUserQueuePosition();
        UserIDType uid = new UserIDType();
        request.setGetUserQueuePosition(uid);
        uid.setUserQName(user1.getNamespace() + ':' + user1.getName());
        
        GetUserQueuePositionResponse resp = this.queuer.getUserQueuePosition(request);
        
        UserQueueType q = resp.getGetUserQueuePositionResponse();
        assertNotNull(q);
        assertTrue(q.getInQueue());
        assertFalse(q.getInSession());
        assertFalse(q.getQueueSuccessful());
        assertNull(q.getAssignedResource());
        assertEquals(1, q.getPosition());
        int time = Math.round((System.currentTimeMillis() - ses1.getRequestTime().getTime()) / 60000);
        assertEquals(time, q.getTime());
        
        ResourceIDType res = q.getQueuedResouce();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        QueueType qt = q.getQueue();
        assertNotNull(qt);
        assertTrue(qt.getIsQueueable());
        assertTrue(qt.getViable());
        assertFalse(qt.getHasFree());
        assertFalse(qt.getIsCodeAssignable());
        
        res = qt.getQueuedResource();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        QueueTargetType targets[] = qt.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        
        assertTrue(targets[0].getViable());
        assertFalse(targets[0].getIsFree());
        res = targets[0].getResource();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
        Queue.getInstance().runRigAssignment(r.getId(), db);
        resp = this.queuer.getUserQueuePosition(request);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit();
        db.close();
        
        q = resp.getGetUserQueuePositionResponse();
        assertNotNull(q);
        assertFalse(q.getInQueue());
        assertTrue(q.getInSession());
        assertFalse(q.getQueueSuccessful());
        assertNull(q.getQueuedResouce());
        assertEquals(0, q.getPosition());
        
        time = Math.round((System.currentTimeMillis() - ses1.getAssignmentTime().getTime()) / 60000);
        assertEquals(time, q.getTime());
        
        res = q.getAssignedResource();
        assertNotNull(res);
        assertEquals(r.getId().intValue(), res.getResourceID());
        assertEquals(r.getName(), res.getResourceName());
        assertEquals("RIG", res.getType());
        
        qt = q.getQueue();
        assertNull(qt);
        
        OMElement ele = resp.getOMElement(GetUserQueuePositionResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#getUserQueuePosition(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition)}.
     */
    public void testGetUserQueuePositionNotInQueue() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);       
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);

        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        rt.setCodeAssignable(false);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("foo,bar,baz");
        db.persist(caps);

        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(false);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(before);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(before);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        db.getTransaction().commit();
        
        /* Request parameters. */
        
        GetUserQueuePosition request = new GetUserQueuePosition();
        UserIDType uid = new UserIDType();
        request.setGetUserQueuePosition(uid);
        uid.setUserQName(user1.getNamespace() + ':' + user1.getName());
        
        GetUserQueuePositionResponse resp = this.queuer.getUserQueuePosition(request);

        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit();
        db.close();
        
        UserQueueType q = resp.getGetUserQueuePositionResponse();
        assertNotNull(q);
        assertFalse(q.getInQueue());
        assertFalse(q.getInSession());
        assertFalse(q.getQueueSuccessful());
        assertNull(q.getQueuedResouce());
        assertNull(q.getAssignedResource());
        assertNull(q.getQueue());
        assertEquals(-1, q.getPosition());
        assertEquals(0, q.getTime());
        
        OMElement ele = resp.getOMElement(GetUserQueuePositionResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueue() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNSName() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserQName(user.getNamespace() + ":" + user.getName());
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));   
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueActive() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e");
        db.persist(caps);
        Rig rig = new Rig();
        rig.setName("in_ses_test");
        rig.setContactUrl("foo:///");
        rig.setInSession(true);
        rig.setLastUpdateTimestamp(new Date());
        rig.setRigCapabilities(caps);
        rig.setRigType(type);
        db.persist(rig);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        ses.setAssignmentTime(now);
        ses.setRig(rig);
        ses.setAssignedRigName(rig.getName());
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(rig);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertTrue(queue.getInSession());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        ResourceIDType res = queue.getAssignedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig.getName(), res.getResourceName());
        assertEquals(rig.getId().intValue(), res.getResourceID());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueTwoSes() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        Session ses2 = new Session();
        ses2.setActive(false);
        ses2.setActivityLastUpdated(now);
        ses2.setPriority((short) 1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setRequestedResourceId(type.getId());
        ses2.setRequestedResourceName(type.getName());
        ses2.setResourcePermission(perm);
        ses2.setResourceType("TYPE");
        db.persist(ses2);
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNot() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type_perm_test");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(false);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNoSessions() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNoUser() throws Exception
    {   
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserQName("ns:does_not_exist");
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    @Test
    public void testGetUserFromUserID() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserID(String.valueOf(user.getId()));
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDNmNsSeq() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        UserNSNameSequence seq = new UserNSNameSequence();
        seq.setUserNamespace(user.getNamespace());
        seq.setUserName(user.getName());
        uid.setUserNSNameSequence(seq);
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDQName() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ":" + user.getName());
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDNotExist() throws Exception
    {
        UserIDType uid = new UserIDType();
        uid.setUserQName("QU_TEST:does_not_exist");
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        assertNull(meth.invoke(this.queuer, uid, ses));
        
        ses.close();
    }

}