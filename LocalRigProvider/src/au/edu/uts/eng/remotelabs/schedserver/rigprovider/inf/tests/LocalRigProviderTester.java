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
 * @date 19th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilitiesId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.IdentityTokenRegister;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.ProviderResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.StatusType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigType;

/**
 * Tests the {@link LocalRigProvider} class.
 */
public class LocalRigProviderTester extends TestCase
{
    /** Object of class under test. */
    private LocalRigProvider provider;
    
    public LocalRigProviderTester(String name) throws Exception
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
    public void setUp() throws Exception
    {
        IdentityTokenRegister.getInstance().expunge();
        this.provider = new LocalRigProvider();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#registerRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig)}.
     * @throws MalformedURIException 
     */
    @Test
    public void testRegisterRig() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RegisterRig reg = new RegisterRig();
        RegisterRigType regType = new RegisterRigType();
        reg.setRegisterRig(regType);
        StatusType status = new StatusType();
        regType.setStatus(status);
        
        regType.setName(name);
        regType.setType(type);
        regType.setCapabilities(caps);
        regType.setContactUrl(new URI(contactUrl));
        status.setIsOnline(true);
    
        RegisterRigResponse resp = this.provider.registerRig(reg);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(new RigDao(ses).findByName(name));
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRegisterRigResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNotNull(prov.getIdentityToken());
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(RegisterRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertTrue(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#registerRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig)}.
     * @throws MalformedURIException 
     */
    @Test
    public void testRegisterRigError() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, null, false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RegisterRig reg = new RegisterRig();
        RegisterRigType regType = new RegisterRigType();
        reg.setRegisterRig(regType);
        StatusType status = new StatusType();
        regType.setStatus(status);
        
        regType.setName(name);
        regType.setType(type);
        regType.setCapabilities(caps);
        regType.setContactUrl(new URI(contactUrl));
        status.setIsOnline(true);
    
        RegisterRigResponse resp = this.provider.registerRig(reg);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(new RigDao(ses).findByName(name));
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRegisterRigResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(RegisterRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#updateRigStatus(au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus)}.
     */
    @Test
    public void testUpdateRigStatus() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, "Is broken", false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        UpdateRigStatus up = new UpdateRigStatus();
        UpdateRigType upTy = new UpdateRigType();
        up.setUpdateRigStatus(upTy);
        upTy.setName(name);
        StatusType st = new StatusType();
        upTy.setStatus(st);
        st.setIsOnline(true);
    
        UpdateRigStatusResponse resp = this.provider.updateRigStatus(up);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(new RigDao(ses).findByName(name));
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getUpdateRigStatusResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNotNull(prov.getIdentityToken()); 
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertTrue(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#updateRigStatus(au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus)}.
     */
    @Test
    public void testUpdateRigStatusError() throws Exception
    {        
        String name = "lb1";
               
        UpdateRigStatus up = new UpdateRigStatus();
        UpdateRigType upTy = new UpdateRigType();
        up.setUpdateRigStatus(upTy);
        upTy.setName(name);
        StatusType st = new StatusType();
        upTy.setStatus(st);
    
        UpdateRigStatusResponse resp = this.provider.updateRigStatus(up);
        assertNotNull(resp);
       
        ProviderResponse prov = resp.getUpdateRigStatusResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#removeRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus)}.
     */
    @Test
    public void testRemoveRig() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, "Is broken", false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RemoveRig rm = new RemoveRig();
        RemoveRigType rmTy = new RemoveRigType();
        rm.setRemoveRig(rmTy);
        rmTy.setName(name);
        rmTy.setRemovalReason("shutting down");
        
        RemoveRigResponse resp = this.provider.removeRig(rm);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(new RigDao(ses).findByName(name));
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRemoveRigResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.LocalRigProvider#removeRig(RemoveRig)}.
     */
    @Test
    public void testRemoveRigError() throws Exception
    {        
        RemoveRig rm = new RemoveRig();
        RemoveRigType rmTy = new RemoveRigType();
        rm.setRemoveRig(rmTy);
        rmTy.setName("lb1");
        rmTy.setRemovalReason("Doesn't exist");
    
        RemoveRigResponse resp = this.provider.removeRig(rm);
        assertNotNull(resp);
       
        ProviderResponse prov = resp.getRemoveRigResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(RemoveRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
}