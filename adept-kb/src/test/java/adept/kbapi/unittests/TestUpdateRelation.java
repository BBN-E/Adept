package adept.kbapi.unittests;

import adept.serialization.*;
import adept.utilities.*;
import adept.common.*;
import adept.kbapi.*;
import adept.metadata.*;
import adept.io.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;
import com.google.common.collect.ImmutableMap;

/**
 * Unit test class to test relation update
 * functionality
 */
public class TestUpdateRelation {
	
	@Test
	public void testUpdateRelation()
	{
		try
		{
			HltContentContainer hltContentContainer = new HltContentContainer();
			Document document = DocumentMaker.getInstance().createDefaultDocument( "sample.txt", null, "Text", "sample_relation_1.txt", "English",
	        		Reader.getInstance().getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample.txt"), 
	        		hltContentContainer);
			CharOffset testCharOff = new CharOffset(0, 16);
			String testText = "BBN Technologies";
			Token testToken = new Token(0, testCharOff, testText);
			TokenStream testTokenStream = new TokenStream(TokenizerType.WHITESPACE, TranscriptType.SOURCE,"English", 
					ChannelName.NONE, ContentType.TEXT, document);
			TokenOffset testTokenOffset = new TokenOffset(0, 0);
			testTokenStream.add(testToken);

				
			//==================================================================================
			
			// entity and entity mention creation
			Entity locationentity = new Entity(1,new Type("location"));
			Entity personentity = new Entity(2, new Type("person"));
				
			// create mention
			EntityMention locationmention = new EntityMention(0, testTokenOffset, testTokenStream); 
			locationmention.addEntityConfidencePair(locationentity.getEntityId(), 0.9f);
			locationmention.setMentionType(new Type("NAME"));
			locationmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
			
			EntityMention personmention = new EntityMention(1, testTokenOffset, testTokenStream);
			personmention.addEntityConfidencePair(personentity.getEntityId(), 0.9f);
			personmention.setMentionType(new Type("NAME"));
			personmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
			       
			// set canonical mention and confidences on entities
			locationentity.setCanonicalMentions(locationmention);
			locationentity.setEntityConfidence(0.8);
			locationentity.setCanonicalMentionConfidence(0.63);
			
			personentity.setCanonicalMentions(personmention);
			personentity.setEntityConfidence(0.87);
			personentity.setCanonicalMentionConfidence(0.1);

			// create relation mention
			RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type("Resident"));
			relationMentionBuilder.setConfidence(0.8f);
            relationMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));
            relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(personmention, 
            		new Type("person"), 0.53f));
            relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(locationmention, 
            		new Type("location"), 0.54f));
            
            List<RelationMention> provenances = new ArrayList<RelationMention>();
            provenances.add(relationMentionBuilder.build());
            
            // create document relation
			DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(new Type("Resident"));
			documentRelationBuilder.addProvenances(provenances);
            documentRelationBuilder.setConfidence(0.8f);
            documentRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(personentity, 
            		new Type("person"), 0.53f));
            documentRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(locationentity, 
            		new Type("location"), 0.54f));
			
			// external KB IDs
            List<KBRelation> externalKbIds = new ArrayList<KBRelation>();
            externalKbIds.add(new KBRelation("External_Resident_Relation", new Type("Resident"), "ExampleKB"));
            
            // map from document entities to Adept KB entities
            Map<Long, KBEntity> entityMap = new HashMap<Long, KBEntity>();
            entityMap.put(new Long(1), new KBEntity(TestKBUri.getKBEntityUri().getKBUri(), new Type("person"), KBParameters.ADEPT_KB_IDENTIFIER));
            entityMap.put(new Long(2), new KBEntity(TestKBUri.getKBEntityUri().getKBUri(), new Type("location"), KBParameters.ADEPT_KB_IDENTIFIER));
            
			// insert relation into KB
			KBUpdateProcessor updateProcessor = new KBUpdateProcessor();
			boolean result = updateProcessor.updateRelation(TestKBUri.getKBRelationUri(), documentRelationBuilder.build(),
					entityMap, null);
            assertTrue(result);
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}