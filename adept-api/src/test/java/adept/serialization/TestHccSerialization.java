package adept.serialization;

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
			// Create an instance of XMLSerializer
			XMLSerializer deserializer = new XMLSerializer(
					SerializationType.XML);

			String filename = "adept/test/fbis_eng_20010901.0003.txt.xml";
			InputStream is = ClassLoader.getSystemResourceAsStream(filename);					
			String content = Reader.getInstance().convertStreamToString(is);
			//String file = ClassLoader.getSystemResource(filename).toString();
			//System.out.println(file);
			//String content = Reader.getInstance().readFileIntoString(file);
			
			HltContentContainer hcc;
			
			if (content != null) {								
				hcc = (HltContentContainer) deserializer
						.deserializeString(content,
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
