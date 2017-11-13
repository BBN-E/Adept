/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

package adept.io;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;



/**
 * The Class Writer.
 */
public class Writer {

	/** singleton instance. */
	private static Writer instance;

	/**
	 * Gets the single instance of Writer.
	 * 
	 * @return single instance of Writer
	 */
	public static Writer getInstance() {
		if (instance == null)
			instance = new Writer();
		return instance;
	}

	/**
	 * Writes input byte array to specified file.
	 * 
	 * @param path
	 *            the path
	 * @param data
	 *            the data
	 */
	public void writeToFile(String path, byte[] data) {
		try {
			File file = new File(path);
			if (file.exists())
				file.delete();
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			FileChannel fc = raf.getChannel();
			ByteBuffer bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, data.length);
			bb.put(data);
			fc.close();
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write to file.
	 *
	 * @param filename the filename
	 * @param data the data
	 */
	public void writeToFile(String filename, String data)
	{
		try ( java.io.Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
		    out.write(data);
                    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Lines to file.
	 *
	 * @param filename the filename
	 * @param lines the lines
	 */
	public void linesToFile(String filename, List<String> lines)
	{
		try{
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			for(Iterator<String> i = lines.iterator(); i.hasNext(); ) {
				String line = i.next();
				out.write(line);
				// Send to console also.
				System.out.println(line);
			}
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}