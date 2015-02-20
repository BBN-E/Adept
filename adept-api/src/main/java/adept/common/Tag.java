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

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Tag, which represents an SGML tag within a structured document.
 */
public class Tag {

    protected final String tagName;

    // Could be made immutable.
    protected final Map<String,Token> attributes;

    protected final CharOffset charOffset;

	public Tag(String tagName, Map<String,Token> attributes, int begin, int end) {
        checkArgument(attributes!=null);
        for( String key : attributes.keySet()) {
            checkArgument(key!=null);
            checkArgument(attributes.get(key)!=null);
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