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
 * Unit test class to test entity update
 * functionality
 */
public class TestUpdateEntity
{
	@Test
	public void testUpdateEntity()
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
			Entity entity = new Entity(1,new Type("location"));
	
			// create mention
			List<EntityMention> mentions = new ArrayList<EntityMention>();
			EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream); 
			mention.addEntityConfidencePair(entity.getEntityId(), 0.8f); /** note change here */
			mention.setMentionType(new Type("NAME"));
			mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
			mentions.add(mention);
            
			// set canonical mention
			entity.setCanonicalMentions(mention);
			entity.setEntityConfidence(0.3);
			entity.setCanonicalMentionConfidence(0.21);
			
			// insert entity into KB
			KBUpdateProcessor updateProcessor = new KBUpdateProcessor();

			List<KBEntity> externalKBEntities = new ArrayList<KBEntity>();
			externalKBEntities.add(new KBEntity("BBN_Wikipedia_2", new Type("organization"), "Wikipedia"));
			
			System.out.println("Updating entity URI: " + TestKBUri.getKBEntityUri());
			boolean result = updateProcessor.updateEntity(TestKBUri.getKBEntityUri(), entity, mentions, externalKBEntities);
		    System.out.println("updated entity: " + result);
		    assertTrue(result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}