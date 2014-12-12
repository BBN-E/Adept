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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import adept.module.AdeptModuleException;
import junit.framework.TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class AdeptExceptionTest.
 */
public class AdeptExceptionTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test.
	 */
	@Test
	public void test() {
		AdeptException ae = new AdeptException("This is ADEPT exception");
		AdeptModuleException ame = new AdeptModuleException("This is ADEPT module exception");
		System.out.println("Adept Exception"+ae.getMessage());
		System.out.println("Adept Module Exception"+ame.getMessage());
		
	}

}
