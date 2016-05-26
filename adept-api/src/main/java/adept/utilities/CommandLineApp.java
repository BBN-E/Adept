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

// Next line is site-specific.
package adept.utilities;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.DocumentList;
import adept.common.HltContentContainer;
import adept.common.HltContentContainerList;
import adept.io.Writer;
import adept.io.Reader;

import adept.serialization.BinaryFromJSONSerializer;
import adept.serialization.JSONSerializer;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;
import adept.common.TokenizerType;



/**
 * The Class CommandLineApp.
 */
public abstract class CommandLineApp 
{

	/** The xml serializer. */
	private static XMLSerializer xmlSerializer;

	/** The json serializer. */
	private static JSONSerializer jsonSerializer;

	/** The binary serializer. */
	private static BinaryFromJSONSerializer binarySerializer;

	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

	/** The input file path. And list of file names. */
//	private static inputFileList;
	private static String packagePath;

	/** files to be written on serialization. */
	private static String xmlFilename, jsonFilename, binaryFilename;

	/** Command line arguments. */
	private static CommandLineArgParse theArgs;

    /** The type of tokenizer to be used. */
    protected TokenizerType tokenizerType = TokenizerType.APACHE_OPENNLP;


    /**
	 * Abstract methods.
	 *
	 * @param algorithmConfig the algorithm config
	 */
	protected abstract void doActivate(String algorithmConfig);
	
	/**
	 * Do deactivate.
	 */
	protected abstract void doDeactivate();
	
	/**
	 * Do process.
	 *
	 * @param document the document
	 * @param hltContentContainer the hlt content container
	 * @return the hlt content container
	 */
	protected abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer);		

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @param algorithmConfig the algorithm config
	 */
	public void Run(String[] args, String algorithmConfig)
	{
		/** initialize and configure */
		long totalStart = System.nanoTime();    
		theArgs = new CommandLineArgParse( args);
		if ( theArgs.bHelp || theArgs.bVersion) return;
		if ( !theArgs.bRunOneFile && !theArgs.bRunList) 
		{
			System.out.println("Invalid arguments - exiting.");
			return;
		}
		initialize();
		boolean bSuccess = true;
		if ( !isNullOrEmpty(theArgs.algorithmConfig))
		{
			algorithmConfig = theArgs.algorithmConfig;
		}
		List<File> fileList = new ArrayList<File>();
		if ( theArgs.bRunOneFile)
		{
			File file = new File(theArgs.inputFile);
			fileList.add(file);
		}
		else if ( theArgs.bRunList )
		{
			List<String> inputFileList = fileToLines(theArgs.inputList);
			File dir = new File(theArgs.inputDirectory);
			for ( String line : inputFileList)
			{
				if ( line.startsWith("#")) continue;	// this means the line was a comment not a filename
				File file = new File(dir, line);
				if(!file.exists()) 
				{
					System.out.println("Input file not found:  " + file.getAbsolutePath());
					continue;
				}
				fileList.add(file);
				System.out.println("Input file found:  " + file.getAbsolutePath());
			}
			if ( fileList.isEmpty() ) throw new RuntimeException("List " + inputFileList + " is empty.");
		}
		if ( theArgs.bCrossDoc) 
		{
			bSuccess = processListOfFiles( algorithmConfig, fileList);
		}
		else 
		{
			doActivate(algorithmConfig);
			for ( File fInFile : fileList) 
			{
				if ( ! processInputFile( fInFile) ) bSuccess = false;
			}			
		}
		doDeactivate();
		long totalSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - totalStart, TimeUnit.NANOSECONDS);
		System.out.format("%s  Processed %d files in %d seconds.\n", bSuccess?"SUCCESS!":"ERROR!", fileList.size(), totalSeconds);
	}

	/**
	 * Initialize.
	 */
	public void initialize()
	{
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
		String packageName = new Object(){}.getClass().getPackage().getName();
		packagePath = packageName.replace(".", "/") + "/";

		/** Initialize logger instance */
		logger = LoggerFactory.getLogger(new Object(){}.getClass());
		try {
			/** The log config file path. */
			logConfigFilePath = packagePath + "log4j.file.properties";
			DataInputStream rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
			PropertyConfigurator.configure(rf);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();			
		}	
		logger.info("JUnit version " + junit.runner.Version.id());

		/** Initialize serializer instances */
		xmlSerializer = new XMLSerializer(SerializationType.XML);
		jsonSerializer = new JSONSerializer(SerializationType.JSON);
		binarySerializer = new BinaryFromJSONSerializer(SerializationType.BINARY);
	}

	// Make HltContentContainer from file and save to file with timing.
	/**
	 * Process input file.
	 *
	 * @param file the file
	 * @return true, if successful
	 */
	private boolean processInputFile( File file) {
		long start = System.nanoTime();  
		String filename = theArgs.outputFile;
		if ( isNullOrEmpty(filename)) filename = file.getName();
		xmlFilename = filename + ".xml";
		if ( !isNullOrEmpty( theArgs.outputDirectory )) {
			File f = new File(theArgs.outputDirectory);
			File x = new File(f,xmlFilename);
			xmlFilename = x.getAbsolutePath();
		}	
		HltContentContainer hltContentContainer = new HltContentContainer();
		//Document document = createDocument( file, hltContentContainer);
		
		/** Create a document object. */
		String docId = "001";
		Corpus corpus = null;
		String docType = "Text"; 
		String uri = file.getAbsolutePath();
		String language = "English";
		Document document = DocumentMaker.getInstance().createDefaultDocument(
                docId,
                corpus,
                docType,
                uri,
                language,
                uri,
                hltContentContainer,
                tokenizerType);
		
		hltContentContainer = doProcess(document, hltContentContainer);
		boolean bOK =  saveHLTCC( hltContentContainer  );	
		long seconds = TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
		System.out.format("Seconds=%d File=%s\n", seconds, file.getName());
		return bOK;
	}	

	// Make HltContentContainer from file list and save to file.
	/**
	 * Process list of files.
	 *
	 * @param algorithmConfig the algorithm config
	 * @param fileList the file list
	 * @return true, if successful
	 */
	public boolean processListOfFiles(String algorithmConfig, List<File> fileList )
	{		
		DocumentList documentList = new DocumentList();
		HltContentContainerList hltccList = new HltContentContainerList();
		for ( File file : fileList )		{
			HltContentContainer hltcc = new HltContentContainer();
			Document document = createDocument( file, hltcc);
			documentList.add( document );
			hltccList.add(hltcc);
		}
		doActivate(algorithmConfig);
		File file = new File(theArgs.inputList);
		String baseFilename = file.getName().replace(".sgm", "");
		File fOutDir = new File(theArgs.outputDirectory );
		File fOutBase = new File( fOutDir, baseFilename);
		String baseName =  fOutBase.getAbsolutePath();
		xmlFilename = baseName + ".output.xml";
		HltContentContainer hltContentContainer = doProcess(documentList,hltccList);
		return saveHLTCC( hltContentContainer  );
	}

	// Serialize HltContentContainer and optionally print as table and validate.
	/**
	 * Save hltcc.
	 *
	 * @param hltContentContainer the hlt content container
	 * @return true, if successful
	 */
	protected boolean saveHLTCC(HltContentContainer hltContentContainer  ) {
		if ( hltContentContainer == null ) 
		{
			System.out.println("Null HLT content container.");
			return false;
		}	
		String xmlTemp = serialize(hltContentContainer);
		boolean bOK = true;
		if ( theArgs.bValidateXml) 
		{
			bOK = doValidate( xmlTemp );
		}
		if ( theArgs.bTableOutput)
		{
			printHltContent.writeFile(xmlFilename, xmlFilename + ".txt");
		}
		return bOK;
	}

	/**
	 * Make output filename.
	 *
	 * @param file the file
	 * @return the string
	 */
