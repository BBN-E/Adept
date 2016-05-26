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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.io.Reader;
import adept.serialization.JSONSerializer;
import adept.serialization.XMLSerializer;
import adept.serialization.SerializationType;
import adept.utilities.DocumentMaker;
import adept.common.TokenizerType;



/**
 * The Class RestServlet.
 */
public abstract class RestServlet extends HttpServlet
{
    
    /** Default serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The serializer. */
	private JSONSerializer serializer;
	
	/** The state. */
	protected ServerState state = ServerState.STOPPED;
	
	/** The logger. */
	private static Logger logger;
	
	/** The log config file path. */
	private static String logConfigFilePath;

    /** The type of tokenizer to be used. */
    protected TokenizerType tokenizerType = TokenizerType.APACHE_OPENNLP;


    /**
	 * Abstract methods.
	 */
	protected  abstract void doActivate();
	
	/**
	 * Do deactivate.
	 */
	protected  abstract void doDeactivate();
	
	/**
	 * Do process.
	 *
	 * @param document the document
	 * @return the hlt content container
	 */
	protected  abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer);
	
	protected void setState(ServerState serverState) {
		state = serverState;
	}

	/** Locking mechanism for 
     * algorithms that do not support 
     * concurrent execution
     */
    private boolean locked = false;
    private final ReentrantLock lock = new ReentrantLock();
    
    public boolean isLocked()
    {
    	return locked;
    }
    
    public boolean isThreadSafe()
    {
    	return true;
    }
    	
	/**
	 * Instantiates a new stanford core nlp servlet.
	 */
	public RestServlet()
	{		
		//state = ServerState.INITIALIZING;		
	}

        
	
	
    // not needed for now
	/* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException 
	{
		super.init();
		try {
            state = ServerState.INITIALIZING;
 	
			/** The log config file path. */
			String packageName = new Object(){}.getClass().getPackage().getName();
			String packagePath = packageName.replace(".", "/") + "/";
			/** Initialize logger instance */
			logger = LoggerFactory.getLogger(new Object(){}.getClass());
			/** The log config file path. */
			logConfigFilePath = packagePath + "log4j.file.properties";
			DataInputStream rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
			PropertyConfigurator.configure(rf);
			logger.info("JUnit version " + junit.runner.Version.id());
			// call to subclass.
			doActivate();
			serializer = new JSONSerializer(SerializationType.JSON);
			state = ServerState.IDLE;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();			
		}	
	}


	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() 
	{
		super.destroy();
		// call to subclass.
		doDeactivate();
	}


	/**
	 * populate response with the json serialized form of the resultant HltContentContainer.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	//@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		response.setContentType("application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);  
        response.getWriter().write("Server is: " + state);
        response.getWriter().flush();	
    }
    
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	File tempFile = null;
    	try
    	{     		
    		// get request URI
    		String encodedURI = request.getRequestURI();
    		String decodedMethodName = UrlEncoded.decodeString(encodedURI, 0, encodedURI.length(), "UTF-8");
    		
    		// get request body
    		InputStream is = request.getInputStream();
			String tempFilename = "temp." + request.getHeader( "documentFilename");
			// Note Oregon's process() creates folder with URI from tempFile, so delete by name.
			boolean bOK = deleteFolder(new File(tempFilename));
			if ( ! bOK ) System.out.println("ERROR: Unable to delete " + tempFilename);
			tempFile = new File(tempFilename);
			int byteCount = 0;
    		try {
    		    OutputStream os = new FileOutputStream(tempFile);
    		    try {
    		        byte[] buffer = new byte[4096];
    		        for (int n; (n = is.read(buffer)) != -1; ) 
    		        {
    		            os.write(buffer, 0, n);
    		            byteCount += n;
    		        }
    		    }
    		    finally { os.close(); }
    		}
    		finally { is.close(); }    	
    		System.out.println("Created " + tempFilename);
    		
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
   		
    		if(byteCount>0) 
    		{
    			if (decodedMethodName.equals("/process")) 
        		{
    				/** Create a document object. */
    				String documentId = request.getHeader( "documentId");
    				String documentCorpus = request.getHeader( "documentCorpus" );
    				String documentType = request.getHeader( "documentType");
    				String documentLanguage = request.getHeader( "documentLanguage");
    				Corpus corpus = null;
    				String uri = tempFile.getAbsolutePath(); 
    				// TODO documentCorpus
					HltContentContainer hltcc = new HltContentContainer();
					Document document = DocumentMaker.getInstance().createDefaultDocument(
							documentId, 
							corpus, 
							documentType, 
							uri, 
							documentLanguage, 
							uri,		// filename
							hltcc,
                            tokenizerType);
    				System.out.println("Created document length = " + byteCount);
    				
    				if(this.isLocked())
    				{
    					response.setContentType("application/octet-stream");
    	    			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    				}
    				else
    				{
    					boolean lockstate = false;
    					HltContentContainer hltcontentcontainer = doSuperclassProcess(document,hltcc,lockstate);
            			if (hltcontentcontainer == null) 
            			{
            				// set response fields
            				if(lockstate)
            				{
            					response.setContentType("application/octet-stream");
            	    			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            				}
            				else
            				{
            					System.out.println("ERROR: Unable to create HltContentContainer.");
            	    			response.setContentType("application/octet-stream");
            	    			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            				}
            					    
            			}
            			else 
            			{
            				// set response fields
            				System.out.println("Returning HltContentContainer.");
        	    			response.setContentType("application/octet-stream");
        	    			response.setStatus(HttpServletResponse.SC_OK);
        	    			response.getWriter().write(serializer.serializeAsString(hltcontentcontainer));
        	    			response.getWriter().flush();	    				    			
            			}    				
    				}
        			
        		}
        		else if (decodedMethodName.equals("/processAsync"))
        		{
        			//TODO:call processAsync()
        		}
    		}
    		
    		// delete temp file created
    		if(tempFile.delete())
    			System.out.println("SUCCESS: Temporary file deleted successfully");
    		else 
    			System.out.println("WARNING: Problem deleting temporary file in RestServlet during clean up phase");
    	}
    	catch(Exception e)
    	{
    		if(tempFile!=null)
    		{
    			if(tempFile.delete())
        			System.out.println("SUCCESS: Temporary file deleted successfully");
        		else 
        			System.out.println("WARNING: Problem deleting temporary file in RestServlet during clean up phase");
        		e.printStackTrace();
    		}
    		
    	}
    }
    
    /**
     * Delete folder.
     *
     * @param folder the folder
     * @return true, if successful
     */
    public static boolean deleteFolder(File folder) {
    	boolean bOK = true;
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    bOK &= deleteFolder(f);
                } else {
                	bOK &= f.delete();
                }
            }
        }
        bOK &= folder.delete();
        return bOK;
    }
    

    
    /**
     * Do process.
     *
     * @param document the document
     * @return the hlt content container
     */
    private HltContentContainer doSuperclassProcess(Document document, HltContentContainer hltcontentcontainer, boolean lockstate)  
    {
    	if(!this.isThreadSafe())
    	{
    		boolean result = lock.tryLock();
    		if(!result)
    		{
    			lockstate = true;
    			return null;
    		}
    		else locked = true;
    	}
    	try
    	{
    		// call process function in AdeptModule
            if(hltcontentcontainer==null) hltcontentcontainer = new HltContentContainer();
        	long start = System.currentTimeMillis();
        	
        	
        	// call to subclass.
        	hltcontentcontainer = doProcess( document, hltcontentcontainer);
        	if (hltcontentcontainer ==null) state = ServerState.ERROR;
        	else state=ServerState.IDLE;
            long end = System.currentTimeMillis();
        	        
            System.out.println("Processing took: " + (end - start) + "ms");
            logger.info("Processing took: " + (end - start) + "ms");
            return hltcontentcontainer;

    	}
        finally
        {
        	if(!this.isThreadSafe())
        	{
        		lock.unlock();
        		locked = false;
        	}
        }
    }   
    
}
