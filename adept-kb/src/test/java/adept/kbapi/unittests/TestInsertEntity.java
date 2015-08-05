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
import adept.metadata.*;
import adept.kbapi.*;
import adept.io.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;
import com.google.common.collect.ImmutableMap;

/**
 * Unit test class to test entity insertion
 * functionality
 */
public class TestInsertEntity {
	
	@Test
	public void testInsertEntity()
	{
		try
		{
			HltContentContainer hltContentContainer = new HltContentContainer();
	        Document document = DocumentMaker.getInstance().createDefaultDocument( "sample.txt", null, "Text", "sample_entity_1.txt", "English",
	        		Reader.getInstance().getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample.txt"), 
	        		hltContentContainer);
			CharOffset testCharOff = new CharOffset(0, 16);
			String testText = "BBN Technologies";
			Token testToken = new Token(0, testCharOff, testText);
			TokenStream testTokenStream = new TokenStream(TokenizerType.WHITESPACE, TranscriptType.SOURCE,"English", ChannelName.NONE, ContentType.TEXT, document);
			TokenOffset testTokenOffset = new TokenOffset(0, 0);
			testTokenStream.add(testToken);
			
			// create entity
			Entity entity = new Entity(1,new Type("organization"));
	
			// create mention
			List<EntityMention> mentions = new ArrayList<EntityMention>();
			EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream); 
			mention.addEntityConfidencePair(entity.getEntityId(), 0.9f);
			mention.setMentionType(new Type("NAME"));
			mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
			mentions.add(mention);

			// set canonical mention
			entity.setCanonicalMentions(mention);
			entity.setEntityConfidence(0.8);
			entity.setCanonicalMentionConfidence(0.63);
			entity.addType(new Type("person"), 0.22);
			entity.addType(new Type("location"), 0.33);
			
			// insert entity into KB
			KBUpdateProcessor updateProcessor = new KBUpdateProcessor();

			List<KBEntity> externalKBEntities = new ArrayList<KBEntity>();
			externalKBEntities.add(new KBEntity("BBN_Wikipedia", new Type("organization"), "Wikipedia"));
			
			KBID entityId = updateProcessor.insertEntity(entity, mentions, externalKBEntities);
		    System.out.println("Inserted entity: " + entityId.getKBUri());
		    assertTrue(entityId != null);
		    
		    TestKBUri.setKBEntityUri(entityId);
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}