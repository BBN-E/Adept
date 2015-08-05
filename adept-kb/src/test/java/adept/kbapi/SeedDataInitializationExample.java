package adept.kbapi;

import adept.serialization.*;
import adept.common.*;
import adept.utilities.*;
import adept.metadata.*;
import adept.io.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

import edu.stanford.nlp.StanfordCoreNlpProcessor;
import edu.stanford.nlp.MIMLRelationExtractor;

import com.google.common.collect.ImmutableMap;

/**
 * This script is only meant as an example to
 * demonstrate how initial seed data can be inserted 
 * into the Adept KB. 
 *
 * While it makes use of the Stanford mention detector and coreference 
 * module for populating entities and entity mentions into the KB, 
 * it only references a potential relation extractor in its inline comments.
 * This is because no existing Adept relation extractor produces
 * relations conforming to either the TAC-SF or the Rich ERE
 * ontologies, which are the only two that the KB supports.
 * 
 * It primarily demonstrates how instances of {@link adept.common.DocumentRelation}
 * class, that has been newly added in Adept API v2.0 to support relations
 * in the KB, can be created from instances of {@link adept.common.Relation}, which
 * upto this point was the only way that Adept supported the notion of relations.
 * For entity linking, exact string matching of the entity mentions
 * is used. Exact string match is also used to align relation arguments 
 * with entity mentions.
 */
public class SeedDataInitializationExample {
	
	private static StanfordCoreNlpProcessor processor;
	
	// example relation extractor is used, because no existing Adept
	// relation extractor currently produces relations
	// conforming to TAC-SF or Rich ERE.
	// private static SomeRelationExtractor extractor;
	
	// this map is required for inserting relations into the KB
	private static Map<Long, KBEntity> entityKbIdMap = new HashMap<Long, KBEntity>();
	
	public static void main(String[] args)
	{
		 doActivate();
			
		 List<String> filenames = new ArrayList<String>();
		 try
		 {
			adept.io.Reader.getInstance().readFileIntoLines(Reader.getAbsolutePathFromClasspathOrFileSystem(args[0]), filenames);
		 }
		 catch(Exception e)
		 {
			System.out.println("Error reading file list");
			e.printStackTrace();
		 }
					
	     List<String> entityIds = new ArrayList<String>();
	     List<String> relationIds = new ArrayList<String>();
		 for(String filename : filenames)
		 {

			    HltContentContainer hltcc = new HltContentContainer();	
				
				// make adept document object
			    Document document = DocumentMaker.getInstance().createDefaultDocument(filename.substring(filename.lastIndexOf("/")+1), null, "Text", filename, "English", filename, hltcc);
			    
				// run the stanford corenlp processor
			    // run relation extractor
			    try
			    {

			    	hltcc = processor.process(document, hltcc);

			    	List<Relation> relations = null;
					//relations = extractor.process(document, hltcc);		
					hltcc.setRelations(relations);
			    }
			    catch(Exception e)
			    {
			    	System.out.println("Exception. Setting hltcc to NULL.");
			    	hltcc = null;
			    }
			    
			   entityIds.addAll(insertEntitiesIntoKb(hltcc));
			   relationIds.addAll(insertRelationsIntoKb(hltcc));

			}
			
			System.out.println("Number of entities inserted: " + entityIds.size());
		    System.out.println("Number of relations inserted: " + relationIds.size());
	}
	
