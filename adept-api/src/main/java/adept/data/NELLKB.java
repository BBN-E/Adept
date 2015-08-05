/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.data;

import java.net.URL;
import java.util.Date;
import java.util.List;

import adept.common.Triple;


/**
 * The Class NELLKB.
 */
public class NELLKB extends TripleStore {

	/**
	 * The Class Metadata.
	 */
	public class Metadata {

		/** The temporal range. */
		Range<Date> temporalRange;

		/** The lat lon point. */
		LatLonPoint latLonPoint;
	}

	/**
	 * Instantiates a new nellkb.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param connectionProperties
	 *            the connection properties
	 */
	public NELLKB(String dataSourceName,
			ConnectionProperties connectionProperties) {
		super(dataSourceName, connectionProperties);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the justifications.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 * @return the justifications
	 */
	public List<Justification> getJustifications(IEntity entity, ISlot slot,
			IValue value) {
		// TODO
		return null;
	}

    /**
     * Get the justifications.
     * 
     * @param entity
     *            the entity
     * @param slot
     *            the slot
     * @param value 
     *            the value
     * @return the justifications
     */
    public Justification getNELLJustifications(IEntity entity, ISlot slot,
            IValue value) {
        // TODO
        return null;
    }

	/**
	 * Gets the relevant text on demand.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @return the relevant text on demand
	 */
	public List<URL> getRelevantTextOnDemand(IEntity entity, ISlot slot) {
		// TODO
		return null;
	}

	/**
	 * Perform kb inference.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @return the i value
	 */
	public IValue performKBInference(IEntity entity, ISlot slot) {
		// TODO
		return null;
	}

	/**
	 * Gets the metadata.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 * @return the metadata
	 */
	public Metadata getMetadata(IEntity entity, ISlot slot, IValue value) {
		// TODO
		return null;
	}
}