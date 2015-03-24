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