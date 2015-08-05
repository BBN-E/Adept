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