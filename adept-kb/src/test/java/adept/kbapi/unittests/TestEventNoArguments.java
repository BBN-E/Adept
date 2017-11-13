/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

/*-
 * #%L
 * adept-kb
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import adept.common.OntType;
import adept.kbapi.KBEvent;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;

/**
 *
 * @author dkolas
 */
public class TestEventNoArguments extends KBUnitTest{


	@Test
	public void testEventWithNoArguments() throws KBUpdateException, KBQueryException{
		KBEvent.InsertionBuilder builder = KBEvent.eventInsertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Meet"), .1f);
		builder.addProvenance(generateProvenance("THINGS"));
		KBEvent event = builder.insert(kb);
		KBEvent queriedEvent = kb.getEventById(event.getKBID());
		Assert.assertNotNull("Queried event should not be null", queriedEvent);
		Assert.assertEquals("Queried event should equal inserted", event, queriedEvent);

		List<KBEvent> events = kb.getEventsByStringReference("THINGS");
		Assert.assertEquals("Should be one event returned", 1, events.size());
		Assert.assertEquals("String queried event should equal inserted", event, events.get(0));

		events = kb.getEventsByStringReference("things");
		Assert.assertEquals("Should be one event returned", 1, events.size());
		Assert.assertEquals("String queried event should equal inserted", event, events.get(0));

		events = kb.getEventsByRegexMatch("th.*", false);
		Assert.assertEquals("Should be one event returned", 1, events.size());
		Assert.assertEquals("String queried event should equal inserted", event, events.get(0));

		events = kb.getEventsByRegexMatch("th.*", true);
		Assert.assertEquals("Should be one event returned", 0, events.size());


	}
}