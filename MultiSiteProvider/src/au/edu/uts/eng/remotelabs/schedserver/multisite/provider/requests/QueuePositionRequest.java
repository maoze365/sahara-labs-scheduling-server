/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 11th August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;

/**
 * Makes a queue position request
 */
public class QueuePositionRequest extends AbstractRequest
{
    /** Response. */
    private QueueType response;
    
    public boolean getQueuePosition(User user, RemoteSite site, Session db)
    {
        this.site = site;
        this.session = db;
        
        GetQueuePosition request = new GetQueuePosition();
        UserIDType userId = new UserIDType();
        this.addSiteID(userId);
        userId.setUserID(user.getName());
        request.setGetQueuePosition(userId);
        
        try
        {
            this.response = this.getStub().getQueuePosition(request).getGetQueuePositionResponse();
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        
        return true;
    }
    
    public boolean isInQueue()
    {
        return this.response.getInQueue();
    }
    
    public boolean isInBooking()
    {
        return this.response.getInBooking();
    }
    
    public boolean isInSession()
    {
        return this.response.getInSession();
    }
    
    public int getPosition()
    {
        return this.response.getPosition();
    }
    
    public int getTime()
    {
        return this.response.getTime();
    }
    
    public String getQueueResourceType()
    {
        return this.response.getQueuedResource() == null ? null : this.response.getQueuedResource().getType();
    }
    
    public String getQueuedResourceName()
    {
        return this.response.getQueuedResource() == null ? null : this.response.getQueuedResource().getName();
    }
}
