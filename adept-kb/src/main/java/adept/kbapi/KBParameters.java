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

package adept.kbapi;

import java.util.*;
import java.lang.*;
import java.io.DataInputStream;

import adept.io.Reader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Reads the KBParameters.xml configuration file,
 * and sets all KB parameters at the outset.
 *
 */
public class KBParameters {
	
	public static final String ADEPT_KB_IDENTIFIER = "ADEPT-KB"; 
	
	private static String configFilename = "KBParameters.xml";
	
	public static final String ontologyBasePrefix;
	
	public static final String ontologyCorePrefix;
	
	public static final String dataInstancesPrefix;
	
	public static final String tripleStoreUrl;
	
	public static final String metadataUrl;
	
	public static final String metadataUsername;
	
	public static final String metadataPassword;
	
	public static final String tsService;
	
	public static final String remoteTokenStreamPath;

	public static final String localTokenStreamCache;
	
	public static final Map<String,String> adeptTypes;
	
	public static final Map<String, String> ontologyMap;
	
	public static final Map<String,String> reverseOntologyMap;
	
	
    static 
    {	
    	String packageName = new Object(){}.getClass().getPackage().getName();
		String packagePath = packageName.replace(".", "/") + "/";
		
		/** The config file path. */
		String configFilePath = packagePath + configFilename;
		Properties testAppConfig = new Properties();
        try
         {
		   DataInputStream dis = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(configFilePath));
		   testAppConfig.loadFromXML(dis);
         }
        catch(Exception e)
         {
        	System.out.println("Could not load KB config file");
        	e.printStackTrace();
         }
       
       // instantiate KBParameters
	   ontologyBasePrefix = testAppConfig.getProperty("ontologyBasePrefix");
	   ontologyCorePrefix = testAppConfig.getProperty("ontologyCorePrefix");
	   dataInstancesPrefix = testAppConfig.getProperty("dataInstancesPrefix");
	   tripleStoreUrl = testAppConfig.getProperty("tripleStoreUrl");
	   metadataUrl = testAppConfig.getProperty("metadataUrl");
	   metadataUsername = testAppConfig.getProperty("metadataUsername");
	   metadataPassword = testAppConfig.getProperty("metadataPassword");
	   remoteTokenStreamPath = testAppConfig.getProperty("remoteTokenStreamPath");
	   localTokenStreamCache = testAppConfig.getProperty("localTokenStreamCache");
	   tsService = testAppConfig.getProperty("tokenStreamService");
	   
	   
	   // load Adept ontology types
	   adeptTypes = new HashMap<String, String>();
	   try
	   {
		   Properties mappingProps = new Properties();
		   mappingProps.loadFromXML(Reader.findStreamInClasspathOrFileSystem("adept/kbapi/adeptOntologyTypes.xml"));
		   Set<String> keys = mappingProps.stringPropertyNames();
		   for(String key : keys)
		   {
			   adeptTypes.put(key, mappingProps.getProperty(key));
		   }
	   }
	   catch(Exception e)
	   {
		   System.out.println("Could not load Adept types: " + e.getMessage());   
	   }
	   
	   
	   //populate hashmap with ontology mapping
	   ontologyMap = new HashMap<String, String>();
	   try
	   {
		   Properties mappingProps = new Properties();
		   mappingProps.loadFromXML(Reader.findStreamInClasspathOrFileSystem(testAppConfig.getProperty("ontologyMappingFile")));
		   Set<String> keys = mappingProps.stringPropertyNames();
		   for(String key : keys)
		   {
			   ontologyMap.put(key.toLowerCase(), mappingProps.getProperty(key));
		   }
	   }
	   catch(Exception e)
	   {
		   System.out.println("Could not load ontology mapping: " + e.getMessage() + "....Assuming that user will attempt to populate Adept ontology "
		   		+ "types directly.");   
	   }
	   
	   
	   //populate hashmap with reverse ontology mapping
	   reverseOntologyMap = new HashMap<String, String>();
	   try
	   {
		   Properties mappingProps = new Properties();
		   mappingProps.loadFromXML(Reader.findStreamInClasspathOrFileSystem(testAppConfig.getProperty("reverseOntologyMappingFile")));
		   Set<String> keys = mappingProps.stringPropertyNames();
		   for(String key : keys)
		   {
			   reverseOntologyMap.put(key.toLowerCase(), mappingProps.getProperty(key));
		   }
	   }
	   catch(Exception e)
	   {
		   System.out.println("Could not load reverse ontology mapping: " + e.getMessage());
	   }
	   
	}
    
}