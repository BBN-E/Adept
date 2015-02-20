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

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

// TODO: Auto-generated Javadoc
/**
 * The Class Relation.
 */
public class RelationHandler extends HltContentHandler implements RelationService.Iface {

	private Relation myRelation;

	/**
	 * Instantiates a new relation.
	 * 
	 * @param relationType
	 *            the relation type
	 */
	public RelationHandler(Type relationType) {
		super();
		myRelation = new Relation(0L, relationType);
		myRelation.id = myItem.id;
		myRelation.value = myItem.value;
	}

	/**
	 * Gets the arguments.
	 * 
	 * @return myRelation.the arguments
	 */
	public List<Argument> getArguments() {
		return myRelation.arguments;
	}

	/**
	 * Adds the argument.
	 * 
	 * @param argument
	 *            the argument
	 * @return myRelation.true, if successful
	 */
	public boolean addArgument(Argument argument) {
		return myRelation.arguments.add(argument);
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myRelation.the confidence
	 */
	public double getConfidence() {
		return myRelation.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		myRelation.confidence = confidence;
	}

	/**
	 * Gets the relation type.
	 * 
	 * @return myRelation.the relation type
	 */
	public String getRelationType() {
		return myRelation.type.getType();
	}
	
	/**
	 * Gets the relation ont type.
	 * 
	 * @return myRelation.the relation type
	 */
//	public OntClass getRelationOntType() {
//		return myRelation.relationType.getOntClass();
//	}

	public Relation getRelation() {
		return myRelation;
	}

}