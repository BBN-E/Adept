package adept.kbapi;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.rdf.model.RDFNode;

import adept.common.*;
import adept.metadata.*;
import adept.io.*;
import adept.kbapi.sparql.*;
import adept.kbapi.sql.*;
import adept.serialization.*;

import java.util.*;
import java.io.DataInputStream;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.collect.ImmutableMap;


/**
 * Implementations of the various KB update API methods.
 *
 */
public class KBUpdateProcessor implements IUpdate {
	
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
	@Override
	public KBID insertEntity(Entity entity, List<EntityMention> mentions, List<KBEntity> externalKBIds)
	{
		//Add a check to see if external map already contains an Adept ID.
		// If yes, throw exception and say are you sure you dont
		// want to update existing entity?
		String existingAdeptId = null;
		if(externalKBIds != null)
		{
			for(KBEntity kbEntity : externalKBIds)
			{
				if(kbEntity.getSourceKB().equals(KBParameters.ADEPT_KB_IDENTIFIER))
				{
					existingAdeptId = kbEntity.getKBUri();
					break;
				}
			}
		}
		
		if(existingAdeptId != null)
		{
			throw new RuntimeException("This entity already seems to have an entry in the Adept KB with ID = "
					+ existingAdeptId + ". Please use the updateEntity() method to update this existing entity.");
		}
		else
		{
			return insertEntity(entity, mentions, externalKBIds, false);
		}
		
	}
	
	/**
	 * overloaded method to distinguish between inserting a fresh entity
	 * and reinserting an entity (which can happen when there is a failure 
	 * in the update method). This is a private method. In other words,
	 * an external KB user is NOT allowed to re-insert an existing 
	 * entity in the Adept KB.
	 */
	private KBID insertEntity(Entity entity, List<EntityMention> mentions, List<KBEntity> externalKBIds, 
			boolean usePreExistingAdeptKbId)
	{	
        Connection sqlConnection = null;
		String entityId;
		
		if(usePreExistingAdeptKbId)
		{
			entityId = null;
			for(KBEntity kbEntity : externalKBIds)
			{
				if(kbEntity.getSourceKB().equals(KBParameters.ADEPT_KB_IDENTIFIER))
				{
					entityId = kbEntity.getKBUri();
					break;
				}
			}
			if(entityId==null)
			{
				throw new RuntimeException("ERROR: No pre existing Adept KB ID found!");
			}
		}	
		else
		{
			entityId = entity.getId().toString();
		}
			
		double entityConfidence = entity.getEntityConfidence();
		double canonicalMentionConfidence = entity.getCanonicalMentionConfidence();
		boolean preconditions = true;      
           
		// get connection to metadata store
        try {
               sqlConnection = QuickJDBC.getConnection();
        } catch (SQLException ex) {
                System.out.println("Insert entity failed. Unable to get connection to SQL DB");
                ex.printStackTrace();
                return null;
        }
		
		try
		{              
			// check entity insert preconditions
			// check that there is atleast one mention
			if(entity.getCanonicalMention()==null && (mentions==null || mentions.size()==0))
			{
				preconditions = false;
				throw new RuntimeException("Entity should have at least one mention. Cannot complete insertion for entity ID: "
						+ entity.getEntityId() + " and value: " + entity.getValue());
			}
			
			// check that all mentions contain this entity in their Entity ID distribution lists
			for(EntityMention mention : mentions)
			{
				Map<Long,Float> entityIdDistribution = mention.getEntityIdDistribution();
				if(!entityIdDistribution.containsKey(entity.getEntityId()))
				{
					preconditions = false;
					throw new RuntimeException("Not all entity mentions contain the entity ID in their entityIdDistribution lists. "
							+ "Please fix this.");
				}
			}
							
			// insert entity triples
			UpdateRequest entityInsertRequest = SparqlQueryBuilder.createEntityInsertQueries(entity, entityId, entityConfidence, 
					canonicalMentionConfidence);
			UpdateProcessor upp = UpdateExecutionFactory.createRemote(
	        		entityInsertRequest, 
	                KBParameters.tripleStoreUrl+"/update");
			upp.execute();
			
			// insert canonical mention
			if(entity.getCanonicalMention()!=null) insertEntityMention(entity, entity.getCanonicalMention(), sqlConnection);
			
			// insert other entity mentions
			if(mentions != null)
			{
				for(EntityMention mention : mentions)
				{
					insertEntityMention(entity, mention, sqlConnection);
				}
			}
			
			// insert external KB ID mapping
			if(!usePreExistingAdeptKbId)
				addMapToExternalKBIds(new KBID(entityId, KBParameters.ADEPT_KB_IDENTIFIER), externalKBIds, sqlConnection);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("rollback on ID: " + entityId);
			if(preconditions)
			{
				boolean success = deleteKBObject(new KBID(entityId, KBParameters.ADEPT_KB_IDENTIFIER));
	        	System.out.println("Rollback successful? " + success);
			}
        	
        	return null;
		}
        finally 
        {
            try 
            { 
                 if (sqlConnection != null) sqlConnection.close(); 
            }
            catch (Exception e) 
            {
            	System.out.println("ERROR: Could not close SQL connection");        	
            }
        }

		return new KBID(entityId, KBParameters.ADEPT_KB_IDENTIFIER);
	}
	
