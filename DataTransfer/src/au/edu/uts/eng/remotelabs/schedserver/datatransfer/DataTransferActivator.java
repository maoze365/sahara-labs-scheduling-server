/**
 * SAHARA Rig Client
 * 
 * Software abstraction of physical rig to provide rig session control
 * and rig device control. Automatically tests rig hardware and reports
 * the rig status to ensure rig goodness.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @dat 18th March 2013
 */

package au.edu.uts.eng.remotelabs.schedserver.datatransfer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.datatransfer.impl.DataFilesTransferImpl;
import au.edu.uts.eng.remotelabs.schedserver.datatransfer.impl.RedboxIngestFiles;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;

/**
 * Activator for the DataTransfer module.
 */
public class DataTransferActivator implements BundleActivator 
{
    /** Service registration for the Data Files service. */
    private ServiceRegistration<DataFilesService> dataFilesReg;
    
    /** Service registration for the session event listener to generate
     *  metadata ingest files. */
    private ServiceRegistration<SessionEventListener> sessionListenerReg;

    /** Logger. */
    private Logger logger;
    
    @Override
    public void start(BundleContext context) throws Exception 
    {
        this.logger.debug("Data transfer module starting up...");
        
        /* Register the data files service. */
        this.dataFilesReg = context.registerService(DataFilesService.class, new DataFilesTransferImpl(), null);
        
        /* Register a session notifier which auto-generates ingest files. */
        this.sessionListenerReg = context.registerService(SessionEventListener.class, new RedboxIngestFiles(), null);
	}

    @Override
	public void stop(BundleContext context) throws Exception 
	{
        this.logger.debug("Data transfer module shutting down...");
        
        /* Unregister bundle services. */
        this.dataFilesReg.unregister();
        this.sessionListenerReg.unregister();
        
	}

}