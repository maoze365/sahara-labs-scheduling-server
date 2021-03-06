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
 * @date 17th July 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf;

import java.rmi.RemoteException;

import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AddToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CheckAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetUserStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetUserStatusResponse;

/**
 * MultiSite client interface.
 */
public interface MultiSite
{
    /**
     * Gets the status of the user including whether they are in a queue, in
     * a booking or in session.
     * 
     * @param getUserStatus request
     * @return response	
     */
    public GetUserStatusResponse getUserStatus(GetUserStatus getUserStatus) throws RemoteException;

    /**
     * Checks the availability of a permission to determine whether a user may
     * use the permissions resources.
     * 
     * @param checkAvailability request
     * @return response
     */
    public CheckAvailabilityResponse checkAvailability(CheckAvailability checkAvailability) throws RemoteException;

    /**
     * Finds the times a resource may be booked within a time period.
     * 
     * @param findFreeBookings request 
     * @return response
     */
    public FindFreeBookingsResponse findFreeBookings(FindFreeBookings findFreeBookings) throws RemoteException;

    /**
     * Gets the session information for a in-progress session.
     * 
     * @param getSessionInformation request
     * @return response
     */
    public GetSessionInformationResponse getSessionInformation(GetSessionInformation getSessionInformation) 
            throws RemoteException;

    /**
     * Gets the users position in the queue.
     * 
     * @param getQueuePosition request
     * @return response
     */
    public GetQueuePositionResponse getQueuePosition(GetQueuePosition getQueuePosition) throws RemoteException;

    /**
     * Adds a user to the queue.
     * 
     * @param addToQueue request
     * @return response
     */
    public AddToQueueResponse addToQueue(AddToQueue addToQueue) throws RemoteException;

    /**
     * Creates a booking for a user.
     * 
     * @param createBooking request
     * @return response
     */
    public CreateBookingResponse createBooking(CreateBooking createBooking) throws RemoteException;

    /**
     * Cancels a booking.
     * 
     * @param cancelBooking request 
     * @return response
     */
    public CancelBookingResponse cancelBooking(CancelBooking cancelBooking) throws RemoteException;

    /**
     * Finishes a session.
     * 
     * @param finishSession request
     * @return response
     */
    public FinishSessionResponse finishSession(FinishSession finishSession) throws RemoteException;
}