	/**
	 * Insert entity mention. 
	 * See overloaded function for more details.
	 */
	private void insertEntityMention(Entity entity, EntityMention mention, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException
	{
		insertEntityMention(null, entity, mention, sqlConnection);
	}
	
	/**
	 * Insert entity mention into SQL metadata
	 * DB. Overloaded version that takes 4 arguments.
	 * First argument is the entity URI that this mention
	 * resolves to. If this is null, the URI is obtained from
	 * the entity argument instead.
	 * 
	 * The second and third arguments are details about the entity
	 * and mention themselves. The fourth is the SQL connection
	 * instance to be used.
	 */
	private void insertEntityMention(String entityUri, Entity entity, EntityMention mention, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException
	{
		String entityId = entity.getId().toString();
		
		// insert chunk
		insertChunk(mention, mention.getSourceAlgorithm(), sqlConnection);
		
		// insert source algorithm
		if(mention.getSourceAlgorithm()!=null)
		{
			insertSourceAlgorithm(mention.getSourceAlgorithm(), sqlConnection);
		}
		
		// insert mention
		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesEntityMentionExistQuery(mention.getId().toString(), sqlConnection)))
		{
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createEntityMentionInsertQuery(mention.getId().toString(), 
					mention.getMentionType().getType(), 
					mention.getId().toString(), (mention.getSourceAlgorithm()!=null?mention.getSourceAlgorithm().getAlgorithmName():null), 
					(entityUri==null?entity.getId().toString():entityUri), (mention.getEntityIdDistribution().get(entity.getEntityId())!=null?
							mention.getEntityIdDistribution().get(entity.getEntityId()):0.0f), sqlConnection));
		}
		
