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

package adept.kbapi;

import com.hp.hpl.jena.ontology.OntClass;

import adept.common.*;

import java.util.List;
import java.sql.SQLException;
import java.util.Map;



/**
 * DEFT Knowledge Base update interface
 */
public interface IUpdate {


	/**
	 * <p>API to insert Adept Entity into the KB. The method accepts an Adept
	 * entity object, a list of mentions that resolve to this entity and
	 * a map containing the external KB IDs that this entity resolves to, if any.</p>
	 *  
	 * <p>There is a check to see if the entity that is to be inserted already
	 * has an ID in the Adept-KB, in which case the operation fails and
	 * the user will be asked to reconsider the insert. To enable insert upon 
	 * consideration, user will need to remove the Adept KB ID from the list of 
	 * external KB IDs.</p>
	 *  
	 * <p>The properties of the entity that get inserted into the triple store
	 * are its type, canonical mention identifier (in the SQL metadata DB),
	 * and the canonical mention string value. The mention metadata, including 
	 * token offsets, mention type, source document and source algorithm are 
	 * stored as part of the metadata database.</p>
	 * 
	 * @param entity	the Adept entity object to be inserted.
	 * 
	 * @param mentions	a list of Adept Entitymention objects
	 * that have been resolved to the entity being inserted. Every mention is expected to have 
	 * its entityIdDistribution field populated with at least one entry -- i.e., the current 
	 * entity. If this is not true, the method fails with a runtime exception. 
	 * This list may or may not include the canonical mention, in either case we do check 
	 * separately for it in the Entity object.
	 * 
	 * @param externalKBIDs	a list of KBEntity instances that belong
	 * to non-Adept KBs, that the entity being inserted has been known to resolve to.
	 * No additional checks are performed here, so the responsibility to
	 * ensure correctness of the map is left to the TA-1 algorithm.
	 * 
	 * @return the KB ID corresponding to inserted entity
	 */
	public KBID insertEntity(Entity e, List<EntityMention> mentions, List<KBEntity> externalKbIds);
	
	
	/**
	 * <p>Insert ADEPT DocumentRelation into KB. The method accepts an Adept DocumentRelation
	 * object, which can contain references to multiple provenances (instances
	 * of the Adept RelationMention object). Each of these provenances has a pointer to a
	 * text chunk that serves as its justification, along with information about
	 * the argument chunks of the relation. An argument chunk must
	 * necessarily align with an entity mention. In the future versions of the KB API, 
	 * once support for date and number arguments is added, the argument chunks will also be 
	 * allowed to align with date and number mentions. The entity mention represented by
	 * any argument chunk must link to a KB entity that has already been inserted.</p>
	 * 
	 * <p>In addition to the DocumentRelation instance, the method also expects information
	 * about the mapping from document entities (that the relation argument mentions resolve 
	 * to) to Adept KB entities, and a map with associated confidences with information on any 
	 * external KB IDs that this document relation has already been resolved to.</p>
	 * 
	 * <p>There is a check to see if the relation that is to be inserted already
	 * has an ID in the Adept KB, in which case the operation fails and
	 * the user will be asked to reconsider the insert. To enable insert upon 
	 * consideration, user will need to remove the Adept KB ID from the list of 
	 * external KB IDs.</p>
	 * 
	 * The following preconditions are also checked. The method fails if even one of them
	 * is not satisfied.<br>
	 * (1) The document relation has at least one relation mention provenance
	 *     entry.<br>
	 * (2) All argument mentions in the provenances map to a valid argument entity
	 *     in the document with the highest confidence. That is, the getBestEntityId()
	 *     call on the entity mention returns the argument entity.<br>
	 * (3) The roles of the argument mentions agree with the role of the
	 *     argument entities that they resolve to.<br>
	 * (4) All argument entities and mentions already exist in the ADEPT KB. An entity mention
	 *     will be considered identical to an existing entity mention in the KBmetadata store,
	 *     if and only if all of the following fields match: mention type, mention token offsets,
	 *     string value, source document, and the KB entity that the mention resolves to.
	 *     The KB entity on the argument mention is obtained through 
	 *     mention.getBestEntity().getBestKBEntity() -- i.e. 1-best resolution.<br>
	 * (5) TODO: Check that the entity type of the argument's entity is as expected in 
	 *     ontology.<br>
	 * (6) TODO: Check that all slots of the relation are filled as is expected in
	 *     ontology.<br>
	 *     
	 * <p>The properties of the relation that are inserted into the triple store are the relation type, 
	 * the KB level arguments, and argument confidences. The argument confidence value
	 * is the one associated with the DocumentRelation.Filler objects.
	 * The relation justifications and argument justifications are stored in the metadata store.</p>
	 * 
	 * @param documentRelation	the document relation instance to be inserted
	 * @param kbIdsMap	map from the document entity IDs of the arguments to the KB entities they map to
	 * @param externalKbIds	any external KB relations that the document relation is known to resolve to
	 * 
	 * 
	 * @return the KB ID corresponding to the relation after insertion
	 * 
	 * TODO: add support for date and number arguments. 
	 */
   public <T extends KBID> KBID insertRelation(DocumentRelation r, Map<Long,T> kbEntitiesMap, List<KBRelation> externalKbIds);
   
