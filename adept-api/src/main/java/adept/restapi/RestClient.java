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

package adept.restapi;

//import java.lang.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.Address;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.UrlEncoded;

import adept.common.Corpus;
import adept.common.HltContentContainer;
import adept.io.Writer;
import adept.serialization.JSONSerializer;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;
import adept.utilities.printHltContent;



/**
 * The Class RestClient.
 * This class has a main() method that connects to the server at the 
 * specified address and port, sends the request along with the serialized Document
 * object, and gets back the serialized HltContentContainer which
 * is written to file.
 */
public class RestClient
{

	/** The client. */
	private HttpClient client;

	/** The sleep duration. */
	private int sleepMS=500;

	/** TODO make a parameter. */
	private boolean bSaveXML = true;

	/**
	 * Instantiates a new REST client.
	 */
	protected RestClient()
	{
		client = new HttpClient();
		client.setIdleTimeout(3000000);
		// for possible future use:
		//exchange_get = new ContentExchange()
		{
			// define the callback method to process the response when you get it back
			// protected void onResponseComplete() throws IOException
			// {
			//   super.onResponseComplete();
			//   int responseStatus = this.getStatus();

			// do something with the response content
			//System.out.println("Response status: " + responseStatus);
			//System.out.println(this.getResponseContent());
			//	  }
		};
	}


	/**
	 * Start client.
	 *
	 * @throws Exception the exception
	 */
	protected void startClient() throws Exception
	{
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.start();
	}


	/**
	 * Gets the server state.
	 *
	 * @param address the address
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the uRI syntax exception
	 */
	protected void get(Address address) throws IOException, URISyntaxException
	{
		while(true) { 
			ContentExchange exchange_get = new ContentExchange(); 
			exchange_get.setMethod("GET");
			exchange_get.setAddress(address);
			exchange_get.setRequestURI("/");
			client.send(exchange_get);
			try {
				exchange_get.waitForDone();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(exchange_get.getResponseStatus() == HttpServletResponse.SC_OK) {
				System.out.println("Server is up!...response status: " + exchange_get.getResponseStatus());
				sleepMS=500;
				break;
			}
			else {
				System.out.printf("Server not yet up...sleeping for %dms and trying again.\n", sleepMS);
				try {
					//thread to sleep for the specified number of milliseconds
					Thread.sleep(sleepMS);
					sleepMS += sleepMS;
				} catch ( java.lang.InterruptedException ie) {
					System.out.println(ie);
				}
			}                    
		}
	}


	/**
	 * Post.
	 *
	 * @param infile the infile
	 * @param server the server
	 * @param port the port
	 * @param outfile the outfile
	 * @param documentId the document id
	 * @param documentCorpus the document corpus
	 * @param documentType the document type
	 * @param documentLanguage the document language
	 * @param flags the flags
	 */
	public void run(
			String infile,
			String server,
			String port,
			String outfile,
			String documentId, 
			Corpus documentCorpus,
			String documentType, 
			String documentLanguage, 
			String flags) {
		try {
			if ( flags != null) {
				// TODO more principled flag processing
				if ( flags.toLowerCase().contains("-q")) bSaveXML = false;
			}				
			startClient();

			File f = new File(infile);
			String infileAbsolute = f.getAbsolutePath();
			String filename = f.getName();
			System.out.println("Doc path: " + infileAbsolute);

			/** Create address and call the GET method */
			Address address = new Address(server,Integer.parseInt(port));
			System.out.println("Sending GET request to check status of server " + address.toString());
			get(address);

			/** Call to the POST method */
			System.out.println("Sending POST request to server...");

			// Create request and define document in POST header fields.
			ContentExchange exchange_post = makeExchange( address, infileAbsolute );
			exchange_post.addRequestHeader( "documentFilename", filename);
			exchange_post.addRequestHeader( "documentId", documentId);
			String corpus = (documentCorpus==null)?"null":documentCorpus.getName();
			exchange_post.addRequestHeader( "documentCorpus", corpus);
			exchange_post.addRequestHeader( "documentType", documentType);
			exchange_post.addRequestHeader( "documentLanguage", documentLanguage);

			String responseContent = post(exchange_post);
			
			// Write serialized result to file.
			if (responseContent != null) {
				Writer writer = new Writer();
				writer.writeToFile(outfile,responseContent);
				if ( bSaveXML ) saveHLTCC( outfile, responseContent );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save hltcc.
	 *
	 * @param outfile the outfile
	 * @param responseContent the response content
	 */
	protected void saveHLTCC( String outfile, String responseContent ) {
		try {
			JSONSerializer jsonSerializer = new JSONSerializer(SerializationType.JSON);
			HltContentContainer hltcc = 
					(HltContentContainer) jsonSerializer.deserializeString(responseContent,HltContentContainer.class);
			if ( hltcc == null ) 
			{
				System.out.println("Null HLT content container.");
				return;
			}
			XMLSerializer xmlSerializer = new XMLSerializer(SerializationType.XML);
			String xmlTemp = xmlSerializer.serializeAsString(hltcc);
			String xmlFilename = outfile + ".xml";
			System.out.println("Writing to:  " + xmlFilename);
			Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Make exchange.
	 *
	 * @param address the address
	 * @param infile the infile
	 * @return the content exchange
	 * @throws FileNotFoundException the file not found exception
	 */
	protected ContentExchange makeExchange( Address address, String infile ) throws FileNotFoundException
	{
		ContentExchange exchange_post = new ContentExchange();
		exchange_post.setRequestContentType("application/octet-stream");
		exchange_post.setMethod("POST");
		// Encoding done to prevent special characters in the parameter names such as
		// $,%,# etc. from throwing exceptions
		exchange_post.setAddress(address);
		String uri = "/" + UrlEncoded.encodeString("process",StringUtil.__UTF8);
		exchange_post.setRequestURI(uri);

		// set request body
		FileInputStream fis = new FileInputStream(infile);
		exchange_post.setRequestContentSource(fis);
		return exchange_post;
	}
	
	
	/**
	 * Post.
	 *
	 * @param exchange_post the exchange_post
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected String post(ContentExchange exchange_post) throws IOException {
		long startTime = System.currentTimeMillis(), duration;
		client.send(exchange_post);
		System.out.println("Request sent.");
		//
		while(true) 
		{ 
			if(exchange_post.isDone()) 
			{
				duration = System.currentTimeMillis() - startTime;
				break;
			}	
		}
		System.out.println("Response status: " + exchange_post.getResponseStatus());
		if(exchange_post.getResponseStatus() == HttpServletResponse.SC_OK)
		{
			System.out.println("Processing completed in " + duration + " ms.");
			return exchange_post.getResponseContent();
		}
		else 
		{
			System.out.println("Processing failed with error code: " + exchange_post.getResponseStatus());
			return null;
		}				
	}

}
