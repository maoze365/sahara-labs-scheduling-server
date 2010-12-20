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
 * @date 15th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking.BType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * The bookings for a day.
 * <br />
 * This class is not thread safe and must be externally synchronized before 
 * invoking any of it's methods.
 */
public class DayBookings
{
    /** Loaded rig bookings. */
    private final Map<Rig, RigBookings> rigBookings;
    
    /** Type rig targets. */
    private final Map<RigType, RigBookings> typeTargets;
    
    /** Loaded request capabilities. */
    private final Map<RequestCapabilities, RigBookings> capsTargets;
    
    /** The day key of this day. */
    private final String day;
    
    /** The beginning time of this day. */
    private final Date dayBegin;
    
    /** The end time of this day. */
    private final Date dayEnd;
    
    /** Logger. */
    private Logger logger;
    
    public DayBookings(String day)
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.debug("Loading day bookings for day " + day + '.');
        
        this.rigBookings = new HashMap<Rig, RigBookings>();
        
        this.day = day;
        this.dayBegin= TimeUtil.getDayBegin(this.day).getTime();
        this.dayEnd = TimeUtil.getDayEnd(this.day).getTime();
        
        this.typeTargets = new HashMap<RigType, RigBookings>();
        this.capsTargets = new HashMap<RequestCapabilities, RigBookings>();
    }
    
    /**
     * Creates a booking for the rig. This methods assumes it has free rein 
     * to choose which rig that matches the booked resoruce may be scheduled 
     * in the day. Therefore a multi-day rig type or request capabilities
     * booking must be converted to an appropriate rig booking.
     * 
     * @param mb memory booking
     * @param ses database session
     * @return
     */
    public boolean createBooking(MBooking mb, Session ses)
    {
        RigBookings rb, next;
        
        switch (mb.getType())
        {
            case RIG:
                rb = this.getRigBookings(mb.getBooking().getRig(), ses);
                if (rb.areSlotsFree(mb))
                {
                    return rb.commitBooking(mb);
                }
                
                /* No directly free slots are found try a load balancing run to
                 * free some slots. */
                return this.outerLoadBalance(rb, mb, false) && this.outerLoadBalance(rb, mb, true) && rb.commitBooking(mb);
                
            case TYPE:
                RigType rt = mb.getRigType();
                if ((rb = this.typeTargets.get(rt)) == null)
                {
                    Set<Rig> rigs = rt.getRigs();
                    if (rigs.size() == 0)
                    {
                        this.logger.info("Cannot make a booking for the rig type " + rt.getName() + " because it has " +
                        		"no rigs.");
                        return false;
                    }
                    rb = this.getRigBookings(rigs.iterator().next(), ses);
                }
                
                next = rb;
                do
                {
                    if (next.areSlotsFree(mb))
                    {
                        return next.commitBooking(mb);
                    }
                    next = next.getTypeLoopNext();
                }
                while (next != rb);
                
                /* No directly free slots are found try a load balancing run to
                 * free some slots. */
                return this.outerLoadBalance(rb, mb, false) && this.outerLoadBalance(rb, mb, true) && rb.commitBooking(mb);
                
            case CAPABILITY:
                RequestCapabilities caps = mb.getRequestCapabilities();
                if ((rb = this.capsTargets.get(caps)) == null)
                {
                    List<RequestCapabilities> capsList = new ArrayList<RequestCapabilities>();
                    capsList.add(caps);
                    this.loadRequestCapabilities(capsList, ses, false);
                    
                    if ((rb = this.capsTargets.get(caps)) == null)
                    {
                        this.logger.info("Cannot make a booing for the request capabilities " + caps.getCapabilities() +
                                " because it has no matching rigs.");
                        return false;
                    }
                }
                
                next = rb;
                do 
                {
                    if (next.areSlotsFree(mb))
                    {
                        return next.commitBooking(mb);
                    }
                    next = next.getCapsLoopNext(caps);
                }
                while (next != rb);

                /* No directly free slots are found try a load balancing run to
                 * free some slots. */
                return this.outerLoadBalance(rb, mb, false) && this.outerLoadBalance(rb, mb, true) && rb.commitBooking(mb);
        }
        
        return false;
    }
    
    /**
     * Finds the best fits for booking. This will generally attempt to provide 
     * an early solution and a late solution.
     * 
     * @param mb booking that couldn't be commited
     * @param ses
     * @return
     * @todo Implement balance operation in determining best fits
     */
    public List<MRange> findBestFits(MBooking mb, Session ses)
    {
        ///////////////////////////////////////////////////////////////////////
        // DODGY: Does not do a balance operation to work out best fits      //
        ///////////////////////////////////////////////////////////////////////
        
        MRange ef = null, lf = null;
        
        RigBookings rb, next;
        switch (mb.getType())
        {
            case RIG:
                rb = this.getRigBookings(mb.getBooking().getRig(), ses);
                ef = rb.getEarlyFit(mb);
                lf = rb.getLateFit(mb);
                break;
                
            case TYPE:
                if ((next = rb = this.typeTargets.get(mb.getRigType())) == null)
                {
                    Set<Rig> rigs = mb.getRigType().getRigs();
                    if (rigs.size() == 0) return Collections.<MRange>emptyList();
                    next = rb = this.getRigBookings(rigs.iterator().next(), ses);
                }

                do
                {
                    ef = this.compareBestFits(mb, ef, next.getEarlyFit(mb), true);
                    lf = this.compareBestFits(mb, lf, next.getLateFit(mb), false);
                    next = next.getTypeLoopNext();
                }
                while (next != rb);
                break;
                
            case CAPABILITY:
                RequestCapabilities caps = mb.getRequestCapabilities();
                if ((next = rb = this.capsTargets.get(caps)) == null)
                {
                    List<RequestCapabilities> capsList = new ArrayList<RequestCapabilities>();
                    capsList.add(caps);
                    this.loadRequestCapabilities(capsList, ses, false);
                    
                    if ((next = rb = this.capsTargets.get(caps)) == null) return Collections.<MRange>emptyList();
                }

                do
                {
                    ef = this.compareBestFits(mb, ef, next.getEarlyFit(mb), true);
                    lf = this.compareBestFits(mb, lf, next.getLateFit(mb), false);
                    next = next.getCapsLoopNext(caps);
                }
                while (next != rb);
                break;           
        }

        List<MRange> fits = new ArrayList<MRange>(2);
        if (ef != null) fits.add(ef);
        if (lf != null) fits.add(lf);
        return fits;
    }
    
    /**
     * Compares two best fits with a designated booking. The /best/ best fit
     * is the one which is cloest to the desginated booking.
     * 
     * @param mb desired booking
     * @param r1 first fit
     * @param r2 second fit
     * @param whether the fit is early (before) or late (after)
     * @return the /best/ best fit
     */
    private MRange compareBestFits(MBooking mb, MRange r1, MRange r2, boolean early)
    {
        if (r1 == null) return r2;
        if (r2 == null) return r1;
        
        if (early)
        {
            if      (r1.getEndSlot() > r2.getEndSlot()) return r1;     // r1 is closer to designated booking
            else if (r1.getEndSlot() < r2.getEndSlot()) return r2;     // r2 is closer to designated booking
            else return r1.getNumSlots() > r2.getNumSlots() ? r1 : r2; // r1 is longer
        }
        else
        {
            if      (r1.getStartSlot() < r2.getStartSlot()) return r1; // r1 is closer to designated booking
            else if (r1.getStartSlot() > r2.getStartSlot()) return r2; // r2 is closer to designated booking
            else return r1.getNumSlots() > r2.getNumSlots() ? r1 : r2; // r1 is longer
        }
    }
    
    /**
     * Gets the free slots for the rig type during the day.
     * 
     * @param rigType the rig type to find free slots of
     * @param thres minimum number of slots required
     * @param ses database session
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(RigType rigType, int thres, Session ses)
    {
        return this.getFreeSlots(rigType, 0, SlotBookingEngine.NUM_SLOTS - 1, thres, ses);
    }
    
    /**
     * Gets the free slots for the request capabilities matching rigs during the day.
     * 
     * @param reqCaps request capabilities  
     * @param thres minimum number of slots required
     * @param ses database session
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(RequestCapabilities reqCaps, int thres, Session ses)
    {
        return this.getFreeSlots(reqCaps, 0, SlotBookingEngine.NUM_SLOTS - 1, thres, ses);
    }
    
    /**
     * Gets the free slots for the rig during day.
     * 
     * @param rig the rig to find free slots of
     * @param thres minimum number of slots required
     * @param ses database session
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(Rig rig, int thres, Session ses)
    {
        return this.getFreeSlots(rig, 0, SlotBookingEngine.NUM_SLOTS - 1, thres, ses);
    }
    
    /**
     * Gets the free slots for the rig type between the start and end period
     * inclusive.
     * 
     * @param rigType the rig type to find the free slots of
     * @param start start slot
     * @param end end slot
     * @param thres minimum number of slots required
     * @param ses database session
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(RigType rigType, int start, int end, int thres, Session ses)
    {
        RigBookings ts;
        
        if (!this.typeTargets.containsKey(rigType))
        {
            Set<Rig> rigs = rigType.getRigs();
            if (rigs.size() == 0)
            {
                this.logger.info("Attempting to get the free slots of a rig type '" + rigType.getName() + "' that " +
                		"doesn't have any rigs.");
                /* If the type doesn't have any rigs, then it doesn't have any
                 * free slots. */
                return Collections.emptyList();
            }
            
            ts = this.getRigBookings(rigs.iterator().next(), ses);
        }
        else ts = this.typeTargets.get(rigType);
        
        /* Navigate the type resource loop to find the actual free slots. */
        List<MRange> free = new ArrayList<MRange>();
        RigBookings next = ts;
        do
        {
            free.addAll(next.getFreeSlots(start, end, thres));
            next = next.getTypeLoopNext();
        }
        while (next != ts);
        
        /* Try load balancing to freeable slots. */
        for (MRange inuse : MRange.complement(MRange.collapseRange(free), this.day))
        {
            int outerrs = inuse.getStartSlot();
            /* If the seek start is after the end of the period specified, no need
             * to run load balancing. */
            if (outerrs > end) continue;
            /* If the seek start is before the period specified, start at the period
             * specified. */
            if (outerrs < start) outerrs = start;

            int outerre =  inuse.getEndSlot();
            /* If the seek end is before the beginning of the period specified, no
             * need to run load balancing. */
            if (outerre < start) continue;
            /* If the seek end is after the period specified, end at the period
             * specified. */
            if (outerre > end) outerre = end;
            
            ts = next;
            do
            {
                int innerrs = outerrs;
                while (innerrs <= outerre)
                {
                    MBooking bk = next.getNextBooking(innerrs);
                    
                    /* There isn't much point trying to load balance a type booking,
                     * because in enclosing range, the type loop is saturated. */
                    if (bk.getType() == BType.CAPABILITY && this.innerLoadBalance(next, bk, false))
                    {
                        free.add(new MRange(bk.getStartSlot(), bk.getEndSlot(), bk.getDay()));
                        
                        /* No need to further load balance bookings on other rigs
                         * when they the slots have already been found to be freeable. */
                        outerrs = bk.getEndSlot() + 1;
                    }
                    
                    innerrs = bk.getEndSlot() + 1;
                }

                next = next.getTypeLoopNext();
            }
            while (ts != next);
            
            if (end < inuse.getEndSlot()) break;
        }
        
        return MRange.collapseRange(free);
    }
    
   /**
    * Gets the free slots for the request capabilities matching rigs between the
    * start and end period inclusive.
    * 
    * @param reqCaps request capabilities  
    * @param thres minimum number of slots required
    * @param ses database session
    * @return list of free slots
    */
    public List<MRange> getFreeSlots(RequestCapabilities reqCaps, int start, int end, int thres, Session ses)
    {
        if (!this.capsTargets.containsKey(reqCaps))
        {
            List<RequestCapabilities> capsList = new ArrayList<RequestCapabilities>();
            capsList.add(reqCaps);
            this.loadRequestCapabilities(capsList, ses, false);
        }
        
        RigBookings cs = this.capsTargets.get(reqCaps);
        if (cs == null)
        {
            /* No rigs match the request capabilities, so there can't be any 
             * free slots. */
            return Collections.<MRange>emptyList();
        }
        
        List<MRange> free = new ArrayList<MRange>();
        
        /* Navigate the capabilities loop to find the actual free slots. */
        RigBookings next = cs;
        do
        {
            free.addAll(next.getFreeSlots(start, end, thres));
            next = next.getCapsLoopNext(reqCaps);
        }
        while (cs != next);
        
        /* Try load balancing to find freeable slots. */
        for (MRange inuse : MRange.complement(MRange.collapseRange(free), this.day))
        {
            int outerrs = inuse.getStartSlot();
            /* If the seek start is after the end of the period specified, no need
             * to run load balancing. */
            if (outerrs > end) continue;
            /* If the seek start is before the period specified, start at the period
             * specified. */
            if (outerrs < start) outerrs = start;

            int outerre =  inuse.getEndSlot();
            /* If the seek end is before the beginning of the period specified, no
             * need to run load balancing. */
            if (outerre < start) continue;
            /* If the seek end is after the period specified, end at the period
             * specified. */
            if (outerre > end) outerre = end;
            
            cs = next;
            do
            {
                int innerrs = outerrs;
                while (innerrs <= outerre)
                {
                    MBooking bk = next.getNextBooking(innerrs);
                    
                    if (this.innerLoadBalance(next, bk, false))
                    {
                        free.add(new MRange(bk.getStartSlot(), bk.getEndSlot(), bk.getDay()));
                    }
                    
                    innerrs = bk.getEndSlot() + 1;
                }

                next = next.getCapsLoopNext(reqCaps);
            }
            while (cs != next);
            
            if (end < inuse.getEndSlot()) break;
        }
        
        return MRange.collapseRange(free);
    }
    
    /**
     * Gets the free slots for the rig between the start and end period 
     * inclusive.
     * 
     * @param rig the rig to find free slots of
     * @param start start slot
     * @param end end slot
     * @param thres minimum number of slots required
     * @param ses database session
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(Rig rig, int start, int end, int thres, Session ses)
    {
        RigBookings rb = this.getRigBookings(rig, ses);
        
        List<MRange> free = rb.getFreeSlots(start, end, thres);
        
        /* For the other free times, attempt to load balance the bookings off the rig. */
        int fs = start;
        while (fs < end)
        {
            MBooking membooking = rb.getNextBooking(fs);
            if (membooking == null)
            {
                break;
            }
            
            /* If multi-day booking, we can't load balance in this perspective
             * as this could leave the rig in a inconsistent state. */
            if (membooking.isMultiDay())
            {
                fs = membooking.getEndSlot() + 1;
                continue;
            }
            
            RigBookings next;
            switch (membooking.getType())
            {
                /* Rig bookings can't be loaded balanced. */
                case RIG:
                    break;
                    
                case TYPE:
                    next = rb.getTypeLoopNext();
                    do
                    {
                        if (this.innerLoadBalance(next, membooking, false))
                        {
                            free.add(new MRange(membooking.getStartSlot(), membooking.getEndSlot(), this.day));
                            break;
                        }
                        next = next.getTypeLoopNext();
                    }
                    while (next != rb);
                    break;
                    
                case CAPABILITY:
                    next = rb.getCapsLoopNext(membooking.getRequestCapabilities());
                    do
                    {
                        if (this.innerLoadBalance(next, membooking, false))
                        {
                            free.add(new MRange(membooking.getStartSlot(), membooking.getEndSlot(), this.day));
                            break;
                        }
                        next = next.getCapsLoopNext(membooking.getRequestCapabilities());
                    }
                    while (next != rb);
                    break;
            }
            
            fs = membooking.getEndSlot() + 1;
        }
        
        return MRange.collapseRange(free);
    }
    
    /**
     * Removes the booking from this day bookings. 
     * 
     * @param booking booking to remove
     * @return true if successful
     */
    public boolean removeBooking(MBooking booking)
    {
        /* Find the rig that has the booking. */
        RigBookings rb = null;
        switch (booking.getType())
        {
            case RIG:
                Rig rig = booking.getBooking().getRig();
                if (!this.rigBookings.containsKey(rig))
                {
                    /* The rig  isn't loaded, so no need to unload it. */
                    return true;
                }
                rb = this.rigBookings.get(rig);
                break;
            case TYPE:
                RigType rigType = booking.getRigType();
                if (!this.typeTargets.containsKey(rigType))
                {
                    /* The type isn't loaded so need to remove a booking from
                     * it. */
                    return true;
                }
                rb = this.typeTargets.get(rigType);
                RigBookings next = rb;
                do
                {
                    if (next.hasBooking(booking))
                    {
                        rb = next; 
                        break;
                    }
                    next = next.getTypeLoopNext();
                }
                while (next != rb);
                break;
            case CAPABILITY:
                RequestCapabilities reqCaps = booking.getRequestCapabilities();
                if (!this.capsTargets.containsKey(reqCaps))
                {
                    /* The capability isn't loaded so no need to remove a 
                     * booking from it. */
                    return true;
                }
                rb = this.capsTargets.get(reqCaps);
                next = rb;
                do
                {
                    if (next.hasBooking(booking))
                    {
                        rb = next;
                        break;
                    }
                    next = next.getCapsLoopNext(reqCaps);
                }
                while (rb != next);
                break;
        }
        
        return rb.removeBooking(booking);
    }
    
    /**
     * Gets the rig bookings for the rig. If the rig bookings hasn't been 
     * loaded for the rig, it is loaded by:
     * <ul>
     *  <li>Loading the rig and its booking for the day.</li>
     *  <li>Loading all the rigs in the rig type and their bookings.</li>
     *  <li>Linking the rig type resource loop.</li>
     *  <li>Loading the rig type bookings and assigning them to a rig.</li>
     *  <li>Loading the request capabilities resource loops for those that
     *  have at least one booking.</li>
     *  <li>Loading the request capabilities bookings.</li>
     * </ul>
     * 
     * @param rig rig to find bookings of
     * @return rig bookings
     */
    private RigBookings getRigBookings(Rig rig, Session ses)
    {
        if (!this.rigBookings.containsKey(rig))
        {
            this.logger.debug("Loaded day bookings for rig '" + rig.getName() + "' on day " + this.day + ".");
            
            List<RequestCapabilities> capsToLoad = new ArrayList<RequestCapabilities>();

            RigBookings rb = new RigBookings(rig, this.day);
            this.loadRig(rb, rig, ses);
            this.rigBookings.put(rig, rb);
            
            /* Add the capabilities that need to be loaded. */
            for (MatchingCapabilities match : rig.getRigCapabilities().getMatchingCapabilitieses())
            {
                capsToLoad.add(match.getRequestCapabilities());
            }

            this.loadRigType(rig, ses, capsToLoad);
            
            /* Load the request capabilities resource loops for those that have 
             * bookings. */
            this.loadRequestCapabilities(capsToLoad, ses, true);
        }
        
        return this.rigBookings.get(rig);
    }
    
    /**
     * Outer load balance. Load balancing is trying to fit the specifed booking
     * onto the rig.
     * 
     * @param rb rig to balance to
     * @param mb booling to balance onto rig 
     * @param dryRun whether to actually commit the changes.
     * @return true if successful
     */
    private boolean outerLoadBalance(RigBookings rb, MBooking mb, boolean dryRun)
    {
        RigBookings next;
        /* We need to rule a load balance pass to try and free up 
         * space for the booking. */
        MBooking ex = rb.getNextBooking(mb.getStartSlot());
        while (ex.getStartSlot() <= mb.getEndSlot())
        {
            switch (ex.getType())
            {
                case RIG: return false;
                case TYPE:
                    next = rb.getTypeLoopNext();
                    do
                    {
                        if (this.innerLoadBalance(next, ex, dryRun))
                        {
                            rb.removeBooking(ex);
                            break;
                        }
                        next = next.getTypeLoopNext();
                    }
                    while (next != rb);
                    
                    if (rb.hasBooking(ex)) return false;
                    break;
                    
                case CAPABILITY:
                    next = rb.getCapsLoopNext(ex.getRequestCapabilities());
                    do
                    {
                        if (this.innerLoadBalance(next, ex, dryRun))
                        {
                            rb.removeBooking(ex);
                            break;
                        }
                        next = next.getCapsLoopNext(ex.getRequestCapabilities());
                    }
                    while (next != rb);
                    
                    if (rb.hasBooking(ex)) return false;
            }
            
            ex = rb.getNextBooking(ex.getEndSlot() + 1);
        }
        return true;
    }
    
    /**
     * Runs load balancing of a rig. Load balancing attempts to fit the 
     * provided booking onto the specified rig. The booking may be fitted
     * if the slots are free or the bookings in the slot range can be 
     * provided to an equivalent matching rig.
     * 
     * @param rb the rig bookings to free the slots
     * @param bk bookings to try and fit it
     * @param doCommit whether the load balancing will actually be committed 
     * @return true if successfully able to load balance
     */
    private boolean innerLoadBalance(RigBookings br, MBooking bk, boolean doCommit)
    {
        int start = bk.getStartSlot();
        
        while (start <= bk.getEndSlot())
        {
            MBooking ex = br.getNextBooking(start);
            
            if (ex == null || ex.getStartSlot() > bk.getEndSlot())
            {
                /* We have already finished balancing all the required bookings,
                 * so no more work to do. */
                return true;
            }
                        
            /* We will not balance any cross-day bookings. The reason is, 
             * cross day bookings need to be balancing in sequence with
             * the other days rigs. */
            if (ex.isMultiDay()) return false;
            
            boolean hasBalanced = false;
            RigBookings next;
            switch (ex.getType())
            {
                case RIG:
                    /* Unable to move rig bookings. They are fixed to the 
                     * booked rig. */
                    return false;
                    
                case TYPE:
                    /* Run through the type loop to try to balance the booking. */
                    next = br.getTypeLoopNext();
                    while (next != br)
                    {
                        if (next.areSlotsFree(ex))
                        {
                            /* Found free slots. */
                            if (doCommit)
                            {
                                this.logger.debug("Balancing type booking from rig " + br.getRig().getName() + " to rig " +
                                        next.getRig().getName() + ".");
                                br.removeBooking(ex);
                                next.commitBooking(ex);
                            }
                            start = ex.getEndSlot() + 1;
                            hasBalanced = true;
                            break;
                        }
                        
                        next = next.getTypeLoopNext();
                    }
                    
                    /* We weren't able to balance booking so fail load balance to
                     * this rig. */
                    if (!hasBalanced) return false;
                    
                    break;
                    
                case CAPABILITY:
                    RequestCapabilities reqCaps = ex.getRequestCapabilities();
                    /* Run through the capability loop to try to balance the booking. */
                    next = br.getCapsLoopNext(reqCaps);
                    while (next != br)
                    {
                        if (next.areSlotsFree(ex))
                        {
                            /* Found free slots. */
                            if (doCommit)
                            {
                                this.logger.debug("Balancing capability booking from rig " + br.getRig().getName() + " to rig " +
                                        next.getRig().getName() + ".");
                                br.removeBooking(ex);
                                next.commitBooking(ex);
                            }
                            start = ex.getEndSlot() + 1;
                            hasBalanced = true;
                            break;
                        }
                        
                        next = next.getCapsLoopNext(reqCaps);
                    }
                    
                    /* We weren't able to balance booking so fail load balance to
                     * this rig. */
                    if (!hasBalanced) return false;
                    
                    break;
            }
        }
        
        return true;
    }

    /**
     * Loads the request capabilities.
     * 
     * @param capsList capabilities list
     * @param ses database session
     */
    private void loadRequestCapabilities(List<RequestCapabilities> capsList, Session ses, boolean ignoreNoBookings)
    {
        while (capsList.size() > 0)
        {
            RequestCapabilities reqCaps = capsList.get(0);
            this.logger.debug("Attempting to load bookings for request capabilities " + reqCaps.getCapabilities() + '.');
            

            Criteria qu = ses.createCriteria(Bookings.class)
                .add(Restrictions.eq("resourceType", ResourcePermission.CAPS_PERMISSION))
                .add(Restrictions.eq("requestCapabilities", reqCaps))
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.lt("startTime", this.dayEnd))
                .add(Restrictions.gt("endTime", this.dayBegin));
            @SuppressWarnings("unchecked")
            List<Bookings> bookings = qu.list();
            
            /* There are no bookings for this class so no need to load it
             * yet. */
            if (ignoreNoBookings && bookings.size() == 0)
            {
                this.logger.debug("Not going to load request capabilities " + reqCaps.getCapabilities() + " because " +
                		"it has no bookings.");
                capsList.remove(0);
                continue;
            }
            
            /* Find all the matching rigs. */
            List<Rig> matchingRigs = new ArrayList<Rig>();
            for (MatchingCapabilities match: reqCaps.getMatchingCapabilitieses())
            {
                matchingRigs.addAll(match.getRigCapabilities().getRigs());
            }
            
            /* If the request capabilities has no matching rigs, we cannot load 
             * the request capabilities loop. */
            if (matchingRigs.size() == 0)
            {
                this.logger.debug("Cannot load up request capbilities resource loop for '" + reqCaps.getCapabilities() +
                        "' because it has no matching rigs.");
                capsList.remove(0);
                continue;
            }
            
            /* Make sure all the rigs are loaded. */
            for (Rig r : matchingRigs)
            {
                if (!this.rigBookings.containsKey(r))
                {
                    RigBookings b = new RigBookings(r, this.day);
                    this.loadRig(b, r, ses);
                    this.rigBookings.put(r, b);
                    /* By definition, since a rig wasn't loaded, it's type wasn't 
                     * loaded either. */
                    this.loadRigType(r, ses, capsList);
                }
            }
            
            /* Complete the request capabilities resource loop. */
            RigBookings first = this.rigBookings.get(matchingRigs.get(0));
            RigBookings prev = first;
            for (int i = 1; i < matchingRigs.size(); i++)
            {
                RigBookings next = this.rigBookings.get(matchingRigs.get(i));
                prev.setCapsLoopNext(reqCaps, next);
                prev = next;
            }
            prev.setCapsLoopNext(reqCaps, first);
            
            /* Load the request capabilities bookings. */
            for (Bookings booking : bookings)
            {
                MBooking membooking = new MBooking(booking, this.day);
                RigBookings next = first;

                do
                {
                    if (next.areSlotsFree(membooking))
                    {
                        if (next.commitBooking(membooking))
                        {
                            /* If there is a next booking, try load it to the next rig. */
                            first = next.getCapsLoopNext(reqCaps);
                            break;

                        }
                        else
                        {
                            this.logger.error("Failed to commit a booking to a slots that should have been empty. This " +
                                    "is a probable race condition. Ominous, but the loading search will continue regardless.");
                        }
                    }
                    next = next.getCapsLoopNext(reqCaps);
                }
                while (next != first);
                
                /* The assignment loop was completed and no position was found to put
                 * the booking, so run load balance to try to free a resource. */
                if (!next.hasBooking(membooking))
                {
                    do
                    {
                        if (this.innerLoadBalance(next, membooking, true))
                        {
                            if (next.commitBooking(membooking))
                            {
                                /* If there is a next booking, try load it to the next rig. */
                                first = next.getCapsLoopNext(reqCaps);
                                break;

                            }
                            else
                            {
                                this.logger.error("Failed to commit a booking to a slots that should have been empty. " +
                                		"This is a probable race condition. Ominous, but the loading search will " +
                                		"continue regardless.");
                            }
                        }
                        next = next.getCapsLoopNext(reqCaps);
                    }
                    while (next != first);                    
                }

                /* The balancing loop was completed and no position was found to put
                 * the booking, so the booking will need to be canceled. */
                if (!next.hasBooking(membooking))
                {
                    this.logger.error("Request capabilities '" + reqCaps.getCapabilities() + "' is over commited " +
                            "and has over lapping bookings. The booking for '" + booking.getUserNamespace() + ':' + 
                            booking.getUserName() + "' starting at " + booking.getStartTime() + " is being cancelled.");
                    booking.setActive(false);
                    booking.setCancelReason("Request capabilities was overbooked.");
                    ses.beginTransaction();
                    ses.flush();
                    ses.getTransaction().commit();
         
                    // TODO Cancel notification
                }
            }
            
            this.capsTargets.put(capsList.remove(0), prev);
        }
    }

    /**
     * Loads a rig type by creating the rig bookings type loop, then loading 
     * the rig type bookings to rigs.
     * 
     * @param rig rig in type
     * @param ses database session
     * @param capsToLoad list of request capabilities that may need to be loaded from
     *      the rigs in type
     */
    @SuppressWarnings("unchecked")
    private void loadRigType(Rig rig, Session ses, List<RequestCapabilities> capsToLoad)
    {
        RigBookings first = this.rigBookings.get(rig);
        RigBookings prev = first;
        
        /* Set up the rig type navigation loop. */
        RigType rigType = rig.getRigType();
        Set<Rig> rigs = rigType.getRigs();
        for (Rig r : rigs)
        {
            for (MatchingCapabilities match : r.getRigCapabilities().getMatchingCapabilitieses())
            {
                RequestCapabilities reqCaps = match.getRequestCapabilities();
                if (!capsToLoad.contains(reqCaps))
                {
                    capsToLoad.add(reqCaps);
                }
            }
            
            /* Don't duplicate the initial rig. */
            if (r.equals(rig)) continue;

            RigBookings next = new RigBookings(r, this.day);
            this.loadRig(next, r, ses);
            this.rigBookings.put(r, next);
            prev.setTypeLoopNext(next);
            prev = next;
        }
        
        /* Complete the type loop. */
        prev.setTypeLoopNext(first);
        
        /* Load up the type bookings. */
        Criteria qu = ses.createCriteria(Bookings.class)
            .add(Restrictions.eq("resourceType", ResourcePermission.TYPE_PERMISSION))
            .add(Restrictions.eq("rigType", rigType))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("startTime", this.dayEnd))
            .add(Restrictions.gt("endTime", this.dayBegin));
        
        for (Bookings booking : (List<Bookings>)qu.list())
        {
            MBooking membooking = new MBooking(booking, this.day);
            RigBookings next = first;

            do
            {
                if (next.areSlotsFree(membooking))
                {
                    if (next.commitBooking(membooking))
                    {
                        /* If there is a next booking, try load it to the next rig. */
                        first = next.getTypeLoopNext();
                        break;

                    }
                    else
                    {
                        this.logger.error("Failed to commit a booking to a slots that should have been empty. This " +
                                "is a probable race condition. Ominous, but the loading search will continue regardless.");
                    }
                }
                next = next.getTypeLoopNext();
            }
            while (next != first);
            
            /* The assignment loop was completed and no position was found to put
             * the booking, so run load balance to try to free a resource. */
            if (!next.hasBooking(membooking))
            {
                do
                {
                    if (this.innerLoadBalance(next, membooking, true))
                    {
                        if (next.commitBooking(membooking))
                        {
                            /* If there is a next booking, try load it to the next rig. */
                            first = next.getTypeLoopNext();
                            break;

                        }
                        else
                        {
                            this.logger.error("Failed to commit a booking to a slots that should have been empty. " +
                                    "This is a probable race condition. Ominous, but the loading search will " +
                                    "continue regardless.");
                        }
                    }
                    next = next.getTypeLoopNext();
                }
                while (next != first);                    
            }

            
            /* The balancing loop was completed and no position was found to put 
             * the booking, so the type was over-booked. The booking will need to be canceled. */
            if (!next.hasBooking(membooking))
            {
                
                this.logger.error("Rig type '" + rigType.getName() + "' is over commited and has over lapping bookings. " +
                    "The booking for '" + booking.getUserNamespace() + ':' + booking.getUserName() + "' starting at " +
                    booking.getStartTime() + " is being cancelled.");
                booking.setActive(false);
                booking.setCancelReason("Rig type was overbooked.");
                ses.beginTransaction();
                ses.flush();
                ses.getTransaction().commit();
   
                // TODO Cancel notification
            }
        }
                    
        this.typeTargets.put(rigType, first);
    }

    /**
     * Loads the rig booking times for the rig. If the rig is over committed
     * (overlapping rig bookings), one of the bookings is canceled.
     * 
     * @param bookings rig bookings container
     * @param rig rig to load bookings from
     * @param ses database session
     */
    @SuppressWarnings("unchecked")
    private void loadRig(RigBookings bookings, Rig rig, Session ses)
    {
        Criteria qu = ses.createCriteria(Bookings.class)
            .add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION))
            .add(Restrictions.eq("rig", rig))
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("startTime", this.dayEnd))
            .add(Restrictions.gt("endTime", this.dayBegin));

        for (Bookings booking : (List<Bookings>)qu.list())
        {
            if (!bookings.commitBooking(new MBooking(booking, this.day)))
            {
                /* The rig has been over booked so this booking will 
                 * need to be canceled. */
                this.logger.error("Rig '" + rig.getName() + "' is over commited and has over lapping bookings. " +
                        "The booking for '" + booking.getUserNamespace() + ':' + booking.getUserName() + "' starting at " +
                        booking.getStartTime() + " is being cancelled.");
                booking.setActive(false);
                booking.setCancelReason("Rig was overbooked.");
                ses.beginTransaction();
                ses.flush();
                ses.getTransaction().commit();

                // TODO Cancel notification
            }
        }
    }
    
    public String getDay()
    {
        return this.day;
    }
}
