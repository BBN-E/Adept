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

package adept.kbapi.sparql;

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

import com.google.common.collect.ImmutableMap;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * escape Sparql special characters.
 */
public class SparqlUtils {

	/**
	 * See http://www.w3.org/TR/rdf-sparql-query/#grammarEscapes
	 * 
	 * @param name
	 * @return
	 */
	private static final ImmutableMap<String, String> SPARQL_ESCAPE_SEARCH_REPLACEMENTS = ImmutableMap
			.<String, String> builder().put("\t", "\\t").put("\n", "\\n").put("\r", "\\r")
			.put("\b", "\\b").put("\f", "\\f").put("\"", "\\\"").put("'", "\\'").put("\\", "\\\\")
			.build();

	public static String escape(String string) {

		StringBuffer bufOutput = new StringBuffer(string);
		for (int i = 0; i < bufOutput.length(); i++) {
			String replacement = SPARQL_ESCAPE_SEARCH_REPLACEMENTS.get("" + bufOutput.charAt(i));
			if (replacement != null) {
				bufOutput.deleteCharAt(i);
				bufOutput.insert(i, replacement);
				// advance past the replacement
				i += (replacement.length() - 1);
			}
		}
		return bufOutput.toString();
	}

	/**
	 * @param resource
	 * @return
	 */
	public static String getLocalName(Resource resource) {
		String uri = resource.getURI();
		return uri.substring(uri.indexOf("#") + 1);
	}

}