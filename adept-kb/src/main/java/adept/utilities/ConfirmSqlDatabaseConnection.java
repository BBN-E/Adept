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

import adept.kbapi.KBParameters;
import adept.kbapi.sql.QuickJDBC;
import adept.kbapi.sql.SqlQueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class ConfirmSqlDatabaseConnection {
	public static void main(String[] args) {
		Connection sqlConnection = null;
		String corpusTestId = UUID.randomUUID().toString();
		KBParameters kbParameters = null;
		try {
			if (args.length > 0) {
				kbParameters = new KBParameters(args[0]);
			} else {
				kbParameters = new KBParameters();
			}

			System.out.println("Testing sql database connection: " + kbParameters.metadataUrl);

			QuickJDBC quickJDBC = new QuickJDBC(kbParameters);
			sqlConnection = quickJDBC.getConnection();

			System.out.println("Inserting test corpus with id " + corpusTestId);

			if (!quickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery(corpusTestId,
					sqlConnection))) {
				quickJDBC.executeSqlUpdate(SqlQueryBuilder.createCorpusInsertQuery(corpusTestId,
						"TEST_TYPE", "TEST_NAME", "TEST_URI", sqlConnection));
			} else {
				throw new Exception("Corpus entry with id " + corpusTestId
						+ " somehow already exists. Try running this test again.");
			}

			System.out.println("Checking for existence of inserted corpus entry...");
			if (!quickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery(corpusTestId,
					sqlConnection))) {
				throw new Exception(
						"Corpus entry with id "
								+ corpusTestId
								+ " does not exist after insertion. Connection to SQL database is not working.");
			}
			System.out.println("Corpus entry exists.");

			System.out.println("Deleting test corpus entry.");
			PreparedStatement preparedStatement = sqlConnection
					.prepareStatement("DELETE FROM \"Corpus\" WHERE \"ID\" = ? ");
			preparedStatement.setString(1, corpusTestId);
			quickJDBC.executeSqlUpdate(preparedStatement);
			if (quickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery(corpusTestId,
					sqlConnection))) {
				throw new Exception("Failed to delete corpus entry with id " + corpusTestId);
			}

			System.out.println("Successfully deleted test corpus entry.");
			System.out.println("Confirmation complete.");
		} catch (Exception ex) {
			System.out.println("Failure to connect to sql database.");
			ex.printStackTrace();
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}
}
