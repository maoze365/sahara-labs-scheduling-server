/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;

/**
 * Data access object for the {@link RigOfflineSchedule} class.
 */
public class RigOfflineScheduleDao extends GenericDao<RigOfflineSchedule>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public RigOfflineScheduleDao()
    {
        super(RigOfflineSchedule.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public RigOfflineScheduleDao(Session ses)
    {
        super(ses, RigOfflineSchedule.class);
    }
 
    /**
     * Returns true if the rig is currently in a scheduled off-line period.
     * 
     * @param rig rig to check
     * @return true if off-line
     */
    public boolean isOffline(Rig rig)
    {
        Date now = new Date();
        return (Integer)this.session.createCriteria(this.clazz)
            .add(Restrictions.eq("rig", rig))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("startTime", now))
            .add(Restrictions.gt("endTime", now))
            .setProjection(Projections.rowCount()).uniqueResult() > 0;
    }
    
    /**
     * Returns a list of offline periods for the rig.
     * 
     * @param rig rig
     * @return list of offline periods
     */
    @SuppressWarnings("unchecked")
    public List<RigOfflineSchedule> getOfflinePeriods(Rig rig)
    {
        return this.session.createCriteria(this.clazz)
            .add(Restrictions.eq("rig", rig))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.gt("endTime", new Date()))
            .list();
    }
}