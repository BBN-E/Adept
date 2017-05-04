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
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.jena.atlas.io.IO;
import org.apache.jena.riot.WebContent;
import org.apache.jena.riot.web.HttpResponseHandler;

import com.hp.hpl.jena.sparql.ARQException;
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote;
import com.hp.hpl.jena.sparql.util.Context;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
  
/**
 * 
 * @author dkolas
 */
public class ParliamentUpdateProcessor extends UpdateProcessRemote implements UpdateProcessor {

	private String endpoint;
	private UpdateRequest request;
	
	/**
	 * @param request
	 * @param endpoint
	 * @param context
	 */
	public ParliamentUpdateProcessor(UpdateRequest request, String endpoint, Context context) {
		super(request, endpoint, context);
		this.request = request;
		this.endpoint = endpoint;
	}

	
	@Override
    public void execute()
    {
        if ( endpoint == null )
            throw new ARQException("Null endpoint for remote update") ;
        String reqStr = request.toString() ;
        execHttpPost(endpoint, WebContent.contentTypeSPARQLUpdate, reqStr, null, null, getHttpContext()) ;
    }

	
	public static void execHttpPost(String url, String contentType, String content,
			String acceptType, Map<String, HttpResponseHandler> handlers, HttpContext httpContext) {
		StringEntity e = null;
		try {
			e = new StringEntity(content, "UTF-8");
			e.setContentType(contentType);
			execHttpPost(url, e, acceptType, handlers, httpContext);
		} finally {
			closeEntity(e);
		}
	}
	public static void execHttpPost(String url, HttpEntity provider, String acceptType,
			Map<String, HttpResponseHandler> handlers, HttpContext context) {
		CloseableHttpClient httpclient = null;
		try {
			HttpPost httppost = new HttpPost(url);
			// Execute
			httpclient = HttpClientBuilder.create().build();
			httppost.setEntity(provider);
			HttpResponse response = httpclient.execute(httppost, context);
			//httpResponse(id, response, baseIRI, handlers);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode  != 200){
				throw new RuntimeException("Parliament update request failed.");
			}
		} catch (IOException ex) {
			IO.exception(ex);
		} finally {
			if (httpclient != null){
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			closeEntity(provider);
		}
	}

	private static void closeEntity(HttpEntity entity) {
		if (entity == null)
			return;
		try {
			entity.getContent().close();
		} catch (Exception e) {
		}
	}
}
