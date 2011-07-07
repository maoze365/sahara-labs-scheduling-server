/**
 * SAHARA Scheduling Server
 *
 * CreatePIF.java 
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
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 *
 * @author Herber Yeung
 * @date 4th November 2010
 */         
package au.edu.labshare.schedserver.scormpackager.types;
            

/**
*  CreatePIF bean class
*/
        
public class CreatePIF implements org.apache.axis2.databinding.ADBBean
{
	
        
    /**
	 * Default value - serial versionUID
	 */
	private static final long serialVersionUID = 1L;

	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
	"http://labshare.edu.au:8080/ScormPackager/",
	"createPIF",
	"ns1");

            

    private static java.lang.String generatePrefix(java.lang.String namespace) 
    {
        if(namespace.equals("http://labshare.edu.au:8080/ScormPackager/"))
        {
            return "ns1";
        }
        
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

        

   /**
    * field for Content
    * This was an Array!
    */
    protected java.lang.String[] localContent ;
            
	 /*  This tracker boolean wil be used to detect whether the user called the set method
	  *   for this attribute. It will be used to determine whether to include this field
	  *   in the serialized XML
	  */
    protected boolean localContentTracker = false ;
       

    /**
     * Auto generated getter method
     * @return java.lang.String[]
     */
    public  java.lang.String[] getContent()
    {
    	return localContent;
    }
                               
    /**
     * validate the array for Content
     */
    protected void validateContent(java.lang.String[] param)
    {
    }


    /**
     * Auto generated setter method
     * @param param Content
     */
    public void setContent(java.lang.String[] param)
    {
  
    	validateContent(param);

   
    	if (param != null)
    	{
    		//update the setting tracker
    		localContentTracker = true;
    	} 
    	else 
    	{
    		localContentTracker = false;
    	}
          
    	this.localContent=param;
    }
                           
    /**
     * Auto generated add method for the array for convenience
     * @param param java.lang.String
     */
    @SuppressWarnings("unchecked")
	public void addContent(java.lang.String param)
    {
    	if (localContent == null)
    	{
    		localContent = new java.lang.String[]{};
    	}
                           
    	//update the setting tracker
        localContentTracker = true;
        
        @SuppressWarnings("rawtypes")
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localContent);
        list.add(param);
        this.localContent = (java.lang.String[])list.toArray(new java.lang.String[list.size()]);
    }

    /**
     * field for ExperimentName
     */
    protected java.lang.String localExperimentName;
                                

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getExperimentName()
    {
    	return localExperimentName;
    }
                        
    /**
     * Auto generated setter method
     * @param param ExperimentName
     */
    public void setExperimentName(java.lang.String param)
    {
    	this.localExperimentName=param;
    }
                            

    /**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) 
    {
    	boolean isReaderMTOMAware = false;
        
        try
        {
          isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch(java.lang.IllegalArgumentException e)
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
    public org.apache.axiom.om.OMElement getOMElement (final javax.xml.namespace.QName parentQName,
               										   final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
    {
        
    	org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME)
    	{
	        public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException 
	        {
	        	CreatePIF.this.serialize(MY_QNAME,factory,xmlWriter);
	        }
    	};
        
    	return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME,factory,dataSource);
            
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
    					  final org.apache.axiom.om.OMFactory factory,
    					  org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
    					  throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
    	serialize(parentQName,factory,xmlWriter,false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
    					  final org.apache.axiom.om.OMFactory factory,
                          org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
                          boolean serializeType)
            			  throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
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
        		xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            } 
        	else 
        	{
        		if (prefix == null) 
        		{
        			prefix = generatePrefix(namespace);
                }

        		xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
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
        	java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://labshare.edu.au:8080/ScormPackager/");
            
        	if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        	{
        		writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type", namespacePrefix+":createPIF", xmlWriter);
        	} 
        	else 
        	{
        		writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type", "createPIF", xmlWriter);
        	}
        }
                
        if (localContentTracker)
        {
        	if (localContent!=null) 
        	{
        		namespace = "";
                boolean emptyNamespace = namespace == null || namespace.length() == 0;
                prefix =  emptyNamespace ? null : xmlWriter.getPrefix(namespace);
                for (int i = 0;i < localContent.length;i++)
                {
                	if (localContent[i] != null)
                	{                    
                		if (!emptyNamespace) 
                		{
                			if (prefix == null) 
                			{
                				java.lang.String prefix2 = generatePrefix(namespace);
                				xmlWriter.writeStartElement(prefix2,"content", namespace);
                				xmlWriter.writeNamespace(prefix2, namespace);
                                xmlWriter.setPrefix(prefix2, namespace);
                			} 
                			else 
                			{
                				xmlWriter.writeStartElement(namespace,"content");
                			}
                		} 
                		else 
                		{
                			xmlWriter.writeStartElement("content");
                		}
                		
                		xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localContent[i]));                            
                		xmlWriter.writeEndElement();                      
                	} 
                }
        	} 
        	else 
        	{
        		throw new org.apache.axis2.databinding.ADBException("content cannot be null!!");                    
        	}
        }
        	namespace = "";
            
        	if (! namespace.equals("")) 
        	{
        		prefix = xmlWriter.getPrefix(namespace);

        		if (prefix == null) 
        		{
        			prefix = generatePrefix(namespace);

        			xmlWriter.writeStartElement(prefix,"experimentName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

        		} 
        		else 
        		{
        			xmlWriter.writeStartElement(namespace,"experimentName");
        		}
        	} 
        	else 
        	{
        		xmlWriter.writeStartElement("experimentName");
        	}
            
        	if (localExperimentName==null)
        	{
        		// write the nil attribute
                throw new org.apache.axis2.databinding.ADBException("experimentName cannot be null!!");                                      
        	}
        	else
        	{
        		xmlWriter.writeCharacters(localExperimentName);                            
        	}
                                    
            xmlWriter.writeEndElement();                
            xmlWriter.writeEndElement();
        }

    	/**
    	 * Util method to write an attribute with the ns prefix
    	 */
    	private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
    								java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
        {
    		if (xmlWriter.getPrefix(namespace) == null) 
    		{
    			xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
    		}

    		xmlWriter.writeAttribute(namespace,attName,attValue);
        }

    	/**
         * Register a namespace prefix
         */
         private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException 
         {
        	 java.lang.String prefix = xmlWriter.getPrefix(namespace);

        	 if (prefix == null) 
        	 {
        		 prefix = generatePrefix(namespace);

        		 while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) 
        		 {
        			 prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
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
        @SuppressWarnings("unchecked")
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException
         {
        	 @SuppressWarnings("rawtypes")
			java.util.ArrayList elementList = new java.util.ArrayList();
        	 @SuppressWarnings("rawtypes")
			java.util.ArrayList attribList = new java.util.ArrayList();

        	 if (localContentTracker)
        	 {
        		 if (localContent!=null)
        		 {
        			 for (int i = 0;i < localContent.length;i++)
        			 {                 
        				 if (localContent[i] != null)
        				 {
        					 elementList.add(new javax.xml.namespace.QName("", "content"));
        					 elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localContent[i]));
        				 } 
        			 }
        		 }
        		 else 
        		 {             
        			 throw new org.apache.axis2.databinding.ADBException("content cannot be null!!");           
        		 }
        	 }
        	 
        	 elementList.add(new javax.xml.namespace.QName("", "experimentName"));
                                 
        	 if (localExperimentName != null)
        	 {
        		 elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExperimentName));
        	 } 
        	 else 
        	 {
        		 throw new org.apache.axis2.databinding.ADBException("experimentName cannot be null!!");
        	 }
               
        	 return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
         }


         /**
          *  Factory class that keeps the parse method
          */
         public static class Factory
         {
        	 /**
        	  * static method to create the object
        	  * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        	  *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        	  * Postcondition: If this object is an element, the reader is positioned at its end element
        	  *                If this object is a complex type, the reader is positioned at the end element of its outer element
        	  */
        	@SuppressWarnings("unchecked")
			public static CreatePIF parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        	 {
        		 CreatePIF object = new CreatePIF();
        		 
        		 try 
        		 {
        			 while(!reader.isStartElement() && !reader.isEndElement())
        				 reader.next();

        			 if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null)
        			 {
        				 java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
        				 
        				 if (fullTypeName!=null)
        				 {
        					 java.lang.String nsPrefix = null;

        					 if (fullTypeName.indexOf(":") > -1)
        					 {
        						 nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
        					 }
        					 
        					 nsPrefix = nsPrefix==null?"":nsPrefix;
        					 java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
        					 if (!"createPIF".equals(type))
        					 {
        						 //find namespace for the prefix
        						 java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
        						 return (CreatePIF)au.edu.labshare.schedserver.scormpackager.types.ExtensionMapper.getTypeObject(nsUri,type,reader);
                              }
        				 }
        			 }

        			 // Note all attributes that were handled. Used to differ normal attributes
        			 // from anyAttributes.
        			 @SuppressWarnings("rawtypes")
					java.util.ArrayList list1 = new java.util.ArrayList();
        			 reader.next();
                                   
        			 while (!reader.isStartElement() && !reader.isEndElement()) 
        				 reader.next();
                                
        			 if (reader.isStartElement() && new javax.xml.namespace.QName("","content").equals(reader.getName()))
        			 {
        				 // Process the array and step past its final element's end.
        				 list1.add(reader.getElementText());
                                            
        				 //loop until we find a start element that is not part of this array
                         boolean loopDone1 = false;
                         while(!loopDone1)
                         {
                        	 // Ensure we are at the EndElement
                        	 while (!reader.isEndElement())
                        	 {
                        		 reader.next();
                        	 }
                        	 
                        	 // Step out of this element
                             reader.next();
                             
                             // Step to next element event.
                             while (!reader.isStartElement() && !reader.isEndElement())
                            	 reader.next();
                             
                             if (reader.isEndElement())
                             {
                            	 //two continuous end elements means we are exiting the xml structure
                                 loopDone1 = true;
                             } 
                             else 
                             {
                            	 if (new javax.xml.namespace.QName("","content").equals(reader.getName()))
                            	 {                                 
                            		 list1.add(reader.getElementText());
                            	 }
                            	 else
                            	 {
                            		 loopDone1 = true;
                            	 }
                             }
                         }
                         
                         // call the converter utility  to convert and set the array                   
                         object.setContent((java.lang.String[])
                         list1.toArray(new java.lang.String[list1.size()]));
                                                
        			 }// End of if for expected property start element          
                                    
        			 while (!reader.isStartElement() && !reader.isEndElement()) 
        				 reader.next();
                                
        			 if (reader.isStartElement() && new javax.xml.namespace.QName("","experimentName").equals(reader.getName()))
        			 {           
        				 java.lang.String content = reader.getElementText();
                                    
        				 object.setExperimentName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
        				 reader.next();           
        			 }// End of if for expected property start element          
        			 else
        			 {
        				 // A start element we are not expecting indicates an invalid parameter was passed
        				 throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
        			 }
                              
        			 while(!reader.isStartElement() && !reader.isEndElement())
        				 reader.next();
                            
        			 if (reader.isStartElement())
        				 // A start element we are not expecting indicates a trailing invalid property
        				 throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
        		 }
        		 catch (javax.xml.stream.XMLStreamException e)
        		 {
        			 throw new java.lang.Exception(e);
        		 }
        		 
        		 return object;
        	 }
         }//end of factory class
}
           
          