package adept.kbapi;

import adept.common.*;
import adept.utilities.*;
import adept.kbapi.sparql.SparqlQueryBuilder;
import adept.kbapi.sql.objects.EntityMentionsResult;
import adept.kbapi.sql.QuickJDBC;
import adept.kbapi.sql.objects.RelationMentionsResult;
import adept.kbapi.sql.objects.SourceAlgorithmsResult;
import adept.kbapi.sql.objects.SourceDocumentsResult;
import adept.kbapi.sql.SqlQueryBuilder;
import adept.kbapi.sql.objects.ArgumentsResult;
import adept.kbapi.sql.objects.CorpusResult;
import adept.kbapi.sql.objects.TextChunksResult;
import adept.metadata.SourceAlgorithm;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;
import adept.io.Reader;
import adept.io.Writer;
import com.google.common.collect.ImmutableMap;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * Implementations of various KB query API methods
 */
public class KBQueryProcessor implements IQuery {
    private static final Random rand = new Random();
    
    /**
     * <p>Query KB entity given corresponding Adept KBID object.
     * This method uses the reverse ontology map
     * to convert from Adept ontology type to user
     * ontology type.</p>
     * 
     * <p>Note that this method does not return any information
     * about whether this entity maps to any external KB entity.
     * You must separately query for this information
     * using the getExternalKBIDs() API method.</p>
     * 
     * @param kbId KBID
     * 
     * @return Adept Entity object
     */
    @Override
    public Entity getEntityById(KBID kbId) 
    {
    	if(kbId == null) return null;
    	String kbUri = kbId.getKBUri();
    	
        Connection sqlConnection = null;
        PreparedStatement entityMentionQueryPreparedStmt = null;
        java.sql.ResultSet entityMentionQueryResult = null;
        
        try 
        {
        	// get SQL connection instance
            sqlConnection = QuickJDBC.getConnection();
            
            // query triples from triple store
            HashMap<String, List<String>> output = buildPredicateObjectMapFromId(kbUri);

            // retrieve entity types, and type confidences
            List<String> types = output.get("type");
            Map<IType,Double> typeConfidences = new HashMap<IType,Double>();
            String typeWithHighestConfidence=null;
            double highestConfidence = 0.0;
            for(String type : types)
            {
            	double typeconfidence = 0.0;
            	HashMap<String, List<String>> reifiedStmtOutput = buildPredicateObjectMapFromId(getReifiedStmtId(kbUri, "rdf:type", "adept-kb:"+type));
                if(reifiedStmtOutput!=null && reifiedStmtOutput.get("confidence") != null)
                {
                	typeconfidence = Double.parseDouble(reifiedStmtOutput.get("confidence").get(0));
                }
            	
                if(KBParameters.reverseOntologyMap.containsKey(type)) type = KBParameters.reverseOntologyMap.get(type);
            	   typeConfidences.put(new Type(type), typeconfidence);
            	
            	if(typeconfidence >= highestConfidence)
            	{
            		highestConfidence = typeconfidence;
            		typeWithHighestConfidence = type;
            	}
            }
            
            long entityId = rand.nextLong();
            Entity entity = new Entity(entityId, new Type(typeWithHighestConfidence));
            entity.addTypes(typeConfidences);
                        
            // retrieve canonical mention ID, and create it.
            String mention = output.get("canonicalMention").get(0);
            if (mention != null) {
                EntityMentionsResult canonicalMentionResult = null;

                entityMentionQueryPreparedStmt = SqlQueryBuilder.createEntityMentionByIdQuery(mention, sqlConnection);
                entityMentionQueryResult = entityMentionQueryPreparedStmt.executeQuery();
                
                if (entityMentionQueryResult.next()) {
                    canonicalMentionResult = new EntityMentionsResult(entityMentionQueryResult);
                }     

                Chunk textChunk = getChunkById(canonicalMentionResult.getChunk());
                
                // the sequence ID assigned to this canonical mention is random
                EntityMention entityMention = new EntityMention(rand.nextLong(), textChunk.getTokenOffset(), textChunk.getTokenStream());
                entityMention.addEntityConfidencePair(entityId, (float)canonicalMentionResult.getConfidence());
                
                String canonicalMentionType = canonicalMentionResult.getMentionType();
                entityMention.setMentionType(new Type(canonicalMentionType));                             
                
                entityMention.setSourceAlgorithm(getSourceAlgorithmById(canonicalMentionResult.getSourceAlgorithm()));
                
                entity.setCanonicalMentions(entityMention);   
            }

            // add confidences on canonical mention, and entity confidence
            if(output.get("confidence")!=null)
            {
            	double entityConfidence = Double.parseDouble(output.get("confidence").get(0));
                //System.out.println("Entity confidence is: " + entityConfidence);
                entity.setEntityConfidence(entityConfidence);
            }
            
            HashMap<String,List<String>> reifiedStmtOutput = buildPredicateObjectMapFromId(getReifiedStmtIdBySubjectAndPredicate(kbUri, "adept-base:canonicalMention"));
            if(reifiedStmtOutput!=null && reifiedStmtOutput.get("confidence") != null &&  reifiedStmtOutput.get("confidence").size()>0)
            {
            	double canonicalMentionConfidence = Double.parseDouble(reifiedStmtOutput.get("confidence").get(0));
                //System.out.println("Canonical mention confidence is: " + canonicalMentionConfidence);
                entity.setCanonicalMentionConfidence(canonicalMentionConfidence);
            }
            
            return entity;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entity with ID = "+kbUri);
        }   
        finally {
            try { if (entityMentionQueryResult != null) entityMentionQueryResult.close(); } catch (Exception e) {};
            try { if (entityMentionQueryPreparedStmt != null) entityMentionQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return null;
    }
	  
    /**
    * <p>Query KB relation given corresponding KBID.</p>
    * 
    * <p>This method uses the reverse ontology map
    * to convert from Adept ontology type to user
    * ontology type.</p>
    * 
    * <p>Note that this method does not return any information
    * about whether this entity maps to any external KB relation.
    * You must separately query for this information
    * using the getExternalKBIDs() API method.</p>
    * 
    * @param kbId KBID
    * 
    * @return Adept DocumentRelation object
    */
    @Override
    public DocumentRelation getRelationById(KBID kbId) {
    	
    	if(kbId == null) return null;
    	String kbUri = kbId.getKBUri();
    	
        Connection sqlConnection = null;
        PreparedStatement relationMentionsQueryPreparedStmt = null;
        PreparedStatement argumentsQueryQueryPreparedStmt = null;
        java.sql.ResultSet relationMentionsQueryResult = null;
        java.sql.ResultSet argumentsQueryResult = null;
        
        try {
        	
        	// get SQL connection instance
            sqlConnection = QuickJDBC.getConnection();
            
            // get triples from triple store corresponding to relation URI
            HashMap<String, List<String>> output = buildPredicateObjectMapFromId(kbUri);

            String documentRelationType = output.get("type").get(0); 
            String newDocumentRelationType = documentRelationType;
            if(KBParameters.reverseOntologyMap.containsKey(documentRelationType))
  			   newDocumentRelationType = KBParameters.reverseOntologyMap.get(documentRelationType);
             
            DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(new Type(newDocumentRelationType));   
            
            List<RelationMention> provenances = getRelationMentionsForKBRelation(kbId); 
            documentRelationBuilder.addProvenances(provenances);
            if(output.get("confidence")!=null && output.get("confidence").size()>0) 
            	documentRelationBuilder.setConfidence(Float.parseFloat(output.get("confidence").get(0)));
            
            // make DocumentRelation.Filler arguments from RelationMention.Filler argument mentions
            for(RelationMention relationMention : provenances)
            {
            	for(RelationMention.Filler argument : relationMention.getArguments())
            	{
            		String argType = argument.getArgumentType();
            		String newArgType = argType;
            		// get the entity arg of this type
            		Entity argEntity = getEntityById(new KBID(output.get(argType).get(0),KBParameters.ADEPT_KB_IDENTIFIER));
            		if(argEntity != null)
            		{
                        if(KBParameters.reverseOntologyMap.containsKey(documentRelationType + "." + argType))
                        	newArgType = KBParameters.reverseOntologyMap.get(documentRelationType + "." + argType);
            			
            			documentRelationBuilder.addArgument(DocumentRelation.Filler.fromEntity(argEntity, 
                        		new Type(newArgType), getArgumentConfidence(kbUri, output.get(argType).get(0), documentRelationType, argType)));
            			
            			if(argument.asEntityMention().isPresent())
                		{
                			EntityMention argMention = argument.asEntityMention().get();
                    		argMention.getEntityIdDistribution().put(argEntity.getEntityId(), argMention.getEntityIdDistribution().get(new Long(-1)));
                    		argMention.getEntityIdDistribution().remove(new Long(-1));
                		}
            		}
            		            		
            		//TODO: add handling for date and number arguments
            	}
            }
            
            DocumentRelation documentRelation = documentRelationBuilder.build();
            documentRelation = applyTACSpecificRules(documentRelation);
            return documentRelation;              

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for relation with id "+kbUri);
        }   
        finally {
            try { if (argumentsQueryResult != null) argumentsQueryResult.close(); } catch (Exception e) {};
            try { if (relationMentionsQueryResult != null) relationMentionsQueryResult.close(); } catch (Exception e) {};
            try { if (argumentsQueryQueryPreparedStmt != null) argumentsQueryQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (relationMentionsQueryPreparedStmt != null) relationMentionsQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return null;
    }
	 
    /**
     * Query KB event by URI
     * 
     * @return Adept Event object
     */
    @Override
    public Event getEventById(KBID kbId) {
            //TODO
    	if(kbId == null) return null;
    	String kbUri = kbId.getKBUri();
            
    	return null;
    }
	  
    /**
     * <p>Query entity objects by mention value.
     * case insensitive exact string match.</p>
     * 
     * <p>Note that this method 
     * only returns the KB IDs. To get the actual Entity object,
     * you will need to make an additional call to
     * getEntityById().</p>
     * 
     * @return List of Adept KB entity IDs that have atleast one
     * mention that matches the argument.
     */
    @Override
    public List<KBID> getEntitiesByStringReference(String value) {
        Connection sqlConnection = null;
        PreparedStatement entitiesByStringReferenceQueryPreparedStmt = null;
        java.sql.ResultSet entitiesByStringReferenceQueryResult = null;
        
        try {
            sqlConnection = QuickJDBC.getConnection();
       
            entitiesByStringReferenceQueryPreparedStmt = SqlQueryBuilder.createEntityIdsByChunkValueQuery(value.toLowerCase(), sqlConnection);
            entitiesByStringReferenceQueryResult = entitiesByStringReferenceQueryPreparedStmt.executeQuery();
            List<String> discoveredEntities = new ArrayList<String>();
            while (entitiesByStringReferenceQueryResult.next()) {
                String entityId = entitiesByStringReferenceQueryResult.getString("KBEntityId");
                if (entityId != null && !discoveredEntities.contains(entityId)) {
                	discoveredEntities.add(entityId);
                }
            }
            
            List<KBID> kbIds = new ArrayList<KBID>();
            for(String discoveredEntity : discoveredEntities)
            {
            	kbIds.add(new KBID(discoveredEntity, KBParameters.ADEPT_KB_IDENTIFIER));
            }
            
            return kbIds;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entities with reference "+value);
        }  
        finally {
            try { if (entitiesByStringReferenceQueryResult != null) entitiesByStringReferenceQueryResult.close(); } catch (Exception e) {};
            try { if (entitiesByStringReferenceQueryPreparedStmt != null) entitiesByStringReferenceQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return null;
    }
    
    /**
     * <p>Query entity objects by mention value.
     * Regex match. Uses SQL regexes.</p>
     * 
     * <p>Note that this method 
     * only returns the KB IDs. To get the actual Entity object,
     * you will need to make an additional call to
     * getEntityById().</p>
     * @return List of Adept KB entity IDs that have atleast one
     * mention that matches the argument. 
     */
    @Override
    public List<KBID> getEntitiesByRegexMatch(String regex) {
        Connection sqlConnection = null;
        PreparedStatement entitiesByRegexQueryPreparedStmt = null;
        java.sql.ResultSet entitiesByRegexQueryResult = null;
        
        try {
            sqlConnection = QuickJDBC.getConnection();
            
            List<Entity> entities = new ArrayList<Entity>();
            
            entitiesByRegexQueryPreparedStmt = SqlQueryBuilder.createEntityIdsByChunkRegexQuery(regex, sqlConnection);
            entitiesByRegexQueryResult = entitiesByRegexQueryPreparedStmt.executeQuery();
            List<String> discoveredEntities = new ArrayList<String>();
            while (entitiesByRegexQueryResult.next()) {
                String entityId = entitiesByRegexQueryResult.getString("KBEntityId");
                if (entityId != null && !discoveredEntities.contains(entityId)) {
                	discoveredEntities.add(entityId);
                }
            }
            
            List<KBID> kbIds = new ArrayList<KBID>();
            for(String discoveredEntity : discoveredEntities)
            {
            	kbIds.add(new KBID(discoveredEntity, KBParameters.ADEPT_KB_IDENTIFIER));
            }
            
            return kbIds;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entities with reference "+regex);
        }  
        finally {
            try { if (entitiesByRegexQueryResult != null) entitiesByRegexQueryResult.close(); } catch (Exception e) {};
            try { if (entitiesByRegexQueryPreparedStmt != null) entitiesByRegexQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return null;
    }
	  
    /**
     * get Adept KB entity IDs by type. Note that this method 
     * only returns the KB IDs. To get the actual Entity object,
     * you will need to make an additional call to
     * getEntityById().
     * 
     * @return List<KBID>
     */
    @Override
    public List<KBID> getEntitiesByType(IType type) {
        try {
            List<String> entities = new ArrayList<String>();

            String typeString = type.getType();
            if(KBParameters.ontologyMap.containsKey(typeString))
    			   typeString = KBParameters.ontologyMap.get(typeString);
            
            Query query = SparqlQueryBuilder.createGetSubjectsByTypeQuery(typeString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
            com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();                

            while (resultSet.hasNext()) {
                QuerySolution item = resultSet.next();

                RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
                String subjectAsString = subject.asResource().getURI();

                entities.add(subjectAsString.split("#")[1]);
            }

            List<KBID> kbIds = new ArrayList<KBID>();
            for(String entityUri : entities)
            {
            	kbIds.add(new KBID(entityUri, KBParameters.ADEPT_KB_IDENTIFIER));
            }
            
            return kbIds;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entities with type "+type.getType());
        }   

        return null;
    }
    
    /** get Adept KB entity corresponding to an external KB entity. 
     * The method will return an Adept KB URI, given the external KB ID
     * has already been stored in the database. Else, return null.
     * 
     * @return KBID
     */
    @Override
    public KBID getAdeptEntityByExternalID(KBID externalKbId)
    {
    	if(externalKbId == null)
    		return null;
    	
    	Connection sqlConnection = null;
        PreparedStatement entitiesByExternalIdPreparedStmt = null;
        java.sql.ResultSet entitiesByExternalIdQueryResult = null;
        String adeptEntity = null;
        try {
            sqlConnection = QuickJDBC.getConnection();            
            entitiesByExternalIdPreparedStmt = SqlQueryBuilder.getAdeptIdByExternalIdQuery(externalKbId.getKBUri(), externalKbId.getSourceKB(), sqlConnection);
            entitiesByExternalIdQueryResult = entitiesByExternalIdPreparedStmt.executeQuery();
            if(entitiesByExternalIdQueryResult.next()) {
                adeptEntity = entitiesByExternalIdQueryResult.getString("AdeptKBID");
            }
            
            return new KBID(adeptEntity, KBParameters.ADEPT_KB_IDENTIFIER);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entities with external ID "+externalKbId.getKBUri());
        }  
        finally {
            try { if (entitiesByExternalIdQueryResult != null) entitiesByExternalIdQueryResult.close(); } catch (Exception e) {};
            try { if (entitiesByExternalIdPreparedStmt != null) entitiesByExternalIdPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return null;
    }
    
	  
    /**
     * get Adept KB entity IDs by value and type. Note that
     * this method only returns the KB IDs. To get the actual 
     * Entity object, you will need to make an additional call 
     * to getEntityById().
     * 
     * @return List<KBID>
     */
    @Override
    public List<KBID> getEntitiesByValueAndType(String value, IType type) {
        try {
            List<KBID> entities = new ArrayList<KBID>();
            List<KBID> valueMatchedEntities = getEntitiesByStringReference(value);
            
            //System.out.println("Size of valueMatchedEntities: " + valueMatchedEntities.size());
            
            String typeString = type.getType();
            
            for (KBID valueMatchedEntity : valueMatchedEntities) {
                if (getEntityById(valueMatchedEntity).getEntityType().getType().equals(typeString)) {
                    entities.add(valueMatchedEntity);
                }
            }

            
            return entities;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for entities with type "+type.getType()+" and reference "+value);
        } 
        return null;
    }
	  
    /**
     * get Adept KB relation IDs by KB argument URI. Note that this method 
     * only returns the KB IDs. To get the actual relation object,
     * you will need to make an additional call to
     * getRelationById().
     * 
     * @return List of document relation IDs containing input URI
     * as argument
     */
    @Override
    public List<KBID> getRelationsByArg(KBID kbId) {
        try 
        {
        	if(kbId==null) return null;
        	String kbUri = kbId.getKBUri();
        	
            List<String> relations = new ArrayList<String>();
            Query query = SparqlQueryBuilder.createGetTriplesByObjectURIQuery(kbUri);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
            com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution item = resultSet.next();
                RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
                String subjectAsString = subject.isLiteral() ? subject.asLiteral().getString() : subject.asResource().getURI();
                RDFNode predicate = item.get(SparqlQueryBuilder.PREDICATE);
                String predicateAsString = predicate.isLiteral() ? predicate.asLiteral().getString() : predicate.asResource().getLocalName();
                
                if (!predicateAsString.equals("object")) {
                    relations.add(subjectAsString.split("#")[1]);
                }
            }
            
            List<KBID> kbIds = new ArrayList<KBID>();
            for(String relationUri : relations)
            {
            	kbIds.add(new KBID(relationUri, KBParameters.ADEPT_KB_IDENTIFIER));
            }
            
            return kbIds;
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to query for relations with arg "+ kbId.getKBUri());
        } 
        return null;
    }
    
    /**
     * get Adept KB relation IDs by mention value. Note that this method 
     * only returns the KB IDs. To get the actual relation object,
     * you will need to make an additional call to
     * getRelationById().
     * 
     * @return List<KBID>
     */
    @Override
    public List<KBID> getRelationsByStringReference(String value) {
        Connection sqlConnection = null;
        PreparedStatement relationsByStringReferenceQueryPreparedStmt = null;
        java.sql.ResultSet relationsByStringReferenceQueryResult = null;
        
    	 try 
    	 {
             sqlConnection = QuickJDBC.getConnection();
             relationsByStringReferenceQueryPreparedStmt = SqlQueryBuilder.createRelationIdsByChunkValueQuery(value, sqlConnection);
             relationsByStringReferenceQueryResult = relationsByStringReferenceQueryPreparedStmt.executeQuery();
             List<String> discoveredRelations = new ArrayList<String>();
             while (relationsByStringReferenceQueryResult.next()) {
                 String relationId = relationsByStringReferenceQueryResult.getString("KBRelationId");
                 if (relationId != null && !discoveredRelations.contains(relationId)) {
                 	 discoveredRelations.add(relationId);
                 }
             }

             List<KBID> kbIds = new ArrayList<KBID>();
             for(String relationUri : discoveredRelations)
             {
             	kbIds.add(new KBID(relationUri, KBParameters.ADEPT_KB_IDENTIFIER));
             }
             
             return kbIds;
         } 
    	 catch (Exception ex) 
         {
             ex.printStackTrace();
             System.out.println("Failed to query for relations with reference "+value);
         }  
         finally {
            try { if (relationsByStringReferenceQueryResult != null) relationsByStringReferenceQueryResult.close(); } catch (Exception e) {};
            try { if (relationsByStringReferenceQueryPreparedStmt != null) relationsByStringReferenceQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
         
         return null;
    }
	  
    /**
     * get Adept KB relation IDs by Type. Note that this method 
     * only returns the KB IDs. To get the actual relation object,
     * you will need to make an additional call to
     * getRelationById().
     * 
     * @return List<KBID> corresponding to input type
     */
    @Override
    public List<KBID> getRelationsByType(IType type) {
        try {
            List<String> relations = new ArrayList<String>();

            String typeString = type.getType();
            if(KBParameters.ontologyMap.containsKey(typeString))
    			   typeString = KBParameters.ontologyMap.get(typeString);
            
            Query query = SparqlQueryBuilder.createGetSubjectsByTypeQuery(typeString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
            com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();                

            while (resultSet.hasNext()) {
                QuerySolution item = resultSet.next();

                RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
                String subjectAsString = subject.asResource().getURI();

                relations.add(subjectAsString.split("#")[1]);
            }

            List<KBID> kbIds = new ArrayList<KBID>();
            for(String relationUri : relations)
            {
            	kbIds.add(new KBID(relationUri, KBParameters.ADEPT_KB_IDENTIFIER));
            }
            
            return kbIds;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for relations with type "+type.getType());
        }   

        return null;
    }
	  
    /**
     * get Adept KB relation IDs by argument and type. Note that 
     * this method only returns the KB IDs. To get the actual 
     * relation object, you will need to make an additional call to
     * getRelationById().
     * 
     * @return List<KBID>
     */
    @Override
    public List<KBID> getRelationsByArgAndType(KBID kbId, IType type) {
        try {
            List<KBID> relations = new ArrayList<KBID>();
            List<KBID> argMatchedEntities = getRelationsByArg(kbId);
            
            String typeString = type.getType();
            for (KBID argMatchedEntity : argMatchedEntities) {
                if (getRelationById(argMatchedEntity).getRelationType().getType().equals(typeString)) {
                    relations.add(argMatchedEntity);
                }
            }
            
            return relations;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to query for relations with type "+type.getType()+" and arg "+kbId.getKBUri());
        }   

        return null;
    }
	  
    /**
     * Get all entity mentions given KB Entity URI.
     * Note that while the entityIdDistribution field
     * is set in the mentions returned, the resolved entity ID (long)
     * isn't, since this value is not known to this method.
     * This must be set explicitly by the client, if required. This method sets
     * the entity ID in the map to -1.
     */
    public List<EntityMention> getEntityMentionsForKBEntity(KBID kbId)
    {
    	if(kbId==null) return null;
    	String kbUri = kbId.getKBUri();
    	
        Connection sqlConnection = null;
        PreparedStatement entityMentionQueryPreparedStmt = null;
        java.sql.ResultSet entityMentionQueryResult = null;
        
    	try
    	{
            sqlConnection = QuickJDBC.getConnection();
            
    		List<EntityMention> mentions = new ArrayList<EntityMention>();
                    		
                entityMentionQueryPreparedStmt = SqlQueryBuilder.createEntityMentionsByEntityIdQuery(kbUri, sqlConnection);
        	entityMentionQueryResult = entityMentionQueryPreparedStmt.executeQuery();
            while(entityMentionQueryResult.next()) 
            {
                EntityMentionsResult entityMentionsResult = new EntityMentionsResult(entityMentionQueryResult);
                
                Chunk textChunk = getChunkById(entityMentionsResult.getChunk());

                EntityMention entityMention = new EntityMention(rand.nextLong(), textChunk.getTokenOffset(), textChunk.getTokenStream());
                entityMention.addEntityConfidencePair(-1, (float)entityMentionsResult.getConfidence());
                
                String mentionType = entityMentionsResult.getMentionType();
                entityMention.setMentionType(new Type(mentionType));                         
                entityMention.setSourceAlgorithm(getSourceAlgorithmById(entityMentionsResult.getSourceAlgorithm()));

                mentions.add(entityMention);
            }     

            return mentions;
    	}
    	catch(Exception e)
    	{
    		System.out.println("Could not fetch mentions for entity ID: " + kbUri);
    		e.printStackTrace();
    	}
        finally {
            try { if (entityMentionQueryResult != null) entityMentionQueryResult.close(); } catch (Exception e) {};
            try { if (entityMentionQueryPreparedStmt != null) entityMentionQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
    	
        return null;
    }
    
    /**
     * get relation mentions associated with given KB relation URI.
     * 
     * Note that reverse ontology type mapping (to convert from
     * Adept ontology types to user ontology types) is not
     * applied at the mention level.
     * 
     */
    public List<RelationMention> getRelationMentionsForKBRelation(KBID kbId)
    {
    	if(kbId==null) return null;
    	String kbUri = kbId.getKBUri();
    	
        Connection sqlConnection = null;
        PreparedStatement relationMentionsQueryPreparedStmt = null;
        PreparedStatement argumentsQueryPreparedStmt = null;
        java.sql.ResultSet relationMentionsQueryResult = null;
        java.sql.ResultSet argumentsQueryResult = null;
        
    	try
    	{
            sqlConnection = QuickJDBC.getConnection();
            
    		List<RelationMention> relationMentions = new ArrayList<RelationMention>();
                    		
            relationMentionsQueryPreparedStmt = SqlQueryBuilder.createRelationMentionsByKBRelationIdQuery(kbUri, sqlConnection);
    		relationMentionsQueryResult = relationMentionsQueryPreparedStmt.executeQuery();
            while (relationMentionsQueryResult.next()) {
            	
                RelationMentionsResult relationMentionsResult = new RelationMentionsResult(relationMentionsQueryResult); 
                
                String relationMentionType = relationMentionsResult.getRelationType();
                
                RelationMention.Builder relationMentionBuilder =  RelationMention.builder(new Type(relationMentionType));
                relationMentionBuilder.setConfidence(relationMentionsResult.getConfidence());
                relationMentionBuilder.addJustification(getChunkById(relationMentionsResult.getChunk()));
                
                argumentsQueryPreparedStmt = SqlQueryBuilder.createArgumentsByRelationMentionIdQuery(relationMentionsResult.getID(), sqlConnection);
                argumentsQueryResult = argumentsQueryPreparedStmt.executeQuery();
                while (argumentsQueryResult.next()) {                              
                    ArgumentsResult argumentsResult = new ArgumentsResult(argumentsQueryResult);

                    EntityMention argumentEntityMention = getEntityMentionById(argumentsResult.getEntityMention());

                    String argType = argumentsResult.getArgType();
                    relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(argumentEntityMention, 
                    		new Type(argType), argumentsResult.getConfidence()));
                }

                RelationMention relationMention = relationMentionBuilder.build();
                relationMention.setSourceAlgorithm(getSourceAlgorithmById(relationMentionsResult.getSourceAlgorithm()));
                relationMentions.add(relationMention);                    
            }
            
            return relationMentions;
    	}
    	catch(Exception e)
    	{
    		System.out.println("Could not fetch mentions for relation ID: " + kbUri);
    		e.printStackTrace();
    	}
        finally {
            try { if (argumentsQueryResult != null) argumentsQueryResult.close(); } catch (Exception e) {};
            try { if (relationMentionsQueryResult != null) relationMentionsQueryResult.close(); } catch (Exception e) {};
            try { if (argumentsQueryPreparedStmt != null) argumentsQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (relationMentionsQueryPreparedStmt != null) relationMentionsQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
    	
    	return null;
    }
    
    
    /**
     * get KB event IDs given event argument
     * 
     * @return List<KBID> containing input URI as argument 
     */
    @Override
    public List<KBID> getEventsByArg(KBID kbId) {
          //TODO
           return null;
    }
	  
    /**
     * get KB events by Type
     * 
     * @return List<KBID> corresponding to input type
     */
    @Override
    public List<KBID> getEventsByType(IType type) {
          //TODO
          return null;
    }
	  
    /**
     * get KB events by argument and type
     * 
     * @return List<KBID>
     */
    @Override
    public List<KBID> getEventsByArgAndType(KBID kbId, IType type) {
          //TODO
          return null;
    }

    /**
     * get related entities upto given depth
     * 
     * @return  HltContentContainer containing relevant entities
     * and relations
     */
    @Override
    public HltContentContainer getRelatedEntities(KBID kbId, int depth) {
          //TODO
          return null;
    }

    /**
     * get related entities by relation type upto given depth
     * 
     * @return  HltContentContainer containing relevant entities
     * and relations
     */
    @Override
    public HltContentContainer getRelatedEntitiesByRelationType(KBID kbId, int depth, IType type) {
          //TODO
          return null;
    }


    /**
     * Get KB objects contained within input chunk
     * 
     * @return HltContentContainer containing relevant entities,
     * relations and events
     */
    @Override
    public HltContentContainer getKBObjectsWithinChunk(Chunk chunk)
    {
    	HltContentContainer hltcc = new HltContentContainer();
    	
    	try
    	{
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
    		
    		// perform actual queries and object construction
    		List<String> entityUris = getEntityIdsInChunk(chunk);
    		for(String entityUri : entityUris)
    		{
    			Entity entity = getEntityById(new KBID(entityUri,KBParameters.ADEPT_KB_IDENTIFIER));
    			List<EntityMention> queriedMentions = getEntityMentionsForKBEntity(new KBID(entityUri,KBParameters.ADEPT_KB_IDENTIFIER));
    			Iterator<EntityMention> queriedMentionsIter = queriedMentions.iterator();
    			while (queriedMentionsIter.hasNext()) 
    			{
    				EntityMention m = queriedMentionsIter.next();
    				m.addEntityConfidencePair(entity.getEntityId() , m.getEntityIdDistribution().get(new Long(-1)));
    				m.getEntityIdDistribution().remove(new Long(-1));
    				mentions.add(m);
    			}
    			entities.add(entity);
    		}
    		
    		List<String> relationUris = getRelationIdsInChunk(chunk);
    		for(String relationUri : relationUris)
    		{
    			relations.add(getRelationById(new KBID(relationUri, KBParameters.ADEPT_KB_IDENTIFIER)));
    		}
    		
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    		System.out.println("ERROR: Could not get KB objects within chunk.");
    	}

		return hltcc;
    }
    
    
    /**
     * get all entity IDs that include mentions within a given span of text.
     */
    private List<String> getEntityIdsInChunk(Chunk chunk) throws SQLException
    {
        List<String> entityIds = new ArrayList<String>();
        
        Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
        java.sql.ResultSet result = null;
        
        try {
            sqlConnection = QuickJDBC.getConnection();

            // get source document ID and begin and end offsets from chunk
            String sourceDocId = chunk.getTokenStream().getDocument().getDocId();
            int beginOffset = chunk.getTokenOffset().getBegin();
            int endOffset = chunk.getTokenOffset().getEnd();

            preparedStmt = SqlQueryBuilder.createGetEntityIdsByChunkQuery(sourceDocId, beginOffset, endOffset, sqlConnection);
            result = preparedStmt.executeQuery();
            if (result.next()) 
            {
                    entityIds.add(result.getString("KBEntityId"));
            }
        }
        finally {
            try { if (result != null) result.close(); } catch (Exception e) {};
            try { if (preparedStmt != null) preparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
    	
    	return entityIds;
    }
    
    /**
     * get all relation IDs containing mentions within given text span.
     */
    private List<String> getRelationIdsInChunk(Chunk chunk) throws SQLException
    {
        List<String> relationIds = new ArrayList<String>();
        
        Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
        java.sql.ResultSet result = null;
        
        try {
            sqlConnection = QuickJDBC.getConnection();            

            // get source document ID and begin and end offsets from chunk
            String sourceDocId = chunk.getTokenStream().getDocument().getDocId();
            int beginOffset = chunk.getTokenOffset().getBegin();
            int endOffset = chunk.getTokenOffset().getEnd();

            preparedStmt = SqlQueryBuilder.createGetRelationIdsByChunkQuery(sourceDocId, beginOffset, endOffset, sqlConnection);
            result = preparedStmt.executeQuery();
            if (result.next()) 
            {
                    relationIds.add(result.getString("KBRelationId"));
            }
        }
        finally {
            try { if (result != null) result.close(); } catch (Exception e) {};
            try { if (preparedStmt != null) preparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
    	
    	return relationIds;
    }

    /**
     * execute a Sparql select query.
     * 
     * @return ResultSet
     */
    public com.hp.hpl.jena.query.ResultSet executeSelectQuery(String query) {
    	//TODO: add more elaborate query validation
        Query selectQuery = QueryFactory.create(query);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", selectQuery);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
        return resultSet;
    }
	  
	/**
	 * get all properties from triple store associated with given KB URI.
	 */
    private HashMap<String, List<String>> buildPredicateObjectMapFromId(String id) {
        Query query = SparqlQueryBuilder.createGetTriplesBySubjectURIQuery(id);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

        HashMap<String, List<String>> output = new HashMap<String, List<String>>();

        while (resultSet.hasNext()) {
            QuerySolution item = resultSet.next();

            RDFNode predicate = item.get(SparqlQueryBuilder.PREDICATE);
            String predAsString = predicate.isLiteral() ? predicate.asLiteral().getString() : predicate.toString().split("#")[1];
            
            RDFNode object = item.get(SparqlQueryBuilder.OBJECT);
            String objectAsString = object.isLiteral() ? object.asLiteral().getString() : object.toString().split("#")[1];

            if(!output.containsKey(predAsString))
            {
            	output.put(predAsString, new ArrayList<String>());
            }
            output.get(predAsString).add(objectAsString);
            //System.out.println("Predicate: "+predAsString+"   Object: "+objectAsString);
        }

        return output;
    }  
    
    /**
     * get statement ID containing given subject and predicate values
     */
    private String getReifiedStmtIdBySubjectAndPredicate(String subject, String predicate)
    {
    	Query query = SparqlQueryBuilder.createGetReifiedStmtIdBySubjectAndPredicateQuery(subject, predicate);
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
    
    /** get statement ID containing given subject, predicate
     * and object values.
     * 
     */
    private String getReifiedStmtId(String subject, String predicate, String value)
    {
    	Query query = SparqlQueryBuilder.createGetReifiedStmtIdQuery(subject, predicate, value);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(KBParameters.tripleStoreUrl+"/query", query);
        com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
        if (resultSet.hasNext()) {
            QuerySolution item = resultSet.next();

            RDFNode subjectNode = item.get(SparqlQueryBuilder.SUBJECT);
            String subjectAsString = subjectNode.isLiteral() ? subjectNode.asLiteral().getString() : subjectNode.toString().split("#")[1];
            //System.out.println("reified stmt ID: " + subject.toString());
            return subjectAsString;
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
     * get source document information from metadata store.
     */
    private Document getSourceDocumentById(String id) throws SQLException {
        Document sourceDocument = null;
        
        Connection sqlConnection = null;
        PreparedStatement sourceDocumentQueryPreparedStmt = null;
        java.sql.ResultSet sourceDocumentQueryResult = null;  
        PreparedStatement corpusQueryPreparedStmt = null;
        java.sql.ResultSet corpusQueryResult = null;  
        
        try {
            sqlConnection = QuickJDBC.getConnection();
            
            sourceDocumentQueryPreparedStmt = SqlQueryBuilder.createSourceDocumentByIdQuery(id, sqlConnection);
            sourceDocumentQueryResult = sourceDocumentQueryPreparedStmt.executeQuery();

            if (sourceDocumentQueryResult.next()) {
                SourceDocumentsResult sourceDocumentResult = new SourceDocumentsResult(sourceDocumentQueryResult);

                Corpus corpus = null;
                if (sourceDocumentResult.getCorpus() != null) {
                    corpusQueryPreparedStmt = SqlQueryBuilder.createCorpusIdQuery(sourceDocumentResult.getCorpus(), sqlConnection);
                    corpusQueryResult = corpusQueryPreparedStmt.executeQuery();
                    if (corpusQueryResult.next()) {
                        CorpusResult corpusResult = new CorpusResult(corpusQueryResult);
                        corpus = new Corpus(corpusResult.getID(), corpusResult.getType(), corpusResult.getName(), corpusResult.getURI());
                    }
                }

                sourceDocument = new Document(sourceDocumentResult.getID(), corpus, "Text", sourceDocumentResult.getUri(), sourceDocumentResult.getSourceLanguageId());
                sourceDocument.setPublicationDate(sourceDocumentResult.getPublicationDate()!=null?
                            new SimpleDateFormat().format(sourceDocumentResult.getPublicationDate())
                            : null);
            }
        }
        finally {
            try { if (corpusQueryResult != null) corpusQueryResult.close(); } catch (Exception e) {};
            try { if (corpusQueryPreparedStmt != null) corpusQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sourceDocumentQueryResult != null) sourceDocumentQueryResult.close(); } catch (Exception e) {};
            try { if (sourceDocumentQueryPreparedStmt != null) sourceDocumentQueryPreparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
        
        return sourceDocument;
    }

    /**
     * get source algorithm information from metadata store.
     */
    private SourceAlgorithm getSourceAlgorithmById(String id) throws SQLException {
        SourceAlgorithm sourceAlgorithm = null;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
        java.sql.ResultSet sourceAlgorithmQueryResult = null;        
        
        try {
            sqlConnection = QuickJDBC.getConnection();
            
            preparedStmt = SqlQueryBuilder.createSourceAlgorithmByIdQuery(id, sqlConnection);
            sourceAlgorithmQueryResult = preparedStmt.executeQuery();
            if (sourceAlgorithmQueryResult.next()) {
                SourceAlgorithmsResult sourceAlgorithmsResult = new SourceAlgorithmsResult(sourceAlgorithmQueryResult);
                sourceAlgorithm = new SourceAlgorithm(sourceAlgorithmsResult.getAlgorithmName(), sourceAlgorithmsResult.getContributingSiteName());
            }
        }
        finally {
            try { if (sourceAlgorithmQueryResult != null) sourceAlgorithmQueryResult.close(); } catch (Exception e) {};
            try { if (preparedStmt != null) preparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }

        return sourceAlgorithm;
    }

    /**
     * <p>get text chunk information from metadata store given its primary
     * key ID.</p> 
     * 
     * <p>This method deserializes the tokenstream that this chunk comes from 
     * that is stored on the metadata server. To speed up execution of this query,
     * make sure you specify a local token stream cache path in the
     * KBParameters.xml config file.</p>
     * 
     * <p>If serialized tokenstream file already exists in local cache, it is used.
     * Else, it is queried from the metadata server. If valid local cache location
     * is specified, the serialized file is written to it for future use.</p>
     */
    private Chunk getChunkById(String id) throws Exception {
        Chunk chunk = null;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
        java.sql.ResultSet textChunkQueryResult = null;        
        
        try 
        {
            sqlConnection = QuickJDBC.getConnection();
            
            preparedStmt = SqlQueryBuilder.createTextChunkByIdQuery(id, sqlConnection);
            textChunkQueryResult = preparedStmt.executeQuery();
            if (textChunkQueryResult.next()) {
                TextChunksResult textChunkResult = new TextChunksResult(textChunkQueryResult);

                File localTsFile = new File(KBParameters.localTokenStreamCache + "/" + textChunkResult.getSerializedTokenStream());
                if (!localTsFile.exists())
                    {
                       HttpClient client = new DefaultHttpClient();
                       HttpGet get = new HttpGet(KBParameters.tsService + "/read");
                       // set filepath header
                       get.setHeader("filepath", KBParameters.remoteTokenStreamPath + "/" + textChunkResult.getSerializedTokenStream());

                       // send request
                       HttpResponse response = client.execute(get);
                       String serializedTokenStream = EntityUtils.toString(response.getEntity());

                    XMLSerializer xmls = new XMLSerializer(SerializationType.XML);
                    TokenStream tokenStream = (TokenStream)xmls.deserializeString(serializedTokenStream, TokenStream.class);

                    chunk = new Chunk(new TokenOffset(textChunkResult.getBeginOffset(), textChunkResult.getEndOffset()), tokenStream);

                    // write to cache if specified
                    if(KBParameters.localTokenStreamCache != null && KBParameters.localTokenStreamCache.length()>0)
                    {
                            try
                            {
                                    Writer.getInstance().writeToFile(KBParameters.localTokenStreamCache + "/" + textChunkResult.getSerializedTokenStream(),
                                            serializedTokenStream);
                            }
                            catch(Exception e)
                            {
                                    System.out.println("failed to save serialized token stream to local cache");
                            }
                    }

                    }
                    else
                    {
                            String serializedTokenStream = Reader.getInstance().readFileIntoString(KBParameters.localTokenStreamCache + "/" + textChunkResult.getSerializedTokenStream());
                            XMLSerializer xmls = new XMLSerializer(SerializationType.XML);
                            TokenStream tokenStream = (TokenStream)xmls.deserializeString(serializedTokenStream, TokenStream.class);

                             chunk = new Chunk(new TokenOffset(textChunkResult.getBeginOffset(), textChunkResult.getEndOffset()), tokenStream);
                    }   

            }
        }
        finally {
            try { if (textChunkQueryResult != null) textChunkQueryResult.close(); } catch (Exception e) {};
            try { if (preparedStmt != null) preparedStmt.close(); } catch (Exception e) {};
            try { if (sqlConnection != null) sqlConnection.close(); } catch (Exception e) {};
        }
       
        return chunk;
    }
    
    
    /**
     * Gets entity mention given its primary key ID.
     * 
     */
    private EntityMention getEntityMentionById(String id)
    {
    	EntityMention mention = null;
    	
    	PreparedStatement entityMentionQueryPreparedStmt = null;
    	java.sql.ResultSet entityMentionQueryResult = null;
    	EntityMentionsResult entityMentionResult = null;
    	
    	Connection sqlConnection = null;
    	
    	try
    	{
    		
    		sqlConnection = QuickJDBC.getConnection();
    		
    		entityMentionQueryPreparedStmt = SqlQueryBuilder.createEntityMentionByIdQuery(id, sqlConnection);
    	    entityMentionQueryResult = entityMentionQueryPreparedStmt.executeQuery();
    	    
    	    if (entityMentionQueryResult.next()) {
    	            entityMentionResult = new EntityMentionsResult(entityMentionQueryResult);
    	    }     

    	    Chunk textChunk = getChunkById(entityMentionResult.getChunk());
    	        
    	    mention = new EntityMention(rand.nextLong(), textChunk.getTokenOffset(), textChunk.getTokenStream());
    	    String mentionType = entityMentionResult.getMentionType();
    	    mention.setMentionType(new Type(mentionType));                             
    	    mention.setSourceAlgorithm(getSourceAlgorithmById(entityMentionResult.getSourceAlgorithm()));
    	    mention.addEntityConfidencePair(-1, entityMentionResult.getConfidence());
    	}
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    	
    	return mention;
    }
	

	
	
	/**
	 * gets all external KB IDs that this Adept KB ID 
	 * maps to.
	 */
	@Override
	public List<KBID> getExternalKBIDs(KBID adeptKbId)
	{
		List<KBID> externalKBIDs = new ArrayList<KBID>();
		Connection sqlConnection = null;
        PreparedStatement preparedStmt = null;
		try
		{
			sqlConnection = QuickJDBC.getConnection();
			preparedStmt = SqlQueryBuilder.getExternalKBIDsQuery(adeptKbId.getKBUri(), sqlConnection);
			java.sql.ResultSet resultSet = preparedStmt.executeQuery();
			while(resultSet.next())
			{
				externalKBIDs.add(new KBID(resultSet.getString("ExternalID"), resultSet.getString("ExternalKB")));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return externalKBIDs;
	}
	
	
	/**
	 * <p>!!WARNING!!: This is extremely TAC-SF specific code.</p>
	 * 
	 * <p>While the Adept ontology does not distinguish between cities, state-provinces and countries 
	 * in relations such as location of person's residence or organizational headquarters, TAC-SF does.</p> 
	 * 
	 * <p>This method looks at the entity type of the argument (which can be City, Country or State) to 
	 * figure out the exact TAC-SF type.</p>
	 * 
	 * <p>Note that this will not work unless you explicitly set the entity type of the 
	 * argument to be one of City, StateProvince or Country (as opposed to
	 * just GPE). In future KB releases, we will add more stringent checking during relation 
	 * insert to make sure that the entity type is one of City, Country or StateProvince.</p>
	 */
	private DocumentRelation applyTACSpecificRules(DocumentRelation documentRelation)
	{
        String documentRelationType = documentRelation.getRelationType().getType();
        if(documentRelationType.equals("org:country_of_headquarters|org:stateprovince_of_headquarters|org:city_of_headquarters"))
        {
        	DocumentRelation.Builder builder = null;
        	DocumentRelation.Filler orgArg = null;
        	// go through arguments, check entity types
        	for(DocumentRelation.Filler argument : documentRelation.getArguments())
        	{
        		if(argument.asEntity().get().getEntityType().equals("Country"))
        		{
        			builder = DocumentRelation.builder(new Type("org:country_of_headquarters"));
        			builder.addArgument(argument);
        		}
        		else if(argument.asEntity().get().getEntityType().equals("City"))
        		{
        			builder = DocumentRelation.builder(new Type("org:city_of_headquarters"));
        			builder.addArgument(argument);
        		}
        		else if(argument.asEntity().get().getEntityType().equals("StateProvince"))
        		{
        			builder = DocumentRelation.builder(new Type("org:stateprovince_of_headquarters"));
        			builder.addArgument(argument);
        		}
        		else 
        		{
        			orgArg = argument;
        		}
        	}
        	if(builder!=null)
        	{
        		builder.addArgument(orgArg);
        		return builder.build();
        	}
        }
        
        else if (documentRelationType.equals("per:countries_of_residence|per:statesorprovinces_of_residence|per:cities_of_residence"))
        {
        	DocumentRelation.Builder builder = null;
        	DocumentRelation.Filler personArg = null;
        	// go through arguments, check entity types
        	for(DocumentRelation.Filler argument : documentRelation.getArguments())
        	{
        		if(argument.asEntity().get().getEntityType().equals("Country"))
        		{
        			builder = DocumentRelation.builder(new Type("per:countries_of_residence"));
        			builder.addArgument(argument);
        		}
        		else if(argument.asEntity().get().getEntityType().equals("City"))
        		{
        			builder = DocumentRelation.builder(new Type("per:cities_of_residence"));
        			builder.addArgument(argument);
        		}
        		else if(argument.asEntity().get().getEntityType().equals("StateProvince"))
        		{
        			builder = DocumentRelation.builder(new Type("per:statesorprovinces_of_residence"));
        			builder.addArgument(argument);
        		}
        		else 
        		{
        			personArg = argument;
        		}
        	}
        	
        	if(builder!=null)
        	{
        		builder.addArgument(personArg);
        		return builder.build();
        	}
        	
        }
        
        return documentRelation;  
	}
}