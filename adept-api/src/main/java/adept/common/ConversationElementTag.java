/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class ConversationElementTag, which represents an SGML tag within a structured document.
 * This class has been included to make the DEFT annotations compatible with the representations used by
 * the community, and otherwise doesn't have a lot of semantic justification.
 */
public class ConversationElementTag {

    protected final String tagName;

    // Could be made immutable.
    protected final Map<String,Token> attributes;

    protected final CharOffset charOffset;

	public ConversationElementTag(String tagName, Map<String,Token> attributes, int begin, int end) {
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
