/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., November 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.utilities;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Pair;
import adept.common.ConversationElementTag;
import adept.io.Writer;
import adept.io.Reader;

import adept.serialization.BinaryFromJSONSerializer;
import adept.serialization.JSONSerializer;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;
import adept.utilities.DocumentMaker;
import adept.utilities.CommandLineArgParse;
import adept.utilities.DeftReaderArgParse;
import adept.io.LDCCorpusReader;

import edu.stanford.nlp.dcoref.*;

// TODO: Auto-generated Javadoc
/**
 * The Class DeftReaderApp.
 */
public class DeftReaderApp
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

	/** The package path. */
	private static String packagePath;

	/** The config file path. */
	private static String configFilePath;

	/** Command line arguments. */
	private static DeftReaderArgParse theArgs;

    /** The serialization type. */
    private static SerializationType serializationType;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public void Run(String[] args)
	{
		/** initialize and configure */
		theArgs = new DeftReaderArgParse( args);
		if ( theArgs.bHelp || theArgs.bVersion) return;
		if ( !theArgs.bRunOneFile && !theArgs.bRunList) 
		{
			System.out.println("Invalid arguments - exiting.");
			return;
        }
		//
		initialize();
		boolean bFirst = true;
		int fileCount = 0;
		boolean bSuccess = true;
		if ( theArgs.bRunOneFile)
		{
    		File fInFile = new File(theArgs.inputFile);
            File fAnnFile = null;
            if(!isNullOrEmpty(theArgs.annotationFile))
                fAnnFile = new File(theArgs.annotationFile);
            if(!fInFile.exists()) 
			{
                System.out.println("Input file not found:  " + theArgs.inputFile);
                logger.info("Input file not found:  " + theArgs.inputFile);
                return;
            }
            serialize(processInputFile(fInFile,fAnnFile), theArgs.outputFile);
            ++fileCount;
		}
        else if ( theArgs.bRunList)
		{
            File[] inFiles = getDirectoryContents(theArgs.inputDirectory);
            if(inFiles == null || inFiles.length == 0)
            {
                System.out.println("Unusable input directory - exiting.");
                logger.info("Unusable input directory - exiting.");
                return;
            }
            File outDir = new File(theArgs.outputDirectory);
            if(!outDir.exists())
            {
                if(!outDir.mkdirs())
                {
                    System.out.println("Unable to create directory: " + outDir.getAbsolutePath());
                    logger.info("Unable to create directory: " + outDir.getAbsolutePath());
                    return;
                }
                System.out.println("Created directory: " + outDir.getAbsolutePath());
                logger.info("Created directory: " + outDir.getAbsolutePath());
            }
            if(outDir == null)
            {
                System.out.println("Unusuable output directory - exiting.");
                logger.info("Unusuable output directory - exiting.");
            }
            boolean hasAnnotations = false;
            File[] annFiles = null;
            if(!isNullOrEmpty(theArgs.annotationDirectory))
            annFiles = getDirectoryContents(theArgs.annotationDirectory);
            hasAnnotations = annFiles != null && annFiles.length > 0;
            for(int x = 0; x < inFiles.length; x++)
            {
				File fInFile = new File(inFiles[x].getAbsolutePath());
                File fOutFile = new File(outDir, fInFile.getName());
                File fAnnFile = null;
                if(hasAnnotations)
                {
                    File fAnnFileCandidate = new File(annFiles[x].getAbsolutePath());
                    boolean corr = filesCorrespond(fInFile,fAnnFileCandidate);
                    if(corr)
                        fAnnFile = fAnnFileCandidate;
                    else
                    {
                        System.out.println("trying again: " + fInFile.getName());
                        File newFAnnFileCandidate = null;
                        int dot = fInFile.getName().indexOf(".");
                        String name = fInFile.getName().substring(0,dot);
                        System.out.println("name: " + name);
                        for(int attempt = 0; attempt < annFiles.length; attempt++)
                        {
                            if(annFiles[attempt].getName().contains(name))
                            {
                                newFAnnFileCandidate = annFiles[attempt];
                            }
                        }
                        System.out.println("trying: " + newFAnnFileCandidate.getAbsolutePath());
                        fAnnFile = filesCorrespond(fInFile,newFAnnFileCandidate) ? newFAnnFileCandidate : null;
                    }
                }
                HltContentContainer hltcc = processInputFile(fInFile,fAnnFile);
                serialize(hltcc, fOutFile.getAbsolutePath());
                bFirst = false;
                ++fileCount;

                Scanner in = new Scanner(System.in);
                String input = in.nextLine();
            }
		}
        System.out.println("Processed " + fileCount + " files.");
        logger.info("Processed " + fileCount + " files.");
	}

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
     * Initializes logger, serializers, and such.
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
		logger.info("Current Directory: " + System.getProperty("user.dir"));
		logger.info("JUnit version " + junit.runner.Version.id());


		/** Initialize serializer instances */
		xmlSerializer = new XMLSerializer(SerializationType.XML);
		jsonSerializer = new JSONSerializer(SerializationType.JSON);
		binarySerializer = new BinaryFromJSONSerializer(SerializationType.BINARY);

        if(theArgs.inputFormat == null)
        {
            theArgs.inputFormat = "text";
            System.out.println("No input format specified. Defaulting to 'text'.");
            logger.info("No input format specified. Defaulting to 'text'.");
        }
        if(theArgs.outputFormat == null)
        {
            System.out.println("No output format specified. Defaulting to 'xml'.");
            logger.info("No output format specified. Defaulting to 'xml'.");
            theArgs.outputFormat = "xml";
        }
    }

    /**
     * Files correspond.
     *
     * @param file1 the file1
     * @param file2 the file2
     * @return true, if successful
     */
    private boolean filesCorrespond(File file1, File file2)
    {
        String f1Name = file1.getName().replaceAll("(\\.[a-zA-Z]*)+$","").replaceAll(".*/","");
        String f2Name = file2.getName().replaceAll("(\\.[a-zA-Z]*)+$","").replaceAll(".*/","");
        System.out.println(f1Name + "    :    " + f2Name);
        return f1Name.equals(f2Name);
    }

    /**
     * Gets the directory contents.
     *
     * @param dirPath the dir path
     * @return the directory contents
     */
    private File[] getDirectoryContents(String dirPath)
    {
        File dir = new File(dirPath);
        return dir.listFiles();
    }

	// Make HltContentContainer and save to file with timing.
	/**
	 * Process input file.
	 *
	 * @param fInFile the f in file
	 * @param fAnnFile the f ann file
	 * @return the hlt content container
	 */
    private HltContentContainer processInputFile( File fInFile, File fAnnFile) 
    {
    	HltContentContainer hltcc = null;
    	String inputFormat = theArgs.inputFormat;
        String inputLanguage = theArgs.inputLanguage;
        if(inputLanguage == null)
        {
            System.out.println("No input language found. Defaulting to English.");
            logger.info("No input language found. Defaulting to English.");
            inputLanguage = "English";
        }

    	if ( "text".equals(inputFormat.toLowerCase()) ) {
    		hltcc = processTextDocument(fInFile);
    	}
    	else if ( "sgml".equals(inputFormat.toLowerCase()) ) {
    		hltcc = processSGMDocument(fInFile);
    	}
    	else if ( "ere".equals(inputFormat.toLowerCase()) ) {
    		String annPath = null;
    		if(fAnnFile == null)
    		{
    			System.out.println("No annotation file found corresponding to " + fInFile.getName());
    			logger.info("No annotation file found corresponding to " + fInFile.getName());
    		}
    		else
    			annPath = fAnnFile.getAbsolutePath();
            System.out.println("input: " + fInFile.getAbsolutePath() + "\nannotation: " + annPath);
    		hltcc = Reader.getInstance().EREtoHltContentContainer(fInFile.getAbsolutePath(),annPath, inputLanguage);
    	}
    	else if ( "conll".equals(inputFormat.toLowerCase()) ) {
    		//process coNLL 2011
		hltcc = Reader.getInstance().CoNLLtoHltContentContainer(fInFile.getAbsolutePath());;
    	}
    	else {
    		System.out.println("Unrecognized input format: " + inputFormat + ". Defaulting to 'text'.");
    		logger.info("Unrecognized input format: " + inputFormat + ". Defaulting to 'text'.");
    	}
    	return hltcc;
    }	

    /**
     * Process conll document.
     *
     * @param file the file
     * @return the hlt content container
     */
    /*    private HltContentContainer processCoNLLDocument(String filename)
    {
        return Reader.getInstance().CoNLLtoHltContentContainer(filename);;
        }*/

    /**
     * Process sgm document.
     *
     * @param file the file
     * @return the hlt content container
     */
    private HltContentContainer processSGMDocument( File file )
    {
        return processDefaultDocument(file, "SGM");
    }

    /**
     * Process text document.
     *
     * @param file the file
     * @return the hlt content container
     */
    private HltContentContainer processTextDocument( File file )
    {
        return processDefaultDocument(file, "Text");
    }

    /**
     * Process default document.
     *
     * @param file the file
     * @param docType the doc type
     * @return the hlt content container
     */
    private HltContentContainer processDefaultDocument(File file, String docType)
    {
        /** Create a document object. */
    	String docId = "001";
    	Corpus corpus = null;
		String uri = file.getAbsolutePath();
		String language = "English";
		HltContentContainer hltcc = new HltContentContainer();
        Document document = DocumentMaker.getInstance().createDefaultDocument(docId, corpus, docType, uri, language, uri, hltcc);
        //Document document = DocumentMaker.getInstance().createCleanedXMLDocument(docId, corpus, docType, uri, language, uri, hltcc, true);
        //        List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();			
        //        String text = Reader.getInstance().readFileIntoString(file.getAbsolutePath());
        //        Pair<List<Tag>,Document> docAndTags = LDCCorpusReader.getInstance().readCorpus(text, passageAttributesList, corpus, uri, language);
		return hltcc;
    }

    /**
     * Clean.
     *
     * @param unclean the unclean
     * @return the string
     */
    private String clean(String unclean)
    {
        return unclean.replaceAll("-LRB-", "(")
            .replaceAll("-RRB-", ")")
            .replaceAll("-LSB-", "[")
            .replaceAll("-RSB-", "]")
            .replaceAll("-LCB-", "{")
            .replaceAll("-RCB-", "}")
            .replaceAll("``","\"")
            .replaceAll("&apos;&apos;", "\"")
            .replaceAll("&apos;", "\'")
            .replaceAll("\\.\\.\\.", ".");
    }

	/**
	 * Serialize.
	 *
	 * @param hltcontentcontainer the hltcontentcontainer
	 * @param outputFilename extensionless full filepath to write serialized results too
	 * @return the string
	 */
    public String serialize(HltContentContainer hltcontentcontainer, String outputFilename)
    {
    	if ( hltcontentcontainer == null) return null;
    	String outputFormat = theArgs.outputFormat;
    	String serialized = null;
    	try {
    		if ( !isNullOrEmpty(outputFilename))
    		{
    			if ( "xml".equals(outputFormat.toLowerCase()) ) {
    				serialized = xmlSerializer.serializeAsString(hltcontentcontainer);
    				outputFilename += ".xml";
    				if ( theArgs.bTableOutput)
    					printHltContent.writeFile(outputFilename, outputFilename + ".txt");
    			}
    			else if ( "json".equals(outputFormat.toLowerCase()) ) {
    				serialized = jsonSerializer.serializeAsString(hltcontentcontainer);
    				outputFilename += ".json";
    			}
                else if ( "binary".equals(outputFormat.toLowerCase()) ) {
    				byte[] array = binarySerializer.serializeAsByteArray(hltcontentcontainer);
    				serialized = new String(array);
    				outputFilename += ".bin";
    			}
    			else {
    				System.out.println("Unrecognized output format: " + outputFormat + ". Defaulting to 'xml'.");
    				serialized = xmlSerializer.serializeAsString(hltcontentcontainer);
    				outputFilename += ".xml";
    			}

    			System.out.println("Writing to:  " + outputFilename);
    			Writer.getInstance().writeToFile(outputFilename,clean(serialized));
    			logger.info("file written with serialized values");
    		}
    		else
    		{
    			logger.info("Serialized values could not be written to file. Output filename undefined.");
    		}

    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return serialized;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String args[])
    {
        DeftReaderApp dra = new DeftReaderApp();
        dra.Run(args);
    }
}