//	protected String makeOutputFilename( String filename, String ext)
//	{
//		/** serialization */
//		String baseFilename = filename.replace(".sgm", "");
//		if ( ! isNullOrEmpty(xmlFilename) )
//		{
//			File fOutDir = new File(xmlFilename);
//			File fOutBase = new File( fOutDir, baseFilename);
//			baseFilename = fOutBase.getAbsolutePath();
//		}
//		return baseFilename + ".output." + ext;		
//	}

	/**
	 * Creates the document.
	 *
	 * @param file the file
	 * @return the document
	 */
	private static Document createDocument( File file, HltContentContainer hltcc )
	{
		/** Create a document object. */
		String docId = "001";
		Corpus corpus = null;
		String docType = "Text"; 
		String uri = file.getAbsolutePath();
		String language = "English";
		if (hltcc == null) hltcc = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDefaultDocument(docId, corpus, docType, uri, language, uri, hltcc);
		return document;
	}

	/**
	 * Serialize.
	 *
	 * @param hltContentContainer the hltContentContainer
	 * @return the string
	 */
	public String serialize(HltContentContainer hltContentContainer)
	{
		if ( hltContentContainer == null) return null;
		String xmlTemp = null;
		try {
			xmlTemp = xmlSerializer.serializeAsString(hltContentContainer);
			//System.out.println(xmlTemp);
			if ( !isNullOrEmpty(xmlFilename) )
			{
				System.out.println("Writing to:  " + xmlFilename);
				Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
			}
			if ( !isNullOrEmpty(jsonFilename) )
			{
				System.out.println("Writing to:  " + jsonFilename);
				String jsonTemp = jsonSerializer.serializeAsString(hltContentContainer);
				Writer.getInstance().writeToFile(jsonFilename,jsonTemp);
			}
			if ( !isNullOrEmpty(binaryFilename) )
			{
				System.out.println("Writing to:  " + binaryFilename);
				byte[] binTemp = binarySerializer.serializeAsByteArray(hltContentContainer);
				Writer.getInstance().writeToFile(binaryFilename,binTemp);
			}
			logger.info("files written with serialized values");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlTemp;
	}

	// Utilities

	/**
	 * Checks if is null or empty.
	 *
	 * @param s the s
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmpty(String s)
	{
		if ( s == null ) return true;
		return s.isEmpty();
	}

	/**
	 * File to lines.
	 *
	 * @param filename the filename
	 * @return the list
	 */
	public static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new java.io.File(filename).getAbsolutePath()));
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return lines;
	}

	/**
	 * Lines to file.
	 *
	 * @param filename the filename
	 * @param lines the lines
	 */
	public static void linesToFile(String filename, List<String> lines)
	{
		try{
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fstream);
			for(Iterator<String> i = lines.iterator(); i.hasNext(); ) {
				String line = i.next();
				writer.write(line);
				// Send to console also.
				System.out.println(line);
			}
			//Close the output stream
			writer.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Do validate.
	 *
	 * @param xmlTemp the xml temp
	 * @return true, if successful
	 */
	protected boolean doValidate( String xmlTemp )
	{
		// TODO - implement XSD validation here and in all subclasses.
		return true;
	}

	// Optionally can be overriden
	/**
	 * Do process.
	 *
	 * @param documentList the document list
	 * @return the hlt content container
	 */
	protected HltContentContainer doProcess(DocumentList documentList, HltContentContainerList hltccList)
	{
		System.err.println("Document list processing is not implemented for this algorithm");
		return null;
	}
}
