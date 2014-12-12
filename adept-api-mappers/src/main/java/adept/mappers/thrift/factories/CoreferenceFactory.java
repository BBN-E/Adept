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
import adept.common.Coreference;

public class CoreferenceFactory implements org.dozer.BeanFactory {
	public Coreference createBean(Object source, Class sourceClass, String targetBeanId) {
		return new adept.common.Coreference(0);
	}
}
