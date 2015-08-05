package adept.kbapi.sparql;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.query.*;

import adept.common.*;
import adept.io.Reader;
import adept.kbapi.KBParameters;

import java.util.*;


public class SparqlQueryBuilder {
	
        public static final String SUBJECT = "?subject";
        public static final String PREDICATE = "?predicate";
        public static final String OBJECT = "?object";
        
        
        private static final String GET_REIFIED_STMT_ID_BY_SUBJECT_AND_PREDICATE =
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
    			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
    			        + "SELECT ?subject "
    			        + "WHERE {?subject rdf:subject adept-data:%s  ."
    			        + "?subject rdf:type rdf:Statement ."
    			        + "?subject rdf:predicate %s}";
        
        private static final String GET_REIFIED_STMT_ID_BY_SUBJECT_AND_OBJECT =
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
    			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
    			        + "SELECT ?subject "
    			        + "WHERE {?subject rdf:subject adept-data:%s  ."
    			        + "?subject rdf:type rdf:Statement ."
    			        + "?subject rdf:object %s}";
        
        private static final String GET_REIFIED_STMT_ID =
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
    			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
    			        + "SELECT ?subject "
    			        + "WHERE {?subject rdf:subject adept-data:%s  ."
    			        + "?subject rdf:predicate %s ."
    			        + "?subject rdf:type rdf:Statement ."
    			        + "?subject rdf:object %s}";
        
        
        private static final String GET_RELATION_CONFIDENCE =
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
    			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
    			        + "SELECT ?object "
    			        + "WHERE {adept-data:%s adept-base:confidence ?object }";
        
        private static final String GET_ARGUMENT_CONFIDENCE =
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
    			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
    			        + "SELECT ?object "
    			        + " WHERE {?subject rdf:subject adept-data:%s ."
    			        + "?subject rdf:object adept-data:%s ."
    			        + "?subject rdf:predicate adept-kb:%s ."
    			        + "?subject adept-base:confidence ?object}"
    			        + "ORDER BY DESC(?object) LIMIT 1";
    
        private static final String QUERY_TRIPLES_BY_SUBJECT_URI = 
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                            + "SELECT ?predicate ?object "
                            + "WHERE { adept-data:%s ?predicate ?object }";
        
        private static final String QUERY_SUBJECTS_BY_TYPE = 
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                            + "SELECT ?subject "
                            + "WHERE { ?subject rdf:type adept-kb:%s }";
        
        private static final String QUERY_TRIPLES_BY_OBJECT_URI = 
        		"PREFIX adept-base: <%s> "
        		        + "PREFIX adept-kb: <%s> "
    			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                            + "SELECT ?subject ?predicate "
                            + "WHERE { ?subject ?predicate adept-data:%s }";
	
