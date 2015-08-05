package adept.kbapi.sql;

import adept.common.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Utility to build SQL query and update statements
 */
public class SqlQueryBuilder {
	
	    /** SQL templates for querying metadata */
	    public static final String getExternalKbIds =
	    		"SELECT \"ExternalKBReferences\".\"ExternalID\", \"ExternalKBReferences\".\"ExternalKB\" FROM"
	    		+ "\"ExternalKBReferences\" WHERE \"ExternalKBReferences\".\"AdeptKBID\" = ?";
	    
	    public static final String getAdeptIdByExternalId =
	    		"SELECT \"ExternalKBReferences\".\"AdeptKBID\" FROM"
	    		+ "\"ExternalKBReferences\" WHERE \"ExternalKBReferences\".\"ExternalID\" = ? AND \"ExternalKBReferences\".\"ExternalKB\" = ?";
	
        public static final String entityMentionById =
                        "SELECT * FROM \"EntityMentions\" WHERE \"ID\" = ?";
        
        public static final String textChunkById = 
                        "SELECT * FROM \"TextChunks\" WHERE \"ID\" = ?";
        
        public static final String sourceDocumentById = 
                        "SELECT * FROM \"SourceDocuments\" WHERE \"ID\" = ?";
        
        public static final String sourceAlgorithmByName = 
                        "SELECT * FROM \"SourceAlgorithms\" WHERE \"algorithmName\" = ?";
        
        public static final String corpusById = 
                        "SELECT * FROM \"Corpus\" WHERE \"ID\" = ?";
        
        public static final String relationMentionsByKBRelationId =
                        "SELECT * FROM \"RelationMentions\" WHERE \"KBRelationId\" = ?";
        
        public static final String argumentsByRelationMentionId =
                        "SELECT * FROM \"Arguments\" WHERE \"relationMention\" = ?";
        
        public static final String entityIdsByChunkValue =
                        "SELECT \"EntityMentions\".\"KBEntityId\", \"EntityMentions\".\"confidence\""
                        + "FROM \"EntityMentions\" JOIN \"TextChunks\" ON \"EntityMentions\".\"chunk\" = \"TextChunks\".\"ID\" "
                        + "WHERE LOWER(\"TextChunks\".\"value\") = ?";
        
        public static final String entityIdsByChunkRegex =
                "SELECT \"EntityMentions\".\"KBEntityId\", \"EntityMentions\".\"confidence\""
                + "FROM \"EntityMentions\" JOIN \"TextChunks\" ON \"EntityMentions\".\"chunk\" = \"TextChunks\".\"ID\" "
                + "WHERE \"TextChunks\".\"value\" ~ ?";
        
        public static final String relationIdsByChunkValue =
                "SELECT \"RelationMentions\".\"KBRelationId\", \"RelationMentions\".\"confidence\""
                + "FROM \"RelationMentions\" JOIN \"TextChunks\" ON \"RelationMentions\".\"chunk\" = \"TextChunks\".\"ID\" "
                + "WHERE \"TextChunks\".\"value\" = ?";
        
        public static final String kbRelationIdByArgumentKbEntityId = 
                        "SELECT DISTINCT \"RelationMentions\".\"KBRelationId\" "
                        + "FROM \"RelationMentions\" JOIN \"Arguments\" ON \"RelationMentions\".\"ID\" = \"Arguments\".\"relationMention\" "
                        + "WHERE \"Arguments\".\"KBEntityId\" = ?";
        
        public static final String entityMentionsByEntityUri =
        		        "SELECT * FROM \"EntityMentions\" WHERE \"KBEntityId\" = ?";
        
        public static final String getEntityIdsWithinChunkOffset =
        		"SELECT DISTINCT \"EntityMentions\".\"KBEntityId\" "
        		+ "FROM \"EntityMentions\" JOIN \"TextChunks\" ON \"EntityMentions\".\"chunk\" = \"TextChunks\".\"ID\""
        		+ "WHERE \"TextChunks\".\"sourceDocument\" = ? AND \"TextChunks\".\"beginOffset\" >= ?"
        		+ "AND \"TextChunks\".\"endOffset\" <= ?";

