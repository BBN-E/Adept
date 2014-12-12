/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.ArgumentTuple;

public class ArgumentTupleFactory implements org.dozer.BeanFactory {
	public ArgumentTuple createBean(Object source, Class sourceClass, String targetBeanId) {
		final thrift.adept.common.ArgumentTuple argumentTuple = (thrift.adept.common.ArgumentTuple) source;

		return new adept.common.ArgumentTuple(null);
	}
}
