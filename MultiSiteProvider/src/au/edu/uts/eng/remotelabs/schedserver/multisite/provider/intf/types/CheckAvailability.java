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

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;

/**
 * Check availability bean class.
 */
public class CheckAvailability implements ADBBean
{
    private static final long serialVersionUID = 8111197741188805212L;

    public static final QName MY_QNAME = new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
            "checkAvailability", "ns1");

    protected PermissionIDType checkAvailability;

    public PermissionIDType getCheckAvailability()
    {
        return this.checkAvailability;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, CheckAvailability.MY_QNAME);
        return factory.createOMElement(dataSource, CheckAvailability.MY_QNAME);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.checkAvailability.getPullParser(CheckAvailability.MY_QNAME);
    }

    @Override
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter) throws XMLStreamException,
            ADBException
    {
        this.serialize(parentQName, xmlWriter, false);
    }

    @Override
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter, final boolean serializeType)
            throws XMLStreamException, ADBException
    {
        if (this.checkAvailability == null)
        {
            throw new ADBException("checkAvailability cannot be null!");
        }
        this.checkAvailability.serialize(CheckAvailability.MY_QNAME, xmlWriter);
    }

    public void setCheckAvailability(final PermissionIDType param)
    {
        this.checkAvailability = param;
    }

    public static class Factory
    {
        public static CheckAvailability parse(final XMLStreamReader reader) throws Exception
        {
            final CheckAvailability object = new CheckAvailability();

            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }


                while (!reader.isEndElement())
                {
                    if (reader.isStartElement())
                    {
                        if (reader.isStartElement()
                                && new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                        "checkAvailability").equals(reader.getName()))
                        {
                            object.setCheckAvailability(PermissionIDType.Factory.parse(reader));
                        }
                        else
                        {
                            throw new ADBException("Unexpected subelement " + reader.getName());
                        }
                    }
                    else
                    {
                        reader.next();
                    }
                }
            }
            catch (final XMLStreamException e)
            {
                throw new Exception(e);
            }

            return object;
        }
    }
}
