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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.*;


@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestInsertEntity.class,
   TestInsertRelation.class,
   TestKBQueries.class,
   TestUpdateEntity.class,
   TestUpdateRelation.class,
   TestDeleteKBObject.class,  
})

/**
 * JUnit test suite for adding all adept-kb
 * unit tests.
 *
 */
public class AllKBUnitTests 
{   
	public static void print()
	{
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertEntity run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertRelation run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertNumber run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertDate run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestKbQueries run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestUpdateEntity run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestUpdateRelation run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestDeleteKbObject run as expected.");
	}
	
}