	/** Templates related to entity insertion */
	private static final String INSERT_ENTITY_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "INSERT DATA"
		            + "{ adept-data:%s    adept-base:canonicalMention  " + "\"%s\" ;"
		            + "                         adept-base:canonicalString  " + "\"%s\"  ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";


	
	private static final String TYPE_REIFICATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "INSERT DATA"
			        + "{ adept-data:%s    rdf:type  adept-kb:%s  ." + "} ; "
					+ "INSERT DATA"
			        + "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " + "adept-kb:%s  ;"
					+ "                         rdf:predicate  " + "rdf:type ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	private static final String CANONICALMENTION_REIFICATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "INSERT DATA"
			        + "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " + "\"%s\"  ;"
					+ "                         rdf:predicate  " + "adept-base:canonicalMention ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";

	
	/** Templates related to KB entity update */
	private static String UPDATE_ENTITY_CONFIDENCE_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "DELETE WHERE"
		            + "{ adept-data:%s    adept-base:confidence  " +  "?o" + " ." + "}  ; "
		            + "INSERT DATA "
		            + "{ adept-data:%s    adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";

	
	private static final String UPDATE_CANONICAL_MENTION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "DELETE WHERE"
	                + "{ adept-data:%s    adept-base:canonicalMention    ?o1   ;"
	                + "                   adept-base:canonicalString    ?o2   ." + "} ;"
					+ "DELETE WHERE"
			        + "{ ?s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
					+ "                         rdf:object  " + "?o ;"
					+ "                         adept-base:confidence  " + "?c ;"
					+ "                         rdf:predicate  " + "adept-base:canonicalMention ." +  "};   "
					+ "INSERT DATA"
		            + "{ adept-data:%s    adept-base:canonicalMention  " + "\"%s\" ;"
		            + "                   adept-base:canonicalString  " + "\"%s\"  ." + "} ; "
		            + "INSERT DATA"
		            + "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " + "\"%s\"  ;"
					+ "                         rdf:predicate  " + "adept-base:canonicalMention ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	
	private static final String UPDATE_ENTITY_TYPE_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "DELETE WHERE"
			        + "{ ?s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
			        + "                         rdf:object   " + "adept-kb:%s ;"
					+ "                         rdf:predicate  " + "rdf:type ;"
			        + "                         adept-base:confidence  " +  "?o" + " ." + "} ;  "
					+ "INSERT DATA"
		            + "{ adept-data:%s    rdf:type  " + "adept-kb:%s . };"
		            + "INSERT DATA"
		            + "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " + "adept-kb:%s  ;"
					+ "                         rdf:predicate  " + "rdf:type ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	/** Templates related to insertion of KB relations */
	private static final String INSERT_RELATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "INSERT DATA"
		            + "{ adept-data:%s    rdf:type      adept-kb:%s  ;"
		            + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	private static final String INSERT_ARGUMENT_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "INSERT DATA"
			        + "{ adept-data:%s    adept-kb:%s    adept-data:%s    ." + "}   ";
	
	
	private static final String INSERT_NUMBER_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "INSERT DATA"
			        + "{ adept-data:%s    rdf:type      adept-base:Number  ;"
		            + "                         rdf:value  " +  "\"%s\"" + " ." + "}   ";
	
	private static final String INSERT_DATE_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "INSERT DATA"
			        + "{ adept-data:%s    rdf:type      adept-base:Date  ;"
			        + "                         adept-base:xsdDate  " +  "\"%s\"^^xsd:date ;"
					+ "                         adept-base:xsdDuration  " + "\"%s\"^^xsd:duration ;"
		            + "                         adept-base:timex2String  " +  "\"%s\"" + " ." + "}   ";

	
	
	private static final String ARGUMENT_REIFICATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "INSERT DATA"
			        + "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " +  "adept-data:%s ;"
					+ "                         rdf:predicate  " + "adept-kb:%s ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	
	
	/** Template to update relation confidence */
	private static final String UPDATE_RELATION_CONFIDENCE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "DELETE WHERE"
		            + "{ adept-data:%s    adept-base:confidence  " +  "?o" + " ." + "}  ; "
		            + "INSERT DATA "
		            + "{ adept-data:%s    adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	private static final String UPDATE_ARGUMENT_CONFIDENCE_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "DELETE WHERE"
		            + "{ adept-data:%s    adept-base:confidence  " +  "?o" + " ." + "}  ; "
		            + "INSERT DATA "
		            + "{ adept-data:%s    adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
    /** Templates related to KB relation triples deletion */
	private static final String DELETE_RELATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		            + "DELETE WHERE"
		            + "{ adept-data:%s    rdf:type      adept-kb:%s  ;"
		            + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";
	
	private static final String DELETE_ARGUMENT_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			        + "DELETE WHERE"
			        + "{ adept-data:%s    adept-kb:%s    adept-data:%s    ." + "}   ";

	
	
	private static final String DELETE_ARGUMENT_REIFICATION_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "DELETE WHERE"
			        + "{ ?s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;" 
	                + "                         rdf:object  " +  "adept-data:%s ;"
					+ "                         rdf:predicate  " + "adept-kb:%s ;" 
	                + "                         adept-base:confidence  " +  "\"%s\"^^xsd:float" + " ." + "}   ";

	
	/** Templates related to deletion of KB objects by KB ID only*/
	private static final String DELETE_KBOBJECT_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "DELETE WHERE {adept-data:%s ?p ?o}";
	
	private static final String DELETE_REIFIEDSTATEMENT_TEMPLATE =
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "DELETE WHERE { ?person rdf:type rdf:Statement;"
			        + "                      rdf:subject     adept-data:%s ; "
					+ "                       ?property      ?value }";
	
	
	private static final String DOES_OBJECT_EXIST = 
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "SELECT * {adept-data:%s ?predicate ?object}";
	
