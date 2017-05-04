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
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.GenderTypeFactory;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.Type;
import adept.kbapi.KBEntity;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;
  
/**
 * 
 * @author dkolas
 */
public class TestGender extends KBUnitTest {
	
	
	@Test
	public void testFemaleGender() throws InvalidPropertiesFormatException, FileNotFoundException, KBUpdateException, IOException, KBQueryException{
		testGender("FEMALE", new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Female"));
	}
	
	@Test
	public void testMaleGender() throws InvalidPropertiesFormatException, FileNotFoundException, KBUpdateException, IOException, KBQueryException{
		testGender("MALE", new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Male"));
	}
	
	
	private void testGender(String commonGenderType, OntType kbGenderType) throws KBUpdateException, InvalidPropertiesFormatException, FileNotFoundException, IOException, KBQueryException{
		Pair<Entity, List<EntityMention>> entityAndMentions = createTestEntityWithMentions("per", .3f, "NAME", .2f, .7f, Collections.singletonList(new Pair<String, Double>("per", .3)));
		Entity entity = entityAndMentions.getL();
		Type gender = null;
		while (null == gender || gender.getType().equals("UNKNOWN")) {
			gender = GenderTypeFactory.getInstance().getType(commonGenderType);
			if (gender.getType().equals("UNKNOWN")) {
			    System.out.println("Unknown gender returned!");
			}
		}
		entity.addGender(GenderTypeFactory.getInstance().getType(commonGenderType), .43d);
		KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
				entityAndMentions.getL(), entityAndMentions.getR(),
				KBOntologyMap.getTACOntologyMap());
		KBEntity kbEntity = insertionBuilder.insert(kb);
		
		Assert.assertTrue("KB entity doesn't have gender type "+kbGenderType, kbEntity.getTypes().containsKey(kbGenderType));
		Assert.assertEquals("KB entity doesn't have the right gender confidence", .43d, kbEntity.getTypes().get(kbGenderType).doubleValue(), 0.001d);
		KBEntity queriedEntity = kb.getEntityById(kbEntity.getKBID());
		Assert.assertTrue("Queried KB entity doesn't have gender type "+kbGenderType, queriedEntity.getTypes().containsKey(kbGenderType));
		Assert.assertEquals("Queried KB entity doesn't have the right gender confidence", .43d, queriedEntity.getTypes().get(kbGenderType).doubleValue(), 0.001d);
	}
}
