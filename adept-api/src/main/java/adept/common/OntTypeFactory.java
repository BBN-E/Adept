package adept.common;

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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.PrintUtil;
import java.util.*;
import java.io.*;

import adept.io.Reader;


/**
 * The Class Type.
 */
public class OntTypeFactory implements ITypeFactory {

	/** The ont type catalog. */
	private Properties ontTypeCatalog;
	
	/** The ontology identifier to full URI mapping. */
	private Properties ontURIDirectory;
	
	/** The Ont Model Specification. */
	private OntModelSpec ontModelSpec;
	
	/** Map containing the model instances. */
	private HashMap<String, OntModel> models;
	
	/** The instance map. */
	private static HashMap<String,OntTypeFactory> instances = new HashMap<String, OntTypeFactory>();
	
	
	/**
	 * Gets the single instance of RelationTypeFactory.
	 *
	 * @param ontTypeCatalog the ont type catalog
	 * @return single instance of RelationTypeFactory
	 */
	public static OntTypeFactory getInstance(String ontTypeCatalog) {
		try
		{
			OntTypeFactory otFactory;
			if (instances.get(ontTypeCatalog) == null) {
				otFactory = new OntTypeFactory();
				otFactory.models = new HashMap<String, OntModel>();
				otFactory.loadOntTypeCatalog(ontTypeCatalog);
				otFactory.loadOntURIDirectory("adept/common/IdentifierToURIMapping.xml");
				// set up the reasoner to use
				otFactory.ontModelSpec = OntModelSpec.OWL_MEM;
				Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
				otFactory.ontModelSpec.setReasoner(reasoner);
				
				//insert into hashmap
	            instances.put(ontTypeCatalog, otFactory);
			}
			else {
				//System.out.println("Found a factory instance previously made!");
				otFactory = instances.get(ontTypeCatalog);
			}
			return otFactory;
		}
		catch(InvalidPropertiesFormatException e)
		{
			System.out.println("Trouble loading ontology mapping file: " + ontTypeCatalog);
			e.printStackTrace();
			return null;
		}
		catch(IOException e)
		{
			System.out.println("Trouble loading ontology mapping file: " + ontTypeCatalog);
			e.printStackTrace();
			return null;
		}
		
	}
	

	/**
	 * Gets the single instance of OntTypeFactory.
	 *
	 * @return single instance of OntTypeFactory
	 */
	public static OntTypeFactory getInstance()
	{
			return getInstance("adept/common/OntologyMapping.xml");
	}
	
	
	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public IType getType(String type) {
		String[] temp = type.split("#");
		String schemaLocation = ontTypeCatalog.getProperty(temp[0]);
		System.out.println("Schema location: " + schemaLocation);
		if (schemaLocation == null) {
			return null;
		}
		OntModel model;	
		if (models.get(schemaLocation) == null)
		{
			// load the ontology schema into a Jena Model instance
			//System.out.println("file: " + ClassLoader.getSystemResource(schemaLocation).toString());
			Model schemaModel = FileManager.get().loadModel(schemaLocation);
			model = ModelFactory.createOntologyModel(ontModelSpec, schemaModel);
			
			// insert into hashmap
			models.put(schemaLocation, model);
		}
		else
		{
			//System.out.println("Found a model previously made");
			//model = models.get(schemaLocation);
		}
		
		//return new OntType(model, temp[0], "#"+temp[1]);
    return new OntType(temp[0], temp[1]);
	}
	
	/**
	 * Gets the type.
	 *
	 * @param ontology the ontology
	 * @param type the type
	 * @return the type
	 */
	public IType getType(String ontology, String type) {
		String schemaLocation = ontTypeCatalog.getProperty(ontology);
		if (schemaLocation == null) {
			System.out.println("Null schema location!");
			return null;
		}
		OntModel model;	
		if (models.get(schemaLocation) == null)
		{
			// load the ontology schema into a Jena Model instance
			Model schemaModel = FileManager.get().loadModel(schemaLocation);
			model = ModelFactory.createOntologyModel(ontModelSpec, schemaModel);
			
			// insert into hashmap
			models.put(schemaLocation, model);
		}
		else
		{
			//System.out.println("Found a model previously made");
			//model = models.get(schemaLocation);
		}
		
		//return new OntType(model, ontology, ontURIDirectory.getProperty(ontology)+"#"+type);
                return new OntType(ontology, type);
	}
	
	/**
	 * Load ontology type catalog.
	 *
	 * @param ontTypeCatalogName the ont type catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadOntTypeCatalog(String ontTypeCatalogName) {
		try
		{
			ontTypeCatalog = new Properties();
			Reader.getInstance();
			InputStream is = Reader.findStreamInClasspathOrFileSystem(ontTypeCatalogName);		
			ontTypeCatalog.loadFromXML(is);
		}
		catch(InvalidPropertiesFormatException e)
		{
			System.out.println("Failed to load ontology type catalog:" + ontTypeCatalogName);
			e.printStackTrace();
		}
		catch(IOException e)
		{
			System.out.println("Failed to load ontology type catalog:" + ontTypeCatalogName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Load ont uri directory.
	 *
	 * @param ontURIDirectoryName the ont uri directory name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadOntURIDirectory(String ontURIDirectoryName) throws InvalidPropertiesFormatException, IOException {
		ontURIDirectory = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				ontURIDirectoryName);		
		ontURIDirectory.loadFromXML(is);
		
	}
    
    
}
