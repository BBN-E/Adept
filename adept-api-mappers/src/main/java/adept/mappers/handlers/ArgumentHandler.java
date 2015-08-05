/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
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
