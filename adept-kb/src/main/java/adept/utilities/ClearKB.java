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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBParameters;
import adept.kbapi.ParliamentUpdateProcessor;
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
    KBParameters kbParameters;
    if (args.length > 0) {
      kbParameters = new KBParameters(args[0]);
    } else {
      kbParameters = new KBParameters();
    }
    System.out.println("Running this class will wipe out your configured knowledge base:");
    System.out.println("Triplestore: " + kbParameters.tripleStoreUrl);
    System.out.println("DB: " + kbParameters.metadataUrl);
    System.out.println("Type YES to continue.");
    String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
    if (null != line && line.trim().equals("YES")) {
      wipeKnowledgebase(kbParameters);
      wipeDatabase(kbParameters);
      System.out.println("Success.");
    } else {
      System.out.println("Cancelled.");
    }

  }

  //API to be used by other modules to clear KB

  /**
   * Clears the KB referred to by the input kbParameters
   * @param kbParameters KBParameters object specifying the KB that should be cleared
   * @throws SQLException
   * @throws KBConfigurationException
   * @throws IOException
   */
  public static void clearKB(KBParameters kbParameters) throws SQLException,
                                                               KBConfigurationException, IOException{
    wipeKnowledgebase(kbParameters);
    wipeDatabase(kbParameters);
  }

  //API to be used by other modules to clear KB

  /**
   * Clears the KB referred to by default KB parameters (as configured in KBParameters.xml file).
   * @throws SQLException
   * @throws KBConfigurationException
   * @throws IOException
   */
  public static void clearKB() throws  SQLException, KBConfigurationException, IOException{
    clearKB(new KBParameters());
  }

  /**
   *
   */
  private static void wipeKnowledgebase(KBParameters kbParameters) {
    System.out.println("Clearing Triple Store");
    String delete = "CLEAR DEFAULT;";
    UpdateRequest deleteRequest = UpdateFactory.create(delete);
    UpdateProcessor upp = kbParameters.useParliamentUpdates ?
                          new ParliamentUpdateProcessor(deleteRequest, kbParameters.tripleStoreUrl + kbParameters.updateURLSuffix, null) :
                          UpdateExecutionFactory.createRemote(deleteRequest, kbParameters.tripleStoreUrl + "/update");
    upp.execute();
  }

  private static String[] tables = new String[] { "TextProvenances", "TextChunks", "SourceAlgorithms","SourceDocuments",
      "InferencePaths", "Languages","DocumentTexts" , "Corpus", "SentenceMD5",
      "OverlappingDocument", "OpenIEArguments", "OpenIERelations", "OpenIERelationProvenances",
      "OpenIEArgumentProvenances"};

  private static void wipeDatabase(KBParameters kbParameters) throws SQLException,
                                                                     KBConfigurationException, IOException {
    System.out.println("Clearing DB");
    Connection connection = new QuickJDBC(kbParameters).getConnection();
    for (String table : tables) {
      Statement tableExistsStatement = null;
      ResultSet tableExistsResults = null;
      Statement countStatement = null;
      ResultSet countResults = null;
      Statement dropStatement = null;
      try {
        tableExistsStatement = connection.createStatement();
        tableExistsResults = tableExistsStatement.executeQuery(
            "SELECT table_name FROM information_schema.tables WHERE table_name='" + table + "'");
        if (tableExistsResults.next()) {
          countStatement = connection.createStatement();
          countResults =
              countStatement.executeQuery("SELECT COUNT(*) AS rowCount FROM \"" + table + "\";");
          int rowCount = -1;
          while (countResults.next()) {
            rowCount = countResults.getInt("rowCount");
          }
          dropStatement = connection.createStatement();
          dropStatement.executeUpdate("DROP TABLE \"" + table + "\" CASCADE;");
          System.out.println("Deleted " + rowCount + " rows from " + table);
        } else {
          System.out.println("Table " + table + " does not exist");
        }
      } finally {
        try { if (tableExistsStatement != null) tableExistsStatement.close(); } catch (SQLException e) {}
        try { if (tableExistsResults != null) tableExistsResults.close(); } catch (SQLException e) {}
        try { if (countStatement != null) countStatement.close(); } catch (SQLException e) {}
        try { if (countResults != null) countResults.close(); } catch (SQLException e) {}
        try { if (dropStatement != null) dropStatement.close(); } catch (SQLException e) {}
      }
    }

    URL url = Resources.getResource("adept/utilities/DEFT KB create schema.txt");
    final String createSchemaText = Resources.toString(url, Charsets.UTF_8);

    Statement createSchemaStatement = connection.createStatement();
    createSchemaStatement.executeUpdate(createSchemaText); // OK to ignore findbugs SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE 

    connection.commit();
    connection.close();
  }
}
