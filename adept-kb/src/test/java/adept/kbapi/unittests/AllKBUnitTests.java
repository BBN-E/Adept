/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.kbapi.unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestEntity.class, TestRelation.class, TestSentiment.class, TestBelief.class,
		TestEvent.class, TestDates.class, TestNumbers.class, TestTemporalSpans.class,
		TestKBObjectsWithinChunk.class })
/**
 * JUnit test suite for adding all adept-kb
 * unit tests.
 *
 */
public class AllKBUnitTests {
	public static void print() {
		System.out
				.println("All methods in class adept.kbapi.unittests.TestEntity run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestRelation run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestSentiment run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestBelief run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestEvent run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestDates run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestNumbers run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestTemporalSpans run as expected.");
		System.out
				.println("All methods in class adept.kbapi.unittests.TestKBObjectsWithinChunk run as expected.");
	}
}