		sqlConnection.commit();
	}
	
	/**
	 * Insert Chunk into the SQL database. This method
	 * is responsible for serializing the tokenstream, saving it onto
	 * the metadata server, and storing a pointer
	 * to this serialized file as part of the metadata record.
	 * 
	 */
	private void insertChunk(Chunk chunk, SourceAlgorithm sourceAlgorithm, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, MalformedURLException, 
	IOException
	{
		String chunkId = chunk.getId().toString();
		String value = chunk.getValue();
		int beginOffset = chunk.getTokenOffset().getBegin();
		int endOffset = chunk.getTokenOffset().getEnd();
		Document sourceDocument = chunk.getTokenStream().getDocument();
		TokenStream tokenstream = chunk.getTokenStream();
		
		if(sourceDocument != null)
			insertSourceDocument(sourceDocument, sqlConnection);
		
		// serialize tokenstream and write to file on file system
		// at the path specified in KBParameters.xml
	    XMLSerializer xmls = new XMLSerializer(SerializationType.XML);
	    String serializedTokenStream = xmls.serializeAsString(tokenstream);
	    
	    String tokenStreamFile = sourceDocument.getDocId();
	    if(sourceAlgorithm != null)
	    {
	    	tokenStreamFile = sourceAlgorithm.getContributingSiteName()+"_"+sourceAlgorithm.getAlgorithmName()+ "_"
		    		+ sourceDocument.getDocId();
	    }

	    //System.out.println("Writing file to: "  + tokenStreamFile + " on remote SQL server");

	    HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(KBParameters.tsService + "/write");
		// set filepath header
	    post.setHeader("filepath", KBParameters.remoteTokenStreamPath + "/" + tokenStreamFile);
	    // set contents body
	    post.setEntity(new ByteArrayEntity(serializedTokenStream.getBytes("UTF8")));
	        
	    // send request
	    HttpResponse response = client.execute(post);
	    System.out.println(EntityUtils.toString(response.getEntity()));
	    // insert chunk into SQL DB
		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesChunkExistQuery(chunkId, sqlConnection)))
		{
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createTextChunkInsertQuery(chunkId, value, beginOffset, endOffset, 
					sourceDocument != null?sourceDocument.getDocId():null, tokenStreamFile, sqlConnection));
		}
	}
	
	/**
	 * Insert source document into the SQL DB
	 * 
	 */
	private void insertSourceDocument(Document sourceDocument, Connection sqlConnection) throws SQLException
	{
		if(sourceDocument.getCorpus() != null)
			insertCorpus(sourceDocument.getCorpus(), sqlConnection);

		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesSourceDocExistQuery(sourceDocument.getDocId(), sqlConnection)))
		{
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createSourceDocumentInsertQuery(sourceDocument.getDocId(), sourceDocument.getUri(), sourceDocument.getLanguage(), 
					sourceDocument.getCorpus()!=null?sourceDocument.getCorpus().getCorpusId():null, 
                                        sourceDocument.getPublicationDate()==null?null:java.sql.Date.valueOf(sourceDocument.getPublicationDate()), sqlConnection));
		}
	}
	
	
	/**
	 * 
	 * Insert corpus in the SQL DB.
	 */
	private void insertCorpus(Corpus corpus, Connection sqlConnection) throws SQLException
	{
		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery(corpus.getCorpusId(), sqlConnection)))
		{
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createCorpusInsertQuery(corpus.getCorpusId(), corpus.getType(), 
					corpus.getName(), corpus.getUri(), sqlConnection));
		}
	}
	
	/**
	 * Insert source algorithm in the SQL DB
	 * 
	 */
	private void insertSourceAlgorithm(SourceAlgorithm sourceAlgorithm, Connection sqlConnection) throws SQLException
	{

		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesSourceAlgorithmExistQuery(sourceAlgorithm.getAlgorithmName(), sqlConnection)))
		{
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createSourceAlgorithmInsertQuery(sourceAlgorithm.getAlgorithmName(), 
					sourceAlgorithm.getContributingSiteName(), sqlConnection));
		}
	}
	
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
	 *     mention.getBestEntity().getBestKBEntity() -- i.e. 1-best resolution.<br><br>
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
	 * @param kbIdsMap	map from the document entity IDs of the arguments to the KB entities they map to. Note
	 * that we consider the 1-best KB ID. 
	 * @param externalKbIds	any external KB relations that the document relation is known to resolve to
	 * 
	 * 
	 * @return the KB ID corresponding to the relation after insertion
	 * 
	 * TODO: add support for date and number arguments. 
	 */
	@Override
	public <T extends KBID> KBID insertRelation(DocumentRelation documentRelation, Map<Long,T> kbIdsMap, List<KBRelation> externalKBIds) {
		//Add a check to see if external map already contains an Adept ID.
		// If yes, throw exception and say are you sure you dont
		// want to update existing relation?
		String existingAdeptId = null;
		if(externalKBIds != null)
		{
			for(KBRelation kbRelation : externalKBIds)
			{
				if(kbRelation.getSourceKB().equals(KBParameters.ADEPT_KB_IDENTIFIER))
				{
					existingAdeptId = kbRelation.getKBUri();
					break;
				}
			}
		}
				
		if(existingAdeptId != null)
		{
			throw new RuntimeException("This relation already seems to have an entry in the Adept KB with ID = "
					+ existingAdeptId + ". Please use the updateRelation() method to update this existing relation.");
		}
		else
		{
			return insertRelation(documentRelation, kbIdsMap, externalKBIds, false);
		}

	}
	
	
	/**
	 * overloaded method to distinguish between inserting a fresh relation
	 * and reinserting a relation (can happen when there is a failure in the update
	 * method). This is a private method, or in other words, a user of the KB
	 * is NOT allowed to reinsert an existing KB relation.
	 */
    private <T extends KBID> KBID insertRelation(DocumentRelation documentRelation, Map<Long,T> kbIdsMap, List<KBRelation> externalKbIds,
    		boolean usePreExistingAdeptKbId) 
    {
    	Connection sqlConnection = null;
		String relationId;
		if(usePreExistingAdeptKbId)
		{
			relationId = null;
			for(KBRelation kbRelation : externalKbIds)
			{
				if(kbRelation.getSourceKB().equals(KBParameters.ADEPT_KB_IDENTIFIER))
				{
					relationId = kbRelation.getKBUri();
					break;
				}
			}
			if(relationId==null)
			{
				throw new RuntimeException("ERROR: No pre existing Adept KB ID found!");
			}
		}	
		else
			relationId = documentRelation.getId().toString();
		
		// get SQL connection
        try {
            sqlConnection = QuickJDBC.getConnection();
         } catch (SQLException ex) {
            System.out.println("Insert relation failed. Unable to get connection to SQL DB");
            ex.printStackTrace();
            return null;
         }
                
		boolean preconditions = true;
		try
		{       
			Map<Long,String> argsToEntityMentionMap;
			
			try
			{
				argsToEntityMentionMap = checkRelationPreconditions(documentRelation, kbIdsMap);
			}
			catch(Exception e)
			{
				preconditions = false;
				System.out.println(e.getMessage());
				throw new RuntimeException("Preconditions are not satisfied.");
			}
			
		
			// insert relation triple
			UpdateRequest relationInsertRequest = SparqlQueryBuilder.createRelationInsertQueries(documentRelation, relationId);
			UpdateProcessor upp = UpdateExecutionFactory.createRemote(
				        		relationInsertRequest, 
				                KBParameters.tripleStoreUrl+"/update");
			upp.execute();
			
			
			for(DocumentRelation.Filler argument : documentRelation.getArguments())
			{
				// insert argument triples
				if(argument.asEntity().isPresent())
				{
					Entity argumentEntity = argument.asEntity().get();

				    UpdateRequest argumentInsertRequest = SparqlQueryBuilder.createArgumentInsertQueries(argument.getArgumentType(), argument.getConfidence(),
								kbIdsMap.get(argumentEntity.getEntityId()).getKBUri(), relationId);
						upp = UpdateExecutionFactory.createRemote(
				        		argumentInsertRequest, 
				                KBParameters.tripleStoreUrl+"/update");
						upp.execute();
				}
				//TODO: add support for date and number arguments
			}
			
			for(RelationMention relationMention : documentRelation.getProvenances())
			{
				// insert relation mention into metadata SQL database
			    insertRelationMention(documentRelation, relationMention, relationMention.getConfidence(), argsToEntityMentionMap, sqlConnection);
			}
			
			// insert external KB ID mapping
			if(!usePreExistingAdeptKbId)
				addMapToExternalKBIds(new KBID(relationId, KBParameters.ADEPT_KB_IDENTIFIER), externalKbIds, sqlConnection);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			if(preconditions)
			{
				boolean success = deleteKBObject(new KBID(relationId, KBParameters.ADEPT_KB_IDENTIFIER));
	        	System.out.println("Rollback successful? " + success);
			}
        	
        	return null;
		}
        finally 
        {
            try 
            { if (sqlConnection != null) 
            	sqlConnection.close(); 
            } catch (Exception e) 
            {
            	System.out.println("Failed to close SQL connection.");
            };
        }

		return new KBID(relationId, KBParameters.ADEPT_KB_IDENTIFIER);
	}
	
	/**
	 * Insert relation mention.
	 * See overloaded function for more details.
	 */
	private void insertRelationMention(DocumentRelation documentRelation, RelationMention relationMention, Float confidence, 
			Map<Long, String> argsToEntityMentionMap, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, IOException, MalformedURLException
	{
		insertRelationMention(null, documentRelation, relationMention, confidence, argsToEntityMentionMap, sqlConnection);
	}
	
	/**
	 * Insert relation mention into SQL metadata
	 * DB. Overloaded version that takes 6 arguments.
	 * First argument is the relation URI that this mention
	 * resolves to. If this is null, the URI is obtained from
	 * the document relation argument instead.
	 * 
	 * This method performs the mapping of the relation mention type
	 * to valid Adept ontology type. Note that if mapping was not 
	 * correct, there would have been an error thrown during relation
	 * triple insertion.  
	 */
	private void insertRelationMention(String documentRelationUri, DocumentRelation documentRelation, RelationMention relationMention, 
			Float confidence, Map<Long, String> argsToEntityMentionMap, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, IOException, MalformedURLException
	{
		String documentRelationId = documentRelation.getId().toString();
		
		// insert justification chunk
		Chunk justification = relationMention.getJustification();
		String justificationId=null;
		if(justification != null)
		{
			insertChunk(justification, relationMention.getSourceAlgorithm(), sqlConnection);
			justificationId = justification.getId().toString();
		}
		
		// insert source algorithm
		if(relationMention.getSourceAlgorithm() != null)
			insertSourceAlgorithm(relationMention.getSourceAlgorithm(), sqlConnection);
		
		// insert relation mention
		if(!QuickJDBC.recordExists(SqlQueryBuilder.doesRelationMentionExistQuery(relationMention.getId().toString(), sqlConnection)))
		{
			String relType = relationMention.getRelationType().getType();
			if(KBParameters.ontologyMap.containsKey(relType)) relType = KBParameters.ontologyMap.get(relType);
			
			QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createRelationMentionInsertQuery(relationMention.getId().toString(), 
					relType, 
					justificationId, relationMention.getSourceAlgorithm() != null?relationMention.getSourceAlgorithm().getAlgorithmName():null, 
					documentRelationUri!=null?documentRelationUri:documentRelationId, confidence, sqlConnection));
		}
		
		// insert argument mentions
		KBQueryProcessor queryProcessor = new KBQueryProcessor();
		for(RelationMention.Filler argument : relationMention.getArguments())
		{
			if(argument.asEntityMention().isPresent())
			{
				EntityMention entitymention = argument.asEntityMention().get();
				insertArgumentMention(argument, relationMention, argsToEntityMentionMap.get(argument.asEntityMention().get().getSequenceId()), sqlConnection);
			}
			
			//TODO: add support for date and number mentions
		}
		
		sqlConnection.commit();
	}
	
	/**
	 * Insert argument mention into the SQL DB.
	 * 
	 * This method performs the mapping of the argument mention type
	 * to valid Adept ontology type. Note that if mapping was not 
	 * correct, there would have been an error thrown during argument
	 * triple insertion.
	 */
   private void insertArgumentMention(RelationMention.Filler argument, RelationMention relationMention, String mentionId, Connection sqlConnection) throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException
   {
	        if(argument.asEntityMention().isPresent())
	        {
	        	String relType = relationMention.getRelationType().getType();
				String argType = argument.getArgumentType();
				if(KBParameters.ontologyMap.containsKey(relType+"."+argType)) argType = KBParameters.ontologyMap.get(relType+"."+argType);
				
				QuickJDBC.executeSqlUpdate(SqlQueryBuilder.createArgumentEntityMentionInsertQuery(UUID.randomUUID().toString(), 
						argType, mentionId, 
						relationMention.getId().toString(), argument.getConfidence(), sqlConnection));
	        }
	        
	      //TODO: add support for date and number mentions
   }
	
   /**
    * Insert ADEPT Event into KB
    * 
    * @return the KB ID corresponding to the event after insertion
    */
	@Override
    public KBID insertEvent(Event e) {
		//TODO
		return null;
	}
	
   
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
	@Override
    public boolean updateEntity(KBID kbId, Entity updatedEntity, List<EntityMention> mentions, 
    		List<KBEntity> externalKBIds) {
		
		String kbUri = kbId.getKBUri();
		
		boolean preconditions = true;
		double entityConfidence = updatedEntity.getEntityConfidence();
		double typeConfidence = updatedEntity.getTypeConfidence();
		double canonicalMentionConfidence = updatedEntity.getCanonicalMentionConfidence();
		
		Entity oldEntity=null;
          
		// get SQL connection
		Connection sqlConnection = null;
        try {
             sqlConnection = QuickJDBC.getConnection();
        } catch (SQLException ex) {
             System.out.println("Update failed. Unable to get connection to SQL DB");
             ex.printStackTrace();
             return false;
        }
		
        List<KBEntity> updatedMappings = new ArrayList<KBEntity>();
		try
		{                        
			// check if KB object to be updated exists in KB.
			if(!doesKBObjectExist(kbUri))
			{
				preconditions = false;
				throw new RuntimeException("Entity to be updated does not exist in the KB.");
			}
			// check that all mentions contain this entity in their Entity ID distribution lists
			for(EntityMention mention : mentions)
			{
				Map<Long,Float> entityIdDistribution = mention.getEntityIdDistribution();
				if(!entityIdDistribution.containsKey(updatedEntity.getEntityId()))
				{
					preconditions = false;
					throw new RuntimeException("Not all entity mentions contain the entity ID in their entityIdDistribution lists. "
										+ "Please fix this.");
				}
			}
			
			// query old entity to support rollback in case of failure
			KBQueryProcessor queryProcessor = new KBQueryProcessor();
			oldEntity = queryProcessor.getEntityById(kbId);
			
			// execute updates in triple store
			UpdateRequest entityUpdateRequest = SparqlQueryBuilder.createEntityUpdateQueries(updatedEntity, kbUri, entityConfidence, typeConfidence, canonicalMentionConfidence);
			UpdateProcessor upp = UpdateExecutionFactory.createRemote(
				       entityUpdateRequest, 
				       KBParameters.tripleStoreUrl+"/update");
			upp.execute();
						
			// insert canonical mention
			if(updatedEntity.getCanonicalMention()!=null) insertEntityMention(kbUri, updatedEntity, updatedEntity.getCanonicalMention(), sqlConnection);
			
			// insert entity mentions		
			for(EntityMention mention : mentions)
			{
				insertEntityMention(kbUri, updatedEntity, mention, sqlConnection);
			}
			
			//insert new external KB ID mappings
			updatedMappings = addMapToExternalKBIds(new KBID(kbUri,KBParameters.ADEPT_KB_IDENTIFIER), externalKBIds, sqlConnection);
		}
        catch (Exception e)
        {
            System.out.println("Entity update failed. Rollback will be attempted.");

            if(preconditions)
            {
                updateEntityRollback(kbUri, updatedEntity, oldEntity, mentions, updatedMappings, sqlConnection);
            }
            e.printStackTrace();
            return false;
        }
        finally {
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
		
		return true;
	}
	
	/**
	 * 
	 * Method that handles rollback incase of entity update failure.
	 */
	private void updateEntityRollback(String entityId, Entity updatedEntity, Entity oldEntity, List<EntityMention> newMentions,
			List<KBEntity> updatedMappings, Connection sqlConnection)
	{
		try
		{
			// delete all entity triples
			deleteTriplesInTripleStore(entityId);
			
			// re-insert old entity triples, and the old canonical mention
			// into the metadata store.
			List<EntityMention> mentionsList = new ArrayList<EntityMention>();
			mentionsList.add(oldEntity.getCanonicalMention());
			List<KBEntity> AdeptKbIdMap = new ArrayList<KBEntity>();
			AdeptKbIdMap.add(new KBEntity(entityId, oldEntity.getEntityType(), KBParameters.ADEPT_KB_IDENTIFIER));
			insertEntity(oldEntity, mentionsList, AdeptKbIdMap, true);
			
			// delete new mentions added
			deleteEntityMentionById(updatedEntity.getCanonicalMention().getId().toString(), sqlConnection);
			if(newMentions != null)
			{
				for(EntityMention mention : newMentions)
				{
					deleteEntityMentionById(mention.getId().toString(), sqlConnection);
				}
			}
			
			//delete new external KB ID mappings added	
			if(updatedMappings.size()>0)
			{
				for(KBEntity kbEntity : updatedMappings)
				{
					deleteExternalKBIdMap(entityId, kbEntity.getKBUri(), kbEntity.getSourceKB(), sqlConnection);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Rollback failed. State of entry with ID " + entityId + " is uncertain in the KB."
					+ "Delete and reinsert to be safe.");
			e.printStackTrace();
		}
	}
   
	
   /**
    * <p>Modify existing KB relation. The updates propagate
    * to the metadata SQL DB, or in other words the update may also be to
    * a mention or other metadata associated with the relation.</p>
    * 
    * <p>The method does not allow modification of relation type or argument entities. 
    * The only changes allowed are appending new relation mentions, argument mentions 
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
	@Override
    public <T extends KBID> boolean updateRelation(KBID kbId, DocumentRelation updatedRelation, Map<Long,T> kbIdsMap, 
    		List<KBRelation> externalKBIds)   {
		String kbUri = kbId.getKBUri();
		
        Connection sqlConnection = null;
		boolean preconditions = true;
		
		KBQueryProcessor queryProcessor = new KBQueryProcessor();
		
		//TODO: save old relation attributes, to allow reconstruction in case of failure
		float oldRelationConfidence = 0f;
		Map<String,Float> oldArgConfidences = new HashMap<String,Float>();
         
		// get SQL connection
        try {
           sqlConnection = QuickJDBC.getConnection();
        } catch (SQLException ex) {
           System.out.println("Update failed. Unable to get connection to SQL DB");
           ex.printStackTrace();
           return false;
        }
			
        List<KBRelation> updatedMappings = new ArrayList<KBRelation>();
		try
		{
			// check preconditions for updated relation
            Map<Long,String> argsToEntityMentionMap;
			try
			{
				argsToEntityMentionMap = checkRelationPreconditions(updatedRelation, kbIdsMap);
			}
			catch(Exception e)
			{
				preconditions = false;
				throw new RuntimeException(e.getMessage());
			} 
			
			// check if KB object to be updated exists in KB.
			if(!doesKBObjectExist(kbUri))
			{
			    preconditions = false;
			    throw new RuntimeException("Relation to be updated does not exist in the KB.");
			}
						
			// check that no new arguments are being introduced during update
			for(RelationMention relationMention : updatedRelation.getProvenances())
			{
				for(RelationMention.Filler argument : relationMention.getArguments())
				{
					if(argument.asEntityMention().isPresent() && 
							!relationContainsArgument(kbUri, kbIdsMap.get(argument.asEntityMention().get().getBestEntityId()).getKBUri(), 
							relationMention.getRelationType().getType(), argument.getArgumentType()))
					{
						throw new RuntimeException("ERROR: You are not allowed to introduce new arguments while updating a relation.");
					}
				}
				
				//TODO: update this to handle number and date argument mentions.
			}
			
			// update relation confidence
			oldRelationConfidence = getRelationConfidence(kbUri);
			//System.out.println("Old relation confidence is: " + oldRelationConfidence);
			UpdateRequest confidenceUpdateRequest = SparqlQueryBuilder.createRelationConfidenceUpdateQuery(kbUri, updatedRelation.getConfidence());
			UpdateProcessor upp = UpdateExecutionFactory.createRemote(
					confidenceUpdateRequest, 
	                KBParameters.tripleStoreUrl+"/update");
			upp.execute();
			
			// update argument confidences
			for(DocumentRelation.Filler argument : updatedRelation.getArguments())
			{		
				if(argument.asEntity().isPresent())
				{
					Entity argumentEntity = argument.asEntity().get();
					
					// get old argument confidence for rollback
					float oldArgumentConfidence = getArgumentConfidence(kbUri, kbIdsMap.get(argument.asEntity().get().getEntityId()).getKBUri(), updatedRelation.getRelationType().getType(),
							argument.getArgumentType());
					String reifiedArgStmtId = getReifiedStmtIdBySubjectAndObject(kbUri, 
							"adept-data:"+kbIdsMap.get(argumentEntity.getEntityId()).getKBUri());
					oldArgConfidences.put(reifiedArgStmtId, oldArgumentConfidence);
					
					// perform the argument confidence update
					UpdateRequest argConfidenceUpdateRequest = SparqlQueryBuilder.createArgumentConfidenceUpdateQueries(reifiedArgStmtId,
							argument.getConfidence());
					upp = UpdateExecutionFactory.createRemote(
							argConfidenceUpdateRequest, 
			                KBParameters.tripleStoreUrl+"/update");
				    upp.execute();
					
				}
				
				//TODO: add support for date and number args
			}
			
			// append relation mentions
			for(RelationMention relationMention : updatedRelation.getProvenances())
			{				
				// insert relation mention into metadata SQL database
				insertRelationMention(kbUri, updatedRelation, relationMention, relationMention.getConfidence(), argsToEntityMentionMap, sqlConnection);	
			}
			
			//insert new external KB ID mappings
			updatedMappings = addMapToExternalKBIds(new KBID(kbUri,KBParameters.ADEPT_KB_IDENTIFIER), externalKBIds, sqlConnection);
		}
		catch(Exception e)
		{
			System.out.println("Update failed.");
			    	
			if(preconditions)
			{
				// delete newly inserted relation mentions
				for(RelationMention relationMention : updatedRelation.getProvenances())
				{
					try
					{
						deleteRelationMentionById(relationMention.getId().toString(), sqlConnection);
					}
					catch(Exception e2)
					{
						System.out.println("Rollback failed");
						e2.printStackTrace();
					}
					
				}
				
				//revert to old relation confidence
				UpdateRequest confidenceUpdateRequest = SparqlQueryBuilder.createRelationConfidenceUpdateQuery(kbUri, oldRelationConfidence);
				UpdateProcessor upp = UpdateExecutionFactory.createRemote(
						confidenceUpdateRequest, 
		                KBParameters.tripleStoreUrl+"/update");
				upp.execute();
				
				// revert to old argument confidences
				for(String stmtId : oldArgConfidences.keySet())
				{		
						UpdateRequest argConfidenceUpdateRequest = SparqlQueryBuilder.createArgumentConfidenceUpdateQueries(stmtId,
								oldArgConfidences.get(stmtId));
						upp = UpdateExecutionFactory.createRemote(
								argConfidenceUpdateRequest, 
				                KBParameters.tripleStoreUrl+"/update");
					    upp.execute();
				}
				
				//delete new external KB ID mappings added	
				if(updatedMappings.size()>0)
				{
					try
					{
						for(KBRelation kbRelation : updatedMappings)
						{
							deleteExternalKBIdMap(kbUri, kbRelation.getKBUri(), kbRelation.getSourceKB(), sqlConnection);
						}
					}
					catch(Exception e3)
					{
						System.out.println("Rollback failed");
						e3.printStackTrace();
					}
					
				}
			}
						
			e.printStackTrace();
			return false;
         }
         finally 
         {
             try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
         }
		
		return true;
	}

   
   /**
    * Modify existing KB event. The updates propagate
    * to the metadata SQL DB, or in other words the update may also be to
    * a mention or other metadata associated with the event.
    * 
    * @return boolean value indicating success or failure
    */
	@Override
    public boolean updateEvent(KBID kbId, Event updatedEvent) {
		//TODO
	    return false;
	}
   
   
   /**
    * Delete a first class KB object and all associated 
    * metadata.
    * 
    * @return boolean value indicating success or failure
    */
	@Override
    public boolean deleteKBObject(KBID kbId) {
       
	   String kbUri = kbId.getKBUri();
		
       Connection sqlConnection = null;
       
       // get SQL connection instance
       try {
            sqlConnection = QuickJDBC.getConnection();
       } catch (SQLException ex) {
            System.out.println("KB object deletion failed. Unable to get connection to SQL DB");
            ex.printStackTrace();
            return false;
       }
        
		try
		{
			deleteTriplesInTripleStore(kbUri);
			
			// if in middle of a SQL transaction, rollback. 
			// Else, delete all mentions belonging to entity in the metadata DB
			sqlConnection.rollback();
			deleteEntityMentionsForEntity(kbUri, sqlConnection);
			deleteRelationMentionsForRelation(kbUri, sqlConnection);
			deleteDateMentionsForDate(kbUri, sqlConnection);
			
			// delete external KB ID maps
			deleteAllExternalKBIdMapsForElement(kbUri, sqlConnection);
		}
		catch(Exception e)
		{
			System.out.println("KB object deletion failed.");
			e.printStackTrace();
			return false;
		}  
        finally {
            try
            { 
              if (sqlConnection != null) sqlConnection.close(); 
            } 
            catch (Exception e) 
            {
              System.out.println("Could not close SQL connection.");   		
            };
        }
		
		return true;
	}
	
	/**
	 * deletes only the triples. Does not touch metadata.
	 */
	private void  deleteTriplesInTripleStore(String kbUri)
	{
		// get UpdateRequest that deletes triples that have input URI in subject
		// or object, also deletes coresponding reified statements.
		UpdateRequest deleteRequest = SparqlQueryBuilder.createDeleteQueries(kbUri);
		UpdateProcessor upp = UpdateExecutionFactory.createRemote(
			        		deleteRequest, 
			                KBParameters.tripleStoreUrl+"/update");
		upp.execute();
	}
	
	/**
	 * Delete entity mentions corresponding to a specific KB entity
	 * from the SQL DB
	 */
	private void deleteEntityMentionsForEntity(String entityUri, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createEntityMentionDeleteQuery(entityUri, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/**
	 * Delete relation mentions corresponding to a specific KB relation
	 * from the SQL DB
	 */
	private void deleteRelationMentionsForRelation(String relationUri, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createRelationMentionDeleteQuery(relationUri, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/**
	 * Delete date mentions corresponding to a specific KB Date
	 * from the SQL DB
	 */
	private void deleteDateMentionsForDate(String dateUri, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDateMentionDeleteQuery(dateUri, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/** delete entity mention by ID. Required for entity update rollback */
	private void deleteEntityMentionById(String mentionId, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDeleteEntityMentionByIdQuery(mentionId, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/** delete relation mention by ID. Required for relation update rollback */
	private void deleteRelationMentionById(String mentionId, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDeleteRelationMentionByIdQuery(mentionId, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/** delete arg mention by ID. Required for relation update rollback */
	private void deleteArgumentMentionByRelationMentionId(String mentionId, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDeleteArgumentMentionByRelationMentionIdQuery(mentionId, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/** delete external KB IDs belonging to a given Adept KB ID. */
	private void deleteAllExternalKBIdMapsForElement(String kbUri, Connection sqlConnection) throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDeleteExternalKBIdMapsForAdeptElementQuery(kbUri, sqlConnection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		sqlConnection.commit();
	}
	
	/**
	 * delete specific external map record 
	 */
	private void deleteExternalKBIdMap(String adeptId, String externalId, String sourceKb, Connection connection)
			throws SQLException
	{
		PreparedStatement preparedStmt = SqlQueryBuilder.createDeleteSingleExternalKBIdMapRecordQuery(adeptId, externalId,
				sourceKb, connection);
		QuickJDBC.executeSqlUpdate(preparedStmt);
		connection.commit();
	}
	
	
	/**
	 * Check if a KB object exists in the triple store
	 * 
	 */
	private boolean doesKBObjectExist(String kbUri) {
		Query query = QueryFactory.create(SparqlQueryBuilder.createDoesKBObjectExistQuery(kbUri));
		QueryExecution qexec = QueryExecutionFactory.sparqlService( KBParameters.tripleStoreUrl+"/query", query);
		com.hp.hpl.jena.query.ResultSet results = qexec.execSelect();
		if(results.hasNext())
			return true;
		else return false;
	}
	
	
	/**
	 * Check relation preconditions on insert or update. The following are checked:<br>
	 * (1) The document relation has at least one relation mention provenance
	 *     entry.<br>
	 * (2) All argument mentions in the provenances map to a valid entity
	 *     in the document.<br>
	 * (3) The roles of the argument mentions agree with the role of the
	 *     argument entity that they resolve to.<br>
	 * (4) All argument entities already exist in the ADEPT KB.<br>
	 * (5) TODO: Check that the entity type of the argument's entity is as expected in 
	 *     ontology.<br>
	 * (6) TODO: Check that all slots of the relation are filled as is expected in
	 *     ontology.<br>
	 */
	private <T extends KBID>  Map<Long, String> checkRelationPreconditions(DocumentRelation documentRelation, Map<Long,T> kbIdsMap) 
			throws SQLException
	{
		Map<Long,String> argsToEntityMentionMap = new HashMap<Long,String>();
		
		// check preconditions:
		// 1. There should be at least one provenance entry
		if(documentRelation.getProvenances() == null || documentRelation.getProvenances().size()==0)
		{
			System.out.println("Relation should have atleast one provenance entry.");
			throw new RuntimeException("Relation should have atleast one provenance entry.");
		}
						
		// 2. arguments resolve to document entities correctly
		// 3. All argument entities and mentions have already been inserted into the KB
		for(RelationMention relationMention : documentRelation.getProvenances())
		{
			for(RelationMention.Filler argument : relationMention.getArguments())
			{
				if(argument.asEntityMention().isPresent())
				{
					String argRole = argument.getArgumentType();
					long entityId = argument.asEntityMention().get().getBestEntityId();
					
					// fail unless entity is valid,
					// and if it has same argument role in document relation
					DocumentRelation.Filler entityArg = documentRelation.getArgumentById(entityId);
					if(entityArg == null)
					{
						throw new RuntimeException("Discrepancy between how the relation mention's arguments map to"
											+ "the document relation's arguments. Argument entity with entity ID = "+ entityId 
											+ " that a mention maps to does not exist in document relation.");
					}
					if(!entityArg.getArgumentType().equals(argRole))
				    {
						throw new RuntimeException("Argument roles are inconsistent when going from relation mention to "
											+ "document relation. Argument mention with role " + argRole + 
											" maps to entity with role " + entityArg.getArgumentType() + ". Please fix this.");
					}
								
					// fail if argument entity isn't already in the KB
					if(!kbIdsMap.containsKey(entityId) || !doesKBObjectExist(kbIdsMap.get(entityId).getKBUri()))
					{
						throw new RuntimeException("Please make sure all argument entities are present in KB"
														+ " before inserting relation");
					}	
								
					// fail if argument entity mention isn't already in KB
					KBQueryProcessor queryProcessor = new KBQueryProcessor();
					String mentionId = entityMentionExistsInKB(argument.asEntityMention().get(), 
										kbIdsMap.get(entityId).getKBUri());
					if(mentionId == null)
					{
						throw new RuntimeException("Please make sure all argument entity mentions are present in KB"
											+ " before inserting relation");
					}
					else
					{
						argsToEntityMentionMap.put(argument.asEntityMention().get().getSequenceId(), mentionId);
					}
			
				}
							
				//TODO: add support for when argument is instance of TimePhrase
				// or NumberPhrase
										
			}
		}
					
		//4. TODO: Check that the entity type of the argument's entity is as expected in ontology
		//5. TODO: check that all slots of a relation are filled, as expected by the ontology
					
		return argsToEntityMentionMap;
	}
	
	/**
	 * <p>add map from Adept KB ID to external KB Ids.</p>
	 * 
	 * <p>Note that an external KB ID map to one and only one
	 * Adept KB ID. As a result, if you attempt to map an
	 * already seen external KB entity to a new Adept Entity, 
	 * this method will fail.</p>
	 * 
	 * <p>On the other hand, an Adept KB entity is allowed to map to 
	 * more than one external KB entity. </p>
	 */
	private <T extends KBID> List<T> addMapToExternalKBIds(KBID adeptKbId, List<T> externalKBIds, Connection sqlConnection) throws SQLException
	{
		List<T> externalMapsAdded = new ArrayList<T>();
		
		if(externalKBIds != null)
		{
			for(T kbId : externalKBIds)
			{
				// check if external ID already exists
				PreparedStatement doesExternalIdExistStmt = SqlQueryBuilder.createdoesExternalKBIdExistQuery(kbId.getKBUri(), kbId.getSourceKB(), sqlConnection);
				java.sql.ResultSet doesExternalIdExistResult = doesExternalIdExistStmt.executeQuery();
	            if(doesExternalIdExistResult.next())
	            {
	            	// fail
	            	System.out.println("Mapping already exists for external element with ID: " + kbId.getKBUri() + 
	            			" in KB " + kbId.getSourceKB() + ". New mapping not inserted.");
	            }
	            else
	            {
	            	PreparedStatement preparedStmt = SqlQueryBuilder.createInsertMappingToExternalKBQuery(adeptKbId.getKBUri(), kbId.getKBUri(), kbId.getSourceKB(), sqlConnection);
					QuickJDBC.executeSqlUpdate(preparedStmt);
					sqlConnection.commit();
					
					externalMapsAdded.add(kbId);
	            }
			}
		}
		
		return externalMapsAdded;
	}
	
	
	/**
	 * Gets the overall confidence of the
	 * KB relation from the triple store.
	 */
	private float getRelationConfidence(String kbUri)
	{
		try
		{
			Query query = SparqlQueryBuilder.createGetRelationConfidenceQuery(kbUri);
	        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
	        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
	        if (resultSet.hasNext()) {
	            QuerySolution item = resultSet.next();

	            return Float.parseFloat(item.get(SparqlQueryBuilder.OBJECT).asLiteral().getString());
	        }
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
        return 0f;
	}
	
	/**
     * query to check if an Adept KB relation contains given argument.
     */
    private boolean relationContainsArgument(String relationId, String argumentId, String relationType, String argType)
    {
    	if(KBParameters.ontologyMap.containsKey(relationType + "." + argType))
        	argType = KBParameters.ontologyMap.get(relationType + "." + argType);
    	
    	Query query = SparqlQueryBuilder.createDoesRelationContainArgumentQuery(relationId, argumentId, argType);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
        if (resultSet.hasNext()) {
            return true;
        }
        
        return false;
    }
    
    /**
	 * This method checks if an entity mention exists in Adept KB
	 * metadata store by comparing the following fields: token offsets,
	 * mention value, source document, the KB entity it resolves to and the 
	 * confidence of this resolution.
	 */
	private String entityMentionExistsInKB(EntityMention mention, String kbEntityId)
	{
		java.sql.ResultSet entityMentionQueryResult = null;
		Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
        try
        {
        	sqlConnection = QuickJDBC.getConnection();
        	preparedStmt = SqlQueryBuilder.doesEntityMentionExistQuery(mention.getTokenStream().getDocument().getDocId(), 
        			mention.getTokenOffset().getBegin(), mention.getTokenOffset().getEnd(), 
        			mention.getValue(), kbEntityId, mention.getConfidence(mention.getBestEntityId()), sqlConnection);
        	entityMentionQueryResult = preparedStmt.executeQuery();
        	if(entityMentionQueryResult.next())
        	{
        		return entityMentionQueryResult.getString("ID");
        	}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
		return null;
	}
	
	/**
     * get relation argument confidence.
     */
    private float getArgumentConfidence(String relationId, String argumentId, String relationType, String argumentType)
    {
    	if(KBParameters.ontologyMap.containsKey(relationType + "." + argumentType))
        	argumentType = KBParameters.ontologyMap.get(relationType + "." + argumentType);
    	
    	Query query = SparqlQueryBuilder.createGetArgumentConfidenceQuery(relationId, argumentId, argumentType);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
        if (resultSet.hasNext()) {
            QuerySolution item = resultSet.next();

            RDFNode object = item.get(SparqlQueryBuilder.OBJECT);
            String objectAsString = object.isLiteral() ? object.asLiteral().getString() : object.toString().split("#")[1];
            return Float.parseFloat(objectAsString);
        }
        
        return 0.0f;
    }
    
    /** 
     * get statement ID containing given subject and object values.
     */
    private String getReifiedStmtIdBySubjectAndObject(String subject, String object)
    {
    	Query query = SparqlQueryBuilder.createGetReifiedStmtIdBySubjectAndObjectQuery(subject, object);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
        if (resultSet.hasNext()) {
            QuerySolution item = resultSet.next();

            RDFNode subjectNode = item.get(SparqlQueryBuilder.SUBJECT);
            String subjectAsString = subjectNode.isLiteral() ? subjectNode.asLiteral().getString() : subjectNode.toString().split("#")[1];
            return subjectAsString;
        }
        
        return null;
    }
	
	
}