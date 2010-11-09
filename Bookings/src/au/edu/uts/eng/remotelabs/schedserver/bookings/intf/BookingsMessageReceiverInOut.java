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

package au.edu.uts.eng.remotelabs.schedserver.bookings.intf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.util.JavaUtils;

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

/**
 * Bookings message receiver.
 */
public class BookingsMessageReceiverInOut extends AbstractInOutMessageReceiver
{

    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final BookingsInterface skel = (BookingsInterface) obj;

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("deleteBookings".equals(methodName))
                {
                    final DeleteBookings wrappedParam = (DeleteBookings) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), DeleteBookings.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    DeleteBookingsResponse response = skel.deleteBookings(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("findFreeBookings".equals(methodName))
                {


                    final FindBookingSlots wrappedParam = (FindBookingSlots) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), FindBookingSlots.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    FindFreeBookingsResponse response = skel.findFreeBookings(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("getBooking".equals(methodName))
                {
                    final GetBooking wrappedParam = (GetBooking) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetBooking.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetBookingResponse response = skel.getBooking(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("createBooking".equals(methodName))
                {
                    final CreateBooking wrappedParam = (CreateBooking) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), CreateBooking.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    CreateBookingResponse response = skel.createBooking(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("getBookings".equals(methodName))
                {
                    final GetBookings wrappedParam = (GetBookings) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(), GetBookings.class,
                                    this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetBookingsResponse response = skel.getBookings(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else
                {
                    throw new RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteBookingsResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteBookingsResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FindFreeBookingsResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FindFreeBookingsResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetBookingResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetBookingResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CreateBookingResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CreateBookingResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetBookingsResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetBookingsResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class<?> type, final Map<String, String> extraNamespaces) throws AxisFault
    {

        try
        {

            if (DeleteBookings.class.equals(type))
            {
                return DeleteBookings.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteBookingsResponse.class.equals(type))
            {
                return DeleteBookingsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FindBookingSlots.class.equals(type))
            {
                return FindBookingSlots.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FindFreeBookingsResponse.class.equals(type))
            {
                return FindFreeBookingsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBooking.class.equals(type))
            {
                return GetBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBookingResponse.class.equals(type))
            {
                return GetBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CreateBooking.class.equals(type))
            {
                return CreateBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CreateBookingResponse.class.equals(type))
            {
                return CreateBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBookings.class.equals(type))
            {
                return GetBookings.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBookingsResponse.class.equals(type))
            {
                return GetBookingsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    private Map<String, String> getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map<String, String> returnMap = new HashMap<String, String>();
        final Iterator<?> namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}