        public static final String getRelationIdsWithinChunkOffset =
        		"SELECT DISTINCT \"RelationMentions\".\"KBRelationId\" "
                		+ "FROM \"RelationMentions\" JOIN \"TextChunks\" ON \"RelationMentions\".\"chunk\" = \"TextChunks\".\"ID\""
                		+ "WHERE \"TextChunks\".\"sourceDocument\" = ? AND \"TextChunks\".\"beginOffset\" >= ?"
                		+ "AND \"TextChunks\".\"endOffset\" <= ?";
	
	/** SQL templates for updating metadata */
	public static final String checkExists = 
			"SELECT * from \"%s\" WHERE \"%s\"= ?";
	
	public static final String doesExternalKbIdExist =
			"SELECT * FROM \"ExternalKBReferences\" WHERE \"ExternalID\"= ? AND \"ExternalKB\" = ?";
	
	public static final String insertTextChunk =
			"INSERT INTO \"TextChunks\"(\"ID\", \"value\", \"beginOffset\", \"endOffset\", \"sourceDocument\", \"serializedTokenStream\") "
			+ "values (?, ?, ?, ?, ?, ?)";
	
	public static final String insertEntityMention =
			"INSERT INTO \"EntityMentions\"(\"ID\", \"mentionType\", \"chunk\", \"sourceAlgorithm\", \"KBEntityId\", \"confidence\") "
			+ "values (?, ?, ?, ?, ?, ?)";
	
	public static final String insertRelationMention =
			"INSERT INTO \"RelationMentions\"(\"ID\", \"relationType\", \"chunk\", \"sourceAlgorithm\", \"KBRelationId\", \"confidence\") "
			+ "values (?, ?, ?, ?, ?, ?)";
	
	public static final String insertArgumentEntityMention =
			"INSERT INTO \"Arguments\"(\"ID\", \"argType\", \"entityMention\", \"relationMention\", \"confidence\") "
			+ "values (?, ?, ?, ?, ?)";
	
	public static final String insertDateMention =
			"INSERT INTO \"DateMentions\"(\"ID\", \"chunk\", \"KBDateId\", \"confidence\", \"sourceAlgorithm\") "
					+ "values (?, ?, ?, ?, ?)";
	
	public static final String deleteEntityMention =
			"DELETE FROM \"EntityMentions\" WHERE \"KBEntityId\" = ?";
	
	public static final String deleteRelationMention =
			"DELETE FROM \"RelationMentions\" WHERE \"KBRelationId\" = ?";
	
	public static final String deleteDateMention =
			"DELETE FROM \"DateMentions\" WHERE \"KBDateId\" = ?";
	
	public static final String deleteEntityMentionById =
			"DELETE FROM \"EntityMentions\" WHERE \"ID\" = ?";
	
	public static final String deleteRelationMentionById =
			"DELETE FROM \"RelationMentions\" WHERE \"ID\" = ?";
	
	public static final String deleteExternalKbIdMapsForAdeptElement =
			"DELETE FROM \"ExternalKBReferences\" WHERE \"AdeptKBID\" = ?";
	
	public static final String deleteSingleExternalKbIdMapRecord =
			"DELETE FROM \"ExternalKBReferences\" WHERE \"AdeptKBID\" = ? "
			+ "AND \"ExternalID\" = ? AND \"ExternalKB\" = ?";
	
	public static final String deleteArgumentMentionByRelMentionId =
			"DELETE FROM \"Arguments\" WHERE \"relationMention\" = ?";
	
	public static final String insertSourceDocument =
			"INSERT INTO \"SourceDocuments\"(\"ID\", \"sourceLanguage\", \"corpus\", \"publicationDate\", \"URI\") "
			+ "values (?, ?, ?, ?, ?)";
	