	private static final String DOES_RELATION_CONTAIN_ARGUMENT = 
			"PREFIX adept-base: <%s> "
    		        + "PREFIX adept-kb: <%s> "
			        + "PREFIX adept-data: <%s> "
			        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
					+ "SELECT * {adept-data:%s adept-kb:%s adept-data:%s}";
	
	
	/*** Query  builder methods **/	
     
	/**
	 * Create query to get confidence value associated with a relation argument in the triple store.
	 */
	public static Query createGetArgumentConfidenceQuery(String relationId, String argumentId, String argumentType)
	{
		return QueryFactory.create(String.format(GET_ARGUMENT_CONFIDENCE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
				relationId, argumentId, argumentType));
	}
	
	/**
	 * 
	 */
	public static Query createGetRelationConfidenceQuery(String relationId)
	{
		return QueryFactory.create(String.format(GET_RELATION_CONFIDENCE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
				relationId));
	}
	
    /**
     * 
     * Create query to get ID of the statement(s) that contains given subject and predicate values
     */
	public static Query createGetReifiedStmtIdBySubjectAndPredicateQuery(String kbUri, String predicate)
	{
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID_BY_SUBJECT_AND_PREDICATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
				kbUri, predicate));
	}
	
	/**
     * 
     * Create query to get ID of the statement(s) that contains given subject and object values
     */
	public static Query createGetReifiedStmtIdBySubjectAndObjectQuery(String kbUri, String object)
	{
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID_BY_SUBJECT_AND_OBJECT, 
				KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
				kbUri, object));
	}
	
	
	 /**
     * 
     * Create query to get ID of the statement(s) that contains given subject, predicate and object values
     */
	public static Query createGetReifiedStmtIdQuery(String kbUri, String predicate, String object)
	{
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
				kbUri, predicate, object));
	}
	
	
	
	   /**
	    * Create query to get all triples about a given subject.
	    * 
	    */
        public static Query createGetTriplesBySubjectURIQuery(String kbUri) 
        {
            return QueryFactory.create(String.format(QUERY_TRIPLES_BY_SUBJECT_URI, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
            		SparqlUtils.escape(kbUri)));
        }
        
        /**
	     * create query to get all KB elements having a given type.
	     * 
	     */
        public static Query createGetSubjectsByTypeQuery(String type) 
        {
            return QueryFactory.create(String.format(QUERY_SUBJECTS_BY_TYPE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
            		SparqlUtils.escape(type)));
        }
        
        /**
	     * 
	     * create query to get all KB elements having a given object value.
	     */
        public static Query createGetTriplesByObjectURIQuery(String kbUri) 
        {
            return QueryFactory.create(String.format(QUERY_TRIPLES_BY_OBJECT_URI, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
            		SparqlUtils.escape(kbUri)));
        }
	
	/**
	 * create query to check if a given KB element exists
	 * on the triple store.
	 * 
	 */
	public static Query createDoesKBObjectExistQuery(String kbUri)
	{
		return QueryFactory.create(String.format(DOES_OBJECT_EXIST, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				SparqlUtils.escape(kbUri)));
	}
	
	
	/**
	 * create query to check if an existing KB relation
	 * contains a given argument.
	 */
	public static Query createDoesRelationContainArgumentQuery(String relationUri, String argumentUri, String argumentType)
	{
		return QueryFactory.create(String.format(DOES_RELATION_CONTAIN_ARGUMENT, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				SparqlUtils.escape(relationUri), SparqlUtils.escape(argumentType), SparqlUtils.escape(argumentUri)));
	}
	
	
	
	/**
	 * create update statement to insert new entity into the KB.
	 * 
	 * This method is also responsible for checking that the entity type(s)
	 * are valid Adept ontology types.
	 * 
	 */
	public static UpdateRequest createEntityInsertQueries(Entity entity, String entityId, double entityConfidence,
			double canonicalMentionConfidence)
	{

	   String canonicalMentionSqlId = entity.getCanonicalMention().getId().toString();
	   UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_ENTITY_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
			   SparqlUtils.escape(entityId), SparqlUtils.escape(entity.getCanonicalMention().getId().toString()), SparqlUtils.escape(entity.getCanonicalMention().getValue()), 
			   entityConfidence));
	   
	   for(IType itype : entity.getAllTypes().keySet())
	   {
		   String type = itype.getType();
		   if(KBParameters.ontologyMap.containsKey(type.toLowerCase()))
			   type = KBParameters.ontologyMap.get(type.toLowerCase());
		   
		   
		   if(!KBParameters.adeptTypes.containsValue(type))
		   {
			   throw new RuntimeException("Invalid entity type: " + type + ". Entity insertion failed.");
		   }
		   
		   String typeStmtId = UUID.randomUUID().toString();
		   updateRequest.add(String.format(TYPE_REIFICATION_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				   SparqlUtils.escape(entityId), SparqlUtils.escape(type), SparqlUtils.escape(typeStmtId), 
				   SparqlUtils.escape(entityId), SparqlUtils.escape(type), entity.getAllTypes().get(itype)));
	   }
	   
	   String mentionStmtId = UUID.randomUUID().toString();
	   updateRequest.add(String.format(CANONICALMENTION_REIFICATION_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
			   SparqlUtils.escape(mentionStmtId), SparqlUtils.escape(entityId), 
			   SparqlUtils.escape(canonicalMentionSqlId), canonicalMentionConfidence));
	   
	   return updateRequest;
	}
	
	
	/**
	 * 
	 * create statements to update an existing KB entity.
	 * Entity confidence, canonical mention and canonical string value
	 * are overwritten. If the updated entity has a new type associated with it,
	 * the type is appended to the existing types in the triple store
	 * with appropriate confidence. If the confidence of an existing type is different 
	 * in the updated entity, that confidence is overwritten.\
	 * 
	 * If the updated entity conatins at least one type that does not
	 * map to a valid Adept ontology type, the update fails.
	 */
	public static UpdateRequest createEntityUpdateQueries(Entity entity, String entityId, double entityConfidence, double typeConfidence, 
			double canonicalMentionConfidence)
	{

		String canonicalMentionSqlId = entity.getCanonicalMention().getId().toString();
		
		// overwrite entity confidence
		UpdateRequest updateRequest = UpdateFactory.create(String.format(UPDATE_ENTITY_CONFIDENCE_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				   SparqlUtils.escape(entityId),  SparqlUtils.escape(entityId), entityConfidence, SparqlUtils.escape(entityId)));
		
		// overwrite canonical mention, canonical string, canonical mention confidence
		String mentionStmtId = UUID.randomUUID().toString();
		updateRequest.add(String.format(UPDATE_CANONICAL_MENTION_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				SparqlUtils.escape(entityId), SparqlUtils.escape(entityId), SparqlUtils.escape(entityId), 
				SparqlUtils.escape(entity.getCanonicalMention().getId().toString()), SparqlUtils.escape(entity.getCanonicalMention().getValue()), 
				mentionStmtId, SparqlUtils.escape(entityId), SparqlUtils.escape(entity.getCanonicalMention().getId().toString()), 
				canonicalMentionConfidence));
		
		// if type is not same as existing type, add new type and type confidence. Else overwrite typeconfidence
		for(IType itype : entity.getAllTypes().keySet())
		   {
			   String type = itype.getType();
			   if(KBParameters.ontologyMap.containsKey(type.toLowerCase()))
				   type = KBParameters.ontologyMap.get(type.toLowerCase());
			   
			   if(!KBParameters.adeptTypes.containsValue(type))
			   {
				   throw new RuntimeException("Invalid entity type: " + type + ". Entity insertion failed.");
			   }
			   
			   String typeStmtId = UUID.randomUUID().toString();
			    updateRequest.add(String.format(UPDATE_ENTITY_TYPE_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
						SparqlUtils.escape(entityId), SparqlUtils.escape(type), SparqlUtils.escape(entityId), SparqlUtils.escape(type),
		                typeStmtId, SparqlUtils.escape(entityId), SparqlUtils.escape(type), entity.getAllTypes().get(itype)));
		   }
		
		return updateRequest;
		
	}
	
	
	/**
	 * 
	 * create statements to insert a new KB relation.
	 * 
	 * This method is responsible for checking that the type
	 * of the relation being inserted is a valid Adept ontology
	 * type. If not, fail. 
	 */
	public static UpdateRequest createRelationInsertQueries(DocumentRelation documentRelation, String relationId)
	{
		String type = documentRelation.getRelationType().getType();
		if(KBParameters.ontologyMap.containsKey(type.toLowerCase()))
			   type = KBParameters.ontologyMap.get(type.toLowerCase());
		if(!KBParameters.adeptTypes.containsValue(type))
		{
		    throw new RuntimeException("Invalid relation type: " + type + ". Relation insertion failed.");
		}

	   UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_RELATION_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix,
			   SparqlUtils.escape(relationId), SparqlUtils.escape(type), documentRelation.getConfidence()));
	   
	   return updateRequest;
	}
	
	
	/**
	 * create statements to insert arguments corresponding to a KB relation
	 * into the triple store.
	 * 
	 * This method is responsible for checking that the argument type
	 * being inserted is (or maps to) a valid Adept ontology type.
	 */
	public static UpdateRequest createArgumentInsertQueries(String type, double argumentConfidence, String argumentKbUri, String relationId)
	{

		if(KBParameters.ontologyMap.containsKey(type.toLowerCase()))
			   type = KBParameters.ontologyMap.get(type.toLowerCase());
		if(!KBParameters.adeptTypes.containsValue(type))
		{
		    throw new RuntimeException("Invalid arg type: " + type + ". Argument insertion failed.");
		}
		
	   UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_ARGUMENT_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
			   SparqlUtils.escape(relationId), SparqlUtils.escape(type), SparqlUtils.escape(argumentKbUri)));   

	   String argumentStmtId = UUID.randomUUID().toString();
	   updateRequest.add(String.format(ARGUMENT_REIFICATION_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
			   SparqlUtils.escape(argumentStmtId), SparqlUtils.escape(relationId), SparqlUtils.escape(argumentKbUri), 
			   SparqlUtils.escape(type), Double.toString(argumentConfidence)));

		return updateRequest;
	}
	
	
	/**
	 * 
	 * create statements to update argument confidence of an existing KB relation.
	 */
	public static UpdateRequest createArgumentConfidenceUpdateQueries(String stmtId, float confidence)
	{

	   UpdateRequest updateRequest = UpdateFactory.create(String.format(UPDATE_ARGUMENT_CONFIDENCE_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
			   stmtId, stmtId, confidence));   

		return updateRequest;
	}
	
	
	/**
	 * 
	 * create statements to insert a number type element into the KB.
	 */
	public static UpdateRequest createNumberInsertQueries(String numberId, String numberString)
	{
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_NUMBER_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				   SparqlUtils.escape(numberId), SparqlUtils.escape(numberString))); 
		
		return updateRequest;
	}
	
	/**
	 * 
	 * create statements to insert a Date type element into the KB.
	 */
	public static UpdateRequest createDateInsertQueries(String dateId, String dateString, String xsdDate, String xsdDuration)
	{
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_DATE_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				   SparqlUtils.escape(dateId), xsdDate!=null?SparqlUtils.escape(xsdDate):"null", 
						   xsdDuration!=null?SparqlUtils.escape(xsdDuration):"null", SparqlUtils.escape(dateString))); 
		
		return updateRequest;
	}

	
	/**
	 * 
	 * create delete KB element queries
	 */
	public static UpdateRequest createDeleteQueries(String kbUri)
	{
		UpdateRequest updateRequest = UpdateFactory.create(String.format(DELETE_KBOBJECT_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				SparqlUtils.escape(kbUri)));
		updateRequest.add(String.format(DELETE_REIFIEDSTATEMENT_TEMPLATE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, SparqlUtils.escape(kbUri)));
		
		return updateRequest;
	}
	
	/**
	 * 
	 * create query to update the confidence value on an existing relation.
	 */
	public static UpdateRequest createRelationConfidenceUpdateQuery(String kbUri, float confidence)
	{
		UpdateRequest updateRequest = UpdateFactory.create(String.format(UPDATE_RELATION_CONFIDENCE, KBParameters.ontologyBasePrefix, KBParameters.ontologyCorePrefix, KBParameters.dataInstancesPrefix, 
				SparqlUtils.escape(kbUri), SparqlUtils.escape(kbUri), confidence));
		
		return updateRequest;
	}
		
}