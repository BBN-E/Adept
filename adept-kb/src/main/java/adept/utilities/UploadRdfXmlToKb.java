package adept.utilities;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.*;
import adept.io.Reader;

public class UploadRdfXmlToKb 
{
	// takes in path of the RDF/XML file to upload as parameter
	public static void main(String[] args)
	{
		try
		{
			String serviceURI = "http://deft-dev-01.bbn.com:7600/deft/data";
			DatasetAccessorFactory factory = new DatasetAccessorFactory();
			DatasetAccessor accessor;
			accessor = factory.createHTTP(serviceURI);
			
			Model m = ModelFactory.createDefaultModel();
			m.read(adept.io.Reader.getInstance().getAbsolutePathFromClasspathOrFileSystem(args[0]), "RDF/XML");
			accessor.putModel(m);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