	public static final String insertSourceAlgorithm =
			"INSERT INTO \"SourceAlgorithms\"(\"algorithmName\", \"contributingSiteName\") "
			+ "values (?, ?)";
	
	public static final String insertCorpus =
			"INSERT INTO \"Corpus\"(\"ID\", \"type\", \"name\", \"URI\") "
			+ "values (?, ?, ?, ?)";
	
	public static final String insertExternalKbIdMap =
			"INSERT INTO \"ExternalKBReferences\"(\"AdeptKBID\", \"ExternalID\", \"ExternalKB\") "
			+ "values (?,?,?)";
	
	public static final String checkEntityMentionExists =
			"SELECT \"EntityMentions\".\"ID\" FROM \"EntityMentions\" JOIN \"TextChunks\" ON "
			+ " \"EntityMentions\".\"chunk\" = \"TextChunks\".\"ID\" WHERE \"TextChunks\".\"sourceDocument\" = ?"
					+ " AND \"TextChunks\".\"beginOffset\" = ? AND \"TextChunks\".\"endOffset\" = ? AND "
			+ "\"TextChunks\".\"value\" = ? AND \"EntityMentions\".\"KBEntityId\" = ? AND \"EntityMentions\".\"confidence\" = ?";
    
	/**
	 * create SQL query to get entity mention records that resolve to the given KB entity ID.
	 */
	public static PreparedStatement createEntityMentionsByEntityIdQuery(String entityUri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(entityMentionsByEntityUri);
        preparedStatement.setString(1, entityUri);
        return preparedStatement;
	}
	
	/**
	 * create SQL query to entity mention record given the primary key ID.
	 */
	public static PreparedStatement createEntityMentionByIdQuery(String mentionId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(entityMentionById);
            preparedStatement.setString(1, mentionId);
            return preparedStatement;
        }
	
	 /**
	  * create SQL query to get text chunk given the primary key ID.
	  */
     public static PreparedStatement createTextChunkByIdQuery(String textChunkId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(textChunkById);
            preparedStatement.setString(1, textChunkId);
            return preparedStatement;
        }
	
