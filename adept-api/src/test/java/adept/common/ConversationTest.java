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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.junit.Ignore;
import org.junit.Test;

import adept.io.Reader;
import adept.serialization.XMLSerializer;
import adept.serialization.SerializationType;

import static org.junit.Assert.*;

public class ConversationTest {

    // Create dummy Chunk using code from ChunkTest, for ease of testing
	private Chunk dummyChunk(String content) {
		String corpusId = "corpusId";
		String type = "corpusType";
		String name = "corpusName";
		String uri = "corpusUri";
		Corpus corpus = new Corpus(corpusId, type, name, uri);
		String docId = "docId";
		String docType = "docType";
		String docUri = "docUri";
		String language = "English";
		Document doc = new Document(docId, corpus, docType, docUri, language );
		doc.setValue(content);
		String[] spaceTokens = content.split(" ");
		TokenStream tokenStream = new TokenStream(TokenizerType.WHITESPACE, 
													TranscriptType.SOURCE, 
													language, 
													ChannelName.NONE, 
													ContentType.TEXT, 
													doc);
//		tokenStream.setDocument( doc );
		int startOffset = 0;
		int sequenceId = 0;
		for( String s : spaceTokens)
		{
			int endOffset = Math.min(startOffset + s.length()+1, content.length());
			CharOffset charOffset = new CharOffset( startOffset, endOffset );
			Token t = new Token( sequenceId, charOffset, s);
			startOffset = endOffset;
			++ sequenceId;
			tokenStream.add(t);
		}		
	    final TokenOffset tokenOffset = new TokenOffset(0, sequenceId-1);
	    return new Chunk(tokenOffset, tokenStream);
	}
	
	


    @Test
    public void testDiscussionForumConversationRepresentation() {
        try
        {
        	// read post
        	HltContentContainer hltcc = Reader.getInstance().LDCForumtoHltContentContainer(Reader.getInstance().getAbsolutePathFromClasspathOrFileSystem("adept/test/Columbia_AuthorityClaims_input.xml"), null, "English");
        	System.out.println("HltContentContainer with ConversationElements obtained.");
        	
        	// assert statements
        	assertNotNull(hltcc.getConversationElements());
        	assertEquals(1, hltcc.getConversationElements().size());

        	// Initialize serializer instance
    		//XMLSerializer xmls = new XMLSerializer(SerializationType.XML);

    		// serialize
    		//String serialized = xmls.serializeAsString(hltcc);
    		//System.out.println(serialized);
        }
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }



  
}