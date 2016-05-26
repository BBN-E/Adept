/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.kbapi.unittests;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import adept.kbapi.SPARQLService;

/**
 * 
 * @author dkolas
 */
public class LocalSPARQLService implements SPARQLService {

	private final GraphStore graphStore;
	private final Model defaultGraphModel;

	public LocalSPARQLService(GraphStore graphStore) {
		this.graphStore = graphStore;
		this.defaultGraphModel = ModelFactory.createModelForGraph(graphStore.getDefaultGraph());
	}

	/**
	 * 
	 * @param entityInsertRequest
	 * @return
	 * 
	 * @see adept.kbapi.SPARQLService#getUpdateProcessor(com.hp.hpl.jena.update.UpdateRequest)
	 */
	@Override
	public UpdateProcessor getUpdateProcessor(UpdateRequest updateRequest) {
		return UpdateExecutionFactory.create(updateRequest, graphStore);
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
		return QueryExecutionFactory.create(query, graphStore.toDataset());
	}

	public Model getDefaultGraph() {
		return defaultGraphModel;
	}

}
