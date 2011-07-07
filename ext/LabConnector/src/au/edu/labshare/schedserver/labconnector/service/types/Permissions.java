/**
 * SAHARA Scheduling Server - LabConnector
 * Class type for the web service.
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
 * @author Herbert Yeung
 * @date 27th May 2010
 */
/**
 * Permissions.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.4 Built
 * on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.labshare.schedserver.labconnector.service.types;

/**
 * Permissions bean class
 */
@SuppressWarnings({"unchecked", "unused", "serial"})
public class Permissions implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had name =
     * Permissions Namespace URI = http://labshare.edu.au:8080/LabConnector/
     * Namespace Prefix = ns1
     */

    private static java.lang.String generatePrefix(java.lang.String namespace)
    {
        if (namespace.equals("http://labshare.edu.au:8080/LabConnector/"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for UserID
     */

    protected java.lang.String localUserID;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserID()
    {
        return localUserID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserID
     */
    public void setUserID(java.lang.String param)
    {

        this.localUserID = param;

    }

    /**
     * field for UserGroup This was an Array!
     */

    protected java.lang.String[] localUserGroup;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getUserGroup()
    {
        return localUserGroup;
    }

    /**
     * validate the array for UserGroup
     */
    protected void validateUserGroup(java.lang.String[] param)
    {

        if ((param != null) && (param.length < 1))
        {
            throw new java.lang.RuntimeException();
        }

    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserGroup
     */
    public void setUserGroup(java.lang.String[] param)
    {

        validateUserGroup(param);

        this.localUserGroup = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param
     *            java.lang.String
     */
    public void addUserGroup(java.lang.String param)
    {
        if (localUserGroup == null)
        {
            localUserGroup = new java.lang.String[] {};
        }

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil
                .toList(localUserGroup);
        list.add(param);
        this.localUserGroup = (java.lang.String[]) list
                .toArray(new java.lang.String[list.size()]);

    }

    /**
     * field for ExperimentID
     */

    protected int localExperimentID;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getExperimentID()
    {
        return localExperimentID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ExperimentID
     */
    public void setExperimentID(int param)
    {

        this.localExperimentID = param;

    }

    /**
     * field for RunExperiment
     */

    protected boolean localRunExperiment;

    /*
     * This tracker boolean wil be used to detect whether the user called the
     * set method for this attribute. It will be used to determine whether to
     * include this field in the serialized XML
     */
    protected boolean localRunExperimentTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getRunExperiment()
    {
        return localRunExperiment;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RunExperiment
     */
    public void setRunExperiment(boolean param)
    {

        // setting primitive attribute tracker to true

        localRunExperimentTracker = true;
        this.localRunExperiment = param;

    }

    /**
     * field for ObserveExperiment
     */

    protected boolean localObserveExperiment;

    /*
     * This tracker boolean wil be used to detect whether the user called the
     * set method for this attribute. It will be used to determine whether to
     * include this field in the serialized XML
     */
    protected boolean localObserveExperimentTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getObserveExperiment()
    {
        return localObserveExperiment;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ObserveExperiment
     */
    public void setObserveExperiment(boolean param)
    {

        // setting primitive attribute tracker to true

       localObserveExperimentTracker = true;
       this.localObserveExperiment = param;

    }

    /**
     * field for ModifyExperiment
     */

    protected boolean localModifyExperiment;

    /*
     * This tracker boolean wil be used to detect whether the user called the
     * set method for this attribute. It will be used to determine whether to
     * include this field in the serialized XML
     */
    protected boolean localModifyExperimentTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getModifyExperiment()
    {
        return localModifyExperiment;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ModifyExperiment
     */
    public void setModifyExperiment(boolean param)
    {

        // setting primitive attribute tracker to true

       localModifyExperimentTracker = true;
       this.localModifyExperiment = param;

    }

    /**
     * field for StoreExperimentResults
     */

    protected boolean localStoreExperimentResults;

    /*
     * This tracker boolean wil be used to detect whether the user called the
     * set method for this attribute. It will be used to determine whether to
     * include this field in the serialized XML
     */
    protected boolean localStoreExperimentResultsTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getStoreExperimentResults()
    {
        return localStoreExperimentResults;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            StoreExperimentResults
     */
    public void setStoreExperimentResults(boolean param)
    {

        // setting primitive attribute tracker to true
        localStoreExperimentResultsTracker = true;
        this.localStoreExperimentResults = param;

    }

    /**
     * field for AuthorizeUsers
     */

    protected boolean localAuthorizeUsers;

    /*
     * This tracker boolean wil be used to detect whether the user called the
     * set method for this attribute. It will be used to determine whether to
     * include this field in the serialized XML
     */
    protected boolean localAuthorizeUsersTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getAuthorizeUsers()
    {
        return localAuthorizeUsers;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AuthorizeUsers
     */
    public void setAuthorizeUsers(boolean param)
    {

        // setting primitive attribute tracker to true
        localAuthorizeUsersTracker = true;
        this.localAuthorizeUsers = param;

    }

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(
            javax.xml.stream.XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;

        try
        {
            isReaderMTOMAware = java.lang.Boolean.TRUE
                    .equals(reader
                            .getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (java.lang.IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     * 
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory)
            throws org.apache.axis2.databinding.ADBException
    {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
                this, parentQName)
        {

            public void serialize(
                    org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                Permissions.this.serialize(parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
                parentQName, factory, dataSource);

    }

    public void serialize(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {
        serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
            boolean serializeType) throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0))
        {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null)
            {
                xmlWriter.writeStartElement(namespace, parentQName
                        .getLocalPart());
            }
            else
            {
                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(),
                        namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        else
        {
            xmlWriter.writeStartElement(parentQName.getLocalPart());
        }

        if (serializeType)
        {

            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://labshare.edu.au:8080/LabConnector/");
            if ((namespacePrefix != null)
                    && (namespacePrefix.trim().length() > 0))
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        namespacePrefix + ":Permissions", xmlWriter);
            }
            else
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "Permissions", xmlWriter);
            }

        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "userID", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "userID");
            }

        }
        else
        {
            xmlWriter.writeStartElement("userID");
        }

        if (localUserID == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "userID cannot be null!!");

        }
        else
        {

            xmlWriter.writeCharacters(localUserID);

        }

        xmlWriter.writeEndElement();

        if (localUserGroup != null)
        {
            namespace = "";
            boolean emptyNamespace = namespace == null
                    || namespace.length() == 0;
            prefix = emptyNamespace ? null : xmlWriter.getPrefix(namespace);
            for (int i = 0; i < localUserGroup.length; i++)
            {

                if (localUserGroup[i] != null)
                {

                    if (!emptyNamespace)
                    {
                        if (prefix == null)
                        {
                            java.lang.String prefix2 = generatePrefix(namespace);

                            xmlWriter.writeStartElement(prefix2, "userGroup",
                                    namespace);
                            xmlWriter.writeNamespace(prefix2, namespace);
                            xmlWriter.setPrefix(prefix2, namespace);

                        }
                        else
                        {
                            xmlWriter.writeStartElement(namespace, "userGroup");
                        }

                    }
                    else
                    {
                        xmlWriter.writeStartElement("userGroup");
                    }

                    xmlWriter
                            .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(localUserGroup[i]));

                    xmlWriter.writeEndElement();

                }
                else
                {

                    throw new org.apache.axis2.databinding.ADBException(
                            "userGroup cannot be null!!");

                }

            }
        }
        else
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "userGroup cannot be null!!");

        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "experimentID", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "experimentID");
            }

        }
        else
        {
            xmlWriter.writeStartElement("experimentID");
        }

        if (localExperimentID == java.lang.Integer.MIN_VALUE)
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "experimentID cannot be null!!");

        }
        else
        {
            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localExperimentID));
        }

        xmlWriter.writeEndElement();
        if (localRunExperimentTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "runExperiment",
                            namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "runExperiment");
                }

            }
            else
            {
                xmlWriter.writeStartElement("runExperiment");
            }
            
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRunExperiment));
            xmlWriter.writeEndElement();
        }
        if (localObserveExperimentTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "observeExperiment",
                            namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "observeExperiment");
                }

            }
            else
            {
                xmlWriter.writeStartElement("observeExperiment");
            }

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localObserveExperiment));
                        xmlWriter.writeEndElement();
        }
        if (localModifyExperimentTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "modifyExperiment",
                            namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "modifyExperiment");
                }

            }
            else
            {
                xmlWriter.writeStartElement("modifyExperiment");
            }

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localModifyExperiment));
            xmlWriter.writeEndElement();
        }
        if (localStoreExperimentResultsTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix,
                            "storeExperimentResults", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace,
                            "storeExperimentResults");
                }

            }
            else
            {
                xmlWriter.writeStartElement("storeExperimentResults");
            }

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStoreExperimentResults));
            xmlWriter.writeEndElement();
        }
        if (localAuthorizeUsersTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "authorizeUsers",
                            namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "authorizeUsers");
                }

            }
            else
            {
                xmlWriter.writeStartElement("authorizeUsers");
            }

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAuthorizeUsers));
            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();

    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix,
            java.lang.String namespace, java.lang.String attName,
            java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        if (xmlWriter.getPrefix(namespace) == null)
        {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);

        }

        xmlWriter.writeAttribute(namespace, attName, attValue);

    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace,
            java.lang.String attName, java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        if (namespace.equals(""))
        {
            xmlWriter.writeAttribute(attName, attValue);
        }
        else
        {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace,
            java.lang.String attName, javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter
                .getPrefix(attributeNamespace);
        if (attributePrefix == null)
        {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0)
        {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        }
        else
        {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals(""))
        {
            xmlWriter.writeAttribute(attName, attributeValue);
        }
        else
        {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null)
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null)
            {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0)
            {
                xmlWriter.writeCharacters(prefix
                        + ":"
                        + org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qname));
            }
            else
            {
                // i.e this is the default namespace
                xmlWriter
                        .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qname));
            }

        }
        else
        {
            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        if (qnames != null)
        {
            // we have to store this data until last moment since it is not
            // possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++)
            {
                if (i > 0)
                {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null)
                {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0))
                    {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0)
                    {
                        stringToWrite
                                .append(prefix)
                                .append(":")
                                .append(
                                        org.apache.axis2.databinding.utils.ConverterUtil
                                                .convertToString(qnames[i]));
                    }
                    else
                    {
                        stringToWrite
                                .append(org.apache.axis2.databinding.utils.ConverterUtil
                                        .convertToString(qnames[i]));
                    }
                }
                else
                {
                    stringToWrite
                            .append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(
            javax.xml.stream.XMLStreamWriter xmlWriter,
            java.lang.String namespace)
            throws javax.xml.stream.XMLStreamException
    {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null)
        {
            prefix = generatePrefix(namespace);

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = org.apache.axis2.databinding.utils.BeanUtil
                        .getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     * 
     */
    public javax.xml.stream.XMLStreamReader getPullParser(
            javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException
    {

        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new javax.xml.namespace.QName("", "userID"));

        if (localUserID != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localUserID));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "userID cannot be null!!");
        }

        if (localUserGroup != null)
        {
            for (int i = 0; i < localUserGroup.length; i++)
            {

                if (localUserGroup[i] != null)
                {
                    elementList.add(new javax.xml.namespace.QName("",
                            "userGroup"));
                    elementList
                            .add(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(localUserGroup[i]));
                }
                else
                {

                    throw new org.apache.axis2.databinding.ADBException(
                            "userGroup cannot be null!!");

                }

            }
        }
        else
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "userGroup cannot be null!!");

        }

        elementList.add(new javax.xml.namespace.QName("", "experimentID"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localExperimentID));
        if (localRunExperimentTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "runExperiment"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localRunExperiment));
        }
        if (localObserveExperimentTracker)
        {
            elementList.add(new javax.xml.namespace.QName("",
                    "observeExperiment"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localObserveExperiment));
        }
        if (localModifyExperimentTracker)
        {
            elementList.add(new javax.xml.namespace.QName("",
                    "modifyExperiment"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localModifyExperiment));
        }
        if (localStoreExperimentResultsTracker)
        {
            elementList.add(new javax.xml.namespace.QName("",
                    "storeExperimentResults"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localStoreExperimentResults));
        }
        if (localAuthorizeUsersTracker)
        {
            elementList
                    .add(new javax.xml.namespace.QName("", "authorizeUsers"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localAuthorizeUsers));
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
                qName, elementList.toArray(), attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        /**
         * static method to create the object Precondition: If this object is an
         * element, the current or next start element starts this object and any
         * intervening reader events are ignorable If this object is not an
         * element, it is a complex type and the reader is at the event just
         * after the outer start element Postcondition: If this object is an
         * element, the reader is positioned at its end element If this object
         * is a complex type, the reader is positioned at the end element of its
         * outer element
         */
        public static Permissions parse(javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            Permissions object = new Permissions();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                        "http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    java.lang.String fullTypeName = reader
                            .getAttributeValue(
                                    "http://www.w3.org/2001/XMLSchema-instance",
                                    "type");
                    if (fullTypeName != null)
                    {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName
                                    .indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        java.lang.String type = fullTypeName
                                .substring(fullTypeName.indexOf(":") + 1);

                        if (!"Permissions".equals(type))
                        {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader
                                    .getNamespaceContext().getNamespaceURI(
                                            nsPrefix);
                            return (Permissions) au.edu.labshare.schedserver.labconnector.service.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal
                // attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list2 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "userID")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setUserID(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "userGroup")
                                .equals(reader.getName()))
                {

                    // Process the array and step past its final element's end.
                    list2.add(reader.getElementText());

                    // loop until we find a start element that is not part of
                    // this array
                    boolean loopDone2 = false;
                    while (!loopDone2)
                    {
                        // Ensure we are at the EndElement
                        while (!reader.isEndElement())
                        {
                            reader.next();
                        }
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement()
                                && !reader.isEndElement())
                            reader.next();
                        if (reader.isEndElement())
                        {
                            // two continuous end elements means we are exiting
                            // the xml structure
                            loopDone2 = true;
                        }
                        else
                        {
                            if (new javax.xml.namespace.QName("", "userGroup")
                                    .equals(reader.getName()))
                            {
                                list2.add(reader.getElementText());

                            }
                            else
                            {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setUserGroup((java.lang.String[]) list2
                            .toArray(new java.lang.String[list2.size()]));

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "experimentID")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setExperimentID(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "runExperiment")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setRunExperiment(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("",
                                "observeExperiment").equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setObserveExperiment(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "modifyExperiment")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setModifyExperiment(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("",
                                "storeExperimentResults").equals(reader
                                .getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setStoreExperimentResults(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "authorizeUsers")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setAuthorizeUsers(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement())
                    // A start element we are not expecting indicates a trailing
                    // invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());

            }
            catch (javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}
