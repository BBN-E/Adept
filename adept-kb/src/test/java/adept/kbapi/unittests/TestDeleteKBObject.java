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

package adept.kbapi.unittests;

import adept.serialization.*;
import adept.utilities.*;
import adept.common.*;
import adept.kbapi.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Unit test class to test deletion
 */
public class TestDeleteKBObject {
	
	@Test
	public void testDeleteKBObject()
	{
		try
		{
		    KBUpdateProcessor updateProcessor = new KBUpdateProcessor();
						
			boolean success = updateProcessor.deleteKBObject(TestKBUri.getKBEntityUri());
			assertTrue(success);
			
			success = updateProcessor.deleteKBObject(TestKBUri.getKBRelationUri());
			assertTrue(success);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}