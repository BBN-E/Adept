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

import adept.common.DocumentRelation;
import adept.common.Item;
import adept.common.OntType;
import adept.common.Pair;
import adept.kbapi.KBEntity;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBUpdateException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


public class TestRelatedEntityQueries extends KBUnitTest {

	@Test
	public void testRelatedEntityQueries() throws KBUpdateException, KBQueryException, InvalidPropertiesFormatException, FileNotFoundException, IOException {
            KBEntity entity1 = insertDefaultTestEntity();
            KBEntity entity2 = insertDefaultTestEntity();
            KBEntity entity3 = insertDefaultTestEntity();
            KBEntity entity4 = insertDefaultTestEntity();
            KBEntity entity5 = insertDefaultTestEntity();            

            insertDefaultTestRelation(entity1, entity2);
            insertDefaultTestRelation(entity2, entity3);
            insertDefaultTestRelation(entity3, entity4);
            insertDefaultTestRelation(entity4, entity5);
            
            // Query for entities again as types could have been updated after being added to relations
            entity1 = kb.getEntityById(entity1.getKBID());
            entity2 = kb.getEntityById(entity2.getKBID());
            entity3 = kb.getEntityById(entity3.getKBID());
            entity4 = kb.getEntityById(entity4.getKBID());
            entity5 = kb.getEntityById(entity5.getKBID());
            
            List<KBEntity> testDepth1 = kb.getRelatedEntities(entity1.getKBID(), 1);
            Assert.assertTrue("getRelatedEntities() returned incorrect number of related entities. Expected 1, got " + testDepth1.size(), testDepth1.size() == 1);
            Assert.assertEquals("Incorrect KBEntity returned by getRelatedEntities()", testDepth1.get(0), entity2);
            
            List<KBEntity> testDepth4 = kb.getRelatedEntities(entity1.getKBID(), 4);
            Assert.assertTrue("getRelatedEntities() returned incorrect number of related entities. Expected 4, got " + testDepth4.size(), testDepth4.size() == 4);
            
            boolean foundEntity2 = false;
            boolean foundEntity3 = false;
            boolean foundEntity4 = false;
            boolean foundEntity5 = false;
            for (KBEntity kbEntity : testDepth4) {
                if (kbEntity.equals(entity2)) foundEntity2 = true;
                else if (kbEntity.equals(entity3)) foundEntity3 = true;
                else if (kbEntity.equals(entity4)) foundEntity4 = true;
                else if (kbEntity.equals(entity5)) foundEntity5 = true;
            }
            Assert.assertTrue("Incorrect KBEntities returned by getRelatedEntities()", foundEntity2 && foundEntity3 && foundEntity4 && foundEntity5);
            
            List<KBEntity> testDepth2WithType = kb.getRelatedEntitiesByRelationType(entity2.getKBID(), 2, new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Resident"));
            Assert.assertTrue("getRelatedEntitiesByRelationType() returned incorrect number of related entities. Expected 3, got " + testDepth2WithType.size(), testDepth2WithType.size() == 3);
            boolean foundEntity1 = false;
            foundEntity3 = false;
            foundEntity4 = false;
            for (KBEntity kbEntity : testDepth2WithType) {
                if (kbEntity.equals(entity1)) foundEntity1 = true;
                else if (kbEntity.equals(entity3)) foundEntity3 = true;
                else if (kbEntity.equals(entity4)) foundEntity4 = true;
            }
            Assert.assertTrue("Incorrect KBEntities returned by getRelatedEntities()", foundEntity1 && foundEntity3 && foundEntity4);
            
            List<KBEntity> testWithIncorrectType = kb.getRelatedEntitiesByRelationType(entity2.getKBID(), 2, new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Founder"));
            Assert.assertTrue("getRelatedEntitiesByRelationType() returned incorrect number of related entities. Expected 0, got " + testWithIncorrectType.size(), testWithIncorrectType.isEmpty());
                        
            Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
					entity3, entity4, "org:founded_by", defaultRelationConfidence,
					defaultRelationMentionConfidence);
            
            KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
                            relationWithEntityMap.getL(), relationWithEntityMap.getR(),
                            KBOntologyMap.getTACOntologyMap());
	        insertionBuilder.insert(kb);
            
            entity3 = kb.getEntityById(entity3.getKBID());
            entity4 = kb.getEntityById(entity4.getKBID());
            
            List<KBEntity> testDepth4WithType = kb.getRelatedEntitiesByRelationType(entity3.getKBID(), 4, new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Founder"));      
            Assert.assertTrue("getRelatedEntitiesByRelationType() returned incorrect number of related entities. Expected 1, got " + testDepth4WithType.size(), testDepth4WithType.size() == 1);
            Assert.assertEquals("Incorrect KBEntity returned by getRelatedEntitiesByRelationType()", testDepth4WithType.get(0), entity4);
        }    
}