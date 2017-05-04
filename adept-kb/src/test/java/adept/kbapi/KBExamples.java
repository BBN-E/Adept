package adept.kbapi;

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

import com.google.common.base.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import adept.common.DocumentRelation;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.IType;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.kbapi.KB;
import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBEntity;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBParameters;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

public class KBExamples {
  public static void main(String[] args)
      throws IOException, KBConfigurationException, KBQueryException, KBUpdateException {
    KBParameters kbParameters = new KBParameters();
    KB kb = new KB(kbParameters);

    // Common inputs
    Entity entity = new Entity(1, new OntType(KBOntologyModel.ONTOLOGY_BASE_PREFIX, "Entity"));
    DocumentRelation relation = DocumentRelation.builder(new OntType(KBOntologyModel.ONTOLOGY_BASE_PREFIX, "Relation")).build();
    List <EntityMention> entityMentions = new ArrayList<EntityMention>();
    List<KBID> externalIDs = new ArrayList<KBID>();

    // Retrieve entity by name
    List<KBEntity> matchingEntities = kb.getEntitiesByStringReference(entity.getCanonicalMention().getValue());

    // Align to external entity
    KBEntity.InsertionBuilder entityInsertionBuilder = KBEntity.entityInsertionBuilder(entity, entityMentions, KBOntologyMap.loadOntologyMap("adept/kb/example-to-adept.iml", "adept/kb/adept-to-example.iml"));
    entityInsertionBuilder.addExternalKBIds(new HashSet<KBID>(externalIDs));
    KBEntity insertedEntity = entityInsertionBuilder.insert(kb);

    // Update existing entities
    KBEntity.UpdateBuilder updateBuilder = insertedEntity.updateBuilder();
    updateBuilder.setConfidence((float) entity.getEntityConfidence());
    KBTextProvenance.InsertionBuilder canonicalMentionBuilder =
        KBTextProvenance.builder(entity.getCanonicalMention(),
            (float) entity.getCanonicalMentionConfidence());
    updateBuilder.addProvenance(canonicalMentionBuilder);
    updateBuilder
        .setNewCanonicalMention(canonicalMentionBuilder,
            (float) entity.getCanonicalMentionConfidence());
    for (IType entityType : entity.getAllTypes().keySet()) {
      OntType ontType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, entityType.getType());
      if (updateBuilder.getUpdatedTypes().containsKey(ontType)) {
        updateBuilder.alterTypeConfidence(ontType, entity.getAllTypes().get(entityType).floatValue());
      } else {
        updateBuilder.addNewType(ontType, entity.getAllTypes().get(entityType).floatValue());
      }
    }
    KBEntity updatedEntity = updateBuilder.update(kb);

    // Insert relation
    Map<Item, KBPredicateArgument> argumentToKBArgumentMap = new HashMap<Item, KBPredicateArgument>();
    KBRelation.InsertionBuilder relationInsertionBuilder = KBRelation.relationInsertionBuilder(relation, argumentToKBArgumentMap, KBOntologyMap.loadOntologyMap("adept/kb/example-to-adept.iml", "adept/kb/adept-to-example.iml"));
    relationInsertionBuilder.addExternalKBIds(new HashSet<KBID>(externalIDs));
    KBRelation insertedRelation = relationInsertionBuilder.insert(kb);

    // Get relations by entity
    List<KBRelation> matchingRelations = new ArrayList<KBRelation>();
    for (KBEntity matchingEntity : matchingEntities) {
      matchingRelations.addAll(kb.getRelationsByArg(matchingEntity.getKBID()));
    }

    // Get KB entity for external entity
    Optional<KBPredicateArgument> kbObject = kb.getKBObjectByExternalID(externalIDs.get(0));
    if (kbObject.isPresent()) {
      KBID adeptId = kbObject.get().getKBID();
    }
  }
}