   /**
    * Insert ADEPT Event into KB
    * 
    * @return the KB ID corresponding to the event after insertion
    */
   public KBID insertEvent(Event e);
   

     
   /**
    * <p>Modify existing KB entity. The updates propagate
    * to the metadata SQL DB, or in other words the update may also be to
    * a mention or other metadata associated with the Entity.</p>
    * 
    * <p>If the type associated with the updated entity is different from the original 
    * type(s), it gets appended into the triple store. The canonical mention ID and canonical
    * string value get overwritten by the update. Entity mentions and external
    * KB IDs get appended in the metadata DB. Confidences get overwritten. </p>
    * 
    * @param kbId	the Adept KB ID to be updated
    * @param updatedEntity	the updated Adept entity object
    * @param mentions	Adept entity mention objects that resolve to the updated entity.
    * These will be appended to the already existing mentions of the entity. Every mention is expected to have 
	* its entityIdDistribution field populated with at least one entry -- i.e., the current 
	* entity. If this is not true, the method fails with a runtime exception.
    * @param externalKBIds	a list of KBEntity instances that belong
	* to non-Adept KBs, that the updated entity being inserted has been known to resolve to.
    * 
    * @return boolean value indicating success or failure
    */
   public boolean updateEntity(KBID kbId, Entity updatedEntity, List<EntityMention> mentions, List<KBEntity> externalKbIds) ;
   
   
   /**
    * <p>Modify existing KB relation. The updates propagate
    * to the metadata SQL DB, or in other words the update may also be to
    * a mention or other metadata associated with the relation.</p>
    * 
    * <p>It does not allow modification of relation type or argument entities. 
    * The only things allowed are appending new relation mentions, argument mentions 
    * (for existing arguments) and external KB IDs, and overwriting the relation and 
    * argument confidences.</p>
    * 
    * <p>Similar preconditions are checked as in the case of relation insertion. </p>
    * 
    * @param kbId	the Adept KB ID to be updated
    * @param updatedRelation	the updated Adept document relation object
    * @param kbIdsMap	map from the document entity IDs of the arguments to the KB entities they map to
	* @param externalKbIds	any external KB relations that the document relation is known to resolve to. These
	* will be updated to the existing external KB IDs.
    * 
    * @return boolean value indicating success or failure
    */
   public <T extends KBID> boolean updateRelation(KBID kbId, DocumentRelation updatedRelation, Map<Long,T> kbEntitiesMap, 
   		List<KBRelation> externalKBIds);
   
   /**
    * Modify existing KB event. The updates propagate
    * to the metadata SQL DB, or in other words the update may also be to
    * a mention or other metadata associated with the event.
    * 
    * @return boolean value indicating success or failure
    */
   public boolean updateEvent(KBID kbId, Event updatedEvent);
   
   
   /**
    * Delete a first class KB object and all associated 
    * metadata.
    * 
    * @return boolean value indicating success or failure
    */
   public boolean deleteKBObject(KBID kbId);
   
   // TODO: Do we want to include methods to delete attributes
   // on KB objects, such as types, arguments, mentions etc.?
}