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

package adept.common;

/*-
 * #%L
 * adept-api
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

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;


/**
 * The Class ConversationElementTag, which represents an SGML tag within a structured document.
 * This class has been included to make the DEFT annotations compatible with the representations used by
 * the community, and otherwise doesn't have a lot of semantic justification.
 */
public class ConversationElementTag implements Serializable {

	private static final long serialVersionUID = 5258320815351914885L;

	protected final String tagName;

    // Could be made immutable.
    protected final Map<String,Token> attributes;

    protected final CharOffset charOffset;

	public ConversationElementTag(String tagName, Map<String,Token> attributes, int begin, int end) {
        checkArgument(attributes!=null);
        for( Map.Entry<String, Token> entry : attributes.entrySet()) {
            checkArgument(entry.getKey()!=null);
            checkArgument(entry.getValue()!=null);
        }
        this.tagName = tagName;
        this.attributes = attributes;
        this.charOffset = new CharOffset(begin, end);
	}

	public String getTagName() { return tagName; }

	public Map<String,Token> getAttributes() {
        return attributes;
	}

	public CharOffset getCharOffset() {
        return charOffset;
	}

}