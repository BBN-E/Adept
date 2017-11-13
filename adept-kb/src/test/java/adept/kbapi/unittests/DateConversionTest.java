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

import com.google.common.base.Optional;

import adept.kbapi.KBDate;

/**
 * 
 * @author dkolas
 */
public class DateConversionTest {

	@Test
	public void testConvertXSDToTimex() {
		String[] xsdDates = new String[] { "2001-10-26", "2001-10-26+02:00", "2001-10-26Z",
				"2001-10-26+00:00" };

		for (String xsdDate : xsdDates) {
			Assert.assertEquals("Timex date should be equal to xsd date", xsdDate,
					KBDate.xsdToTimex(xsdDate));
		}
	}

	@Test
	public void testConvertTimexToXSD() {
		String[] xsdDates = new String[] { "2001-10-26", "2001-10-26+02:00", "2001-10-26Z",
				"2001-10-26+00:00" };

		for (String xsdDate : xsdDates) {
			Optional<String> converted = KBDate.timexToXSD(xsdDate);
			Assert.assertTrue(converted.isPresent());
			Assert.assertEquals("XSD date should be equal to timex date when present", xsdDate,
					KBDate.timexToXSD(xsdDate).get());
		}

		String[] timexOnlyDates = new String[] { "200X", "2001-01-EV", "2001" };

		for (String timexOnlyDate : timexOnlyDates) {
			Optional<String> converted = KBDate.timexToXSD(timexOnlyDate);
			Assert.assertFalse("Optional should be absent on timex only dates",
					converted.isPresent());
		}

	}

}