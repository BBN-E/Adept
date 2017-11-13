/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

package adept.utilities;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import adept.common.Pair;
import adept.kbapi.KB;
import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBParameters;
import adept.kbapi.RemoteSPARQLService;
import adept.kbapi.sparql.SparqlQueryBuilder;
import adept.kbapi.sparql.SparqlUtils;
import adept.kbapi.sql.QuickJDBC;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
  
/**
 * 
 * @author dkolas
 */
public class UpdateKB {

	
	private static final String INSERT_TEMPLATE = SparqlQueryBuilder.PREFIXES + "INSERT DATA"
			+ "{ <%s>    adept-base:canonicalMention  adept-data:%s ."
			+ "  adept-data:%2$s    adept-base:confidence  \"%s\"^^xsd:float" + " ." + "}   ";
	/**
	 * @param args
	 * @throws KBConfigurationException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
	public static void main(String[] args) throws KBConfigurationException, SQLException, InvalidPropertiesFormatException, IOException {
		
		KBParameters kbParameters = new KBParameters(args[0]);
		KB kb = new KB(kbParameters);
		QuickJDBC quickJDBC = new QuickJDBC(kbParameters);
		List<Pair<Resource, Literal>> canonicalMentionLiterals = getCanonicalMentionLiterals(kb);
		RemoteSPARQLService remoteService = new RemoteSPARQLService(kbParameters);
		int count = canonicalMentionLiterals.size();
		for (int i = 0; i < canonicalMentionLiterals.size(); i++) {
			Pair<Resource, Literal> entityCanonicalMentionPair = canonicalMentionLiterals.get(i);
			if (i % 1000 == 0){
				System.out.println(i+"/"+count);
			}
			String canonicalMentionId = entityCanonicalMentionPair.getR().getString();
			float confidence = getConfidence(canonicalMentionId, quickJDBC);
			//System.out.println(entityCanonicalMentionPair.getL().getURI()+ " " + canonicalMentionId + " " + confidence);
			insertStatements(entityCanonicalMentionPair.getL(), canonicalMentionId, confidence, remoteService);
		}
	}

	/**
	 * @param entityCanonicalMentionPair
	 * @param confidence
	 * @param remoteService 
	 */
	private static void insertStatements(Resource entity, String canonicalMentionId, 
			float confidence, RemoteSPARQLService remoteService) {
		int retries = 0;
		RuntimeException t = null;
		while (retries < 3){
			try{
				String query = String.format(INSERT_TEMPLATE, entity.getURI(), SparqlUtils.escape(canonicalMentionId), confidence);
//				System.out.println(query);
				UpdateProcessor processor = remoteService.getUpdateProcessor(UpdateFactory.create(query));
				processor.execute();
				break;
			}catch(RuntimeException throwable){
				throwable.printStackTrace();
				t = throwable;
				retries ++;
			}
		}
		if (retries == 3){
			throw t;
		}
	}

	/**
	 * @param canonicalMentionId
	 * @param quickJDBC
	 * @return
	 * @throws SQLException 
	 */
	private static float getConfidence(String canonicalMentionId, QuickJDBC quickJDBC) throws SQLException {
		Connection connection = null;
		PreparedStatement confidenceStatement = null;
		java.sql.ResultSet resultSet = null;
		
		float confidence = 0f;
		try{
			connection = quickJDBC.getConnection();
			confidenceStatement = connection.prepareStatement("SELECT confidence from \"TextProvenances\" where \"ID\"=?;");
			confidenceStatement.setString(1, canonicalMentionId);
			resultSet = confidenceStatement.executeQuery();
			if (resultSet.next()){
				confidence = resultSet.getFloat("confidence");
			}else{
				throw new RuntimeException("Could not find confidence for id: "+canonicalMentionId);
			}
		} finally {
			if (resultSet != null){
				resultSet.close();
			}
			if (confidenceStatement != null){
				confidenceStatement.close();
			}
			if (connection != null){
				connection.close();
			}
		}
		return confidence;
	}

	/**
	 * @param kb
	 * @return
	 */
	private static List<Pair<Resource, Literal>> getCanonicalMentionLiterals(KB kb) {
		List<Pair<Resource, Literal>> result = new ArrayList<Pair<Resource,Literal>>();
		ResultSet resultSet = kb.executeSelectQuery(SparqlQueryBuilder.PREFIXES + "\n SELECT ?entity ?cm WHERE { \n"
				+ "?entity <http://adept-kb.bbn.com/adept-base#canonicalMention> ?cm \n"
				+ "FILTER (isLiteral(?cm)) \n"
				+ "FILTER NOT EXISTS { ?entity adept-base:canonicalMention ?cmr .\n"
				+ "?cmr adept-base:confidence ?conf }\n"
				+ "} ");
		while (resultSet.hasNext()){
			QuerySolution solution = resultSet.next();
			result.add(new Pair<Resource, Literal>(solution.getResource("?entity"), solution.getLiteral("?cm")));
		}
		return result;
	}

}