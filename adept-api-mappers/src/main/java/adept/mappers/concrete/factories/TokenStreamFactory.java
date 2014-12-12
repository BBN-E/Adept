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

package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.TokenStream;

public class TokenStreamFactory implements org.dozer.BeanFactory {
	public TokenStream createBean(Object source, Class sourceClass, String targetBeanId) {

		adept.common.Document document = new adept.common.Document("", null, "", "", "");
		document.setValue("");
		return new adept.common.TokenStream(null, null, "", null, null, document);
	}
}
