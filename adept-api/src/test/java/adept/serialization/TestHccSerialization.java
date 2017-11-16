package adept.serialization;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import adept.common.*;
import adept.io.Reader;


/**
 * The Class TestHccSerialization.
 */
public class TestHccSerialization {

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		try {
			// Create an instance of XMLStringSerializer
			XMLStringSerializer deserializer = new XMLStringSerializer();

			String filename = "adept/test/fbis_eng_20010901.0003.txt.xml";
			InputStream is = ClassLoader.getSystemResourceAsStream(filename);					
			String content = Reader.getInstance().convertStreamToString(is);
			//String file = ClassLoader.getSystemResource(filename).toString();
			//System.out.println(file);
			//String content = Reader.getInstance().readFileIntoString(file);
			
			HltContentContainer hcc;
			
			if (content != null) {								
				hcc = (HltContentContainer) deserializer
						.deserializeFromString(content,
								HltContentContainer.class);
				System.out.println("Success in deserializing HltContentContainer in file: " + filename);
			} else {
				
				System.out.println("Error deserializing HltContentContainer in file: " + filename);						
			}					

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
