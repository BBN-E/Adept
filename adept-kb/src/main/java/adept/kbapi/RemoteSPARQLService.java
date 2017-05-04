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

import java.io.IOException;

import adept.kbapi.sparql.RemoteModel;
import adept.kbapi.sparql.SparqlQueryBuilder;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

/**
 * 
 * @author dkolas
 */
public class RemoteSPARQLService implements SPARQLService {

	private final String tripleStoreURL;
	private final SparqlQueryBuilder sparqlQueryBuilder;
	private final String queryURLSuffix;
	private final String updateURLSuffix;
	private boolean useParliamentUpdates;

	public RemoteSPARQLService(KBParameters kbParameters) {
		this.tripleStoreURL = kbParameters.tripleStoreUrl;
		this.queryURLSuffix = kbParameters.queryURLSuffix;
		this.updateURLSuffix = kbParameters.updateURLSuffix;
		this.useParliamentUpdates = kbParameters.useParliamentUpdates;
		
		sparqlQueryBuilder = new SparqlQueryBuilder();

		if (!hasOntologyBeenLoaded()) {
			
			// Model model =
			// ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
			Model model = ModelFactory.createDefaultModel();
			model.read(getClass().getResourceAsStream("/adept/ontology/adept-base.ttl"), "", "TTL");
			model.read(getClass().getResourceAsStream("/adept/ontology/adept-core.ttl"), "", "TTL");
			if (!useParliamentUpdates){
				DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(kbParameters.tripleStoreUrl);
				accessor.add(model);
			}else{
				RemoteModel remoteModel = new RemoteModel(null, tripleStoreURL+"/bulk");
				try {
					remoteModel.insertStatements(model);
				} catch (IOException e) {
					throw new RuntimeException("Triple store did not have ontology preloaded, and an exception was thrown while attempting to load it.", e);
				}
				
			}
		}
	}

	/**
	 * 
	 * @param entityInsertRequest
	 * @return
	 * 
	 * @see adept.kbapi.SPARQLService#getUpdateProcessor(com.hp.hpl.jena.update.UpdateRequest)
	 */
	@Override
	public UpdateProcessor getUpdateProcessor(UpdateRequest entityInsertRequest) {
		if (!useParliamentUpdates){
			return UpdateExecutionFactory.createRemote(entityInsertRequest, tripleStoreURL + updateURLSuffix);
		}else{
			return new ParliamentUpdateProcessor(entityInsertRequest, tripleStoreURL + updateURLSuffix, null);
		}
	}

	/**
	 * 
	 * @param query
	 * @return
	 * 
	 * @see adept.kbapi.SPARQLService#getQueryExecution(com.hp.hpl.jena.query.Query)
	 */
	@Override
	public QueryExecution getQueryExecution(Query query) {
		return QueryExecutionFactory.sparqlService(tripleStoreURL + queryURLSuffix, query);
	}

	private boolean hasOntologyBeenLoaded() {
		Query query = sparqlQueryBuilder.createGetEntityClassQuery();
		QueryExecution qexec = null;
		
		try{
			qexec = getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
	
			boolean doesEntityClassExist = false;
			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				if (item.get("?class").isResource()
						&& item.get("?class").asResource().toString()
								.equals("http://www.w3.org/2002/07/owl#Class")) {
					doesEntityClassExist = true;
					break;
				}
			}
			return doesEntityClassExist;
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}
}
