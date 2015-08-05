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

package adept.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import adept.module.AdeptModuleException;
import junit.framework.TestCase;


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