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
 * Unit test class to test KB query
 * functionality
 */
public class TestKBQueries {

	 static KBQueryProcessor queryProcessor = new KBQueryProcessor();
	
     @Test
     public void testQueryEntityById()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 Entity entity = queryProcessor.getEntityById(TestKBUri.getKBEntityUri());
    		 
    		 if(entity==null)
    			 assertion = false;
    		 //System.out.println("entity type: " + entity.getEntityType().getType());
    		 if(!entity.getEntityType().getType().equals("organization"))
    			 assertion = false;
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryEntitiesByType()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> entityIds = queryProcessor.getEntitiesByType(new Type("organization"));
    		 
    		 for(KBID entityId : entityIds)
    		 {
    			 Entity entity = queryProcessor.getEntityById(entityId);
    			 if(!entity.getEntityType().getType().equals("organization"))
    				 assertion = false;
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryEntitiesByStringRef()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> entityIds = queryProcessor.getEntitiesByStringReference("BBN Technologies");
    		 
    		 int entitiesSize = entityIds.size();
    		 assertTrue("Size of entities is not greater than 0", entitiesSize>0);
    		 
    		 for(KBID entityId : entityIds)
    		 {
    			 // get mentions for entity
    			 List<EntityMention> mentions = queryProcessor.getEntityMentionsForKBEntity(TestKBUri.getKBEntityUri());
    			 
    			 int mentionsSize = mentions.size();
    			 assertTrue(mentionsSize>0);
    			 
    			 // for each mention, check that it matches with the arg string
    			 for(EntityMention mention : mentions)
    			 {
    				 if(!mention.getValue().equals("BBN Technologies"))
    					 assertion = false;
    			 }
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryEntitiesByRegexMatch()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> entityIds = queryProcessor.getEntitiesByRegexMatch("^BBN");
    		 
    		 int entitiesSize = entityIds.size();
    		 assertTrue("Size of entities is not greater than 0 in getEntitiesByRegexMatch test", entitiesSize>0);
    		 
    		 for(KBID entityId : entityIds)
    		 {
    			 // get mentions for entity
    			 List<EntityMention> mentions = queryProcessor.getEntityMentionsForKBEntity(TestKBUri.getKBEntityUri());
    			 
    			 int mentionsSize = mentions.size();
    			 assertTrue(mentionsSize>0);
    			 
    			 // for each mention, check that it matches with the arg string
    			 for(EntityMention mention : mentions)
    			 {
    				 if(!mention.getValue().equals("BBN Technologies"))
    					 assertion = false;
    			 }
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     
     @Test
     public void testQueryEntitiesByValAndType()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> entityIds = queryProcessor.getEntitiesByValueAndType("BBN Technologies", new Type("organization"));
    		 int entitiesSize = entityIds.size();
    		 assertTrue("Size of entities is not greater than 0", entitiesSize>0);

    		 for(KBID entityId : entityIds)
    		 {
    			 Entity entity = queryProcessor.getEntityById(entityId);
    			 
    			 if(!entity.getEntityType().getType().equals("organization"))
    				 assertion = false;
    			 
    			 List<EntityMention> mentions = queryProcessor.getEntityMentionsForKBEntity(TestKBUri.getKBEntityUri());
    			 
    			 int mentionsSize = mentions.size();
    			 assertTrue("Size of mentions is not greater than 0", mentionsSize>0);
    			 
    			 // for each mention, check that it matches with the arg string
    			 for(EntityMention mention : mentions)
    			 {
    				 if(!mention.getValue().equals("BBN Technologies"))
    				 {
    					 assertion = false;
    				 }
    					 
    			 }
    			 
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
    @Test
     public void testQueryRelationById()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 DocumentRelation relation = queryProcessor.getRelationById(TestKBUri.getKBRelationUri());
    		 
    		 if(relation==null)
    			 assertion = false;
    		 if(!relation.getRelationType().getType().equals("Resident"))
    			 assertion = false;
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryRelationByType()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> relationIds = queryProcessor.getRelationsByType(new Type("Resident"));
    		 
    		 for(KBID relationId : relationIds)
    		 {
    			 DocumentRelation relation = queryProcessor.getRelationById(relationId);
    			 if(!relation.getRelationType().getType().equals("Resident"))
    				 assertion = false;
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryRelationByArg()
     {
    	 try
    	 {
    		 boolean assertion = false;
        	 
        	 List<KBID> relationIds = queryProcessor.getRelationsByArg(TestKBUri.getKBEntityUri());
        	 int size = relationIds.size();
        	 assertTrue("Size of relations is not > 0", size>0);
        	 
        	 for(KBID relationId : relationIds)
        	 {
        		 DocumentRelation relation = queryProcessor.getRelationById(relationId);
        		 List<KBID> externalKbIds = queryProcessor.getExternalKBIDs(relationId);
        		 for(KBID kbId : externalKbIds)
        		 {
        			if(kbId.getKBUri().equals("External_Resident_Relation"))
        			{
        				assertion = true;
        				break;
        			}
        		 }
        			 
        	 }
        	 
        	 assertTrue("assertion is false in testQueryRelationByArg", assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryRelationByArgAndType()
     {
    	 try
    	 {
        	 List<KBID> relationIds = queryProcessor.getRelationsByArgAndType(TestKBUri.getKBEntityUri(), new Type("LivesIn"));
        	 int size = relationIds.size();
        	 assertTrue(size==0);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryRelationByStringRef()
     {
    	 try
    	 {
    		 boolean assertion = true;
    		 
    		 List<KBID> relationIds = queryProcessor.getRelationsByStringReference("BBN Technologies");
    		 
    		 int size = relationIds.size();
    		 assertTrue("Size of relations is not greater than zero", size>0);
    		 
    		 for(KBID relationId : relationIds)
    		 {
    			 // get mentions for relation
    			 List<RelationMention> mentions = queryProcessor.getRelationMentionsForKBRelation(TestKBUri.getKBRelationUri());
    			 
    			 int mentionsSize = mentions.size();
    			 assertTrue(mentionsSize>0);
    			 
    			 // for each mention, check presence of justification
    			 for(RelationMention mention : mentions)
    			 {
    				 assertTrue("Relation justification is null", mention.getJustification()!=null);
    				 
    				 // TODO: verify justification value
    			 }
    		 }
    		 
    		 assertTrue(assertion);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
    
     }
     
     @Test
     public void testQueryMentionsForEntity()
     {
    	 try
    	 {
    		 List<EntityMention> mentions = queryProcessor.getEntityMentionsForKBEntity(TestKBUri.getKBEntityUri());
    		 int mentionsSize = mentions.size();
    		 assertTrue(mentionsSize==1);
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     @Test
     public void testQueryMentionsForRelation()
     {
    	 try
    	 {
    		 List<RelationMention> mentions = queryProcessor.getRelationMentionsForKBRelation(TestKBUri.getKBRelationUri());
    		 int mentionsSize = mentions.size();
    		 assertTrue(mentionsSize==1); 
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
     
     @Test
     public void testQueryKBObjectsWithinChunk()
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
    		 
    		 HltContentContainer hltcc = queryProcessor.getKBObjectsWithinChunk(new Chunk(testTokenOffset, testTokenStream));
    		 assertTrue("Contains <=0 number of relations", hltcc.getDocumentRelations().size()>0);
    		 assertTrue("Contains <=0 number of entities", hltcc.getCoreferences().get(0).getEntities().size()>0);

    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
 

}