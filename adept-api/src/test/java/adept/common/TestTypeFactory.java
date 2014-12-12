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

package adept.common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating TestType objects.
 */
public class TestTypeFactory {

	/** The st factory. */
	static SyntacticTagFactory stFactory;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test syntactic tag factory.
	 */
	@Test
	public void testSyntacticTagFactory() {
		
		try {
			stFactory = SyntacticTagFactory.getInstance();
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		IType stType = stFactory.getType("NP-PRN");		
		System.out.println("Syntactic Tag Type: " + stType.getType().toString());		
		assertEquals("Syntactic Tag Test", "NP-PRN", stType.getType().toString());
	}
	
}
