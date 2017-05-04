package adept.kbapi.unittests;

/*-
 * #%L
 * adept-kb
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


import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.GenericThing;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.NumberPhrase;
import adept.common.NumericValue;
import adept.common.OntType;
import adept.common.RelationMention;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.io.Reader;
import adept.kbapi.KBEntity;
import adept.kbapi.KBGenericThing;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

import com.google.common.base.Optional;

public class TestAllRelationTypes extends KBUnitTest {
    private static final HashMap<String, String> tacEntityReverseMap = new HashMap<String, String>();
    static {
        tacEntityReverseMap.put("Person","per");
        tacEntityReverseMap.put("GeoPoliticalEntity","gpe");
        tacEntityReverseMap.put("Organization","org");
        tacEntityReverseMap.put("Location","loc");
        tacEntityReverseMap.put("Facility","fac");
    }
    
    List<String> validEntityTypes = Arrays.asList("Entity","Person","GeoPoliticalEntity","Organization","Location","Facility");

    private static final HashMap<String, List<String>> tacRelationTypesWithArguments = new HashMap<String, List<String>>();
    static {
        tacRelationTypesWithArguments.put("per:children",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:otherfamily",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:parents",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:siblings",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:spouse",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:employee_or_member_of",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:schools_attended",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:cities_of_residence",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:statesorprovinces_of_residence",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:countries_of_residence",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:shareholders",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:founded_by",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:top_members_employees",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:member_of",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("gpe:member_of",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:members",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:parents",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:subsidiaries",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:city_of_headquarters",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:stateprovince_of_headquarters",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:country_of_headquarters",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:age",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:origin",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:title",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:religion",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:political_religious_affiliation",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:number_of_employees_members",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:website",Arrays.asList("arg-1","arg-2"));
    }
    
    private static final HashMap<String, List<String>> rereRelationTypesWithArguments = new HashMap<String, List<String>>();
    static {
        rereRelationTypesWithArguments.put("locatednear",Arrays.asList("entity","loc"));
        rereRelationTypesWithArguments.put("resident",Arrays.asList("per","loc"));
        rereRelationTypesWithArguments.put("orgheadquarter",Arrays.asList("org","loc"));
        rereRelationTypesWithArguments.put("orglocationorigin",Arrays.asList("org","loc"));
        rereRelationTypesWithArguments.put("subsidiary",Arrays.asList("suborg","parent"));
        rereRelationTypesWithArguments.put("membership",Arrays.asList("member","org"));
        rereRelationTypesWithArguments.put("family",Arrays.asList("per"));
        rereRelationTypesWithArguments.put("role",Arrays.asList("per","role"));
        rereRelationTypesWithArguments.put("employmentmembership",Arrays.asList("employeemember","org"));
        rereRelationTypesWithArguments.put("leadership",Arrays.asList("leader","entity"));
        rereRelationTypesWithArguments.put("investorshareholder",Arrays.asList("investorshareholder","org"));
        rereRelationTypesWithArguments.put("studentalum",Arrays.asList("studentalumni","org"));
        rereRelationTypesWithArguments.put("NAMownership",Arrays.asList("owner","org"));
        rereRelationTypesWithArguments.put("founder",Arrays.asList("founder","org"));
        rereRelationTypesWithArguments.put("more",Arrays.asList("per","entity"));
        rereRelationTypesWithArguments.put("personage",Arrays.asList("per","age"));
        rereRelationTypesWithArguments.put("orgwebsite",Arrays.asList("org","url"));
        rereRelationTypesWithArguments.put("opra",Arrays.asList("org","entity"));
    }
                
                
    @Test
    public void testAllTacRelationTypes() throws Exception {         
        
        for (String relationType : tacRelationTypesWithArguments.keySet()) {
            // Test founder relation with founder argument of types other than Person
            if (relationType.equals("org:founded_by")) {
                KBRelation relationWithOrgArg = createTestRelationWithArguments(relationType, "org", KBOntologyMap.getTACOntologyMap(), "tac");
                confirmRelationWithTypeExists(relationType, KBOntologyMap.getTACOntologyMap());
                kb.deleteKBObject(relationWithOrgArg.getKBID());
                
                KBRelation relationWithGpeArg = createTestRelationWithArguments(relationType, "gpe", KBOntologyMap.getTACOntologyMap(), "tac");
                confirmRelationWithTypeExists(relationType, KBOntologyMap.getTACOntologyMap());
                kb.deleteKBObject(relationWithGpeArg.getKBID());
                }
            KBRelation relation = createTestRelationWithArguments(relationType, "per", KBOntologyMap.getTACOntologyMap(), "tac");
            confirmRelationWithTypeExists(relationType, KBOntologyMap.getTACOntologyMap());
            kb.deleteKBObject(relation.getKBID());
        }
    } 
    
    @Test
    public void testAllRereRelationTypes() throws Exception {  
        for (String relationType : rereRelationTypesWithArguments.keySet()) {
            KBRelation relation = createTestRelationWithArguments(relationType, "per", KBOntologyMap.getRichEREOntologyMap(), "rere");
            confirmRelationWithTypeExists(relationType, KBOntologyMap.getRichEREOntologyMap());
            kb.deleteKBObject(relation.getKBID());
        }
    }  
    
    private void confirmRelationWithTypeExists(String relationType, KBOntologyMap ontologyMap) throws KBQueryException, InvalidPropertiesFormatException, FileNotFoundException, IOException {
        OntType adeptType = ontologyMap.getKBTypeForType(new Type(relationType)).get();
        List<KBRelation> relationIds = kb.getRelationsByType(adeptType);

        assertTrue("No relations returned.", relationIds.size() > 0);
        assertTrue("Expected one relation returned, got " + relationIds.size(), relationIds.size() == 1);

        for (KBRelation relation : relationIds) {
            if (!relation.getType().getType().equals(adeptType.getType())) {
                Assert.fail("Invalid relation type returned from getRelationsByType().");
            }
        }
    }

    private KBRelation createTestRelationWithArguments(String relationType, String defaultEntityType, KBOntologyMap ontologyMap, String ontMapType) {
        try {
            float relationConfidence = 0.9f;
            float relationMentionConfidence = 0.8f;

            HashMap<Item, KBPredicateArgument> argumentMap = new HashMap<Item, KBPredicateArgument>();

            Type actualRelationType = new Type(relationType);
            OntType adeptType = ontologyMap.getKBTypeForType(actualRelationType).get();
            
            // create relation mention
            RelationMention.Builder relationMentionBuilder = RelationMention.builder(actualRelationType);
            relationMentionBuilder.setConfidence(relationMentionConfidence);
            relationMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));

            // create document relation
            DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(actualRelationType);		
            documentRelationBuilder.setConfidence(relationConfidence);
            
            int entityId = 1;
            HashMap<String, List<String>> relationTypesWithArguments = ontMapType.equals("tac") ? tacRelationTypesWithArguments : rereRelationTypesWithArguments;
            for (String mappingArgumentType : relationTypesWithArguments.get(relationType)) {
                OntType kbArgument = ontologyMap.getKBTypeForType(new Type(relationType+"."+mappingArgumentType)).get();
                String kbArgType = KBOntologyModel.instance().getRelationArgumentTypes().get(adeptType.getType()).get(kbArgument.getType());
                if (kbArgType.equals("Number")) {
                        HltContentContainer hltContentContainer = new HltContentContainer();
                        Document document = DocumentMaker.getInstance().createDocument("sample_numbers.txt",
                                        null, "Text", "sample_numbers.txt", "English",
                                        Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_numbers.txt"),
                                        hltContentContainer);

                        CharOffset bbnCharOffset = new CharOffset(0, 17);
                        String bbnText = "BBN Technologies";
                        Token bbnToken = new Token(0, bbnCharOffset, bbnText);

                        CharOffset numberCharOffset = new CharOffset(35, 38);
                        String numberText = "700";
                        Token numberToken = new Token(1, numberCharOffset, numberText);
                        TokenOffset numberTokenOffset = new TokenOffset(1, 1);

                        TokenStream testTokenStream = new TokenStream(TokenizerType.WHITESPACE,
                                        TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
                        testTokenStream.add(bbnToken);
                        testTokenStream.add(numberToken);

                        NumericValue numericValue = new NumericValue(new Integer(700));

                        NumberPhrase numberPhrase = new NumberPhrase(numberTokenOffset, testTokenStream);

                        KBNumber.InsertionBuilder numberInsertionBuilder1 = KBNumber.numberInsertionBuilder(
                                        numericValue, Collections.singletonList(numberPhrase));
                        KBNumber number1 = numberInsertionBuilder1.insert(kb);

                        argumentMap.put(numericValue, number1); 

                    RelationMention.Filler argFiller = RelationMention.Filler.fromNumberPhrase(numberPhrase, new Type(mappingArgumentType), 0.55f);
                        relationMentionBuilder.addArgument(argFiller);

                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(mappingArgumentType), 
                                DocumentRelationArgument.Filler.fromNumericValue(numericValue), 0.55f);
                        argBuilder.addProvenance(argFiller);
                        documentRelationBuilder.addArgument(argBuilder.build());
                } else if (validEntityTypes.contains(kbArgType)) {
                        String type = null;
                    if (kbArgType.equals("Entity")) {
                            type = defaultEntityType;
                        } else {
                        type = tacEntityReverseMap.get(kbArgType);
                        }
                    Entity entityArgument = new Entity(entityId++, new Type(type));

                        List<EntityMention> entityMentions = new ArrayList<EntityMention>();
                        EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream);
                        mention.addEntityConfidencePair(entityArgument.getEntityId(), 0.9f);
                        mention.setMentionType(new Type("NAME"));
                        mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
                        entityMentions.add(mention);

                        entityArgument.setCanonicalMentions(mention);
                        entityArgument.setEntityConfidence(0.8f);
                        entityArgument.setCanonicalMentionConfidence(0.63);

                        KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
                                        entityArgument, entityMentions,
                                    ontologyMap);
                        KBEntity kbEntity = insertionBuilder.insert(kb);

                        argumentMap.put(entityArgument, kbEntity);

                    RelationMention.Filler argFiller = RelationMention.Filler.fromEntityMention(mention, new Type(mappingArgumentType), 0.51f);
                        relationMentionBuilder.addArgument(argFiller);

                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(mappingArgumentType), 
                                DocumentRelationArgument.Filler.fromEntity(entityArgument), 0.51f);
                        argBuilder.addProvenance(argFiller);
                        documentRelationBuilder.addArgument(argBuilder.build());
                } else { 
                    // Generic thing
                    Type type = null;
                    if (kbArgType.equals("Thing")) {
                        type = new Type("crime");
                    } else {
                        type = new Type(kbArgType);
                    }
                    GenericThing genericThing = new GenericThing(type, kbArgument.getType());
                        Chunk chunk = new Chunk(testTokenOffset, testTokenStream);
                        KBGenericThing.InsertionBuilder thingBuilder = KBGenericThing
				.genericThingInsertionBuilder(genericThing, Collections.singletonList(chunk),
                                            ontologyMap);
                        KBGenericThing kbGenericThing = thingBuilder.insert(kb);
                        
                        argumentMap.put(genericThing, kbGenericThing);

                    RelationMention.Filler argFiller = RelationMention.Filler.fromGenericChunk(chunk, new Type(mappingArgumentType), 0.55f);
                        relationMentionBuilder.addArgument(argFiller);

                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(mappingArgumentType), 
                                DocumentRelationArgument.Filler.fromGenericThing(genericThing), 0.55f);
                        argBuilder.addProvenance(argFiller);
                        documentRelationBuilder.addArgument(argBuilder.build());
                    }
                }

            List<RelationMention> provenances = new ArrayList<RelationMention>();
            provenances.add(relationMentionBuilder.build());

            documentRelationBuilder.addProvenances(provenances);

            DocumentRelation documentRelation = documentRelationBuilder.build();

            KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
                                    documentRelation, argumentMap,
                                    ontologyMap);
            return insertionBuilder.insert(kb);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
            return null;
        }
    }
    
    @Test
    public void testEndorsement() throws KBUpdateException, KBQueryException{
    	OntType personType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person");
    	KBEntity entity = KBEntity.entityInsertionBuilder(Collections.singletonMap(personType, .5f), generateProvenance("entity"), .5f, .5f).insert(kb);
    	OntType threadType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Thread");
    	KBGenericThing genericThing = KBGenericThing.genericThingInsertionBuilder(threadType, "threadURL").insert(kb);
    	OntType endorsementType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Endorsement");
    	KBRelation.InsertionBuilder relationBuilder = KBRelation.relationInsertionBuilder(endorsementType, .5f);
    	OntType entityRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "entity");
    	relationBuilder.addArgument(KBRelationArgument.insertionBuilder(entityRole, entity, .5f));
    	OntType threadRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "thread");
    	relationBuilder.addArgument(KBRelationArgument.insertionBuilder(threadRole, genericThing, .5f));
    	
    	relationBuilder.addProvenance(generateProvenance("endorsement provenance"));
    	KBRelation insertedRelation = relationBuilder.insert(kb);
    	
    	KBRelation queriedRelation = kb.getRelationById(insertedRelation.getKBID());
    	
    	Assert.assertEquals("Inserted and queried objects should be .equals", insertedRelation, queriedRelation ); 
    	
    	
    }
}
