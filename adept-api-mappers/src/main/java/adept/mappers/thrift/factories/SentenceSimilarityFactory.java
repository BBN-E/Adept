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
import adept.common.SentenceSimilarity;

import java.util.Map;
import java.util.HashMap;

public class SentenceSimilarityFactory implements org.dozer.BeanFactory {
	public SentenceSimilarity createBean(Object source, Class sourceClass, String targetBeanId) {
        
		Map<String,Float> similarityscore = new HashMap<String,Float>();
		//similarityscore.put("SCORE", (Float)0);
		return new adept.common.SentenceSimilarity(similarityscore, null, null);
	}
}
