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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class JointRelationCoreference represents the container for the output of
 * the algorithm that processes both coreference resolution and relation
 * extraction simultaneously.
 */
public class JointRelationCoreference extends HltContent {

	/** The coreference. */
	private Coreference coreference;

	/** The relations. */
	private List<Relation> relations;

	/**
	 * Instantiates a new joint relation coreference.
	 */
	public JointRelationCoreference() {

	}

	/**
	 * Gets the coreference.
	 * 
	 * @return the coreference
	 */
	public Coreference getCoreference() {
		return coreference;
	}

	/**
	 * Sets the coreference.
	 * 
	 * @param coreference
	 *            the new coreference
	 */
	public void setCoreference(Coreference coreference) {
                checkArgument(coreference!=null);
		this.coreference = coreference;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public List<Relation> getRelations() {
		return relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
                checkArgument(relations!=null);
		this.relations = relations;
	}

}