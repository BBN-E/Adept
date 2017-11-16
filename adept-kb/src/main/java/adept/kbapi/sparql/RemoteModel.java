// Parliament is licensed under the BSD License from the Open Source
// Initiative, http://www.opensource.org/licenses/bsd-license.php
//
// Copyright (c) 2001-2009, BBN Technologies, Inc.
// All rights reserved.
package adept.kbapi.sparql;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author dkolas
 * @author sallen
 */
public class RemoteModel implements Serializable {
	private static final long serialVersionUID = 6623488295366585881L;

	public static final String PARLIAMENT_NS = "http://parliament.semwebcentral.org/parliament#";
	public static final String RESULT_INDIVIDUAL = PARLIAMENT_NS + "Result";
	public static final String ERROR_CLASS = PARLIAMENT_NS + "Error";
	public static final String EXCEPTION_TRACE_PROPERTY = PARLIAMENT_NS + "exceptionTrace";

	protected static final String P_CLEAR_ALL = "clearAll";
	protected static final String P_GRAPH = "graph";
	protected static final String P_PERFORM_CLEAR = "performClear";
	protected static final String P_VERIFY = "verifyData";


   public enum RDF_FORMAT {
      RDF_XML("RDF/XML"),
      N3("N3"),
      TURTLE("N3"),
      NTRIPLES("N-TRIPLES");

      private final String _str;

      RDF_FORMAT(String str) {
         _str = str;
      }

      @Override
		public String toString() {
         return _str;
      }
   }

	private String _sparqlEndpointUrl;
   private String _bulkEndpointUrl;
	private int _bufferSize = 2048;
	private Map<String, Object> _defaultParams;
	
	/**
	 * Create a new RemoteModel, pointing to an enhanced Joseki endpoint.
	 *
	 * @param sparqlEndpointUrl The SPARQL endpoint URL (usually "http://host/parliament/sparql").
    * @param bulkEndpointUrl The Bulk endpoint URL (usually "http://host/parliament/bulk").
	 */
	public RemoteModel(String sparqlEndpointUrl, String bulkEndpointUrl, Map<String, Object> defaultParams) {
		this._sparqlEndpointUrl = sparqlEndpointUrl;
      this._bulkEndpointUrl = bulkEndpointUrl;
      this._defaultParams = new HashMap<String, Object>(defaultParams);
	}
	
	public RemoteModel(String sparqlEndpointUrl, String bulkEndpointUrl) {
      this._sparqlEndpointUrl = sparqlEndpointUrl;
      this._bulkEndpointUrl = bulkEndpointUrl;
      this._defaultParams = Collections.emptyMap();
	}
	

