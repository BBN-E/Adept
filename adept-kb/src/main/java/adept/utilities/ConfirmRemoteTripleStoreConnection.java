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

package adept.utilities;

import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBParameters;
import adept.kbapi.RemoteSPARQLService;
import adept.kbapi.sparql.SparqlUtils;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import java.util.UUID;

public class ConfirmRemoteTripleStoreConnection {
	public static void main(String[] args) {
		boolean wasTripleInserted = false;
		KBParameters kbParameters = null;
		RemoteSPARQLService sparqlService = null;
		String id = UUID.randomUUID().toString();

		try {
			if (args.length > 0) {
				kbParameters = new KBParameters(args[0]);
			} else {
				kbParameters = new KBParameters();
			}

			System.out.println("Testing connection to remote triple store at "
					+ kbParameters.tripleStoreUrl);

			sparqlService = new RemoteSPARQLService(kbParameters);

			// Insert a triple with type adept-kb:Organization
			UpdateRequest insertRequest = UpdateFactory.create(String.format(
					"PREFIX adept-base: <%s> " + "PREFIX adept-kb: <%s> "
							+ "PREFIX adept-data: <%s> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " + "INSERT DATA "
							+ "{ adept-data:%s rdf:type adept-kb:Organization }",
					KBOntologyModel.ONTOLOGY_BASE_PREFIX, KBOntologyModel.ONTOLOGY_CORE_PREFIX,
					KBOntologyModel.DATA_INSTANCES_PREFIX, SparqlUtils.escape(id)));
			UpdateProcessor insertProcessor = sparqlService.getUpdateProcessor(insertRequest);
			insertProcessor.execute();

			wasTripleInserted = true;
			System.out.println("Successfully inserted triple with id: " + id);

			// Confirm inference is turned on in triple store
			Query query = QueryFactory.create(String.format("PREFIX adept-base: <%s> "
					+ "PREFIX adept-kb: <%s> " + "PREFIX adept-data: <%s> "
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " + "SELECT ?type "
					+ "WHERE { adept-data:%s rdf:type ?type }",
					KBOntologyModel.ONTOLOGY_BASE_PREFIX, KBOntologyModel.ONTOLOGY_CORE_PREFIX,
					KBOntologyModel.DATA_INSTANCES_PREFIX, SparqlUtils.escape(id)));
			QueryExecution qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			boolean foundEntityType = false;
			while (resultSet.hasNext()) {
				RDFNode typeNode = resultSet.next().get("?type");
				if (typeNode.isResource()) {
					if (typeNode.asResource().toString()
							.equals(KBOntologyModel.ONTOLOGY_BASE_PREFIX + "Entity")) {
						foundEntityType = true;
						break;
					}
				}
			}
			qexec.close();

			if (!foundEntityType) {
				throw new Exception(
						"Inference is not turned on for remote triple store. Unable to find type superclass adept-base:Entity for type adept-core:Organization");
			}

			System.out.println("Confirmed inference is turned on in remote triple store.");

			// Delete inserted triple
			deleteTripleById(id, kbParameters, sparqlService);

			System.out.println("Successfully deleted test triple.");
			System.out.println("Confirmation complete.");

		} catch (Exception ex) {
			System.out.println("Failure to connect and confirm inference on remote triple store.");
			ex.printStackTrace();

			// delete triple if it was already inserted
			if (wasTripleInserted && kbParameters != null && sparqlService != null) {
				deleteTripleById(id, kbParameters, sparqlService);
				System.out.println("Successfully deleted test triple.");
			}
			System.out.println("Confirmation failed.");
		}
	}

	private static void deleteTripleById(String id, KBParameters kbParameters,
			RemoteSPARQLService sparqlService) {
		UpdateRequest deleteRequest = UpdateFactory.create(String.format("PREFIX adept-base: <%s> "
				+ "PREFIX adept-kb: <%s> " + "PREFIX adept-data: <%s> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " + "DELETE "
				+ "WHERE { adept-data:%s rdf:type ?type }", KBOntologyModel.ONTOLOGY_BASE_PREFIX,
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, KBOntologyModel.DATA_INSTANCES_PREFIX,
				SparqlUtils.escape(id)));
		UpdateProcessor deleteProcessor = sparqlService.getUpdateProcessor(deleteRequest);
		deleteProcessor.execute();
	}
}