	 /**
	  * create SQL query to get source document given primary key ID.
	  */
     public static PreparedStatement createSourceDocumentByIdQuery(String sourceDocumentId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sourceDocumentById);
            preparedStatement.setString(1, sourceDocumentId);
            return preparedStatement;
        }        
        
     /**
      * create SQL query to get source algorithm given primary key ID.
      */
     public static PreparedStatement createSourceAlgorithmByIdQuery(String sourceAlgorithmId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sourceAlgorithmByName);
            preparedStatement.setString(1, sourceAlgorithmId);
            return preparedStatement;
        }
        
     /**
      * create SQL query to get corpus record given primary key ID. 
      */
     public static PreparedStatement createCorpusIdQuery(String corpusId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(corpusById);
            preparedStatement.setString(1, corpusId);
            return preparedStatement;
        }   
        
     /**
      * create SQL query to get relation mentions that resolve to the given KB relation ID.
      */
     public static PreparedStatement createRelationMentionsByKBRelationIdQuery(String kbRelationId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(relationMentionsByKBRelationId);
            preparedStatement.setString(1, kbRelationId);
            return preparedStatement;
        }
        
     /**
      * create SQL qeury to get argument mentions that belong to a specific relation mention.
      */
     public static PreparedStatement createArgumentsByRelationMentionIdQuery(String relationMentionId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(argumentsByRelationMentionId);
            preparedStatement.setString(1, relationMentionId);
            return preparedStatement;
        }
        
     /**
      * create SQL query to get the KB Entity IDs that contain mentions within a given
      * span of text.
      */
     public static PreparedStatement createEntityIdsByChunkValueQuery(String chunkValue, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(entityIdsByChunkValue);
            preparedStatement.setString(1, chunkValue);
            return preparedStatement;
        }
     
     /**
      * create SQL query to get the KB Entity IDs that contain mentions within a given
      * span of text.
      */
     public static PreparedStatement createEntityIdsByChunkRegexQuery(String regex, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(entityIdsByChunkRegex);
            preparedStatement.setString(1, regex);
            return preparedStatement;
        }
     
     /**
      * create SQL query to get KB relation IDs that contain mentions within a given
      * span of text.
      */
     public static PreparedStatement createRelationIdsByChunkValueQuery(String chunkValue, Connection connection) throws SQLException
     {
         PreparedStatement preparedStatement = connection.prepareStatement(relationIdsByChunkValue);
         preparedStatement.setString(1, chunkValue);
         return preparedStatement;
     }
        
     /**
      * create SQL query to get the KB relation IDs given the KB-level argument IDs that participate
      * in them.
      */
     public static PreparedStatement createKBRelationIdByArgumentKBEntityIdQuery(String kbEntityId, Connection connection) throws SQLException
        {
            PreparedStatement preparedStatement = connection.prepareStatement(kbRelationIdByArgumentKbEntityId);
            preparedStatement.setString(1, kbEntityId);
            return preparedStatement;
        }

    
     /**
      * create SQL update statement to insert a new entity mention.
      */
	public static PreparedStatement createEntityMentionInsertQuery(String mentionId, String mentionType, String chunkId, String sourceAlgId, 
			String entityId, float confidence, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(insertEntityMention);
		
		preparedStatement.setString(1, mentionId);
		preparedStatement.setString(2, mentionType);
		preparedStatement.setString(3, chunkId);
		preparedStatement.setString(4, sourceAlgId);
		preparedStatement.setString(5, entityId);
		preparedStatement.setDouble(6, confidence);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to insert a new relation mention. 
	 */
	public static PreparedStatement createRelationMentionInsertQuery(String mentionId, String mentionType, String chunkId, String sourceAlgId, 
			String relationId, float confidence, Connection connection) throws SQLException
	{
        PreparedStatement preparedStatement = connection.prepareStatement(insertRelationMention);
		
		preparedStatement.setString(1, mentionId);
		preparedStatement.setString(2, mentionType);
		preparedStatement.setString(3, chunkId);
		preparedStatement.setString(4, sourceAlgId);
		preparedStatement.setString(5, relationId);
		preparedStatement.setDouble(6, confidence);
		
		return preparedStatement;
	}
	

	
	/**
	 * create SQL update statement to insert a new argument mention.
	 */
	public static PreparedStatement createArgumentEntityMentionInsertQuery(String argumentId, String argumentType, 
			String entityMentionId, String relationMentionId, double confidence, Connection connection) throws SQLException
	{
        PreparedStatement preparedStatement = connection.prepareStatement(insertArgumentEntityMention);
		
		preparedStatement.setString(1, argumentId);
		preparedStatement.setString(2, argumentType);
		preparedStatement.setString(3, entityMentionId);
		preparedStatement.setString(4, relationMentionId);
		preparedStatement.setDouble(5, confidence);
		
		return preparedStatement;
	}
	
	
	/**
	 * create SQL update statement to insert a new date mention. 
	 */
	public static PreparedStatement createDateMentionInsertQuery(String mentionId, String chunkId, String sourceAlgId, 
			String KbDateId, float confidence, Connection connection) throws SQLException
	{
        PreparedStatement preparedStatement = connection.prepareStatement(insertDateMention);
		
		preparedStatement.setString(1, mentionId);
		preparedStatement.setString(2, chunkId);
		preparedStatement.setString(3, KbDateId);
		preparedStatement.setDouble(4, confidence);
		preparedStatement.setString(5, sourceAlgId);
		
		return preparedStatement;
	}
	
	
	/**
	 * create SQL update statement to insert a new text chunk.
	 */
	public static PreparedStatement createTextChunkInsertQuery(String chunkId, String value, int beginOffset, int endOffset, String sourceDocument,
			String serializedTokenStream, Connection connection)
			throws SQLException
	{
        PreparedStatement preparedStatement = connection.prepareStatement(insertTextChunk);
		
		preparedStatement.setString(1, chunkId);
		preparedStatement.setString(2, value);
		preparedStatement.setInt(3, beginOffset);
		preparedStatement.setInt(4, endOffset);
		preparedStatement.setString(5, sourceDocument);
		preparedStatement.setString(6, serializedTokenStream);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if a text chunk already exists in the DB given its primary key ID.
	 */
	public static PreparedStatement doesChunkExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "TextChunks", "ID"));
		preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if an entity mention already exists in the DB given its primary key ID.
	 */
	public static PreparedStatement doesEntityMentionExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "EntityMentions", "ID"));
		preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if entity mention really exists in the SQL DB given its chunk info, and 
	 * the KB entity that it resolves to.
	 */
	public static PreparedStatement doesEntityMentionExistQuery(String documentId, int beginTokenOffset, int endTokenOffset, 
			String value, String kbEntityId, float confidence, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkEntityMentionExists));
		preparedStatement.setString(1, documentId);
		preparedStatement.setInt(2, beginTokenOffset);
		preparedStatement.setInt(3, endTokenOffset);
		preparedStatement.setString(4, value);
		preparedStatement.setString(5, kbEntityId);
		preparedStatement.setDouble(6, confidence);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if a relation mention already exists in the DB given its primary key ID.
	 */
	public static PreparedStatement doesRelationMentionExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "RelationMentions", "ID"));
        preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if a date mention already exists in the DB given its primary key ID.
	 */
	public static PreparedStatement doesDateMentionExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "DateMentions", "ID"));
        preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing entity mention given the KB entity ID that it
	 * resolves to.
	 */
	public static PreparedStatement createEntityMentionDeleteQuery(String entityUri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteEntityMention);
		preparedStatement.setString(1, entityUri);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing relation mention given its KB relation ID.
	 */
	public static PreparedStatement createRelationMentionDeleteQuery(String relationUri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteRelationMention);
		preparedStatement.setString(1, relationUri);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing date mention given its KB Date ID.
	 */
	public static PreparedStatement createDateMentionDeleteQuery(String dateUri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDateMention);
		preparedStatement.setString(1, dateUri);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing entity mention
	 * given its primary key ID.
	 */
	public static PreparedStatement createDeleteEntityMentionByIdQuery(String entityMentionId, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteEntityMentionById);
		preparedStatement.setString(1, entityMentionId);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing relation mention given its primary key ID.
	 */
	public static PreparedStatement createDeleteRelationMentionByIdQuery(String relationMentionId, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteRelationMentionById);
		preparedStatement.setString(1, relationMentionId);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to delete an existing argument mention given its relation
	 * mention ID.
	 */
	public static PreparedStatement createDeleteArgumentMentionByRelationMentionIdQuery(String relMentionId, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteArgumentMentionByRelMentionId);
		preparedStatement.setString(1, relMentionId);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if a source document already exists.
	 */
	public static PreparedStatement doesSourceDocExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "SourceDocuments", "ID"));
        preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to insert a new source document.
	 */
	public static PreparedStatement createSourceDocumentInsertQuery(String sourceDocId, String sourceDocUri, String language, String corpusName, Date pubDate, Connection connection)
			throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(insertSourceDocument);
		preparedStatement.setString(1, sourceDocId);
		preparedStatement.setString(2, language);
		preparedStatement.setString(3, corpusName);
		preparedStatement.setDate(4, pubDate);
		preparedStatement.setString(5, sourceDocUri);
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if a source algorithm already exists in the DB.
	 */
	public static PreparedStatement doesSourceAlgorithmExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "SourceAlgorithms", "algorithmName"));
        preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to insert a new source algorithm.
	 */
	public static PreparedStatement createSourceAlgorithmInsertQuery(String algName, String site, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(insertSourceAlgorithm);
		preparedStatement.setString(1, algName);
		preparedStatement.setString(2, site);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if corpus already exists in the DB.
	 */
	public static PreparedStatement doesCorpusExistQuery(String ID, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(String.format(checkExists, "Corpus", "ID"));
        preparedStatement.setString(1, ID);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL update statement to insert new corpus.
	 */
	public static PreparedStatement createCorpusInsertQuery(String ID, String type, String name, String uri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(insertCorpus);
		preparedStatement.setString(1, ID);
		preparedStatement.setString(2, type);
		preparedStatement.setString(3, name);
		preparedStatement.setString(4, uri);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to get KB entity IDs that exist in a given text span.
	 */
	public static PreparedStatement createGetEntityIdsByChunkQuery(String sourceDocId, int beginOffset, int endOffset, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(getEntityIdsWithinChunkOffset);
		preparedStatement.setString(1, sourceDocId);
		preparedStatement.setInt(2, beginOffset);
		preparedStatement.setInt(3, endOffset);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to get KB relation IDs that exist in a given text span.
	 */
	public static PreparedStatement createGetRelationIdsByChunkQuery(String sourceDocId, int beginOffset, int endOffset, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(getRelationIdsWithinChunkOffset);
		preparedStatement.setString(1, sourceDocId);
		preparedStatement.setInt(2, beginOffset);
		preparedStatement.setInt(3, endOffset);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to check if an external KB element is already mapped to an
	 * existing Adept KB element with any amount of confidence.
	 */
	public static PreparedStatement createdoesExternalKBIdExistQuery(String externalKbUri, String externalKbName, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(doesExternalKbIdExist);
		preparedStatement.setString(1, externalKbUri);
		preparedStatement.setString(2, externalKbName);
		
		return preparedStatement;
	}
	
	/**
	 * create SQL query to add a new entry into the external KB references table in the
	 * metadata store.
	 */
	public static PreparedStatement createInsertMappingToExternalKBQuery(String adeptKbElementId, String externalKbElementId,
			String externalKbName, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(insertExternalKbIdMap);
		preparedStatement.setString(1, adeptKbElementId);
		preparedStatement.setString(2, externalKbElementId);
		preparedStatement.setString(3, externalKbName);
		
		return preparedStatement;
	}
	
	/**
	 * delete all records concerning a specified Adept KB entity
	 * from the external KB references table.
	 */
	public static PreparedStatement createDeleteExternalKBIdMapsForAdeptElementQuery(String adeptKbUri, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteExternalKbIdMapsForAdeptElement);
		preparedStatement.setString(1, adeptKbUri);
		
		return preparedStatement;
	}
	
	/**
	 * delete a specific record in the external KB references table.
	 */
	public static PreparedStatement createDeleteSingleExternalKBIdMapRecordQuery(String adeptUri, String externalUri, String externalKb, Connection connection)
	throws SQLException 
	{
		PreparedStatement preparedStatement = connection.prepareStatement(deleteSingleExternalKbIdMapRecord);
		preparedStatement.setString(1, adeptUri);
		preparedStatement.setString(2, externalUri);
		preparedStatement.setString(3, externalKb);
		
		return preparedStatement;
	}
	
	/**
	 * get external KB IDs that the given Adept KB ID maps to.
	 */
    public static PreparedStatement getExternalKBIDsQuery(String adeptKbUri, Connection connection) throws SQLException
    {
    	PreparedStatement preparedStatement = connection.prepareStatement(getExternalKbIds);
		preparedStatement.setString(1, adeptKbUri);
		
		return preparedStatement;
    }
    
    /** get Adept KB ID that an external KBID maps to */
    public static PreparedStatement getAdeptIdByExternalIdQuery(String externalId, String externalKbName, Connection connection) throws SQLException
    {
    	PreparedStatement preparedStatement = connection.prepareStatement(getAdeptIdByExternalId);
		preparedStatement.setString(1, externalId);
		preparedStatement.setString(2, externalKbName);
		return preparedStatement;
    }
	
}