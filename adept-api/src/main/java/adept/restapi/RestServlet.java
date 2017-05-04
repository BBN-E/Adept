package adept.restapi;

/*-
 * #%L
 * adept-api
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adept.common.Document;
import adept.common.DocumentList;
import adept.common.HltContentContainer;
import adept.common.TokenizerType;
import adept.io.Reader;
import adept.io.Writer;
import adept.serialization.JSONStringSerializer;
import adept.utilities.DocumentMaker;



/**
 * The Class RestServlet.
 */
public abstract class RestServlet extends HttpServlet
{

    /** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The serializer. */
	private JSONStringSerializer serializer;

	/** The state. */
	protected ServerState state = ServerState.STOPPED;

	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

    /** The type of tokenizer to be used. */
    protected TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;


    /**
	 * Abstract methods.
	 */
	protected  abstract void doActivate();

	/**
	 * Do deactivate.
	 */
	protected  abstract void doDeactivate();

	protected  abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer);

	protected  abstract HltContentContainer doProcess(DocumentList documentList);

	protected void setState(ServerState serverState) {
		state = serverState;
	}

	/** Locking mechanism for
     * algorithms that do not support
     * concurrent execution
     */
    private final ReentrantLock lock = new ReentrantLock();


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
			// call to subclass.
			doActivate();
			serializer = new JSONStringSerializer();
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
		response.setContentType("text/plain; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Server is: " + state);
        response.getWriter().flush();
    }

    /**
     * Build a document filename based on the provided document ID.
     *
     * Protect against directory traversal attacks
     *
     * @param docId the Document ID to use
     * @throws ServletException
     */
    protected String checkPath(final String docId) throws ServletException {
      if (null == docId || docId.isEmpty()) {
        logger.error("No Document ID provided in request!");
        throw new ServletException("Invalid Parameter");
      }
      String tempFilename = String.format("temp.%s-%d", docId, System.nanoTime());

    	File file = new File(tempFilename);
    	if (file.isAbsolute()) {
    		// Absolute files are not allowed - could be used to alter existing files
    		logger.error("Attempt to use absolute path '{0}' - internal error!", tempFilename);
    		throw new ServletException("Invalid parameter");
    	}
    	String canonicalPath = null;
    	String absolutePath = null;
    	try {
    		canonicalPath = file.getCanonicalPath();
    		absolutePath = file.getAbsolutePath();
    	} catch (IOException e) {
    		logger.error("Could not obtain path for '{0}' - internal error!", tempFilename, e);
    		throw new ServletException(String.format("Invalid parameter - could not obtain path for %s", tempFilename), e);
    	}
    	// if cpath != apath, then attempted traversal
    	if (!canonicalPath.equals(absolutePath)) {
    		logger.error("canonical path '{0}' != absolute path '{1}' with given path '{2}' -- Traversal attack?!", canonicalPath, absolutePath, tempFilename);
    		throw new ServletException("Invalid parameter");
    	}
    	return tempFilename;
    }

	public List<HltContentContainer> parseHltContentContainers(HttpServletRequest request) throws FileUploadException, IOException, ServletException {
		List<HltContentContainer> ret = new LinkedList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			for (FileItem fi : new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request)) {
				String docId = fi.getFieldName();
				String tempFilename = "temp." + docId + "-" + System.nanoTime();
				// Path Traversal Check.  OK to ignore PT_RELATIVE_PATH_TRAVERSAL
				checkPath(tempFilename);
				String fileContents = fi.isFormField() ? fi.getString() : Reader.getInstance().convertStreamToString(fi.getInputStream());
				Writer.getInstance().writeToFile(tempFilename, fileContents);
				System.out.println("Created " + tempFilename);
				HltContentContainer hltcc = new HltContentContainer();
				String uri = new File(tempFilename).getAbsolutePath();
				DocumentMaker.getInstance().createDocument(
						docId,
						null,
						request.getHeader("documentType"),
						uri,
						request.getHeader("documentLanguage"),
						uri,
						hltcc,
						tokenizerType
				);
				ret.add(hltcc);
			}
		} else {
			String docId = request.getHeader("documentId");
			// Path Traversal Check.  OK to ignore next three PT_RELATIVE_PATH_TRAVERSAL
			String tempFilename = checkPath(request.getHeader("documentId"));
			String fileContents = Reader.getInstance().convertStreamToString(request.getInputStream());
			Writer.getInstance().writeToFile(tempFilename, fileContents); // Path Traversal obviated. See above
			System.out.println("Created " + tempFilename);
			HltContentContainer hltcc = new HltContentContainer();
			String uri = new File(tempFilename).getAbsolutePath(); // Path Traversal obviated. See above
			DocumentMaker.getInstance().createDocument(
					docId,
					null,
					request.getHeader("documentType"),
					uri,
					request.getHeader("documentLanguage"),
					uri,
					hltcc,
					tokenizerType
			);
			ret.add(hltcc);
		}
		return ret;
	}

	public DocumentList hltContentContainersToDocumentList(List<HltContentContainer> hltccs) {
		DocumentList ret = new DocumentList();
		for (HltContentContainer hltcc : hltccs) {
			ret.add(hltcc.getDocument());
		}
		return ret;
	}


    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		if (!isThreadSafe() && !lock.tryLock()) {
			response.setContentType("text/plain; charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		        response.getWriter().write("Server is busy processing input and does not support parallel requests.\n");
			response.getWriter().flush();
			return;
		}
    	try
    	{
    		// get request URI
    		String encodedURI = request.getRequestURI();
    		String decodedMethodName = UrlEncoded.decodeString(encodedURI, 0, encodedURI.length(), "UTF-8");

			List<HltContentContainer> inputs = parseHltContentContainers(request);
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    			if (decodedMethodName.equals("/process"))
        		{
					HltContentContainer hltcontentcontainer = doSuperclassProcess(inputs);
					try {
						if (hltcontentcontainer == null) {
							System.out.println("ERROR: Unable to create HltContentContainer.");
							response.setContentType("text/plain; charset=UTF-8");
							response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

						} else {
							// set response fields
							System.out.println("Returning HltContentContainer.");
							response.setContentType("text/plain; charset=UTF-8");
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().write(serializer.serializeToString(hltcontentcontainer));
							response.getWriter().flush();
						}
					} finally {
						for (HltContentContainer hltcc : inputs) {
							String docTempFileUri = hltcc.getDocument().getUri();
							if(new File(docTempFileUri).delete())
								System.out.println("SUCCESS: Temporary file " + docTempFileUri + " deleted successfully");
							else
								System.out.println("WARNING: Problem deleting temporary file " + docTempFileUri + " in RestServlet during clean up phase");
						}
					}

        		}
        		else if (decodedMethodName.equals("/processAsync"))
        		{
        			//TODO:call processAsync()
        		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		finally {
			if (!isThreadSafe()) {
				lock.unlock();
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


    private HltContentContainer doSuperclassProcess(List<HltContentContainer> inputList)
    {
        	long start = System.currentTimeMillis();
			HltContentContainer hltcontentcontainer;
			if (inputList.size() > 1) {
				hltcontentcontainer = doProcess(hltContentContainersToDocumentList(inputList));
			} else {
				HltContentContainer hltContentContainerIn = inputList.get(0);
				hltcontentcontainer = doProcess(hltContentContainerIn.getDocument(), hltContentContainerIn);
			}
        	if (hltcontentcontainer ==null) state = ServerState.ERROR;
        	else state=ServerState.IDLE;
            long end = System.currentTimeMillis();

            System.out.println("Processing took: " + (end - start) + "ms");
            logger.info("Processing took: " + (end - start) + "ms");
            return hltcontentcontainer;
    }

}
