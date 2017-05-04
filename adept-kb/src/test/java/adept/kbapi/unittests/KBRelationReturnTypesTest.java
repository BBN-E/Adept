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



import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import adept.common.KBID;
import adept.common.OntType;
import adept.kbapi.KBBelief;
import adept.kbapi.KBDate;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEvent;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBSentiment;
import adept.kbapi.KBTemporalSpan;
import adept.kbapi.KBUpdateException;
  
/**
 * 
 * @author dkolas
 */
public class KBRelationReturnTypesTest extends KBUnitTest {
	
	private OntType personType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person");
	private OntType personRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "person");
	private OntType spouse = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "SpousalRelationship");
	private OntType acquit = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Acquit");
	private OntType defendant = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "defendant");
	private KBID externalID = new KBID("id", "external");
	
	@Test
	public void testRelationFromRelationArg() throws KBUpdateException, KBQueryException{
		KBEntity person = getEntity();
		KBRelation relation = KBRelation.relationInsertionBuilder(spouse, .1f)
				.addArgument(KBRelationArgument.insertionBuilder(personRole, person, .1f).addProvenance(generateProvenance("")))
				.addProvenance(generateProvenance(""))
				.addExternalKBId(externalID).insert(kb);
		
		KBRelation queriedRelation = kb.getKBRelationByKBRelationArgument(relation.getArguments().iterator().next().getKBID());
		Assert.assertTrue("Queried relation should be instance of KBRelation", queriedRelation instanceof KBRelation);
		
		queriedRelation = (KBRelation) kb.getKBObjectByExternalID(externalID).get();
		Assert.assertTrue("Queried relation should be instance of KBRelation", queriedRelation instanceof KBRelation);
	}
	
	

	@Test
	public void testEventFromRelationArg() throws KBUpdateException, KBQueryException{
		KBEntity person = getEntity();
		KBEvent event = KBEvent.eventInsertionBuilder(acquit, .1f)
				.addArgument(KBRelationArgument.insertionBuilder(defendant, person, .1f).addProvenance(generateProvenance("")))
				.addProvenance(generateProvenance(""))
				.addExternalKBId(externalID).insert(kb);
		
		KBRelation queriedRelation = kb.getKBRelationByKBRelationArgument(event.getArguments().iterator().next().getKBID());
		Assert.assertTrue("Queried relation should be instance of KBEvent", queriedRelation instanceof KBEvent);
		
		queriedRelation = (KBRelation) kb.getKBObjectByExternalID(externalID).get();
		Assert.assertTrue("Queried relation should be instance of KBEvent", queriedRelation instanceof KBEvent);
	}
	
	@Test
	public void testBeliefFromRelationArg() throws KBUpdateException, KBQueryException{
		KBEntity person = getEntity();
		KBNumber number = KBNumber.numberInsertionBuilder(3).insert(kb);
		KBBelief belief = KBBelief.beliefInsertionBuilder(.1f)
				.addSourceArgument(person, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addTargetArgument(person, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addStrengthArgument(number, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addProvenance(generateProvenance(""))
				.addExternalKBId(externalID).insert(kb);
		
		KBRelation queriedRelation = kb.getKBRelationByKBRelationArgument(belief.getArguments().iterator().next().getKBID());
		Assert.assertTrue("Queried relation should be instance of KBBelief", queriedRelation instanceof KBBelief);
		
		queriedRelation = (KBRelation) kb.getKBObjectByExternalID(externalID).get();
		Assert.assertTrue("Queried relation should be instance of KBBelief", queriedRelation instanceof KBBelief);
	}
	
	@Test
	public void testSentimentFromRelationArg() throws KBUpdateException, KBQueryException{
		KBEntity person = getEntity();
		KBNumber number = KBNumber.numberInsertionBuilder(3).insert(kb);
		KBSentiment sentiment = KBSentiment.sentimentInsertionBuilder(.1f)
				.addSourceArgument(person, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addTargetArgument(person, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addStrengthArgument(number, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addProvenance(generateProvenance(""))
				.addExternalKBId(externalID).insert(kb);
		
		KBRelation queriedRelation = kb.getKBRelationByKBRelationArgument(sentiment.getArguments().iterator().next().getKBID());
		Assert.assertTrue("Queried relation should be instance of KBSentiment", queriedRelation instanceof KBSentiment);
		
		queriedRelation = (KBRelation) kb.getKBObjectByExternalID(externalID).get();
		Assert.assertTrue("Queried relation should be instance of KBSentiment", queriedRelation instanceof KBSentiment);
	}
	
	@Test
	public void testTemporalSpanFromRelationArg() throws KBUpdateException, KBQueryException{
		KBDate date = KBDate.timexInsertionBuilder("2016-02-03").insert(kb);
		KBTemporalSpan temporalSpan = KBTemporalSpan.temporalSpanInsertionBuilder(.1f)
				.addBeginDateArgument(date, .1f, Collections.<KBProvenance.InsertionBuilder>singleton(generateProvenance("")))
				.addProvenance(generateProvenance(""))
				.addExternalKBId(externalID).insert(kb);
		
		KBRelation queriedRelation = kb.getKBRelationByKBRelationArgument(temporalSpan.getArguments().iterator().next().getKBID());
		Assert.assertTrue("Queried relation should be instance of TemporalSpan", queriedRelation instanceof KBTemporalSpan);
		
		queriedRelation = (KBRelation) kb.getKBObjectByExternalID(externalID).get();
		Assert.assertTrue("Queried relation should be instance of TemporalSpan", queriedRelation instanceof KBTemporalSpan);
	}
	
	/**
	 * @return
	 * @throws KBUpdateException 
	 */
	private KBEntity getEntity() throws KBUpdateException {
		return KBEntity.entityInsertionBuilder(Collections.singletonMap(personType, .1f), generateProvenance(""), .1f, .1f).insert(kb);
	}
	
}
