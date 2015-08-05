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

package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.List;


/**
 * The Class JointRelationCoreference represents the container for the output of
 * the algorithm that processes both coreference resolution and relation
 * extraction simultaneously.
 */
public class JointRelationCoreferenceHandler extends HltContentHandler implements JointRelationCoreferenceService.Iface {

	private JointRelationCoreference myJointRelationCoreference;
	/**
	 * Instantiates a new joint relation coreference.
	 */
	public JointRelationCoreferenceHandler() {
		super();
		myJointRelationCoreference = new JointRelationCoreference();
		myJointRelationCoreference.id = myItem.id;
		myJointRelationCoreference.value = myItem.value;

	}

	/**
	 * Gets the coreference.
	 * 
	 * @return myJointRelationCoreference.the coreference
	 */
	public Coreference getCoreference() {
		return myJointRelationCoreference.coreference;
	}

	/**
	 * Sets the coreference.
	 * 
	 * @param coreference
	 *            the new coreference
	 */
	public void setCoreference(Coreference coreference) {
		myJointRelationCoreference.coreference = coreference;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return myJointRelationCoreference.the relations
	 */
	public List<Relation> getRelations() {
		return myJointRelationCoreference.relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
		myJointRelationCoreference.relations = relations;
	}

	public JointRelationCoreference getJointRelationCoreference() {
		return myJointRelationCoreference;
	}

}