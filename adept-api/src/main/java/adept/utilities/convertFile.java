package adept.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


/**
 * The Class convertFile.
 */
public class convertFile {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException
	{
		FileInputStream fis = new FileInputStream("resources/adept/module/StanfordCoreNlpProcessorTestInput.txt");
	    InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
	    BufferedReader in = new BufferedReader(isr);
	    FileOutputStream fos = new FileOutputStream("test.out");
	    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
	    BufferedWriter out = new BufferedWriter(osw);

	    int ch;
	    while ((ch = in.read()) > -1) {
	        out.write(ch);
	    }

	    out.close();
	    in.close();
	}
	

}
