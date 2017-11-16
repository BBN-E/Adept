package adept.kbapi.sparql;

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
import com.hp.hpl.jena.graph.impl.LiteralLabel;
import com.hp.hpl.jena.graph.impl.LiteralLabelFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

import java.util.List;
import java.util.UUID;

import adept.common.DocumentMentalState;
import adept.common.DocumentSentiment;
import adept.common.IType;
import adept.common.KBID;
import adept.common.OntType;
import adept.kbapi.KBEntity;
import adept.kbapi.KBGenericThing;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBQueryException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SparqlQueryBuilder {

	public static final String SUBJECT = "?subject";
	public static final String PREDICATE = "?predicate";
	public static final String OBJECT = "?object";
	public static final String VALUE = "?value";
	public static final String XSD_VALUE = "?xsdValue";
	public static final String TIMEX_VALUE = "?timexValue";
	public static final String BEGIN_DATE = "?beginDate";
	public static final String END_DATE = "?endDate";

	public SparqlQueryBuilder()
	{
	}

	public static final String PREFIXES = "PREFIX adept-base: <" + KBOntologyModel.ONTOLOGY_BASE_PREFIX + "> " +
			"PREFIX adept-core: <" + KBOntologyModel.ONTOLOGY_CORE_PREFIX + "> " +
			"PREFIX adept-data: <" + KBOntologyModel.DATA_INSTANCES_PREFIX + "> " +
			"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ";

	private static final String GET_LEAF_TYPE =
			PREFIXES +
					"SELECT DISTINCT " +
					"?type " +
					"WHERE { " +
					"   adept-data:%s a ?type . " +
					"   { ?type rdfs:subClassOf adept-base:PredicateArgument } " +
					"   UNION " +
					"	{ adept-data:%1$s a rdf:Statement} " +
					"    FILTER NOT EXISTS{ " +
					"        ?subClass rdfs:subClassOf ?type ." +
					"		 adept-data:%1$s a ?subClass . " +
					"        FILTER (?subClass != ?type) " +
					"    } " +
					"} ";

	private static final String GET_IDS_BY_TYPE =
			PREFIXES +
					"SELECT ?id " +
					"WHERE { " +
					"  ?id a %s " +
					"  %s " +
					"} ";

	private static final String MINUS_IDS_BY_TYPE =
					"  MINUS { " +
					"    ?id a %s " +
					"  } ";

	private static final String GET_REIFIED_STMT_ID_BY_SUBJECT_AND_PREDICATE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE {?subject rdf:subject adept-data:%s  ."
					+ "?subject rdf:type rdf:Statement ."
					+ "?subject rdf:predicate %s}";

	private static final String GET_REIFIED_STMT_ID_BY_SUBJECT_AND_OBJECT =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE {?subject rdf:subject adept-data:%s  ."
					+ "?subject rdf:type rdf:Statement ."
					+ "?subject rdf:object %s}";

	private static final String GET_REIFIED_STMT_ID =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE {?subject rdf:subject adept-data:%s  ."
					+ "?subject rdf:predicate %s ."
					+ "?subject rdf:type rdf:Statement ."
					+ "?subject rdf:object %s}";

	private static final String GET_RELATION_CONFIDENCE =
			PREFIXES
					+ "SELECT ?object "
					+ "WHERE {adept-data:%s adept-base:confidence ?object }";

	private static final String GET_ARGUMENT_CONFIDENCE =
			PREFIXES
					+ "SELECT ?object "
					+ " WHERE {?subject rdf:subject adept-data:%s ."
					+ "?subject rdf:object adept-data:%s ."
					+ "?subject rdf:predicate adept-core:%s ."
					+ "?subject adept-base:confidence ?object}"
					+ "ORDER BY DESC(?object) LIMIT 1";

	private static final String GET_TYPE_SUBCLASSES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject ?predicate " +
					"WHERE { " +
					"   ?subject a owl:Class ; " +
					"        rdfs:subClassOf adept-base:Thing . " +
					"   ?predicate rdfs:subClassOf ?subject . " +
					"   FILTER (!isblank(?subject) && !isblank(?predicate)) " +
					"} ";

	private static final String GET_CLASSES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject  " +
					"WHERE { " +
					"   ?subject a owl:Class " +
					"   FILTER (!isblank(?subject)) " +
					"} ";

	private static final String GET_ROLES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject  " +
					"WHERE { " +
					"   ?subject a owl:ObjectProperty " +
					"   FILTER (!isblank(?subject)) " +
					"} ";

	private static final String GET_ENTITY_TYPES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject " +
					"WHERE { " +
					"   ?subject rdfs:subClassOf adept-base:Thing . " +
					"   FILTER(!isblank(?subject)) " +
//					"   FILTER(?subject != adept-base:Number && ?subject != adept-base:Date) " +
					"} ";

	private static final String GET_GENERIC_THING_TYPES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject " +
					"WHERE { " +
					"   ?subject rdfs:subClassOf adept-base:GenericThing . " +
					"   FILTER(!isblank(?subject)) " +
					"   FILTER(?subject != adept-base:GenericThing) " +
					"} ";

	private static final String GET_LEAF_RELATION_TYPES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject ?subClass" +
					"WHERE { " +
					"   ?subject rdfs:subClassOf adept-base:Relation . " +
					"	FILTER NOT EXISTS{ " +
					"		?subject rdfs:subClassOf adept-base:Event " +
					"	} " +
					"	FILTER NOT EXISTS{ " +
					"		?subClass rdfs:subClassOf ?subject " +
					"		FILTER (?subClass != ?subject) " +
					"	} " +
					"   FILTER(!isblank(?subject)) " +
					"   FILTER(?subject != adept-core:Belief && ?subject != adept-core:Sentiment) " +
					"} ";

	private static final String GET_LEAF_EVENT_TYPES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject " +
					"WHERE { " +
					"   ?subject rdfs:subClassOf adept-base:Event . " +
					"   FILTER(!isblank(?subject)) " +
					"} ";

	private static final String GET_STRENGTH_ARGUMENT_CONFIDENCE =
			PREFIXES
					+ "SELECT ?object "
					+ " WHERE {?subject rdf:subject adept-data:%s ."
					+ "?subject rdf:object \"%s\"^^xsd:integer ."
					+ "?subject rdf:predicate adept-core:%s ."
					+ "?subject adept-base:confidence ?object}"
					+ "ORDER BY DESC(?object) LIMIT 1";

	private static final String GET_RELATION_ID_BY_RELATION_ARGUMENT_ID =
			PREFIXES
					+ "SELECT ?relationID ?type"
					+ " WHERE {adept-data:%s rdf:subject ?relationID . "
					+ "?relationID a ?type . "
					+ "    ?type rdfs:subClassOf adept-base:PredicateArgument  "
 					+ "    FILTER NOT EXISTS{ "
					+ "        ?subClass rdfs:subClassOf ?type "
					+ "        FILTER (?subClass != ?type) "
					+ "    } "
					+ "}";

	private static final String GET_EVENT_REALIS_TYPES =
			PREFIXES
					+ "SELECT ?type ?confidence "
					+ " WHERE {?subject rdf:subject adept-data:%s ."
					+ "?subject rdf:object ?type ."
					+ "?subject rdf:predicate rdf:type ."
					+ "?subject adept-base:confidence ?confidence}";

	private static final String GET_RELATION_TYPE_TUPLES =
			PREFIXES +
					"SELECT DISTINCT " +
					"?subject ?predicate ?object " +
					"WHERE " +
					"{ ?subject a owl:Class ; " +
					"     rdfs:subClassOf [ " +
					"        rdf:type owl:Restriction ; " +
					"        owl:onProperty ?predicate ; " +
					"        owl:allValuesFrom ?object " +
					"     ] . " +
					"  FILTER (!(?subject = adept-core:ParentChildRelationship && ?predicate=adept-core:person)) "+
					"  FILTER (?subject != adept-core:Sentiment && ?subject != adept-core:Belief && ?subject != adept-base:Event) " +
					"  FILTER (?predicate != adept-base:validTemporalSpan) " +
					"} " +
					"ORDER BY ?subject";

	private static final String ARGUMENT_QUERY_BLOCK = "{ " +
			"    #Get the argument bits, if it's an entity \n" +
			"    ?argument adept-base:canonicalMention ?entityCanonicalMention; " +
			"		 adept-base:canonicalString ?entityCanonicalString; " +
			"        adept-base:confidence ?entityconfidence; " +
			"        a ?entityType . " +
			"	 OPTIONAL {"+
			"		?argument adept-base:provenanceCount "
			+ "?provenanceCount .} OPTIONAL {?argument "
			+ "adept-base:documentCount ?documentCount ."
			+ " } " +
			"    ?entityTypeStatement a rdf:Statement; " +
			"        rdf:subject ?argument; " +
			"        rdf:predicate rdf:type; " +
			"        rdf:object ?entityType; " +
			"        adept-base:confidence ?entityTypeConfidence . " +
			"    ?entityCanonicalMention " +
			"        adept-base:confidence ?canonicalMentionConfidence . " +
			"}UNION{ " +
			"    # Get the argument bits, if ?argument is a a RelationArgument \n" +
			"    ?argument a rdf:Statement; " +
			"      adept-base:confidence ?relationArgumentConfidence; " +
			"      rdf:predicate ?relationArgumentType; " +
			"      rdf:object ?relationArgumentStatement . " +
			"}UNION{ " +
			"    # Get the argument bits, if ?argument is a number \n" +
			"    ?argument a adept-base:Number; " +
			"        rdf:value ?numberValue " +
			"}UNION { " +
			"    # Get the argument bits, if ?argument is a temporal span \n" +
			"    ?argument a adept-base:TemporalSpan . " +
			"    OPTIONAL { " +
			"        ?beginDateStatement rdf:subject ?argument; " +
			"        	rdf:type rdf:Statement; " +
			"        	rdf:predicate adept-base:beginDate; " +
			"        	rdf:object ?beginDate . " +
			"		 ?beginDate adept-base:timex2String ?beginDateValue . " +
			"	} " +
			"    OPTIONAL { " +
			"        ?endDateStatement rdf:subject ?argument; " +
			"        	rdf:type rdf:Statement; " +
			"        	rdf:predicate adept-base:endDate; " +
			"        	rdf:object ?endDate . " +
			"		 ?endDate adept-base:timex2String ?endDateValue . " +
			"	} " +
			"}UNION { " +
			"    # Get the argument bits, if ?argument is a date \n" +
			"    ?argument a adept-base:Date; " +
			"      	adept-base:timex2String ?date " +
			"}UNION { " +
			"    # Get the argument bits, if ?argument is a relation \n" +
			"    ?argument a ?argumentRelationType . " +
			"    ?argumentRelationType rdfs:subClassOf adept-base:Relation . " +
			"      FILTER NOT EXISTS { " +
			"       ?argumentRelationTypeSubClass rdfs:subClassOf ?argumentRelationType . " +
			"       ?argument rdf:type ?argumentRelationTypeSubClass " +
			"       FILTER (?argumentRelationTypeSubClass != ?argumentRelationType ) " +
			"      } " +
			"}UNION { " +
			"    # Get the argument bits, if ?argument is a generic thing \n" +
			"    ?argument adept-base:canonicalString ?genericThingCanonicalString ; " +
			"               a ?genericThingType " +
			"    FILTER NOT EXISTS { " +
			"       ?subClass rdfs:subClassOf ?genericThingType . " +
			"       ?argument a ?subClass " +
			"       FILTER (?subClass != ?genericThingType) " +
			"    } " +
			"    FILTER NOT EXISTS { ?argument adept-base:canonicalMention ?entityCanonicalMention } " +
			"} " ;

	private static final String QUERY_TRIPLES_BY_SUBJECT_URI =
			PREFIXES
					+ "SELECT ?predicate ?object "
					+ "WHERE { adept-data:%s ?predicate ?object }";

	private static final String QUERY_SUBJECTS_BY_TYPE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { ?subject rdf:type adept-core:%s }";

	private static final String QUERY_XSD_DATES_BY_VALUE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject a adept-base:Date;"
					+ "  adept-base:xsdDate \"%s\"^^xsd:date }";

	private static final String QUERY_XSD_DATE_BY_ID =
			PREFIXES
					+ "SELECT ?value "
					+ "WHERE { "
					+ "adept-data:%s a adept-base:Date;"
					+ "  adept-base:xsdDate ?value }";

	private static final String QUERY_TEMPORAL_VALUE_BY_ID =
			PREFIXES
					+ "SELECT ?xsdValue ?timexValue "
					+ "WHERE { "
					+ "adept-data:%s a adept-base:Date "
					+ "{"
					+ "  adept-data:%s adept-base:xsdDate ?xsdValue "
					+ "}UNION{ "
					+ "  adept-data:%s adept-base:timex2String ?timexValue "
					+ "}"
					+ "}";

	private static final String QUERY_TIMEX_VALUES_BY_VALUE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject a adept-base:Date;"
					+ "  adept-base:timex2String \"%s\"^^adept-base:timex2 }";

	private static final String QUERY_TIMEX_DATE_BY_ID =
			PREFIXES
					+ "SELECT ?value "
					+ "WHERE { "
					+ "adept-data:%s a adept-base:Date;"
					+ "  adept-base:timex2String ?value }";

	private static final String QUERY_TEMPORAL_SPAN_BY_ID =
			PREFIXES
					+ "SELECT ?beginDate ?endDate "
					+ "WHERE { "
					+ "adept-data:%s a adept-base:TemporalSpan ."
					+ "OPTIONAL{ adept-data:%s adept-base:beginDate ?beginDate } "
					+ "OPTIONAL{ adept-data:%s adept-base:endDate ?endDate } "
					+ "}";

	private static final String QUERY_TRIPLES_BY_OBJECT_URI =
			PREFIXES
					+ "SELECT ?subject ?predicate "
					+ "WHERE { ?subject ?predicate adept-data:%s }";

	/** Templates related to entity insertion */
	private static final String INSERT_ENTITY_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    adept-base:canonicalMention  " + "adept-data:%s ;"
					+ "                         adept-base:canonicalString  " + "\"%s\"  ;"
					+ "							adept-base:provenanceCount  " + "\"%s\"^^xsd:integer  ;"
					+ "				"
					+ "			"
					+ "adept-base:documentCount  " +
					"\"%s\"^^xsd:integer  ;"
					+ "                         adept-base:confidence  " + "\"%s\"^^xsd:float" + " ."
					+ "  adept-data:%s    adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	/** Templates related to Timex Date insertion */
	private static final String INSERT_TIMEX_VALUE_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    a adept-base:Date ;"
					+ "      adept-base:timex2String  \"%s\"^^adept-base:timex2  ."
					+ "}   ";

	private static final String INSERT_DATE_VALUE_TEMPLATE =
			PREFIXES

					+ "INSERT DATA"
					+ "{ adept-data:%s    a adept-base:Date ;"
					+ "      adept-base:timex2String  \"%s\"^^adept-base:timex2  ;"
					+ "      adept-base:xsdDate  \"%s\"^^xsd:date  ."
					+ "}   ";

	private static final String TYPE_REIFICATION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type  adept-core:%s  ." + "} ; "
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;"
					+ "                         rdf:object  " + "adept-core:%s  ;"
					+ "                         rdf:predicate  " + "rdf:type ;"
					+ "                         adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	private static final String BASE_TYPE_REIFICATION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type  adept-base:%s  ." + "} ; "
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;"
					+ "                         rdf:object  " + "adept-base:%s  ;"
					+ "                         rdf:predicate  " + "rdf:type ."
					+ "                         }   ";

	private static final String EXTERNALKBID_INSERTION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type                         adept-base:ExternalID ;"
					+ "                   adept-base:externalKbElementId   \"%s\"  ;"
					+ "                   adept-base:externalKbName        \"%s\"  .}   ";

	private static final String ADD_EXTERNALID_TO_KBOBJECT =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    adept-base:externalID    adept-data:%s  .}";

        private static final String EXTERNALKBID_DELETION_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ ?id              rdf:type                         adept-base:ExternalID ;"
					+ "                   adept-base:externalKbElementId   \"%s\"  ;"
					+ "                   adept-base:externalKbName        \"%s\"  .   "
                    + "  adept-data:%s    adept-base:externalID            ?id  .}   ";

	/** Templates related to KB entity update */
	private static String UPDATE_ENTITY_CONFIDENCE_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    adept-base:confidence  " + "?o" + " ." + "}  ; "
					+ "INSERT DATA "
					+ "{ adept-data:%s    adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	private static final String UPDATE_ENTITY_PROVENANCE_COUNT_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    adept-base:provenanceCount  " + "?o" + " ." + "}  ; "
					+ "INSERT DATA "
					+ "{ adept-data:%s    adept-base:provenanceCount  " + "\"%s\"^^xsd:integer" + " ." + "}   ";

	private static final String UPDATE_ENTITY_DOCUMENT_COUNT_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    "
					+ "adept-base:documentCount  " + "?o" +
					" ." + "}  ; "
					+ "INSERT DATA "
					+ "{ adept-data:%s    "
					+ "adept-base:documentCount  " +
					"\"%s\"^^xsd:integer" + " ." + "}   ";

	private static final String UPDATE_CANONICAL_MENTION_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    adept-base:canonicalMention    ?o1   ;"
					+ "                   adept-base:canonicalString    ?o2   ."
					+ "            ?o1    adept-base:confidence    ?o3   ;" + "} ;"
					+ "INSERT DATA"
					+ "{ adept-data:%s    adept-base:canonicalMention  " + "adept-data:%s ;"
					+ "                   adept-base:canonicalString  " + "\"%s\"  ."
					+ "  adept-data:%s    adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "} ;";

	private static final String DELETE_ENTITY_TYPES_TEMPLATE =
			PREFIXES +
					"DELETE WHERE" +
					"{ adept-data:%s    rdf:type    ?t   .};" +
					"DELETE WHERE" +
					"{ ?s    rdf:type    rdf:Statement    ;" +
					"           rdf:subject  adept-data:%s ;" +
					"           rdf:object   ?t ;" +
					"           rdf:predicate  rdf:type ;" +
					"           adept-base:confidence  ?o .} ;";

	private static final String DELETE_EVENT_REALIS_TYPES_TEMPLATE =
			PREFIXES +
					// "DELETE { adept-data:%s rdf:type ?t } WHERE" +
					// "{ adept-data:%s    rdf:type    ?t   ."
					// +
					// "FILTER NOT EXISTS {?t rdfs:subClassOf adept-base:Event}};"
					// +
					"DELETE WHERE" +
					"{ adept-data:%s a ?t . "
					+ "?s    rdf:type    rdf:Statement    ;" +
					"           rdf:subject  adept-data:%s ;" +
					"           rdf:object   ?t ;" +
					"           rdf:predicate  rdf:type ;" +
					"           adept-base:confidence  ?o .} ;";

	/** Templates related to insertion of KB relations */
	private static final String INSERT_RELATION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type      <%s>  ;"
					+ "                         adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	private static final String INSERT_ARGUMENT_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    <%s>    adept-data:%s    ." + "}   ";

	private static final String INSERT_STRENGTH_ARGUMENT_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    adept-core:strength    \"%s\"^^xsd:integer" + " ." + "}   ";

	private static final String INSERT_NUMBER_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type      adept-base:Number  ;"
					+ "                         rdf:value %s .}   ";

	private static final String INSERT_GENERIC_THING_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type      <%s>  ;"
					+ "    adept-base:canonicalString  "
					+ "\"%s\" ; "
					+ "adept-base:provenanceCount  "
					+ "\"%s\"^^xsd:integer  ; adept-base:documentCount \"%s\"^^xsd:integer  .}   ";

	private static final String INSERT_DATE_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type      adept-base:Date  ;"
					+ "                         adept-base:xsdDate  " + "\"%s\"^^xsd:date ;"
					+ "                         adept-base:xsdDuration  " + "\"%s\"^^xsd:duration ;"
					+ "                         adept-base:timex2String  " + "\"%s\"" + " ." + "}   ";

	private static final String ARGUMENT_REIFICATION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;"
					+ "                         rdf:object  " + "adept-data:%s ;"
					+ "                         rdf:predicate  <%s> ;"
					+ "                         adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	private static final String STRENGTH_ARGUMENT_REIFICATION_TEMPLATE =
			PREFIXES
					+ "INSERT DATA"
					+ "{ adept-data:%s    rdf:type    rdf:Statement    ;"
					+ "                         rdf:subject  " + "adept-data:%s ;"
					+ "                         rdf:object  " + "\"%s\"^^xsd:integer ;"
					+ "                         rdf:predicate  " + "adept-core:strength ;"
					+ "                         adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	/** Template to update relation confidence */
	private static final String UPDATE_RELATION_CONFIDENCE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    adept-base:confidence  " + "?o" + " ." + "}  ; "
					+ "INSERT DATA "
					+ "{ adept-data:%s    adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	private static final String UPDATE_ARGUMENT_CONFIDENCE_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE"
					+ "{ adept-data:%s    adept-base:confidence  " + "?o" + " ." + "}  ; "
					+ "INSERT DATA "
					+ "{ adept-data:%s    adept-base:confidence  " + "\"%s\"^^xsd:float" + " ." + "}   ";

	/** Templates related to deletion of KB objects by KB ID only */
	private static final String DELETE_KBOBJECT_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE {adept-data:%s ?p ?o}";

	private static final String DELETE_REIFIEDSTATEMENT_TEMPLATE =
			PREFIXES
					+ "DELETE WHERE { ?person rdf:type rdf:Statement;"
					+ "                      rdf:subject     adept-data:%s ; "
					+ "                       ?property      ?value }";

	private static final String DOES_OBJECT_EXIST =
			PREFIXES
					+ "SELECT * {adept-data:%s ?predicate ?object}";

	private static final String DOES_RELATION_CONTAIN_ARGUMENT =
			PREFIXES
					+ "SELECT * {adept-data:%s adept-core:%s adept-data:%s}";

	private static final String DOES_MENTAL_STATE_CONTAIN_STRENGTH_ARGUMENT =
			PREFIXES
					+ "SELECT * {adept-data:%s adept-core:%s \"%s\"^^xsd:integer}";

	private static final String QUERY_NUMBER_BY_VALUE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject a adept-base:Number;"
					+ "  rdf:value %s }";

	private static final String QUERY_NUMBER_BY_ID =
			PREFIXES
					+ "SELECT ?value "
					+ "WHERE { "
					+ "adept-data:%s a adept-base:Number;"
					+ "  rdf:value ?value }";

	private static final String QUERY_GENERIC_THING_BY_ID =
			PREFIXES
					+ "SELECT ?canonicalString ?type "
					+ "WHERE { "
					+ "adept-data:%s a ?type;"
					+ "  adept-base:canonicalString "
					+ "?canonicalString . "
					+ "OPTIONAL {?id "
					+ "adept-base:provenanceCount "
					+ "?provenanceCount .} OPTIONAL {?id "
					+ " adept-base:documentCount "
					+ "?documentCount ."
					+ " }"
					+ " }";

	private static final String QUERY_GENERIC_THING_BY_VALUE_AND_TYPE =
			PREFIXES
					+ "SELECT ?id "
					+ "WHERE { "
					+ "?id adept-base:canonicalString \"%s\";"
					+ "		a <%s> ."
					+ "OPTIONAL {?id "
					+ "adept-base:provenanceCount "
					+ "?provenanceCount .} OPTIONAL {?id "
					+ " adept-base:documentCount "
					+ "?documentCount ."
					+ " }"
					+ "}";

	private static final String QUERY_EXTERNALID_BY_ID_AND_NAME =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject rdf:type                         adept-base:ExternalID;"
					+ "         adept-base:externalKbElementId   \"%s\" ;"
					+ "         adept-base:externalKbName        \"%s\" . }";

	private static final String QUERY_ADEPTID_BY_EXTERNAL_ID_NAME_AND_TYPE =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject     adept-base:externalID ?externalId  ."
					+ "?externalId  rdf:type  adept-base:ExternalID ;"
					+ "             adept-base:externalKbElementId   \"%s\" ;"
					+ "             adept-base:externalKbName        \"%s\" . "
					+ "?typeId rdf:subject ?subject ."
					+ "?typeId rdf:predicate rdf:type ."
					+ "?typeId rdf:type rdf:Statement ."
					+ "?typeId rdf:object ?type ."
					+ " FILTER (?type = adept-core:%s)."
					+ "}";

	private static final String QUERY_ADEPTID_BY_EXTERNAL_ID_AND_NAME =
			PREFIXES
					+ "SELECT ?subject "
					+ "WHERE { "
					+ "?subject     adept-base:externalID            ?externalId  ."
					+ "?externalId  rdf:type                         adept-base:ExternalID ;"
					+ "             adept-base:externalKbElementId   \"%s\" ;"
					+ "             adept-base:externalKbName        \"%s\" . }";

	private static final String QUERY_EXTERNALIDS_BY_ADEPTID =
			PREFIXES
					+ "SELECT ?externalKbElementId ?externalKbName "
					+ "WHERE { "
					+ "adept-data:%s     adept-base:externalID            ?externalId  ."
					+ "?externalId  rdf:type                         adept-base:ExternalID ;"
					+ "             adept-base:externalKbElementId   ?externalKbElementId ;"
					+ "             adept-base:externalKbName        ?externalKbName . }";

	private static final String DELETE_SINGLE_EXTERNALID_FOR_ADEPTID =
			PREFIXES
					+ "DELETE WHERE { "
					+ "adept-data:%s     adept-base:externalID            ?externalId  ."
					+ "?externalId       rdf:type                         adept-base:ExternalID ;"
					+ "                  adept-base:externalKbElementId   \"%s\" ;"
					+ "                  adept-base:externalKbName        \"%s\" . }";

	private static final String DELETE_ALL_EXTERNALIDS_FOR_ADEPTID =
			PREFIXES
					+ "DELETE WHERE { "
					+ "adept-data:%s     adept-base:externalID            ?externalId  ."
					+ "?externalId       rdf:type                         adept-base:ExternalID ;"
					+ "                  adept-base:externalKbElementId   ?o1 ;"
					+ "                  adept-base:externalKbName        ?o2 . }";

	private static final String QUERY_ENTITY_DATA_BY_IDS =
			PREFIXES +
					"SELECT ?id ?confidence "
					+ "?canonicalMention "
					+ "?canonicalMentionConfidence ?type "
					+ "?typeConfidence "
					+ "?entityCanonicalString "
					+ "?provenanceCount ?documentCount " +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  VALUES ?id { %s } . " +
					"  ?id adept-base:confidence ?confidence . " +
					"  ?id adept-base:canonicalMention ?canonicalMention ; " +
					"	 adept-base:canonicalString ?entityCanonicalString . " +
					"  OPTIONAL {?id "
					+ "adept-base:provenanceCount "
					+ "?provenanceCount .} OPTIONAL {?id "
					+ " adept-base:documentCount "
					+ "?documentCount ."
					+ " }"+
					"  ?typeId rdf:subject ?id . " +
					"  ?typeId rdf:predicate rdf:type . " +
					"  ?typeId rdf:type rdf:Statement . " +
					"  ?typeId rdf:object ?type . " +
					"  ?typeId adept-base:confidence ?typeConfidence . " +
					"  ?canonicalMention adept-base:confidence ?canonicalMentionConfidence . " +
					"}";

	private static final String
			QUERY_MOST_FREQUENT_ENTITY_IDS_BY_PROVENANCE_COUNT =
			PREFIXES +
					"SELECT ?id " +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  ?id adept-base:provenanceCount ?provenanceCount . "+
					"} order by DESC(?provenanceCount) limit %s";

	private static final String
			QUERY_MOST_FREQUENT_ENTITY_IDS_BY_PROVENANCE_COUNT_AND_TYPE =
			PREFIXES +
					"SELECT ?id " +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  ?id adept-base:provenanceCount ?provenanceCount . "+
					"  ?typeId rdf:subject ?id . " +
					"  ?typeId rdf:predicate rdf:type . " +
					"  ?typeId rdf:type rdf:Statement . " +
					"  ?typeId rdf:object ?type . " +
					"  FILTER (?type = adept-core:%s) " +
					"} order by DESC(?provenanceCount) limit %s";

	private static final String
			QUERY_MOST_FREQUENT_ENTITY_IDS_BY_DOCUMENT_COUNT =
			PREFIXES +
					"SELECT ?id " +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  ?id adept-base:documentCount "
					+ "?documentCount . "+
					"} order by DESC(?documentCount) "
					+ "limit %s";

	private static final String
			QUERY_MOST_FREQUENT_ENTITY_IDS_BY_DOCUMENT_COUNT_AND_TYPE =
			PREFIXES +
					"SELECT ?id " +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  ?id adept-base:documentCount "
					+ "?documentCount . "+
					"  ?typeId rdf:subject ?id . " +
					"  ?typeId rdf:predicate rdf:type . " +
					"  ?typeId rdf:type rdf:Statement . " +
					"  ?typeId rdf:object ?type . " +
					"  FILTER (?type = adept-core:%s) " +
					"} order by DESC(?documentCount) "
					+ "limit %s";

	private static final String QUERY_ENTITY_DATA_BY_TYPE =
			PREFIXES +
					"SELECT ?id ?confidence "
					+ "?canonicalMention "
					+ "?canonicalMentionConfidence ?type "
					+ "?typeConfidence "
					+ "?entityCanonicalString "
					+ "?provenanceCount ?documentCount" +
					"WHERE { " +
					"  ?id a adept-base:Entity . " +
					"  ?id adept-base:confidence ?confidence . " +
					"  ?id adept-base:canonicalMention ?canonicalMention ; " +
					"		adept-base:canonicalString ?entityCanonicalString . " +
					"  OPTIONAL {?id "
					+ "adept-base:provenanceCount "
					+ "?provenanceCount . }"+
					"  OPTIONAL {?id "
					+ "adept-base:documentCount "
					+ "?documentCount . }"+
					"  ?typeId rdf:subject ?id . " +
					"  ?typeId rdf:predicate rdf:type . " +
					"  ?typeId rdf:type rdf:Statement . " +
					"  ?typeId rdf:object ?type . " +
					"  FILTER (?type = adept-core:%s) " +
					"  ?typeId adept-base:confidence ?typeConfidence . " +
					"  ?canonicalMention adept-base:confidence ?canonicalMentionConfidence . " +
					"}";

	private static final String QUERY_RELATION_BY_ID =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"adept-data:%s rdf:type ?relationType ; " +
					"    adept-base:confidence ?relationConfidence . " +
					"FILTER NOT EXISTS { " +
					"    ?subClass rdfs:subClassOf ?relationType . " +
					"    adept-data:%1$s a ?subClass " +
					"    FILTER (?subClass != ?relationType) " +
					"} " +
					"  FILTER(?relationType != adept-base:Thing) " +
					"  FILTER(?relationType != adept-base:Relation) " +
					" OPTIONAL { " +
					" adept-data:%1$s ?role ?argument . " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject adept-data:%1$s; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					" } " +
					"} ORDER BY ?role ?argument ";

  private static final String QUERY_RELATION_BY_ID_WITHOUT_ARGS =
      PREFIXES +
	  "SELECT ?relationId ?relationType ?relationConfidence " +
	  "WHERE{ " +
	  "adept-data:%s rdf:type ?relationType ; " +
	  "    adept-base:confidence ?relationConfidence . " +
	  "FILTER NOT EXISTS { " +
	  "    ?subClass rdfs:subClassOf ?relationType . " +
	  "    adept-data:%1$s a ?subClass " +
	  "    FILTER (?subClass != ?relationType) " +
	  "} " +
	  "  FILTER(?relationType != adept-base:Thing) " +
	  "  FILTER(?relationType != adept-base:Relation) ." +
	  "}";

	private static final String QUERY_RELATIONS_BY_IDS =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId a adept-base:Relation . " +
					"  FILTER(%s) " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS { " +
					"    ?subClass rdfs:subClassOf ?relationType . " +
					"    ?relationId a ?subClass " +
					"    FILTER (?subClass != ?relationType) " +
					"} " +
					" %s " +
					" OPTIONAL { " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					"?relationId ?role ?argument . " +
					ARGUMENT_QUERY_BLOCK +
					"} " +
					"} ORDER BY ?role ?argument ";

	private static final String QUERY_RELATIONS_BY_TYPE =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId rdf:type adept-core:%s . " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS { " +
					"    ?subClass rdfs:subClassOf ?relationType . " +
					"    ?relationId a ?subClass " +
					"    FILTER (?subClass != ?relationType) " +
					"} " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument . " +
					"# This triple in additional query block for performance optimization \n"+
					" {?kbRelationArgumentID   adept-base:confidence ?argumentConfidence . } " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?role ?argument ";

	private static final String QUERY_RELATIONS_BY_ARGUMENT =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole adept-data:%s . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp rdfs:subPropertyOf ?otherRole . " +
					"    ?relationId ?subProp adept-data:%1$s . " +
					"    FILTER ( ?subProp != ?otherRole ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS {?subClass rdfs:subClassOf ?relationType . " +
					"   ?relationId rdf:type ?subClass " +
					"   FILTER ( ?subClass != ?relationType ) " +
					"} " +
					"FILTER NOT EXISTS { " +
					"   ?relationType rdfs:subClassOf adept-base:Event . " +
					" } " +
					"FILTER (?relationType != adept-core:Sentiment && ?relationType != adept-core:Belief && ?relationType != adept-base:Thing) " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?relationId ?role ?argument ";

	private static final String QUERY_RELATIONS_BY_ARGUMENTS =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole1 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp1 rdfs:subPropertyOf ?otherRole1 . " +
					"    ?relationId ?subProp1 <%1$s> . " +
					"    FILTER ( ?subProp1 != ?otherRole1 ) " +
					"} " +
					"?relationId ?otherRole2 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp2 rdfs:subPropertyOf ?otherRole2 . " +
					"    ?relationId ?subProp2 <%2$s> . " +
					"    FILTER ( ?subProp2 != ?otherRole2 ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS {?subClass rdfs:subClassOf ?relationType . " +
					"   ?relationId rdf:type ?subClass " +
					"   FILTER ( ?subClass != ?relationType ) " +
					"} " +
					"FILTER NOT EXISTS { " +
					"   ?relationType rdfs:subClassOf adept-base:Event . " +
					" } " +
					"FILTER (?relationType != adept-core:Sentiment && ?relationType != adept-core:Belief && ?relationType != adept-base:Thing) " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?relationId ?role ?argument ";

	private static final String QUERY_SENTIMENTS_BY_ARGUMENTS =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole1 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp1 rdfs:subPropertyOf ?otherRole1 . " +
					"    ?relationId ?subProp1 <%1$s> . " +
					"    FILTER ( ?subProp1 != ?otherRole1 ) " +
					"} " +
					"?relationId ?otherRole2 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp2 rdfs:subPropertyOf ?otherRole2 . " +
					"    ?relationId ?subProp2 <%2$s> . " +
					"    FILTER ( ?subProp2 != ?otherRole2 ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type adept-core:Sentiment ." +
					" BIND (adept-core:Sentiment AS ?relationType) "+
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?relationId ?role ?argument ";

	private static final String QUERY_BELIEFS_BY_ARGUMENTS =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole1 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp1 rdfs:subPropertyOf ?otherRole1 . " +
					"    ?relationId ?subProp1 <%1$s> . " +
					"    FILTER ( ?subProp1 != ?otherRole1 ) " +
					"} " +
					"?relationId ?otherRole2 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp2 rdfs:subPropertyOf ?otherRole2 . " +
					"    ?relationId ?subProp2 <%2$s> . " +
					"    FILTER ( ?subProp2 != ?otherRole2 ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type adept-core:Belief ." +
					" BIND (adept-core:Belief AS ?relationType) "+
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?relationId ?role ?argument ";

	private static final String QUERY_EVENTS_BY_ARGUMENT =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole adept-data:%s . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp rdfs:subPropertyOf ?otherRole . " +
					"    ?relationId ?subProp adept-data:%1$s . " +
					"    FILTER ( ?subProp != ?otherRole ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationType rdfs:subClassOf adept-base:Event . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS {?subClass rdfs:subClassOf ?relationType . " +
					"   ?relationId rdf:type ?subClass " +
					"   FILTER ( ?subClass != ?relationType ) " +
					"} " +
					"FILTER (?relationType != adept-core:Sentiment && ?relationType != adept-core:Belief) " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?role ?argument ";

	private static final String QUERY_EVENTS_BY_ARGUMENTS =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole1 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp1 rdfs:subPropertyOf ?otherRole1 . " +
					"    ?relationId ?subProp1 <%1$s> . " +
					"    FILTER ( ?subProp1 != ?otherRole1 ) " +
					"} " +
					"?relationId ?otherRole2 <%s> . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp2 rdfs:subPropertyOf ?otherRole2 . " +
					"    ?relationId ?subProp2 <%2$s> . " +
					"    FILTER ( ?subProp2 != ?otherRole2 ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationType rdfs:subClassOf adept-base:Event . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS {?subClass rdfs:subClassOf ?relationType . " +
					"   ?relationId rdf:type ?subClass " +
					"   FILTER ( ?subClass != ?relationType ) " +
					"} " +
					"FILTER (?relationType != adept-core:Sentiment && ?relationType != adept-core:Belief) " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?role ?argument ";

	private static final String QUERY_RELATIONS_BY_ARGUMENT_AND_TYPE =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"?relationId ?otherRole adept-data:%s . " +
					"FILTER NOT EXISTS { " +
					"    ?subProp rdfs:subPropertyOf ?otherRole . " +
					"    ?relationId ?subProp adept-data:%1$s . " +
					"    FILTER ( ?subProp != ?otherRole ) " +
					"} " +
					"?relationId ?role ?argument . " +
					"?relationId adept-base:confidence ?relationConfidence . " +
					"?relationId rdf:type ?relationType . " +
					"FILTER NOT EXISTS { " +
					"    ?subClass rdfs:subClassOf ?relationType . " +
					"    ?relationId a ?subClass " +
					"    FILTER (?subClass != ?relationType) " +
					"} " +
					"FILTER (?relationType = adept-core:%s) " +
					"?kbRelationArgumentID a rdf:Statement; " +
					"    rdf:subject ?relationId; " +
					"    rdf:predicate ?role; " +
					"    rdf:object ?argument; " +
					"    adept-base:confidence ?argumentConfidence . " +
					ARGUMENT_QUERY_BLOCK +
					"} ORDER BY ?role ?argument ";

	private static final String GET_ARGUMENT_RELATION_ARGUMENT_BY_ID =
			PREFIXES +
					"SELECT * " +
					"WHERE{ " +
					"# For consistency among various "
					+ "argument queries, make "
					+ "sure that there's a "
					+ "kbRelationArgumentID variable "
					+ "which is the same as input "
					+ "Statement id \n"+
					" ?kbRelationArgumentID a "
					+ "rdf:Statement . "
					+ "FILTER "
					+ "(?kbRelationArgumentID in "
					+ "(adept-data:%s)) ."+
					"?kbRelationArgumentID "+
					"      adept-base:confidence ?argumentConfidence; " +
					"      rdf:predicate ?role; " +
					"      rdf:object ?argument . " +
					"{ " +
					"    #Get the argument bits, if it's an entity \n" +
					"    ?argument adept-base:canonicalMention ?entityCanonicalMention; " +
					"		 adept-base:canonicalString ?entityCanonicalString; " +
					"        adept-base:confidence ?entityconfidence; " +
					"        a ?entityType . " +
					" OPTIONAL "
					+ "{?argument  "
					+ "adept-base:provenanceCount "
					+ "?provenanceCount. }" +
					" OPTIONAL "
					+ "{?argument  "
					+ "adept-base:documentCount "
					+ "?documentCount. }" +
					"    ?entityTypeStatement a rdf:Statement; " +
					"        rdf:subject ?argument; " +
					"        rdf:predicate rdf:type; " +
					"        rdf:object ?entityType; " +
					"        adept-base:confidence ?entityTypeConfidence . " +
					"    ?entityCanonicalMention " +
					"        adept-base:confidence ?canonicalMentionConfidence . " +
					"}UNION{ " +
					"    # Get the argument bits, if ?argument is a a RelationArgument \n" +
					"    ?argument a rdf:Statement; " +
					"      adept-base:confidence ?newRelationArgumentConfidence; " +
					"      rdf:predicate ?newRelationArgumentType; " +
					"      rdf:object ?relationArgumentStatement . " +
					"}UNION{ " +
					"    # Get the argument bits, if ?argument is a number \n" +
					"    ?argument a adept-base:Number; " +
					"        rdf:value ?numberValue " +
					"}UNION { " +
					"    # Get the argument bits, if ?argument is a temporal span \n" +
					"    ?argument a adept-base:TemporalSpan . " +
					"    OPTIONAL { " +
					"        ?beginDateStatement rdf:subject ?argument; " +
					"        	rdf:type rdf:Statement; " +
					"        	rdf:predicate adept-base:beginDate; " +
					"        	rdf:object ?beginDate . " +
					"		 ?beginDate adept-base:timex2String ?beginDateValue . " +
					"	} " +
					"    OPTIONAL { " +
					"        ?endDateStatement rdf:subject ?argument; " +
					"        	rdf:type rdf:Statement; " +
					"        	rdf:predicate adept-base:endDate; " +
					"        	rdf:object ?endDate . " +
					"		 ?endDate adept-base:timex2String ?endDateValue . " +
					"	} " +
					"}UNION { " +
					"    # Get the argument bits, if ?argument is a date \n" +
					"    ?argument a adept-base:Date; " +
					"      	adept-base:timex2String ?date " +
					"}UNION { " +
					"    # Get the argument bits, if ?argument is a relation \n" +
					"    ?argument a ?argumentRelationType . " +
					"    ?argumentRelationType rdfs:subClassOf adept-base:Relation . " +
					"      FILTER NOT EXISTS { " +
					"       ?argumentRelationTypeSubClass rdfs:subClassOf ?argumentRelationType . " +
					"       ?argument rdf:type ?argumentRelationTypeSubClass " +
					"       FILTER (?argumentRelationTypeSubClass != ?argumentRelationType ) " +
					"      } " +
					"}UNION { " +
					"    # Get the argument bits, if ?argument is a generic thing \n" +
					"    ?argument adept-base:canonicalString ?genericThingCanonicalString ; " +
					"               a ?genericThingType " +
					"    FILTER NOT EXISTS { " +
					"       ?subClass rdfs:subClassOf ?genericThingType . " +
					"       ?argument a ?subClass " +
					"       FILTER (?subClass != ?genericThingType) " +
					"    } " +
					"    FILTER NOT EXISTS { ?argument adept-base:canonicalMention ?entityCanonicalMention } " +
					"} " +
					"} ORDER BY ?role ?argument";

	private static final String GET_ENTITY_CLASS =
			PREFIXES +
					"SELECT ?class " +
					"WHERE { adept-base:Entity a ?class }";

    private static final String GET_RELATED_ENTITIES =
                    PREFIXES +
                                    "SELECT DISTINCT * " +
                                    "WHERE { " +
                                    "    adept-data:%s ( %s ) ?relatedEntityId " +
                                    "    FILTER (?relatedEntityId != adept-data:%1$s) " +
                                    "  	?relatedEntityId adept-base:canonicalMention ?entityCanonicalMention; " +
                                    "		adept-base:canonicalString ?entityCanonicalString; " +
                                    "		adept-base:confidence ?entityconfidence; " +
				    "		a ?entityType . " +
				    "	OPTIONAL {?relationEntityId "
				    + "adept-base:provenanceCount "
				    + "?provenanceCount .} " +
				    "	OPTIONAL {?relationEntityId "
				    + "adept-base:documentCount "
				    + "?documentCount .} " +
                                    "	?entityTypeStatement a rdf:Statement; " +
                                    "		rdf:subject ?relatedEntityId; " +
                                    "		rdf:predicate rdf:type; " +
                                    "		rdf:object ?entityType; " +
                                    "		adept-base:confidence ?entityTypeConfidence . " +
                                    "	?entityCanonicalMention " +
                                    "		adept-base:confidence ?canonicalMentionConfidence . " +
                                    "} ";

    private static final String GET_RELATED_ENTITIES_BY_RELATION_TYPE =
                    PREFIXES +
                                    "SELECT DISTINCT * " +
                                    "WHERE { " +
                                    "    adept-data:%s ( %s ) ?relatedEntityId " +
                                    "    FILTER (?relatedEntityId != adept-data:%1$s) " +
                                    "    ?relationId adept-core:entity ?relatedEntityId ; " +
                                    "    		a adept-core:%s . " +
                                    "  	?relatedEntityId adept-base:canonicalMention ?entityCanonicalMention; " +
                                    "		adept-base:canonicalString ?entityCanonicalString; " +
                                    "		adept-base:confidence ?entityconfidence; " +
				    "		a ?entityType . " +
				    "	OPTIONAL{ ?relatedEntityId "
				    + "adept-base:provenanceCount "
				    + "?provenanceCount. }" +
				    "	OPTIONAL{ ?relatedEntityId "
				    + "adept-base:documentCount "
				    + "?documentCount. }" +
                                    "	?entityTypeStatement a rdf:Statement; " +
                                    "		rdf:subject ?relatedEntityId; " +
                                    "		rdf:predicate rdf:type; " +
                                    "		rdf:object ?entityType; " +
                                    "		adept-base:confidence ?entityTypeConfidence . " +
                                    "	?entityCanonicalMention " +
                                    "		adept-base:confidence ?canonicalMentionConfidence . " +
                                    "} ";

	/*** Query builder methods **/

	/**
	 * Create query to get confidence value associated with a relation argument
	 * in the triple store.
	 */
	public Query createGetArgumentConfidenceQuery(String relationId, String argumentId,
			String argumentType) {
		return QueryFactory.create(String.format(GET_ARGUMENT_CONFIDENCE, relationId, argumentId,
				argumentType));
	}

	/**
	 * Create query to get confidence value associated with a mental state
	 * strength argument in the triple store.
	 */
	public Query createGetStrengthArgumentConfidenceQuery(String relationId, int strength,
			String argumentType) {
		return QueryFactory.create(String.format(GET_STRENGTH_ARGUMENT_CONFIDENCE, relationId,
				strength, argumentType));
	}

	/**
	 *
	 */
	public Query createGetRelationConfidenceQuery(String relationId) {
		return QueryFactory.create(String.format(GET_RELATION_CONFIDENCE, relationId));
	}

	/**
	 *
	 * Create query to get ID of the statement(s) that contains given subject
	 * and predicate values
	 */
	public Query createGetReifiedStmtIdBySubjectAndPredicateQuery(String kbUri, String predicate) {
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID_BY_SUBJECT_AND_PREDICATE,
				kbUri, predicate));
	}

	/**
	 *
	 * Create query to get ID of the statement(s) that contains given subject
	 * and object values
	 */
	public Query createGetReifiedStmtIdBySubjectAndObjectQuery(String kbUri, String object) {
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID_BY_SUBJECT_AND_OBJECT,

		kbUri, object));
	}

	/**
	 *
	 * Create query to get ID of the statement(s) that contains given subject,
	 * predicate and object values
	 */
	public Query createGetReifiedStmtIdQuery(String kbUri, String predicate, String object) {
		return QueryFactory.create(String.format(GET_REIFIED_STMT_ID, kbUri, predicate, object));
	}

	/**
	 * Create query to get all triples about a given subject.
	 *
	 */
	public Query createGetTriplesBySubjectURIQuery(String kbUri) {
		return QueryFactory.create(String.format(QUERY_TRIPLES_BY_SUBJECT_URI,
				SparqlUtils.escape(kbUri)));
	}

	/**
	 * Create query to get leaf type a given subject.
	 *
	 */
	public Query createGetLeafTypeBySubjectURIQuery(String kbUri) {
		return QueryFactory.create(String.format(GET_LEAF_TYPE, SparqlUtils.escape(kbUri)));
	}

	/**
	 * create query to get all KB elements having a given type.
	 *
	 */
	public Query createGetSubjectsByTypeQuery(String type) {
		return QueryFactory.create(String.format(QUERY_SUBJECTS_BY_TYPE, SparqlUtils.escape(type)));
	}

	/**
	 *
	 * create query to get all KB elements having a given object value.
	 */
	public Query createGetTriplesByObjectURIQuery(String kbUri) {
		return QueryFactory.create(String.format(QUERY_TRIPLES_BY_OBJECT_URI,
				SparqlUtils.escape(kbUri)));
	}

	/**
	 *
	 * Create query to get tuples of RelationType|Argument|ArgumentType
	 */
	public Query createGetRelationTypeTuplesQuery() {
		return QueryFactory.create(GET_RELATION_TYPE_TUPLES);
	}

	/**
	 *
	 * Create query to get type subclasses
	 */
	public Query createGetTypeSubclassesQuery() {
		return QueryFactory.create(GET_TYPE_SUBCLASSES);
	}

	/**
	 *
	 * Create query to get all entity types
	 */
	public Query createGetEntityTypeQuery() {
		return QueryFactory.create(GET_ENTITY_TYPES);
	}

	/**
	 *
	 * Create query to get all entity types
	 */
	public Query createGetGenericThingTypeQuery() {
		return QueryFactory.create(GET_GENERIC_THING_TYPES);
	}

	/**
	 * create query to check if a given KB element exists on the triple store.
	 *
	 */
	public Query createDoesKBObjectExistQuery(String kbUri) {
		return QueryFactory.create(String.format(DOES_OBJECT_EXIST, SparqlUtils.escape(kbUri)));
	}

	/**
	 * create query to check if an existing KB relation contains a given
	 * argument.
	 */
	public Query createDoesRelationContainArgumentQuery(String relationUri, String argumentUri,
			String argumentType) {
		return QueryFactory.create(String.format(DOES_RELATION_CONTAIN_ARGUMENT,
				SparqlUtils.escape(relationUri), SparqlUtils.escape(argumentType),
				SparqlUtils.escape(argumentUri)));
	}

	/**
	 * create query to check if an existing KB mental state contains a given
	 * strength argument.
	 */
	public Query createDoesMentalStateContainStrengthArgumentQuery(String relationUri,
			int strength, String argumentType) {
		return QueryFactory.create(String.format(DOES_MENTAL_STATE_CONTAIN_STRENGTH_ARGUMENT,
				SparqlUtils.escape(relationUri), SparqlUtils.escape(argumentType),
				Integer.toString(strength)));
	}

	/**
	 * create update statement to insert new entity into the KB.
	 *
	 * This method is also responsible for checking that the entity type(s) are
	 * valid Adept ontology types.
	 *
	 */
	public UpdateRequest createEntityInsertQueries(
			KBEntity.InsertionBuilder entityInsertionBuilder, String entityId) {
		String canonicalMentionSqlId = entityInsertionBuilder.getCanonicalMentionID()
				.getObjectID();
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_ENTITY_TEMPLATE,
				SparqlUtils.escape(entityId), SparqlUtils.escape(canonicalMentionSqlId),
				SparqlUtils.escape(entityInsertionBuilder.getCanonicalMentionValue()),
				entityInsertionBuilder.getProvenances().size
						(), entityInsertionBuilder
						.getDocumentIDsFromProvenances().size(),
				entityInsertionBuilder.getConfidence(),
				SparqlUtils.escape(canonicalMentionSqlId), entityInsertionBuilder.getCanonicalMentionConfidence()));

		for (OntType itype : entityInsertionBuilder.getTypes().keySet()) {
			String type = itype.getType();

			if (!KBOntologyModel.instance().getEntityTypes().contains(type)) {
				throw new RuntimeException("Invalid entity type: " + type
						+ ". Entity insertion failed.");
			}

			String typeStmtId = UUID.randomUUID().toString();
			updateRequest.add(String.format(TYPE_REIFICATION_TEMPLATE,
					SparqlUtils.escape(entityId), SparqlUtils.escape(type),
					SparqlUtils.escape(typeStmtId), SparqlUtils.escape(entityId),
					SparqlUtils.escape(type), entityInsertionBuilder.getTypes().get(itype)));
		}

		return updateRequest;
	}

	/**
	 *
	 * create statements to update the provenanceCount and documentCount
	 * properties of an existing KB generic thing.
	 */
	public UpdateRequest createGenericThingUpdateQueries(KBGenericThing
			.UpdateBuilder genericThingUpdateBuilder,
			String genericThingId) throws KBQueryException {
		UpdateRequest updateRequest = UpdateFactory.create();
		//update provenance count
		updateRequest = updateRequest.add(String.format(UPDATE_ENTITY_PROVENANCE_COUNT_TEMPLATE,
				SparqlUtils.escape(genericThingId), SparqlUtils.escape(genericThingId),
				genericThingUpdateBuilder.getUpdatedProvenanceCount()));

		//update provenance count
		updateRequest = updateRequest.add(String.format(UPDATE_ENTITY_DOCUMENT_COUNT_TEMPLATE,
				SparqlUtils.escape(genericThingId), SparqlUtils.escape(genericThingId),
				genericThingUpdateBuilder.getUpdatedDocumentCount()));

		return updateRequest;
	}

	/**
	 *
	 * create statements to update an existing KB entity. Entity confidence,
	 * canonical mention and canonical string value are overwritten. If the
	 * updated entity has a new type associated with it, the type is appended to
	 * the existing types in the triple store with appropriate confidence. If
	 * the confidence of an existing type is different in the updated entity,
	 * that confidence is overwritten.\
	 *
	 * If the updated entity conatins at least one type that does not map to a
	 * valid Adept ontology type, the update fails.
	 */
	public UpdateRequest createEntityUpdateQueries(KBEntity.UpdateBuilder entityUpdateBuilder,
			String entityId) throws KBQueryException {
		String canonicalMentionSqlId = entityUpdateBuilder.getNewCanonicalMention() != null ? entityUpdateBuilder
				.getNewCanonicalMention().getKBID().getObjectID()
				: null;
		UpdateRequest updateRequest = UpdateFactory.create();

		if (entityUpdateBuilder.getNewConfidence() != null) {
			// overwrite entity confidence
			updateRequest = updateRequest.add(String.format(UPDATE_ENTITY_CONFIDENCE_TEMPLATE,
					SparqlUtils.escape(entityId), SparqlUtils.escape(entityId),
					entityUpdateBuilder.getNewConfidence()));
		}

		//update provenance count
		updateRequest = updateRequest.add(String.format(UPDATE_ENTITY_PROVENANCE_COUNT_TEMPLATE,
				SparqlUtils.escape(entityId), SparqlUtils.escape(entityId),
				entityUpdateBuilder.getUpdatedProvenanceCount()));

		//update provenance count
		updateRequest = updateRequest.add(String.format(UPDATE_ENTITY_DOCUMENT_COUNT_TEMPLATE,
				SparqlUtils.escape(entityId), SparqlUtils.escape(entityId),
				entityUpdateBuilder.getUpdatedDocumentCount()));

		if (canonicalMentionSqlId != null) {
			// overwrite canonical mention, canonical string, canonical mention
			// confidence
			updateRequest.add(String.format(UPDATE_CANONICAL_MENTION_TEMPLATE,
					SparqlUtils.escape(entityId), SparqlUtils.escape(entityId),
					SparqlUtils.escape(canonicalMentionSqlId),
					SparqlUtils.escape(entityUpdateBuilder.getNewCanonicalMention().getValue()),
					SparqlUtils.escape(canonicalMentionSqlId),
					entityUpdateBuilder.getNewCanonicalMentionConfidence()
					));
		}

		// Remove all existing entity types and then re-add based on the new
		// types from the entity builder
		updateRequest.add(String.format(DELETE_ENTITY_TYPES_TEMPLATE, SparqlUtils.escape(entityId),
				SparqlUtils.escape(entityId)));

		for (IType itype : entityUpdateBuilder.getUpdatedTypes().keySet()) {
			String type = itype.getType();

			if (!KBOntologyModel.instance().getEntityTypes().contains(type)) {
				throw new RuntimeException("Invalid entity type: " + type
						+ ". Entity update failed.");
			}

			String typeStmtId = UUID.randomUUID().toString();
			updateRequest.add(String.format(TYPE_REIFICATION_TEMPLATE,
					SparqlUtils.escape(entityId), SparqlUtils.escape(type),
					SparqlUtils.escape(typeStmtId), SparqlUtils.escape(entityId),
					SparqlUtils.escape(type), entityUpdateBuilder.getUpdatedTypes().get(itype)));
		}

		return updateRequest;
	}

	/**
	 *
	 * create statements to insert a new KB relation.
	 *
	 * This method is responsible for checking that the type of the relation
	 * being inserted is a valid Adept ontology type. If not, fail.
	 */
	public UpdateRequest createRelationInsertQueries(OntType ontType, float relationConfidence,
			String relationId) {
		if (!KBOntologyModel.instance().getClasses().contains(ontType.getType())) {
			throw new RuntimeException("Invalid relation type: " + ontType.getType()
					+ ". Relation insertion failed.");
		}

		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_RELATION_TEMPLATE,
				SparqlUtils.escape(relationId), ontType.getURI(), relationConfidence));

		return updateRequest;
	}

	/**
	 *
	 * create statements to insert a new KB sentiment.
	 */
	public UpdateRequest createSentimentInsertQueries(DocumentSentiment documentSentiment,
			String sentimentId) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_RELATION_TEMPLATE,
				SparqlUtils.escape(sentimentId), SparqlUtils.escape("Sentiment"),
				documentSentiment.getConfidence()));

		return updateRequest;
	}

	/**
	 *
	 * create statements to insert a new KB mental state.
	 */
	public UpdateRequest createMentalStateInsertQueries(DocumentMentalState<?> documentMentalState,
			String type, String id) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_RELATION_TEMPLATE,
				SparqlUtils.escape(id), SparqlUtils.escape(type),
				documentMentalState.getConfidence()));

		return updateRequest;
	}

	/**
	 * create statements to insert arguments corresponding to a KB relation into
	 * the triple store.
	 *
	 * This method is responsible for checking that the argument type being
	 * inserted is (or maps to) a valid Adept ontology type.
	 */
	public UpdateRequest createArgumentInsertQueries(OntType role, double argumentConfidence,
			String argumentKbUri, String relationId, String relationType, String argumentStmtId) {
		if (!KBOntologyModel.instance().getRoles().contains(role.getType())) {
			throw new RuntimeException("Invalid arg type: " + role.getType()
					+ ". Argument insertion failed.");
		}

		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_ARGUMENT_TEMPLATE,
				SparqlUtils.escape(relationId), role.getURI(), SparqlUtils.escape(
		    argumentKbUri)));

		updateRequest.add(String.format(ARGUMENT_REIFICATION_TEMPLATE,
				SparqlUtils.escape(argumentStmtId), SparqlUtils.escape(relationId),
				SparqlUtils.escape(argumentKbUri), role.getURI(),
				Double.toString(argumentConfidence)));

		return updateRequest;
	}

	public UpdateRequest createStrengthArgumentInsertQueries(String type,
			double argumentConfidence, int number, String relationId) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				INSERT_STRENGTH_ARGUMENT_TEMPLATE, SparqlUtils.escape(relationId),
				Integer.toString(number)));

		String argumentStmtId = UUID.randomUUID().toString();
		updateRequest.add(String.format(STRENGTH_ARGUMENT_REIFICATION_TEMPLATE,
				SparqlUtils.escape(argumentStmtId), SparqlUtils.escape(relationId),
				Integer.toString(number), Double.toString(argumentConfidence)));

		return updateRequest;
	}

	/**
	 *
	 * create statements to update argument confidence of an existing KB
	 * relation.
	 */
	public UpdateRequest createArgumentConfidenceUpdateQueries(String stmtId, float confidence) {

		UpdateRequest updateRequest = UpdateFactory.create(String.format(
		    UPDATE_ARGUMENT_CONFIDENCE_TEMPLATE, stmtId, stmtId, confidence));

		return updateRequest;
	}

	/**
	 *
	 * create statements to insert a Date type element into the KB.
	 */
	public UpdateRequest createDateInsertQueries(String dateId, String dateString, String xsdDate,
			String xsdDuration) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_DATE_TEMPLATE,
				SparqlUtils.escape(dateId), xsdDate != null ? SparqlUtils.escape(xsdDate) : "null",
				xsdDuration != null ? SparqlUtils.escape(xsdDuration) : "null",
				SparqlUtils.escape(dateString)));
		return updateRequest;
	}

	/**
	 *
	 * create delete KB element queries
	 */
	public UpdateRequest createDeleteQueries(String kbUri) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(DELETE_KBOBJECT_TEMPLATE,
				SparqlUtils.escape(kbUri)));
		updateRequest
				.add(String.format(DELETE_REIFIEDSTATEMENT_TEMPLATE, SparqlUtils.escape(kbUri)));

		return updateRequest;
	}

	/**
	 *
	 * create query to update the confidence value on an existing relation.
	 */
	public UpdateRequest createRelationConfidenceUpdateQuery(String kbUri, float confidence) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				UPDATE_RELATION_CONFIDENCE, SparqlUtils.escape(kbUri), SparqlUtils.escape(kbUri),
				confidence));

		return updateRequest;
	}

	/**
	 * @param timexValue
	 * @param xsdValue
	 * @param id
	 * @return
	 */
	public UpdateRequest createDateValueInsertQueries(String timexValue, Optional<String>
			xsdValue,
			String id) {
		UpdateRequest updateRequest = null;
		if (xsdValue.isPresent()) {
			updateRequest = UpdateFactory.create(String.format(INSERT_DATE_VALUE_TEMPLATE,
					SparqlUtils.escape(id), SparqlUtils.escape(timexValue),
					SparqlUtils.escape(xsdValue.get())));
		} else {
			updateRequest = UpdateFactory.create(String.format(INSERT_TIMEX_VALUE_TEMPLATE,
					SparqlUtils.escape(id), SparqlUtils.escape(timexValue)));
		}
		String typeStmtId = UUID.randomUUID().toString();
		updateRequest.add(String.format(BASE_TYPE_REIFICATION_TEMPLATE,
				SparqlUtils.escape(id), SparqlUtils.escape("Date"),
				SparqlUtils.escape(typeStmtId), SparqlUtils.escape(id),
				SparqlUtils.escape("Date")));

		return updateRequest;
	}

	/**
	 * @param value
	 * @return
	 */
	public Query createGetDateByXSDValueQuery(String value) {
		return QueryFactory.create(String.format(QUERY_XSD_DATES_BY_VALUE,
				SparqlUtils.escape(value)));
	}

	/**
	 * @param value
	 * @return
	 */
	public Query createGetDateByTimexValueQuery(String value) {
		return QueryFactory.create(String.format(QUERY_TIMEX_VALUES_BY_VALUE,
				SparqlUtils.escape(value)));
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createGetXSDDateValueForDateIDQuery(String kbUri) {
		return QueryFactory.create(String.format(QUERY_XSD_DATE_BY_ID, SparqlUtils.escape(kbUri)));
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createGetTimexValueForDateIDQuery(String kbUri) {
		return QueryFactory
				.create(String
				    .format(QUERY_TIMEX_DATE_BY_ID, SparqlUtils.escape(kbUri)));
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createTemporalValueForDateIDQuery(String kbUri) {
		String uri = SparqlUtils.escape(kbUri);
		return QueryFactory.create(String.format(QUERY_TEMPORAL_VALUE_BY_ID, uri, uri, uri));
	}

	/**
	 * Create statements to insert a temporal span into the knowledge base.
	 */
	public UpdateRequest createTemporalSpanInsertQueries(String spanID, KBID beginDateID,
			KBID endDateID) {
		spanID = SparqlUtils.escape(spanID);
		StringBuilder insertTemporalSpan = new StringBuilder();
		insertTemporalSpan.append(String.format(PREFIXES + "INSERT DATA"
				+ "{ adept-data:%s    rdf:type      adept-base:TemporalSpan  ;"
				+ "      adept-base:confidence \"1.0\"^^xsd:float . ", spanID));
		if (beginDateID != null) {
			insertTemporalSpan.append(String.format(
					"adept-data:%s  adept-base:beginDate adept-data:%s . ", spanID,
					SparqlUtils.escape(beginDateID.getObjectID())));
		}
		if (endDateID != null) {
			insertTemporalSpan.append(String.format(
					"adept-data:%s  adept-base:endDate adept-data:%s . ", spanID,
					SparqlUtils.escape(endDateID.getObjectID())));
		}
		insertTemporalSpan.append("}");
		return UpdateFactory.create(insertTemporalSpan.toString());
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createTemporalSpanForIDQuery(String kbUri) {
		String id = SparqlUtils.escape(kbUri);
		return QueryFactory.create(String.format(QUERY_TEMPORAL_SPAN_BY_ID, id, id, id));
	}

	/**
	 * @param number
	 * @return
	 */
	public Query createGetNumberByNumberValueQuery(Number number) {
		String literalString = getLiteralString(number);
		return QueryFactory.create(String.format(QUERY_NUMBER_BY_VALUE, literalString));
	}

	private static String getLiteralString(Number number) {
		LiteralLabel label = LiteralLabelFactory.create(number);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\"");
		stringBuilder.append(label.getLexicalForm());
		stringBuilder.append("\"^^<");
		stringBuilder.append(label.getDatatypeURI());
		stringBuilder.append(">");
		return stringBuilder.toString();

	}

	/**
	 * @param number
	 * @param id
	 * @return
	 */
	public UpdateRequest createNumberInsertQueries(Number number, String id) {
		String literalString = getLiteralString(number);
		UpdateRequest updateRequest = UpdateFactory.create(String.format(INSERT_NUMBER_TEMPLATE,
				SparqlUtils.escape(id), literalString));
		String typeStmtId = UUID.randomUUID().toString();
		updateRequest.add(String.format(BASE_TYPE_REIFICATION_TEMPLATE,
				SparqlUtils.escape(id), SparqlUtils.escape("Number"),
				SparqlUtils.escape(typeStmtId), SparqlUtils.escape(id),
				SparqlUtils.escape("Number")));

		return updateRequest;
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createGetNumberByIDQuery(String kbUri) {
		String uri = SparqlUtils.escape(kbUri);
		return QueryFactory.create(String.format(QUERY_NUMBER_BY_ID, uri));
	}

	public Query createGetExternalIdByIdAndName(String externalKbElementId, String externalKbName) {
		return QueryFactory.create(String.format(QUERY_EXTERNALID_BY_ID_AND_NAME,
				SparqlUtils.escape(externalKbElementId), SparqlUtils.escape(externalKbName)));
	}

	public Query createGetAdeptIdByExternalIdAndName(String externalKbElementId,
			String externalKbName) {
		return QueryFactory.create(String.format(QUERY_ADEPTID_BY_EXTERNAL_ID_AND_NAME,
				SparqlUtils.escape(externalKbElementId), SparqlUtils.escape(externalKbName)));
	}

	public Query createGetAdeptIdByExternalIdNameAndType(String externalKbElementId,
			String externalKbName, String type) {
		return QueryFactory.create(String.format(QUERY_ADEPTID_BY_EXTERNAL_ID_NAME_AND_TYPE,
				SparqlUtils.escape(externalKbElementId), SparqlUtils.escape
						(externalKbName),type));
	}

	public Query createGetExternalIdsByAdeptId(String kbUri) {
		return QueryFactory.create(String.format(QUERY_EXTERNALIDS_BY_ADEPTID,
				SparqlUtils.escape(kbUri)));
	}

	public UpdateRequest createExternalIDInsertQueries(String adeptKbElementId,
			String externalKbElementId, String externalKbName) {
		String externalIDStmtID = UUID.randomUUID().toString();
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				EXTERNALKBID_INSERTION_TEMPLATE, externalIDStmtID,
				SparqlUtils.escape(externalKbElementId), SparqlUtils.escape(externalKbName)));
		updateRequest.add(String.format(ADD_EXTERNALID_TO_KBOBJECT, adeptKbElementId,
				externalIDStmtID));
		return updateRequest;
	}

    public UpdateRequest createExternalIDDeleteQueries(String adeptKbElementId,
			String externalKbElementId, String externalKbName) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				EXTERNALKBID_DELETION_TEMPLATE, SparqlUtils.escape(externalKbElementId), SparqlUtils.escape(externalKbName), adeptKbElementId));
		return updateRequest;
	}

	public UpdateRequest createDeleteAllExternalKbIdsForAdeptId(String kbUri) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				DELETE_ALL_EXTERNALIDS_FOR_ADEPTID, SparqlUtils.escape(kbUri)));

		return updateRequest;
	}

	public UpdateRequest createDeleteExternalKbId(String adeptId, String externalKbId,
			String externalKbName) {
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				DELETE_SINGLE_EXTERNALID_FOR_ADEPTID, SparqlUtils.escape(adeptId),
				SparqlUtils.escape(externalKbId), SparqlUtils.escape(externalKbName)));

		return updateRequest;
	}

	/**
	 * @return
	 */
	public Query createGetLeafRelationTypesQuery() {
		return QueryFactory.create(GET_LEAF_RELATION_TYPES);
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createGetKBRelationByKBRelationArgumentQuery(String kbUri) {
		return QueryFactory.create(String.format(GET_RELATION_ID_BY_RELATION_ARGUMENT_ID,
				SparqlUtils.escape(kbUri)));
	}

	public Query createGetLeafEventTypesQuery() {
		return QueryFactory.create(GET_LEAF_EVENT_TYPES);
	}

	public Query createGetClassesQuery() {
		return QueryFactory.create(GET_CLASSES);
	}

	public Query createGetRolesQuery() {
		return QueryFactory.create(GET_ROLES);
	}

	/**
	 * @param kbid
	 * @return
	 */
	public UpdateRequest createRemoveEventRealisTypes(KBID kbid) {
		String id = kbid.getObjectID();
		return UpdateFactory.create(String.format(DELETE_EVENT_REALIS_TYPES_TEMPLATE,
				SparqlUtils.escape(id), SparqlUtils.escape(id)));
	}

	/**
	 * @param kbid
	 * @param typeStmtId
	 * @param realisType
	 * @return
	 */
	public UpdateRequest createInsertEventRealisType(String kbid, String typeStmtId,
			String realisType, float confidence) {
		return UpdateFactory.create(String.format(TYPE_REIFICATION_TEMPLATE,
				SparqlUtils.escape(kbid), SparqlUtils.escape(realisType),
				SparqlUtils.escape(typeStmtId), SparqlUtils.escape(kbid),
				SparqlUtils.escape(realisType), confidence));
	}

	/**
	 * @param kbId
	 * @return
	 */
	public Query createEventRealisTypesQuery(KBID kbId) {

		return QueryFactory.create(String.format(GET_EVENT_REALIS_TYPES, kbId.getObjectID()));
	}

	/**
	 * @param genericThingInsertionBuilder
	 * @param genericThingId
	 * @return
	 */
	public UpdateRequest createGenericThingInsertQueries(KBGenericThing
			.InsertionBuilder genericThingInsertionBuilder,
			String genericThingId) {
		OntType type = genericThingInsertionBuilder.getType();
		String canonicalString = genericThingInsertionBuilder
				.getCanonicalString();
		int provenanceCount = genericThingInsertionBuilder
				.getProvenances().size();
		int documentCount = genericThingInsertionBuilder
				.getDocumentIDsFromProvenances().size();
		if (!KBOntologyModel.instance().getGenericThingTypes().contains(type.getType())) {
			throw new RuntimeException("Invalid entity type: " + type
					+ ". GenericThing insertion failed.");
		}
		UpdateRequest updateRequest = UpdateFactory.create(String.format(
				INSERT_GENERIC_THING_TEMPLATE, SparqlUtils.escape(genericThingId), type.getURI(),
				SparqlUtils.escape(canonicalString),
				provenanceCount,documentCount));
		String typeStmtId = UUID.randomUUID().toString();
		updateRequest.add(String.format(TYPE_REIFICATION_TEMPLATE,
			SparqlUtils.escape(genericThingId), SparqlUtils.escape(type.getType()),
			SparqlUtils.escape(typeStmtId), SparqlUtils.escape(genericThingId),
			SparqlUtils.escape(type.getType()), 1.0f));


		return updateRequest;
	}

	/**
	 * @param kbUri
	 * @return
	 */
	public Query createGetGenericThingByIDQuery(String kbUri) {
		String uri = SparqlUtils.escape(kbUri);
		return QueryFactory.create(String.format(QUERY_GENERIC_THING_BY_ID, uri));
	}

	/**
	 * @param type
	 * @param canonicalString
	 * @return
	 */
	public Query createGetGenericThingByValueAndTypeQuery(OntType type, String canonicalString) {
		return QueryFactory.create(String.format(QUERY_GENERIC_THING_BY_VALUE_AND_TYPE,
				SparqlUtils.escape(canonicalString), type.getURI()));
	}

	public Query createGetIdsByTypeQuery(String type, String[] ignoredTypes) {
		StringBuilder minusTypes = new StringBuilder();
		for (String ignoredType : ignoredTypes) {
			minusTypes.append(String.format(MINUS_IDS_BY_TYPE, ignoredType));
		}
		return QueryFactory.create(String.format(GET_IDS_BY_TYPE, type, minusTypes));
	}

	/**
	 * Create query to get information for multiple entities
	 *
	 */
	public Query createGetInformationForMultipleEntitiesQuery(List<String> kbUris) {
		if (kbUris == null || kbUris.isEmpty()) {
			return null;
		}
		StringBuilder subjectString = new StringBuilder();
		for (String kbUri : kbUris) {
			subjectString.append("adept-data:").append(kbUri).append(" ");
		}
		subjectString.setLength(subjectString.length() - 1);
		return QueryFactory.create(String.format(QUERY_ENTITY_DATA_BY_IDS,
				SparqlUtils.escape(subjectString.toString())));
	}

	/**
	 * Create query to get information for most frequent entities by
	 * provenanceCount
	 *
	 */
	public Query createGetMostFrequentEntityIdsByProvenanceCountQuery(int
			limit){
		checkArgument(limit>0,"limit must be a positive integer");
		StringBuilder subjectString = new StringBuilder();
		return QueryFactory.create(String.format
				(QUERY_MOST_FREQUENT_ENTITY_IDS_BY_PROVENANCE_COUNT,
				limit));
	}

	/**
	 * Create query to get information for most frequent entities (by
	 * provenance count) given an entityType
	 *
	 */
	public Query createGetMostFrequentEntityIdsByProvenanceCountAndTypeQuery
	(int limit, OntType entityType){
		checkArgument(limit>0,"limit must be a positive integer");
		checkNotNull(entityType);
		checkArgument(!entityType.getType().isEmpty(),"entityType cannot be empty");
		return QueryFactory.create(String.format
				(QUERY_MOST_FREQUENT_ENTITY_IDS_BY_PROVENANCE_COUNT_AND_TYPE,
						entityType.getType(),
				limit));
	}

	/**
	 * Create query to get information for most frequent entities by
	 * document count
	 *
	 */
	public Query createGetMostFrequentEntityIdsByDocumentCountQuery(int
			limit){
		checkArgument(limit>0,"limit must be a positive integer");
		StringBuilder subjectString = new StringBuilder();
		return QueryFactory.create(String.format
				(QUERY_MOST_FREQUENT_ENTITY_IDS_BY_DOCUMENT_COUNT,
						limit));
	}

	/**
	 * Create query to get information for most frequent entities (by
	 * document count) given an entityType
	 *
	 */
	public Query createGetMostFrequentEntityIdsByDocumentCountAndTypeQuery
	(int limit, OntType entityType){
		checkArgument(limit>0,"limit must be a positive integer");
		checkNotNull(entityType);
		checkArgument(!entityType.getType().isEmpty(),"entityType cannot be empty");
		return QueryFactory.create(String.format
				(QUERY_MOST_FREQUENT_ENTITY_IDS_BY_DOCUMENT_COUNT_AND_TYPE,
						entityType.getType(),
						limit));
	}

	public Query createGetEntitiesByTypeQuery(String type) {
		return QueryFactory.create(String.format(QUERY_ENTITY_DATA_BY_TYPE,
				SparqlUtils.escape(type)));
	}

	public Query createGetRelationByIdQuery(String id) {
		String temp = String.format(QUERY_RELATION_BY_ID, SparqlUtils.escape(id));
		return QueryFactory.create(temp);
	}

  public Query createGetRelationByIdWithoutArgsQuery(String id) {
    String temp = String.format(QUERY_RELATION_BY_ID_WITHOUT_ARGS, SparqlUtils.escape(id));
    return QueryFactory.create(temp);
  }

	public Query createGetRelationsByIdsQuery(List<String> ids) {
		return createGetRelationsByIdsQuery(
				ids,
				" FILTER NOT EXISTS { ?relationType rdfs:subClassOf adept-base:Event } "
						+ "FILTER (?relationType != adept-core:Sentiment && ?relationType != adept-core:Belief) ");
	}

	public Query createGetBeliefsByIdsQuery(List<String> ids) {
		return createGetRelationsByIdsQuery(ids, "FILTER (?relationType = adept-core:Belief) ");
	}

	public Query createGetSentimentsByIdsQuery(List<String> ids) {
		return createGetRelationsByIdsQuery(ids, "FILTER (?relationType = adept-core:Sentiment) ");
	}

	public Query createGetEventsByIdsQuery(List<String> ids) {
		return createGetRelationsByIdsQuery(ids,
				"?relationType rdfs:subClassOf adept-base:Event . ");
	}

	private Query createGetRelationsByIdsQuery(List<String> ids, String typeClause) {
		StringBuilder idString = new StringBuilder();
		for (String id : ids) {
			idString.append("?relationId = adept-data:").append(id).append(" || ");
		}
		idString.setLength(idString.length() - 4);
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_IDS,
				SparqlUtils.escape(idString.toString()), typeClause));
	}

	/**
	 * create query to get all KB relations having a given type.
	 *
	 */
	public Query createGetRelationsByTypeQuery(String type) {
		return QueryFactory
				.create(String.format(QUERY_RELATIONS_BY_TYPE, SparqlUtils.escape(type)));
	}

	public Query createGetRelationsByArgumentQuery(String argumentId) {
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_ARGUMENT,
				SparqlUtils.escape(argumentId)));
	}

	public Query createGetRelationsByArgumentsQuery(KBID arg1, KBID arg2) {
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_ARGUMENTS,
				SparqlUtils.escape(arg1.getKBNamespace()+arg1.getObjectID()), SparqlUtils.escape(arg2.getKBNamespace()+arg2.getObjectID())));
	}

	public Query createGetEventsByArgumentQuery(String argumentId) {
		return QueryFactory.create(String.format(QUERY_EVENTS_BY_ARGUMENT,
				SparqlUtils.escape(argumentId)));
	}

	public Query createGetSentimentsByArgumentQuery(String argumentId) {
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_ARGUMENT_AND_TYPE,
				SparqlUtils.escape(argumentId), "Sentiment"));
	}

	public Query createGetBeliefsByArgumentQuery(String argumentId) {
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_ARGUMENT_AND_TYPE,
				SparqlUtils.escape(argumentId), "Belief"));
	}

	public Query createGetRelationsByArgumentAndTypeQuery(String type, String argumentId) {
		return QueryFactory.create(String.format(QUERY_RELATIONS_BY_ARGUMENT_AND_TYPE,
				SparqlUtils.escape(argumentId), SparqlUtils.escape(type)));
	}

	public Query createGetArgumentRelationArgumentQuery(String argumentId) {
		return QueryFactory.create(String.format(GET_ARGUMENT_RELATION_ARGUMENT_BY_ID,
				SparqlUtils.escape(argumentId)));
	}

	public Query createGetEntityClassQuery() {
		return QueryFactory.create(String.format(GET_ENTITY_CLASS));
	}

    public Query createGetRelatedEntitiesQuery(String entityId, int depth) {
        StringBuilder depthString = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthString.append("^adept-core:entity / adept-core:entity");
            if (i + 1 < depth) {
                depthString.append(" / ");
            }
        }

        String query = String.format(GET_RELATED_ENTITIES, SparqlUtils.escape(entityId), SparqlUtils.escape(depthString.toString()));//, Integer.toString(depth));
        return QueryFactory.create(query);
    }

    public Query createGetRelatedEntitiesByRelationTypeQuery(String entityId, int depth, String type) {
        StringBuilder depthString = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthString.append("^adept-core:entity / adept-core:entity");
            if (i + 1 < depth) {
                depthString.append(" / ");
            }
        }

        String query = String.format(GET_RELATED_ENTITIES_BY_RELATION_TYPE, SparqlUtils.escape(entityId), SparqlUtils.escape(depthString.toString()), SparqlUtils.escape(type));//, Integer.toString(depth));
        return QueryFactory.create(query);
    }

	public Query createGetSentimentsByArgumentsQuery(KBID arg1, KBID arg2) {
		return QueryFactory.create(String.format(QUERY_SENTIMENTS_BY_ARGUMENTS,
				SparqlUtils.escape(arg1.getKBNamespace()+arg1.getObjectID()), SparqlUtils.escape(arg2.getKBNamespace()+arg2.getObjectID())));
	}

	public Query createGetBeliefsByArgumentsQuery(KBID arg1, KBID arg2) {
		return QueryFactory.create(String.format(QUERY_BELIEFS_BY_ARGUMENTS,
				SparqlUtils.escape(arg1.getKBNamespace()+arg1.getObjectID()), SparqlUtils.escape(arg2.getKBNamespace()+arg2.getObjectID())));
	}

	public Query createGetEventsByArgumentsQuery(KBID arg1, KBID arg2) {
		return QueryFactory.create(String.format(QUERY_EVENTS_BY_ARGUMENTS,
				SparqlUtils.escape(arg1.getKBNamespace()+arg1.getObjectID()), SparqlUtils.escape(arg2.getKBNamespace()+arg2.getObjectID())));
	}
}