	/**
	 * Issue update query to the remote KB
	 *
	 * @param updateQuery A SPARQL/Update query string
	 * @throws IOException
	 */
	public void updateQuery(String updateQuery) throws IOException
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.putAll(_defaultParams);
		params.put("update", updateQuery);
		sendRequest(params);
	}

	/**
	 * Issue update query to the remote KB
	 *
	 * @param updateQuery A SPARQL/Update query object
	 * @throws IOException
	 */
	public void updateQuery(Query updateQuery) throws IOException
	{
		updateQuery( updateQuery.toString() );
	}

	/**
	 * Execute a select query on the remote repository.
	 *
	 * @param selectQuery the query to execute
	 * @return the ResultSet answer.
	 * @throws IOException
	 */
	public ResultSet selectQuery(String selectQuery) throws IOException {
		HashMap<String, Object> params = new HashMap<String, Object>();
	   params.putAll(_defaultParams);
		params.put("query", selectQuery);
      InputStream results = sendRequest(params);
		return ResultSetFactory.fromXML(results);
	}

	/**
	 * Execute a select query on the remote repository.
	 *
	 * @param selectQuery the query to execute
	 * @return the ResultSet answer.
	 * @throws IOException
	 */
	public ResultSet selectQuery(Query selectQuery) throws IOException {
		return selectQuery(selectQuery.toString());
	}

	/**
	 * Execute an ask query on the remote repository.
	 *
	 * @param askQuery The query to execute, in compiled Jena form
	 * @return the boolean answer to the query
	 * @throws IOException
	 */
	public boolean askQuery(Query askQuery) throws IOException {
		return askQuery(askQuery.toString());
	}

	/**
	 * Execute an ask query on the remote repository.
	 *
	 * @param askQuery The query to execute, in String form
	 * @return the boolean answer to the query
	 * @throws IOException
	 */
	public boolean askQuery(String askQuery) throws IOException {
		HashMap<String, Object> params = new HashMap<String, Object>();
      params.putAll(_defaultParams);
		params.put("query", askQuery);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				sendRequest(params)));
		String line = null;
		boolean result = false;
		while ((line = bufferedReader.readLine()) != null) {
			int startIndex = line.indexOf("<boolean>");
			if (line.length() > startIndex + 12
					&& line.substring(startIndex + 9, startIndex + 13)
							.equals("true")) {
				result = true;
			}
		}
		bufferedReader.close();
		return result;
	}

	/**
	 * Execute a construct query on the remote repository.
	 *
	 * @param constructQuery the query to execute
	 * @return the Model answer.
	 * @throws IOException
	 */
	public Model constructQuery(String constructQuery) throws IOException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.putAll(_defaultParams);
		params.put("query", constructQuery);
		Model model = ModelFactory.createDefaultModel();
		model.read(sendRequest(params), "");
		return model;
	}

	/**
	 * Execute a construct query on the remote repository.
	 *
	 * @param constructQuery the query to execute
	 * @return the Model answer.
	 * @throws IOException
	 */
	public Model constructQuery(Query constructQuery) throws IOException {
		return constructQuery(constructQuery.toString());
	}

   /**
    * Insert data into the default graph of the remote KB.
    *
    * @param serializedStatements The statements in serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @throws IOException
    */
   public void insertStatements(String serializedStatements, String format, String base)
         throws IOException {
      insertStatements(serializedStatements, format, base, "", true);
   }
   
   /**
    * Insert data into the default graph of the remote KB.
    *
    * @param serializedStatements The statements in serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param verifyData Whether or not to verify that the data is valid before inserting statements
    * @throws IOException
    */
   public void insertStatements(String serializedStatements, String format, String base, boolean verifyData)
         throws IOException {
      internalInsertStatements(serializedStatements, format, base, "", verifyData);
   }

   /**
    * Insert data into the remote KB in the specified named graph.
    *
    * @param serializedStatements The statements in serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param namedGraphURI The uri of the graph to insert into
    * @throws IOException
    */
   public void insertStatements(String serializedStatements, String format, String base, String namedGraphURI)
         throws IOException {
      insertStatements(serializedStatements, format, base, namedGraphURI, true);
   }
   
   /**
    * Insert data into the remote KB in the specified named graph.
    *
    * @param serializedStatements The statements in serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param namedGraphURI The uri of the graph to insert into
    * @param verifyData Whether or not to verify that the data is valid before inserting statements
    * @throws IOException
    */
   public void insertStatements(String serializedStatements, String format, String base, String namedGraphURI, boolean verifyData)
         throws IOException {
      internalInsertStatements(serializedStatements, format, base, namedGraphURI, verifyData);
   }

   /**
    * Insert data into the default graph of the remote KB.
    *
    * @param serializedStatements Input stream containing the statements in
    *        serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @throws IOException
    */
   public void insertStatements(InputStream serializedStatements,
         String format, String base) throws IOException {
      insertStatements(serializedStatements, format, base, "", true);
   }
   
   /**
    * Insert data into the default graph of the remote KB.
    *
    * @param serializedStatements Input stream containing the statements in
    *        serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param verifyData Whether or not to verify that the data is valid before inserting statements
    * @throws IOException
    */
   public void insertStatements(InputStream serializedStatements,
         String format, String base, boolean verifyData) throws IOException {
      internalInsertStatements(serializedStatements, format, base, "", verifyData);
   }

   /**
    * Insert data into the default graph of the remote KB.
    *
    * @param model A Jena Model containing the statements to be inserted.
    * @throws IOException
    */
   public void insertStatements(Model model) throws IOException {
      insertStatements(model, "");
   }

	/**
	 * Insert data into the remote KB in the specified named graph.
	 *
	 * @param serializedStatements Input stream containing the statements in
	 * serialized form
	 * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
	 * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES","TURTLE"
	 * @param namedGraphURI The uri of the graph to insert into
	 * @throws IOException
	 */
	public void insertStatements(
			InputStream serializedStatements, String format, String base, String namedGraphURI)
			throws IOException {
	   insertStatements(serializedStatements, format, base, namedGraphURI, true);
	}
	
	/**
    * Insert data into the remote KB in the specified named graph.
    *
    * @param serializedStatements Input stream containing the statements in
    * serialized form
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES","TURTLE"
    * @param namedGraphURI The uri of the graph to insert into
    * @param verifyData Whether or not to verify that the data is valid before inserting statements
    * @throws IOException
    */
   public void insertStatements(
         InputStream serializedStatements, String format, String base, String namedGraphURI, boolean verifyData)
         throws IOException {
      internalInsertStatements(serializedStatements, format, base, namedGraphURI, verifyData);
   }

	/**
	 * Insert data into the remote KB in the specified named graph.
	 *
	 * @param model A Jena Model containing the statements to be inserted.
	 * @param namedGraphURI The uri of the graph to insert into
	 * @throws IOException
	 */
	public void insertStatements(Model model, String namedGraphURI) throws IOException {
      final String format = "N-TRIPLES";

//		StringWriter stringWriter = new StringWriter();
//		model.write(stringWriter, format);
//		insertStatements(stringWriter.toString(), format, namedGraphURI);

      final Model inputModel = model;
      final PipedInputStream in = new PipedInputStream();
      final PipedOutputStream out = new PipedOutputStream(in);
      new Thread(
         new Runnable() {
            @Override
				public void run() {
               try {
                  inputModel.write(out, format);
               } finally {
                  try {
                     out.close();
                  } catch (IOException e) {
                     e.printStackTrace();
                     throw new RuntimeException(e);
                  }
               }
            }
         }
      ).start();
      internalInsertStatements(in, format, null, namedGraphURI, false);
   }

   /**
    * Insert data into the remote KB in the specified named graph.
    *
    * @param serializedStatements The statements in serialized form.  This should
    * be either a String or an InputStream.
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @param base The base uri to be used when converting relative URI's to absolute URI's.
    * The base URI may be null if there are no relative URIs to convert.
    * @param namedGraphURI The uri of the graph to insert into
    * @throws IOException
    */
   protected void internalInsertStatements(Object serializedStatements,
         String format, String base, String namedGraphURI, boolean verifyData) throws IOException {
      HashMap<String, Object> params = new HashMap<String, Object>();
      if (format != null) {
         params.put("dataFormat", format);
      }
      if (base != null) {
         params.put("base", base);
      }
      if (!"".equals(namedGraphURI)) {
         params.put("graph", namedGraphURI);
      }
      params.put(P_VERIFY, verifyData ? "yes" : "no");
      params.put("statements", serializedStatements);

      //return testError(sendBulkRequest(params, "insert"));
      InputStream result = sendBulkRequest(params, "insert", true);
      if (null != result) {
         result.close();
      }
   }

   /**
    * Delete data from the default graph in the remote KB.
    *
    * @param serializedStatements The statements in serialized String form
    * @param format Format of the data. One of "RDF/XML", "N3", "N-TRIPLES", "TURTLE"
    * @throws IOException
    */
   public void deleteStatements(String serializedStatements, String format)
         throws IOException {
      deleteStatements(serializedStatements, format, "");
   }

   /**
    * Delete data from the default graph in the remote KB.
    *
    * @param serializedStatements Input stream containing the statements in
    *        serialized form
    * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES"
    * @throws IOException
    */
   public void deleteStatements(InputStream serializedStatements, String format) throws IOException {
      deleteStatements(serializedStatements, format, "");
   }

   /**
    * Delete data from the default graph in the remote KB.
    *
    * @param model A Jena Model containing the statements to be deleted.
    * @throws IOException
    */
   public void deleteStatements(Model model) throws IOException {
      deleteStatements(model, "");
   }

	/**
	 * Delete data from the remote KB from the specified named graph.
	 *
	 * @param serializedStatements The statements in serialized form
	 * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES","TURTLE"
	 * @param namedGraphURI The URI of the graph to delete from
	 * @throws IOException
	 */
	public void deleteStatements(String serializedStatements,
			String format, String namedGraphURI) throws IOException {
		InputStream in = new ByteArrayInputStream(serializedStatements.getBytes());
		deleteStatements(in, format, namedGraphURI);
	}

	/**
	 * Delete data from the remote KB from the specified named graph.
	 *
	 * @param serializedStatements Input stream containing the statements in
	 *        serialized form
	 * @param format Format of the data. One of "RDF/XML","N3","N-TRIPLES","TURTLE"
	 * @param namedGraphURI The uri of the graph to delete from
	 * @throws IOException
	 */
	public void deleteStatements(
			InputStream serializedStatements, String format, String namedGraphURI)
			throws IOException {
	   Model tempModel = ModelFactory.createDefaultModel();
	   tempModel.read(serializedStatements, "", format);
	   deleteStatements(tempModel, namedGraphURI);
	}

	/**
	 * Delete data from the remote KB from the specified named graph.
	 *
	 * @param model A Jena Model containing the statements to be deleted.
	 * @param namedGraphURI The uri of the graph to delete from
	 * @throws IOException
	 */
	public void deleteStatements(Model model, String namedGraphURI) throws IOException {
	   StringBuilder query = new StringBuilder();

      query.append("DELETE DATA { ");

      //ByteArrayOutputStream baos = new ByteArrayOutputStream(serializedStatements.length());
      StringWriter stringWriter = new StringWriter();
      model.write(stringWriter, "N-TRIPLES");
      query.append(stringWriter.toString());

      query.append(" }");

      HashMap<String, Object> params = new HashMap<String, Object>();
      params .put("update", query.toString());

      sendRequest(params);
      //InputStream results = sendRequest(params);
      //ResultSet rs = ResultSetFactory.fromXML(results);
	}

	/**
    * Create a new named graph in the repository.
    *
    * @param namedGraphURI The uri of the named graph to create
    * @throws IOException
    */
   public void createNamedGraph(String namedGraphURI) throws IOException {
      createNamedGraph(namedGraphURI, false);
   }

	/**
	 * Create a new named graph in the repository.
	 *
	 * @param namedGraphURI The uri of the named graph to create
	 * @param silent The SPARQL/Update service generates an error if the graph referred by
	 * the URI already exists unless the SILENT is set to true, then no error is generated
	 * and execution of the sequence of SPARQL/Update operations continues.
	 * @throws IOException
	 */
	public void createNamedGraph(String namedGraphURI, boolean silent) throws IOException {
	   String query = String.format("create %1$s graph <%2$s>", silent ? "silent" : "", namedGraphURI);
	   this.updateQuery(query);
	}

   /**
    * Drops the specified named graph from the repository (this can handle dropping Union graphs as well).
    *
    * @param namedGraphURI The uri of the named graph to drop
    * @throws IOException
    */
   public void dropNamedGraph(String namedGraphURI) throws IOException {
      dropNamedGraph(namedGraphURI, false);
   }

	/**
    * Drops the specified named graph from the repository (this can handle dropping Union graphs as well).
    *
    * @param namedGraphURI The uri of the named graph to drop
    * @param silent The SPARQL/Update service, by default, is expected to generate an error
    * if the specified named graph does not exist. If SILENT is true, this error is ignored
    * and execution of a sequence of SPARQL/Update operations continues.
    * @throws IOException
    */
	public void dropNamedGraph(String namedGraphURI, boolean silent) throws IOException {
	   String query = String.format("drop %1$s graph <%2$s>", silent ? "silent" : "", namedGraphURI);
	   this.updateQuery(query);
	}

	/**
	 * Create a new Union named graph in the repository.
	 *
	 * @param namedUnionGraphURI The uri of the named graph to create
	 * @param leftGraphURI The first named graph in the union
	 * @param rightGraphURI The second named graph in the union
	 * @throws IOException
	 */
	public void createNamedUnionGraph(String namedUnionGraphURI,
			String leftGraphURI, String rightGraphURI) throws IOException {

      String query = String.format(
         "PREFIX parPF: <java:com.bbn.parliament.jena.pfunction.> \n" +
         "INSERT { } \n" +
         "WHERE \n" +
         "{ \n" +
         "   <%1$s> parPF:createUnionGraph ( <%2$s> <%3$s> ) . \n" +
         "} \n",
         namedUnionGraphURI, leftGraphURI, rightGraphURI
         );

      this.updateQuery(query);
	}


	/**
	 * Clears all of the statements in the given named graph.
	 * Note: does not remove the named graph itself.
	 *
	 * @param namedGraphURI URI of the named graph to clear
	 * @throws IOException
	 */
	public void clear(String namedGraphURI) throws IOException {
	   HashMap<String, Object> params = new HashMap<String, Object>();
	   params.put(P_GRAPH, namedGraphURI);
	   params.put(P_PERFORM_CLEAR, "yes");

	   sendBulkRequest(params, "clear", false);
	}

	/**
	 * Clears the entire repository.
	 *
	 * @throws IOException
	 */
	public void clearAll() throws IOException {
	   HashMap<String, Object> params = new HashMap<String, Object>();
	   params.put(P_CLEAR_ALL, "yes");
	   params.put(P_PERFORM_CLEAR, "yes");

	   sendBulkRequest(params, "clear", false);
	}

   private InputStream sendBulkRequest(HashMap<String, Object> params, String service, boolean multipart)
         throws IOException {

      String separator = _bulkEndpointUrl.endsWith("/") ? "" : "/";

      return sendRequest(params, _bulkEndpointUrl + separator + service, multipart);
   }

   private InputStream sendRequest(HashMap<String, Object> params)
      throws IOException {

      return sendRequest(params, _sparqlEndpointUrl, false);
   }

   /**
    * Sends an HTTP-POST request with the supplied params to the given URL.  The
    * server's response is checked, throwing an exception when it indicates an
    * error.  Otherwise, the data returned by the server is returned as an InputStream.
    *
    * @param params The parameters for the POST request
    * @param endpointUrl The URL to post the data to
    * @param multipart Whether to send this request as "multipart/form-data" or "x-www-url-encoded"
    * @return An InputStream containing the data that was returned by the server.
    * @throws IOException In case of an error in the request or in the processing of
    * it by the server.
    */
	private InputStream sendRequest(HashMap<String, Object> params, String endpointUrl, boolean multipart)
			throws IOException {

      // Create the connection
		HttpURLConnection conn = (HttpURLConnection) new URL(endpointUrl).openConnection();
      HttpClientUtil.setAcceptGZIPEncoding(conn);

      if (multipart) {
         try {
            HttpClientUtil.prepareMultipartPostRequestInputStreamAware(conn, params, "UTF-8");
         }
         catch (UnsupportedEncodingException e) {
            // UTF-8 must be supported by all compliant JVM's, this exception should never be thrown.
            throw new RuntimeException("UTF-8 character encoding not supported on this platform");
         }
      }
      else {
         HttpClientUtil.preparePostRequest(conn, params);
      }

      // Send the request if it's not already been sent
      conn.connect();

      // Check whether the server reported any errors
      checkResponse(conn);

      // Get buffered input stream (HttpClientUtil takes care of any gzip compression)
      return new BufferedInputStream(HttpClientUtil.getInputStream(conn), _bufferSize);
	}

   private void checkResponse(HttpURLConnection conn) throws IOException {
      int responseCode = conn.getResponseCode();

      if (responseCode != HttpURLConnection.HTTP_OK) {
         String responseMsg = conn.getResponseMessage();
//         System.out.println("HTTP Error: " + responseCode + " -- " + responseMsg);
         throw new IOException(responseMsg);
      }
   }

	public int getBufferSize() {
		return _bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this._bufferSize = bufferSize;
	}
}
