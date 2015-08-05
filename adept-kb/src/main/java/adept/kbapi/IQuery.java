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

import com.hp.hpl.jena.query.ResultSet;

import adept.common.*;

import java.util.List;


/**
 * DEFT Knowledge Base query interface
 */
public interface IQuery {

     /**
      * Query KB entity by ID
      * 
      * @return Adept Entity object
      */
	  public Entity getEntityById(KBID kbId);
	  
	  /**
	  * Query KB relation by ID
	  * 
	  * @return Adept Relation object
	  */
	 public DocumentRelation getRelationById(KBID kbId);
	 
	 /**
      * Query KB event by ID
      * 
      * @return Adept Event object
      */
	  public Event getEventById(KBID kbId);
	  
	  /**
	   * Query entity objects by mention value
	   * We use case-insensitive exact string match.
	   * 
	   * @return List of entity IDs that have atleast one
	   * mention that exactly matches the argument string
	   */
	  public List<KBID> getEntitiesByStringReference(String value);
	  
	  /**
	   * Query entity objects by mention value
	   * We use the SQL regex syntax.
	   * 
	   * @return List of entity IDs that have atleast one
	   * mention that matches the argument regex
	   */
	  public List<KBID> getEntitiesByRegexMatch(String regex);
	  
	  /**
	   * get KB entity IDs by type
	   * 
	   * @return List<KBID>
	   */
	  public List<KBID> getEntitiesByType(IType type);
	  
	  /**
	   * get KB entity IDs by value and type
	   * 
	   * @return List<KBID>
	   */
	  public List<KBID> getEntitiesByValueAndType(String value, IType type);
	  
	  /**
	   * get KB relation IDs by argument
	   * 
	   * @return List of relation IDs containing input entity
	   * as argument
	   */
	  public List<KBID> getRelationsByArg(KBID kbId);
          
      /**
	   * get KB relation IDs by mention value
	   * 
	   * @return List<KBID>
	   */
       public List<KBID> getRelationsByStringReference(String value);
	  
	  /**
	   * get KB relation IDs by Type
	   * 
	   * @return List<KBID> corresponding to input type
	   */
	  public List<KBID> getRelationsByType(IType type);
	  
	  /**
	   * get KB relations by argument and type
	   * 
	   * @return List<KBID>
	   */
	  public List<KBID> getRelationsByArgAndType(KBID kbId, IType type);
	  
	  /**
	   * get KB event IDs by argument
	   * 
	   * @return List<KBID> containing input ID as argument 
	   */
	  public List<KBID> getEventsByArg(KBID kbId);
	  
	  /**
	   * get KB event IDs by Type
	   * 
	   * @return List<KBID> corresponding to input type
	   */
	  public List<KBID> getEventsByType(IType type);
	  
	  /**
	   * get KB event IDs by argument and type
	   * 
	   * @return List<KBID>
	   */
	  public List<KBID> getEventsByArgAndType(KBID kbId, IType type);
	  
	  /**
	   * get related entities upto given depth
	   * 
	   * @return  HltContentContainer containing relevant entities
	   * and relations
	   */
	  public HltContentContainer getRelatedEntities(KBID kbId, int depth);
	  
	  /**
	   * get related entities by relation type upto given depth
	   * 
	   * @return  HltContentContainer containing relevant entities
	   * and relations
	   */
	  public HltContentContainer getRelatedEntitiesByRelationType(KBID kbId, int depth, IType type);
	  
	  
	  /**
	   * Get KB objects contained within input chunk
	   * 
	   * @return HltContentContainer containing relevant entities,
	   * relations and events
	   */
	  public HltContentContainer getKBObjectsWithinChunk(Chunk chunk);
	  
	  /**
	   * Custom Sparql queries
	   * 
	   * @return ResultSet
	   */
	  public ResultSet executeSelectQuery(String query);
	  
	  /**
	   * get all entity mentions for a given Adept KB entity ID
	   * from the metadata store
	   * @return List<EntityMention>
	   */
	  public List<EntityMention> getEntityMentionsForKBEntity(KBID kbId);
	  
	  /**
	   * get all relation mentions for a given Adept KB relation
	   * ID from the metadata store
	   *  @return List<RelationMention>
	   */
	  public List<RelationMention> getRelationMentionsForKBRelation(KBID kbId);
	  
	  /**
	   * get all external KB IDs that a given
	   * Adept KB ID maps to.
	   * @return List<KBID>
	   */
	  public List<KBID> getExternalKBIDs(KBID adeptKbId);
	  
	  /**
	   * get the Adept KB ID corresponding to the
	   * extenral KBID object.
	   */
	  public KBID getAdeptEntityByExternalID(KBID externalKbId);
	  
}