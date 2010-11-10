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
 * @date 9th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.intf;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingsRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.DeleteBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.DeleteBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindBookingSlots;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Bookings interface implementation.
 */
public class BookingsService implements BookingsInterface
{
    /** Logger. */
    private Logger logger;
    
    public BookingsService()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public CreateBookingResponse createBooking(CreateBooking createBooking)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeleteBookingsResponse deleteBookings(DeleteBookings deleteBookings)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FindFreeBookingsResponse findFreeBookings(FindBookingSlots findBookingSlots)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetBookingResponse getBooking(GetBooking getBooking)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetBookingsResponse getBookings(GetBookings getBookings)
    {
        BookingsRequestType request = getBookings.getGetBookings();
        String debug = "Received " + this.getClass().getSimpleName() + "#getBookings with params:";
        if (request.getUserID() != null) debug += " user ID=" + request.getUserID().getUserID() + ", user name=" +
                request.getUserID().getUserName() + ", user namespace=" + request.getUserID().getUserNamespace() +
                " user QName=" + request.getUserID().getUserQName();
        if (request.getPermissionID() != null) debug += " permission ID=" + request.getPermissionID().getPermissionID();
        if (request.getResourceID() != null) debug += "resource type=" + request.getResourceID().getType() + 
                ", resource ID=" + request.getResourceID().getResourceID() + ", resource name=" + 
                request.getResourceID().getResourceName();
        debug += " show cancelled=" + request.showCancelled() + " show finished=" + request.showFinished();
        this.logger.debug(debug);
        
        GetBookingsResponse response = new GetBookingsResponse();
        BookingListType bookings = new BookingListType();
        response.setGetBookingsResponse(bookings);
        
        BookingsDao dao = new BookingsDao();
        try
        {
            Session ses = dao.getSession();
            Criteria cri = ses.createCriteria(Bookings.class);
            
            /* If user specificed, add that to query. */
            User user = null;
            if (request.getUserID() != null && (user = this.getUserFromUserID(request.getUserID(), ses)) != null)
            {
                cri.add(Restrictions.eq("user", user));
            }
            
            /* If permission was specified, add that to query. */
            if (request.getPermissionID() != null)
            {
                cri.add(Restrictions.eq("resourcePermission.id", Long.valueOf(request.getPermissionID().getPermissionID())));
            }
            
            /* If resource was specified, add that to query. */
            ResourceIDType rid = request.getResourceID();
            if (rid != null)
            {
                if (ResourcePermission.RIG_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION));
                    if (rid.getResourceID() > 0) cri.add(Restrictions.eq("rig.id", Long.valueOf(rid.getResourceID())));
                    if (rid.getResourceName() != null) cri.add(Restrictions.eq("rig.name", rid.getResourceName()));
                }
                else if (ResourcePermission.TYPE_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.TYPE_PERMISSION));
                    if (rid.getResourceID() > 0) cri.add(Restrictions.eq("rigType.id", Long.valueOf(rid.getResourceID())));
                    if (rid.getResourceName() != null) cri.add(Restrictions.eq("rigType.name", rid.getResourceName()));
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.CAPS_PERMISSION));
                    if (rid.getResourceID() > 0)
                    {
                        cri.add(Restrictions.eq("requestCapabilities.id", Long.valueOf(rid.getResourceID())));
                    }
                    if (rid.getResourceName() != null)
                    {
                        cri.add(Restrictions.eq("requestCapabilities.capabilities", rid.getResourceName()));
                    }
                }
                else
                {
                    this.logger.warn("Not added a resource restriction to existing booking search because the " +
                    		"resourece type '" + rid.getType() + "' is not one of '" + 
                    		ResourcePermission.RIG_PERMISSION + "' '" + ResourcePermission.TYPE_PERMISSION + "' '" + 
                    		ResourcePermission.CAPS_PERMISSION + "'.");
                }
            }
            
            /* Other constraints specified. */
            if (!request.showCancelled() && !request.showFinished())
            {
                cri.add(Restrictions.eq("active", Boolean.TRUE));
            }
            else if (!request.showFinished())
            {
                cri.add(Restrictions.isNotNull("cancelReason"));
            }
            else if (!request.showCancelled())
            {
                cri.add(Restrictions.isNull("cancelReason"));
            }
            
            @SuppressWarnings("unchecked")
            List<Bookings> list = cri.list();
            for (Bookings b : list)
            {
                BookingType bt = new BookingType();
                bt.setBookingID(b.getId().intValue());
                
                ResourceIDType resource = new ResourceIDType();
                resource.setType(b.getResourceType());
                if (ResourcePermission.RIG_PERMISSION.equals(b.getResourceType()))
                {
                    resource.setResourceID(b.getRig().getId().intValue());
                    resource.setResourceName(b.getRig().getName());
                }
                else if (ResourcePermission.TYPE_PERMISSION.equals(b.getResourceType()))
                {
                    resource.setResourceID(b.getRigType().getId().intValue());
                    resource.setResourceName(b.getRigType().getName());
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(b.getResourceType()))
                {
                    resource.setResourceID(b.getRequestCapabilities().getId().intValue());
                    resource.setResourceName(b.getRequestCapabilities().getCapabilities());
                }
                bt.setBookedResource(resource);
                
                PermissionIDType perm = new PermissionIDType();
                perm.setPermissionID(b.getResourcePermission().getId().intValue());
                bt.setPermissionID(perm);
                
                Calendar start = Calendar.getInstance();
                start.setTime(b.getStartTime());
                bt.setStartTime(start);
                Calendar end = Calendar.getInstance();
                end.setTime(b.getEndTime());
                bt.setEndTime(end);
                bt.setDuration(b.getDuration());
                
                bt.setIsFinished(!b.isActive());
                if (b.getCancelReason() == null)
                {
                    bt.setIsCancelled(false);
                }
                else
                {
                    bt.setIsCancelled(true);
                    bt.setCancelReason(b.getCancelReason());
                }
                
                if (b.getCodeReference() == null)
                {
                    bt.setCodeReference(b.getCodeReference());
                }
                
                bookings.addBookings(bt);
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }

    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uid, Session ses)
    {
        UserDao dao = new UserDao(ses);
        User user;
        
        long recordId = this.getIdentifier(uid.getUserID());
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        
        if (recordId > 0 && (user = dao.get(recordId)) != null)
        {
            return user;
        }
        else if (ns != null && nm != null && (user = dao.findByName(ns, nm)) != null)
        {
            return user;
        }
        
        return null;
    }
    
    /**
     * Converts string identifiers to a long.
     * 
     * @param idStr string containing a long  
     * @return long or 0 if identifier not valid
     */
    private long getIdentifier(String idStr)
    {
        if (idStr == null) return 0;
        
        try
        {
            return Long.parseLong(idStr);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }
}
