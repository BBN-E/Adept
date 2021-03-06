package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Prefer to use {@link DocumentEvent} or {@link EventTextSet}.
 */
@Deprecated
public class EventRelations implements Serializable {
	private static final long serialVersionUID = 8872563553886561175L;
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
