/*******************************************************************************
 * Raytheon BBN Technologies Corp., January 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.common;

import java.util.List;
import java.util.ArrayList;

/**
 * Prefer to use {@link DocumentEvent} or {@link EventTextSet}.
 */
@Deprecated
public class EventRelations
{
    /** The coreferences. */
    private List<Event> coreferences;

    /** The causalities. */
    //    private List<List<Event>> causalities;

    /** The parts-of. */
    //    private List<List<Event>> partsOf;

    /**
     * Instantiates a new event relationship
     *
     */
    public EventRelations() 
    {
        coreferences = new ArrayList<Event>();
        //        causalities = new ArrayList<List<Event>>();
        //        partsOf = new ArrayList<List<Event>>();
    }

    /**
	 * Gets the coreferences.
	 *
	 * @return the coreferences
	 */
    public List<Event> getCoreferences()
    {
        return coreferences;
    }
    
	/**
	 * Gets the causalities.
	 *
	 * @param coreferences the new coreferences
	 * @return the causalities
	 */
    /*    public List<List<Event>> getCausalities()
    {
        return causalities;
    }
    */
	/**
	 * Gets the partsOf.
	 *
	 * @return the partsOf
	 */
    /*    public List<List<Event>> getPartsOf()
    {
        return partsOf;
        }*/

	/**
	 * Sets the coreferencs.
	 *
	 * @param coreferences the coreferences
	 */
    public void setCoreferences(List<Event> coreferences)
    {
        this.coreferences = coreferences;
    }

	/**
	 * Sets the coreferencs.
	 *
	 * @param causalities the causalities
	 */
    /*    public void setCausalities(List<List<Event>> causalities)
    {
        this.causalities = causalities;
        }*/

	/**
	 * Sets the coreferencs.
	 *
	 * @param partsOf the partsOf
	 */
    /*    public void setPartsOf(List<List<Event>> partsOf)
    {
        this.partsOf = partsOf;
    }
    */
}