	/**
	 * 
	 * 
	 */
	public static List<String> insertEntitiesIntoKb(HltContentContainer hltcc) 
	{
		List<String> entityUris = new ArrayList<String>();
		
		if(hltcc==null)
			return entityUris;
		
		// see if entities in hltcc already exist in KB. If yes, update
		// previously existing entity with new mentions. Else, add new entity.
		KBUpdateProcessor updater = new KBUpdateProcessor();
		KBQueryProcessor queryer = new KBQueryProcessor();
		for(Entity entity : hltcc.getCoreferences().get(0).getEntities())
		{
			// set source algorithm for canonical mention
			entity.getCanonicalMention().setSourceAlgorithm(new SourceAlgorithm("StanfordCoreNLP", "Stanford University"));
			
			// get mentions associated with this entity
			List<EntityMention> mentions = new ArrayList<EntityMention>();
			for(EntityMention mention : hltcc.getCoreferences().get(0).getResolvedMentions())
			{
				if(mention.getEntityIdDistribution().containsKey(entity.getEntityId()))
				{
					// set algorithm name
					mention.setSourceAlgorithm(new SourceAlgorithm("StanfordCoreNLP", "Stanford University"));
					mentions.add(mention);
				}
			}			
						
			if(mentions.size()>0)
			{
				// determine if entity is already present in KB. It is
				// upto the TA-1 performers to figure out an algorithm to do this. Here
				// we demonstrate a simple way, which is to just compare the associated mentions value
				List<KBID> kbEntityIds = queryer.getEntitiesByStringReference(entity.getValue());
							
				//if already present, just update the KbEntity
				// with new mentions
				if(kbEntityIds != null && kbEntityIds.size()>0)
				{
					System.out.println("found existing KB entity with this value!");
					for(KBID kbId : kbEntityIds)
					{
						Entity kbEntity = queryer.getEntityById(kbId); 

						// updater.updateEntity call
						updater.updateEntity(kbEntity.getBestKBEntity(), kbEntity, mentions, null);

					}
				}
				// else insert fresh entity
				else
				{
					KBID entityUri = updater.insertEntity(entity, mentions, null);
					if(entityUri != null)
					   entityUris.add(entityUri.getKBUri());
					System.out.println("inserted entity: " + entity.getId().toString());
					
					// insert mapping into entity map for use during relation insert
					entityKbIdMap.put(entity.getEntityId(), new KBEntity(entityUri.getKBUri(), entity.getEntityType(), KBParameters.ADEPT_KB_IDENTIFIER));
				}
			}
			else
			{
				//System.out.println("0 mentions");
				// do nothing
			}
		}
		
		return entityUris;
	}
	
	/**
	 * 
	 * 
	 */
	public static List<String> insertRelationsIntoKb(HltContentContainer hltcc) 
	{
		KBUpdateProcessor updater = new KBUpdateProcessor();
		KBQueryProcessor queryer = new KBQueryProcessor();
		List<String> relationUris = new ArrayList<String>();
		
		
		if(hltcc==null)
			return relationUris;
		
		for(Relation r : hltcc.getRelations())
		{	
			List<RelationMention> provenances = new ArrayList<RelationMention>();
			RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type(r.getRelationType()));
			
			for(Argument arg : r.getArguments())
			{
				// resolve argument to some entity mention in the document
				EntityMention mention = mentionChunkAligner(arg.getBestArgument(), hltcc.getEntityMentions());
				
				if(mention == null)
				{
					throw new RuntimeException("ERROR: Argument does not align with any entity mention in document.");
				}
				
				// create new RelationMention argument with this entity mention
				relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(mention, new Type(arg.getArgumentType()), 
						arg.getConfidence()));
				
			}
			RelationMention relationMention = relationMentionBuilder.build();
			provenances.add(relationMention);
			
			DocumentRelation.Builder docRelationBuilder = DocumentRelation.builder(new Type(r.getRelationType()));
			docRelationBuilder.addProvenances(provenances);
			for(RelationMention.Filler argument : relationMention.getArguments())
			{
				// get entity that this mention resolves to
				Entity entity = null;
				long entityId = argument.asEntityMention().get().getBestEntityId(); 
				for(Entity e : hltcc.getCoreferences().get(0).getEntities())
				{
					if(entityId == e.getEntityId())
					{
						entity = e;
						break;
					}
				}
				
				docRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(entity, new Type(argument.getArgumentType()), 
						argument.getConfidence()));
			}
			
            DocumentRelation docRelation = docRelationBuilder.build();

            // insert relation
            KBID relationUri = updater.insertRelation(docRelation, entityKbIdMap, null);
            if(relationUri != null)
            	relationUris.add(relationUri.getKBUri());
		}
		
		return relationUris;
	}
	
	/**
	 * 
	 * 
	 */
	private static void doActivate()
	{				
		//extractor = new SomeRelationExtractor();
		processor = new StanfordCoreNlpProcessor();
		try {
			processor.activate("edu/stanford/nlp/StanfordCoreNlpProcessorConfig.xml");
            //extractor.activate(<config file path>);
            StanfordCoreNlpProcessor pipeline = new StanfordCoreNlpProcessor();
		    pipeline.activate("edu/stanford/nlp/StanfordCoreNlpProcessorConfig.xml");;
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	
	/**
	 * 
	 */
	private static EntityMention mentionChunkAligner(Chunk argChunk, List<EntityMention> entityMentions)
	{
		for(EntityMention mention : entityMentions)
		{
			if(argChunk.equals(mention))
			{
				return mention;
			}
		}
		
		return null;
	}

	
	
}