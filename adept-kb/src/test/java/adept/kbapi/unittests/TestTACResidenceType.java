/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;

import org.junit.Assert;
import org.junit.Test;

import adept.common.DocumentRelation;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEvent;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBUpdateException;

/**
 * 
 * @author dkolas
 */
public class TestTACResidenceType extends KBUnitTest {

	@Test
	public void testCityAssignment() throws KBUpdateException, KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// insert entities into KB
		KBEntity kbEntity1 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);
		KBEntity kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		OntType cityType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "City");
		OntType locationRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "location");

		Assert.assertTrue("Entity should not have city type", !kbEntity2.getTypes().keySet()
				.contains(cityType));

		// create the document relation
		Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
				kbEntity1, kbEntity2, "per:cities_of_residence", .34f, .72f);

		try {
			KBEvent.eventInsertionBuilder(relationWithEntityMap.getL(),
					relationWithEntityMap.getR(), KBOntologyMap.getTACOntologyMap());
			Assert.fail("Getting an event builder for a per:cities_of_residence relation should throw exception.");
		} catch (IllegalArgumentException e) {
			// This is expected!
		}

		KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getTACOntologyMap());
		insertionBuilder.addExternalKBId(new KBID("External_Resident_Relation", "ExampleKB"));
		KBRelation kbRelation = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);

		Assert.assertTrue("Entity should have city type", kb.getEntityById(kbEntity2.getKBID())
				.getTypes().containsKey(cityType));

		KBRelationArgument locationArgument = kbRelation.getArgumentsByRole(locationRole)
				.iterator().next();
		Assert.assertTrue("Entity should have city type", ((KBEntity) locationArgument.getTarget())
				.getTypes().containsKey(cityType));

		KBRelation queriedRelation = kb.getRelationById(kbRelation.getKBID());
		locationArgument = queriedRelation.getArgumentsByRole(locationRole).iterator().next();
		Assert.assertTrue("Entity should have city type", ((KBEntity) locationArgument.getTarget())
				.getTypes().containsKey(cityType));

		Assert.assertFalse("Other entity should NOT have city type ",
				kb.getEntityById(kbEntity1.getKBID()).getTypes().containsKey(cityType));
	}

	@Test
	public void testRelationToEvent() throws KBUpdateException, KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// insert entities into KB
		KBEntity kbEntity1 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);
		KBEntity kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		OntType cityType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "City");
		OntType placeRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "place");

		Assert.assertTrue("Entity should not have city type", !kbEntity2.getTypes().keySet()
				.contains(cityType));

		// create the document relation
		Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
				kbEntity1, kbEntity2, "per:city_of_birth", .34f, .72f);

		try {
			KBRelation.relationInsertionBuilder(relationWithEntityMap.getL(),
					relationWithEntityMap.getR(), KBOntologyMap.getTACOntologyMap());
			Assert.fail("Getting a relation builder for a per:city_of_birth relation should throw exception.");
		} catch (IllegalArgumentException e) {
			// This is expected!
		}

		KBEvent.InsertionBuilder insertionBuilder = KBEvent.eventInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getTACOntologyMap());
		KBRelation kbRelation = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);

		Assert.assertTrue("Entity should have city type", kb.getEntityById(kbEntity2.getKBID())
				.getTypes().containsKey(cityType));

		KBRelationArgument locationArgument = kbRelation.getArgumentsByRole(placeRole).iterator()
				.next();
		Assert.assertTrue("Entity should have city type", ((KBEntity) locationArgument.getTarget())
				.getTypes().containsKey(cityType));

		KBRelation queriedRelation = kb.getRelationById(kbRelation.getKBID());
		locationArgument = queriedRelation.getArgumentsByRole(placeRole).iterator().next();
		Assert.assertTrue("Entity should have city type", ((KBEntity) locationArgument.getTarget())
				.getTypes().containsKey(cityType));

		Assert.assertFalse("Other entity should NOT have city type ",
				kb.getEntityById(kbEntity1.getKBID()).getTypes().containsKey(cityType));
	}
}