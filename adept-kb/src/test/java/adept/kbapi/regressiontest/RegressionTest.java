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

package adept.kbapi.regressiontest;

import adept.common.*;
import adept.serialization.*;
import adept.io.*;
import adept.utilities.*;
import adept.metadata.*;
import adept.kbapi.*;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.util.regex.*;
import java.util.*;
import com.google.common.collect.ImmutableMap;

/**
 * Regression test for the Adept KB.
 * 
 * Note that the KB entities that the relation arguments
 * link to, and the relation arguments themselves are quite meaningless.
 * The values inserted by this script into the KB are *only* for
 * regression testing purposes. The KB is emptied of these records when the
 * program exits.
 *
 */
public class RegressionTest {
	
	static KBUpdateProcessor updater = new KBUpdateProcessor();
	static KBQueryProcessor queryer = new KBQueryProcessor();
	
	static String packageName = new Object(){}.getClass().getPackage().getName();
	static String packagePath = packageName.replace(".", "/") + "/";
	static String workingDirectory = System.getProperty("user.dir") + "/target/test-classes/" + packagePath;
	
	public static void main(String[] args)
	{
		try
		{
			// deserialize
			HltContentContainer hltcc = deserialize(Reader.getAbsolutePathFromClasspathOrFileSystem(
					"adept/kbapi/regressiontest/reference.xml"));
			
			// insert
			List<KBID> entityUris = new ArrayList<KBID>();
			List<KBID> relationUris = new ArrayList<KBID>();
			for(Coreference coref : hltcc.getCoreferences())
			{
				for(Entity entity : coref.getEntities())
				{
					List<EntityMention> mentions = new ArrayList<EntityMention>();
					for(EntityMention mention : coref.getResolvedMentions())
					{
						if(mention.getEntityIdDistribution().containsKey(entity.getEntityId()))
						{
							mentions.add(mention);
						}
					}
					KBID entityId = updater.insertEntity(entity, mentions, null);
					if(entityId!=null)
						entityUris.add(entityId);
				}
			}
			
			// make a few dummy document relations
			if(hltcc.getDocumentRelations() == null)
			{
				List<DocumentRelation> docRelations = new ArrayList<DocumentRelation>();
				for(int i=0; i<hltcc.getRelations().size(); i++)
				{
					Relation r = hltcc.getRelations().get(i);
					EntityMention mention = hltcc.getCoreferences().get(0).
							getResolvedMentions().get(i);
					Entity entity = null;
					long entityId = mention.getBestEntityId(); 
					System.out.println("REGTEST LOG - Best entity ID: " + entityId + ". Mention  ID: " + mention.getId().toString());
					for(Entity e : hltcc.getCoreferences().get(0).getEntities())
					{
						if(entityId == e.getEntityId())
						{
							entity = e;
							break;
						}
					}
					
					RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type(r.getRelationType()));
					relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(mention, new Type("location"), 0.5f));
					relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(mention, new Type("location"), 0.5f));
					relationMentionBuilder.addJustification(mention);
					
					List<RelationMention> provenances = new ArrayList<RelationMention>();
					provenances.add(relationMentionBuilder.build());
					
					DocumentRelation.Builder docRelationBuilder = DocumentRelation.builder(new Type(r.getRelationType()));
					docRelationBuilder.addProvenances(provenances);
					docRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(entity, new Type("location"), 0.5f));
					docRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(entity, new Type("location"), 0.5f));
					
					if(entity!=null)
					{
						docRelations.add(docRelationBuilder.build());
					}
					
				}
				
				hltcc.setDocumentRelations(docRelations);
			}
			
			System.out.println("REGTEST LOG: Size of document relations = " + hltcc.getDocumentRelations().size());
			
			for(DocumentRelation relation : hltcc.getDocumentRelations())
			{
				Map<Long,KBEntity> entityMap = new HashMap<Long, KBEntity>();
				for(DocumentRelation.Filler argument : relation.getArguments())
				{
					if(argument.asEntity().isPresent())
					{
						Entity entity = argument.asEntity().get();
						entityMap.put(entity.getEntityId(), new KBEntity(entityUris.get(0).getKBUri(),entity.getEntityType(),
										KBParameters.ADEPT_KB_IDENTIFIER));	
					}
				}
				
				KBID relationId = null;
				if(entityMap.size()>0)
				{
					relationId = updater.insertRelation(relation, entityMap, null);
				}
				if(relationId!=null)
					relationUris.add(relationId);
			}
			
			
			// query
			hltcc = executeQueries(entityUris, relationUris);
                        		
			serialize(hltcc, workingDirectory+ "/output.xml");
			System.out.println("Serialization done");
			
			// diff the 2 strings
			// Load reference. Compute diff. Get the Patch object. Patch is the container for computed deltas.
			diff(Reader.getAbsolutePathFromClasspathOrFileSystem(
					"adept/kbapi/regressiontest/output.xml"), 
					Reader.getAbsolutePathFromClasspathOrFileSystem(
							"adept/kbapi/regressiontest/reference.xml"));
			
			// delete
			for(KBID entityUri : entityUris)
			{
				updater.deleteKBObject(entityUri);
			}
			for(KBID relationUri : relationUris)
			{
				updater.deleteKBObject(relationUri);
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static HltContentContainer deserialize(String filepath)
	{
		try
		{
			XMLSerializer xmls = new XMLSerializer(SerializationType.XML);
			HltContentContainer hltcc = (HltContentContainer) xmls.deserializeString(Reader.getInstance().readFileIntoString(filepath), 
					HltContentContainer.class);
			return hltcc;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static void serialize(HltContentContainer hltcc, String filepath)
	{
		try
		{
			System.out.println("In serialize");
			XMLSerializer xmls = new XMLSerializer(SerializationType.XML);
			String serialized = xmls.serializeAsString(hltcc);
			Writer.getInstance().writeToFile(filepath, serialized);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	private static HltContentContainer executeQueries(List<KBID> entityUris, List<KBID> relationUris)
	{
		HltContentContainer hltcc = new HltContentContainer();
		List<EntityMention> mentions = new ArrayList<EntityMention>();
		List<DocumentRelation> relations = new ArrayList<DocumentRelation>();
		List<Coreference> corefs = new ArrayList<Coreference>();
		
		List<Entity> entities = new ArrayList<Entity>();
		Coreference coref = new Coreference(1);
		coref.setEntities(entities);
		coref.setResolvedMentions(mentions);
		corefs.add(coref);
		
		hltcc.setEntityMentions(mentions);
		hltcc.setDocumentRelations(relations);
		hltcc.setCoreferences(corefs);
		
		for(KBID entityUri : entityUris)
		{
			Entity queriedEntity = queryer.getEntityById(entityUri);
			List<EntityMention> queriedMentions = queryer.getEntityMentionsForKBEntity(entityUri);
			EntityMention canonicalMention = queriedEntity.getCanonicalMention();
			Iterator<EntityMention> queriedMentionsIter = queriedMentions.iterator();
			while (queriedMentionsIter.hasNext()) 
			{
				EntityMention m = queriedMentionsIter.next();
				if(m.getValue().equals(canonicalMention.getValue()) && m.getTokenOffset().getBegin()==canonicalMention.getTokenOffset().getBegin() &&
						m.getTokenOffset().getEnd() == canonicalMention.getTokenOffset().getEnd() && m.getMentionType().getType().equals(canonicalMention.getMentionType().getType())
						&& canonicalMention.getEntityType() == m.getEntityType())
				{
					queriedEntity.setCanonicalMentions(m);
				}

			    m.addEntityConfidencePair(queriedEntity.getEntityId() , m.getEntityIdDistribution().get(new Long(-1)));
			    m.getEntityIdDistribution().remove(new Long(-1));
			}
			
			entities.add(queriedEntity);
			mentions.addAll(queriedMentions);
		}
		for(KBID relationUri : relationUris)
		{
			relations.add(queryer.getRelationById(relationUri));

		}
		
                try {                    
                    Collections.sort(mentions, new Comparator<EntityMention>() {
                        public int compare(EntityMention o1, EntityMention o2) {
                            int i = o1.getValue().compareTo(o2.getValue());
                            if (i != 0) return i;
                            
                            i = o1.getTokenizerType().toString().compareTo(o2.getTokenizerType().toString());
                            if (i != 0) return i;
                            
                            i = o1.getMentionType().toString().compareTo(o2.getMentionType().toString());
                            if (i != 0) return i;
                            
                            i = o1.getTokenOffset().getBegin() - o2.getTokenOffset().getBegin();
                            if (i != 0) return i;
                            
                            i = o1.getTokenOffset().getEnd()- o2.getTokenOffset().getEnd();
                            if (i != 0) return i;
                            
                            i = o1.getCharOffset().getBegin() - o2.getCharOffset().getBegin();
                            if (i != 0) return i;
                            
                            i = o1.getCharOffset().getEnd() - o2.getCharOffset().getEnd();
                            if (i != 0) return i;
                            
                            i = o1.getDocId().compareTo(o2.getDocId());
                            if (i != 0) return i;
                            
                            if (o1.getConfidence(o1.getBestEntityId()) < o2.getConfidence(o2.getBestEntityId())) return -1;
                            if (o1.getConfidence(o1.getBestEntityId()) > o2.getConfidence(o2.getBestEntityId())) return 1;

                            return 0;
                        }
                    });
                } catch (Exception e) { 
                    e.printStackTrace(); 
                }
                
		return hltcc;
	}
	
	
	private static void diff(String outputfile, String referencefile)
	{
		System.out.println("In method diff()");
		
		List<String> xmlOutput = Reader.getInstance().fileToLines(outputfile);
		List<String> xmlOutputNormalized = normalizeGuidAndUri( xmlOutput);
		List<String> xmlReference  = Reader.getInstance().fileToLines(referencefile);
		List<String> xmlReferenceNormalized = normalizeGuidAndUri(xmlReference);
		Patch patch = DiffUtils.diff(xmlOutputNormalized , xmlReferenceNormalized );
		boolean bIdentical = ( patch.getDeltas().size() == 0 );
		if ( bIdentical )
		{
			System.out.println("Identical!");
		}
		else
		{
			for (Delta delta: patch.getDeltas()) {
				String d = delta.toString();
				{
					System.out.println(delta);
				}
			}
		}
	}
	
	/**
	 * Normalize list guids.
	 *
	 * @param inList the lines from the serialized XML file
	 * @return the list
	 */
	static List<String>  normalizeGuidAndUri(List<String> inList){
		// Must ignore GUID differences.
		Pattern guidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
		// Also ignore URI differences.
        Pattern uriPattern = Pattern.compile("<uri>.*</uri>");
        // Size of TokenStream varies depending on how Java treats ArrayList.
        Pattern intPattern = Pattern.compile("<int>[0-9]*</int>");
        Pattern longPattern = Pattern.compile("<long>-?[0-9]*</long>");
        Pattern floatPattern = Pattern.compile("<float>[0-9]*\\.[0-9]*</float>");
        Pattern entityIdPattern = Pattern.compile("<entityId>-?[0-9]*</entityId>");
        Pattern relationIdPattern = Pattern.compile("<relationId>-?[0-9]*</relationId>");
        Pattern sequenceIdPattern = Pattern.compile("<sequenceId>-?[0-9]*</sequenceId>");
        Pattern entityMentionReferencePattern = Pattern.compile("<adept.common.EntityMention reference=\"../../entities/adept.common.Entity"
        		+ "\\[[0-9]+\\]/canonicalMention\"/>");
        
		boolean uriFound = false;
		List<String> outList = new ArrayList<String>();
		for ( int i =0; i < inList.size(); ++i)
		{
			String temp = inList.get(i);
			Matcher guidMatcher = guidPattern.matcher(temp);
            Matcher intMatcher = intPattern.matcher(temp);
            Matcher longMatcher = longPattern.matcher(temp);
            Matcher floatMatcher = floatPattern.matcher(temp);
            Matcher entityIdMatcher = entityIdPattern.matcher(temp);
            Matcher relationIdMatcher = relationIdPattern.matcher(temp);
            Matcher sequenceIdMatcher = sequenceIdPattern.matcher(temp);
            Matcher entityMentionReferenceMatcher = entityMentionReferencePattern.matcher(temp);
            
            if ( intMatcher.find())
            {
                temp = intMatcher.replaceAll("##");
                outList.add(temp);
            }
            else if ( guidMatcher.find())
			{
				temp = guidMatcher.replaceAll("##########-#####-#####-#####-###############");
				outList.add(temp);
			}
            else if ( longMatcher.find())
			{
				temp = longMatcher.replaceAll("##");
				outList.add(temp);
			}
            else if ( floatMatcher.find())
			{
				temp = floatMatcher.replaceAll("##");
				outList.add(temp);
			}
            else if ( entityIdMatcher.find())
			{
				temp = entityIdMatcher.replaceAll("##");
				outList.add(temp);
			}
            else if ( relationIdMatcher.find())
			{
				temp = relationIdMatcher.replaceAll("##");
				outList.add(temp);
			}
            else if ( sequenceIdMatcher.find())
			{
				temp = sequenceIdMatcher.replaceAll("##");
				outList.add(temp);
			}
            else if ( entityMentionReferenceMatcher.find())
			{
            	//TODO: come up with a better fix
				// do nothing
			}
			else if (!uriFound )
			{
				Matcher uriMatcher = uriPattern.matcher(temp);
				if ( uriMatcher.find()) {
					inList.remove(i);
					uriFound = true;
				}
			}
		}
		
		return outList;
	}
	
	
}