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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adept.kbapi.sparql.SparqlQueryBuilder;

import com.google.common.collect.ImmutableList;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Contains information about the current ontology for use in validation.
 * 
 * @author dkolas
 */
public class KBOntologyModel {

	public static final String ONTOLOGY_BASE_PREFIX = "http://adept-kb.bbn.com/adept-base#";
	public static final String ONTOLOGY_CORE_PREFIX = "http://adept-kb.bbn.com/adept-core#";
	public static final String DATA_INSTANCES_PREFIX = "http://adept-kb.bbn.com/adept-data#";

	private Model model = null;
	private Map<String, Map<String, String>> relationArgumentTypes = null;
	private Map<String, List<String>> entityTypeSubclasses = null;
	private ImmutableList<String> entityTypes = null;
	private ImmutableList<String> leafRelationTypes = null;
	private ImmutableList<String> leafEventTypes = null;
	private ImmutableList<String> classes = null;
	private ImmutableList<String> roles = null;

	private final SparqlQueryBuilder sparqlQueryBuilder;
	private ImmutableList<String> genericThingTypes = null;

	private static KBOntologyModel instance;

	public static KBOntologyModel instance() {
		if (instance == null) {
			instance = new KBOntologyModel();
		}
		return instance;
	}

	private KBOntologyModel() {
		model = ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
		model.read("adept/ontology/adept-base.ttl", "TURTLE");
		model.read("adept/ontology/adept-core.ttl", "TURTLE");

		sparqlQueryBuilder = new SparqlQueryBuilder();
		loadRelationArgumentTypes(model);
		loadEntityTypeSubclasses(model);
		loadEntityTypes(model);
		loadGenericThingTypes(model);
		loadRelationTypes(model);
		loadEventTypes(model);
		loadClasses(model);
		loadRoles(model);
	}

	public Map<String, Map<String, String>> getRelationArgumentTypes() {
		return relationArgumentTypes;
	}

	public Map<String, List<String>> getEntityTypeSubclasses() {
		return entityTypeSubclasses;
	}

	public List<String> getEntityTypes() {
		return entityTypes;
	}

	private void loadRelationArgumentTypes(Model model) {
		relationArgumentTypes = new HashMap<String, Map<String, String>>();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetRelationTypeTuplesQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String relationType = subject.isLiteral() ? subject.asLiteral().getString() : subject
					.asResource().getLocalName();
			RDFNode predicate = item.get(SparqlQueryBuilder.PREDICATE);
			String argument = predicate.isLiteral() ? predicate.asLiteral().getString() : predicate
					.asResource().getLocalName();
			RDFNode object = item.get(SparqlQueryBuilder.OBJECT);
			String argumentType = object.isLiteral() ? object.asLiteral().getString() : object
					.asResource().getLocalName();

			if (relationArgumentTypes.containsKey(relationType)) {
				relationArgumentTypes.get(relationType).put(argument, argumentType);
			} else {
				Map<String, String> argumentMap = new HashMap<String, String>();
				argumentMap.put(argument, argumentType);
				relationArgumentTypes.put(relationType, argumentMap);
			}
		}

		qe.close();
	}

	private void loadEntityTypeSubclasses(Model model) {
		entityTypeSubclasses = new HashMap<String, List<String>>();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetTypeSubclassesQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String superclass = subject.isLiteral() ? subject.asLiteral().getString() : subject
					.asResource().getLocalName();
			RDFNode predicate = item.get(SparqlQueryBuilder.PREDICATE);
			String subclass = predicate.isLiteral() ? predicate.asLiteral().getString() : predicate
					.asResource().getLocalName();

			if (entityTypeSubclasses.containsKey(superclass)) {
				entityTypeSubclasses.get(superclass).add(subclass);
			} else {
				List<String> subclasses = new ArrayList<String>();
				subclasses.add(subclass);
				entityTypeSubclasses.put(superclass, subclasses);
			}
		}

		qe.close();
	}

	private void loadEntityTypes(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetEntityTypeQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.isLiteral() ? subject.asLiteral().getString() : subject
					.asResource().getLocalName();

			listBuilder.add(entityType);
		}
		entityTypes = listBuilder.build();
		qe.close();
	}
	
	private void loadGenericThingTypes(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetGenericThingTypeQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.isLiteral() ? subject.asLiteral().getString() : subject
					.asResource().getLocalName();

			listBuilder.add(entityType);
		}
		genericThingTypes  = listBuilder.build();
		qe.close();
	}

	private void loadRelationTypes(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetLeafRelationTypesQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.asResource().getLocalName();
			listBuilder.add(entityType);
		}
		leafRelationTypes = listBuilder.build();
		qe.close();
	}

	private void loadEventTypes(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetLeafEventTypesQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.asResource().getLocalName();
			listBuilder.add(entityType);
		}
		leafEventTypes = listBuilder.build();
		qe.close();
	}

	private void loadClasses(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(
				sparqlQueryBuilder.createGetClassesQuery(), model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.asResource().getLocalName();
			listBuilder.add(entityType);
		}
		classes = listBuilder.build();
		qe.close();
	}

	private void loadRoles(Model model) {
		ImmutableList.Builder<String> listBuilder = ImmutableList.builder();

		QueryExecution qe = QueryExecutionFactory.create(sparqlQueryBuilder.createGetRolesQuery(),
				model);
		com.hp.hpl.jena.query.ResultSet resultSet = qe.execSelect();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
			String entityType = subject.asResource().getLocalName();
			listBuilder.add(entityType);
		}
		roles = listBuilder.build();
		qe.close();
	}

	/**
	 * @return the leafRelationTypes
	 */
	public List<String> getLeafRelationTypes() {
		return leafRelationTypes;
	}

	/**
	 * @return the leafEventTypes
	 */
	public List<String> getLeafEventTypes() {
		return leafEventTypes;
	}

	/**
	 * @return
	 */
	public ImmutableList<String> getRoles() {
		return roles;
	}

	/**
	 * @return
	 */
	public ImmutableList<String> getClasses() {
		return classes;
	}

	/**
	 * @return
	 */
	public List<String> getGenericThingTypes() {
		return genericThingTypes;
	}
}
