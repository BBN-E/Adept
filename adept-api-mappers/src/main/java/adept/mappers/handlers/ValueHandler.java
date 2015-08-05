/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.mappers.handlers;

import thrift.adept.common.*;



/**
 * The Class Value.
 */
public class ValueHandler implements ValueService.Iface {

	private Value myValue;

	/**
	 * Instantiates a new value.
	 * 
	 * @param valueId
	 *            the value id
	 */
	public ValueHandler(long valueId) {
		super();
		myValue = new Value();
		myValue.valueId = valueId;
	}

	/**
	 * Gets the value id.
	 * 
	 * @return myValue.the value id
	 */
	public long getValueId() {
		return myValue.valueId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.IValue#getValue()
	 */
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public Value getValueStruct() {
		return myValue;
	}

}
