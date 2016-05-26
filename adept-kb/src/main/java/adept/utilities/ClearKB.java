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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBParameters;
import adept.kbapi.sql.QuickJDBC;

/**
 * 
 * @author dkolas
 */
public class ClearKB {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * @throws KBConfigurationException
	 */
	public static void main(String[] args) throws IOException, SQLException,
			KBConfigurationException {
		KBParameters kbParameters = new KBParameters();
		System.out.println("Running this class will wipe out your configured knowledge base:");
		System.out.println("Triplestore: " + kbParameters.tripleStoreUrl);
		System.out.println("DB: " + kbParameters.metadataUrl);
		System.out.println("Type YES to continue.");
		String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
		if (line.trim().equals("YES")) {
			wipeKnowledgebase(kbParameters);
			wipeDatabase(kbParameters);
			System.out.println("Success.");
		} else {
			System.out.println("Cancelled.");
		}

	}

	/**
	 * 
	 */
	private static void wipeKnowledgebase(KBParameters kbParameters) {
		System.out.println("Clearing Triple Store");
		String delete = "CLEAR DEFAULT;";
		UpdateRequest deleteRequest = UpdateFactory.create(delete);
		UpdateProcessor upp = UpdateExecutionFactory.createRemote(deleteRequest,
				kbParameters.tripleStoreUrl + "/update");
		upp.execute();
	}

	private static String[] tables = new String[] { "TextProvenances", "TextChunks", "SourceAlgorithms","SourceDocuments",
		 "InferencePaths", "Languages","DocumentTexts" , "Corpus"};

	private static void wipeDatabase(KBParameters kbParameters) throws SQLException,
			KBConfigurationException {
		System.out.println("Clearing DB");
		Connection connection = new QuickJDBC(kbParameters).getConnection();
		for (String table : tables) {
			Statement statement = connection.createStatement();
			int result = statement.executeUpdate("DELETE FROM \"" + table + "\";");
			System.out.println("Deleted " + result + " rows from " + table);
			statement.close();
		}
		connection.commit();
		connection.close();
	}

}
