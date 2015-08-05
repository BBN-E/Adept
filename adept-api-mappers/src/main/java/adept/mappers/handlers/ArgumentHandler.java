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

import com.hp.hpl.jena.ontology.OntClass;
import java.util.Map;


/**
 * The Class Argument.
 */
public class ArgumentHandler extends HltContentHandler implements ArgumentService.Iface {

	private Argument myArgument;

	/** The argument type. */
	private final Type argumentType;

	/**
	 * Instantiates a new argument.
	 * 
	 * @param argumentType
	 *            the argument type
	 * @param distributionSize
	 *            the distribution size
	 */
	public ArgumentHandler(Type argumentType, int distributionSize) {
		super();
		myArgument = new Argument();
		this.argumentType = argumentType;
		myArgument.id = myItem.id;
		myArgument.value = myItem.value;
	}

	/**
	 * Gets the argument type.
	 * 
	 * @return myArgument.the argument type
	 */
	public String getArgumentType() {
		return this.argumentType.getType();
	}
	
	/**
	 * Gets the argument ont type.
	 * 
	 * @return myArgument.the argument type
	 */
//	public OntClass getArgumentOntType() {
//		return this.argumentType.getOntClass();
//	}

	/**
	 * Gets the argument distribution.
	 * 
	 * @return myArgument.the argument distribution
	 */
	public Map<ChunkUnion, Double> getArgumentDistribution() {
		return myArgument.getArgumentDistribution();
	}

	/**
	 * Sets the argument distribution.
	 * 
	 * @param argumentDistribution
	 *            the argument distribution
	 */
	public void setArgumentDistribution(
			Map<ChunkUnion, Double> argumentDistribution) {
		myArgument.argumentDistribution = argumentDistribution;
	}

	/**
	 * Adds the argument confidence pair.
	 * 
	 * @param argument
	 *            the argument
	 * @param confidence
	 *            the confidence
	 * @return myArgument.true, if successful
	 */
	public boolean addArgumentConfidencePair(ChunkUnion argument, double confidence) {
		myArgument.argumentDistribution.put(argument, confidence);
		return true;
	}

	/**
	 * Gets the best argument.
	 * 
	 * @return myArgument.the best argument
	 */
	public ChunkUnion getBestArgument() {
		// TODO:
		throw new RuntimeException("TODO: ");
	}

	public Argument getArgument() {
		return myArgument;
	}

}