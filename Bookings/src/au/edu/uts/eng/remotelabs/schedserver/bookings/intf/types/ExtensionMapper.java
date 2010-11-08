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
 * @date 8th November 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

/**
 * ExtensionMapper class.
 */
public class ExtensionMapper
{

    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {
        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "state_type1".equals(typeName))
        {
            return SlotState.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "UserIDType".equals(typeName))
        {
            return UserIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "ResourceIDType".equals(typeName))
        {
            return ResourceIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingsRequestType".equals(typeName))
        {
            return BookingsRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingType".equals(typeName))
        {
            return BookingType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingResponseType".equals(typeName))
        {
            return BookingResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingIDType".equals(typeName))
        {
            return BookingIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingSlotType".equals(typeName))
        {
            return BookingSlotType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "CreateBookingType".equals(typeName))
        {
            return CreateBookingType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "TimePeriodType".equals(typeName))
        {
            return TimePeriodType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "PermissionIDType".equals(typeName))
        {
            return PermissionIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingRequestType".equals(typeName))
        {
            return BookingRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingListType".equals(typeName))
        {
            return BookingListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "DeleteBookingType".equals(typeName))
        {
            return DeleteBookingType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "BookingSlotListType".equals(typeName))
        {
            return BookingSlotListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/bookings".equals(namespaceURI)
                && "FindBookingSlotType".equals(typeName))
        {
            return FindBookingSlotType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }

}
