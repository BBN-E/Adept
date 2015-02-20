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

package adept.serialization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * Test to simply serialize and deserialize an SGML file.
 */
public class TestSGMLFileSerialization {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		try {
			// Create an instance of XMLSerializer
			XMLSerializer bs = new XMLSerializer(SerializationType.XML);

			// Serialization
			String serialized = bs
					.serializeAsString(new BufferedReader(
							new FileReader(
									"C:\\d4m\\ears\\git\\deft\\resources\\adept\\test\\eng-NG-31-100100-8816523.sgm")));
			System.out.println(serialized);

			// deserialization
			BufferedReader deserialized = (BufferedReader) bs
					.deserializeString(serialized, BufferedReader.class);
			System.out.println("line::: " + deserialized.readLine());
			System.out.println("line::: " + deserialized.readLine());
			System.out.println("line::: " + deserialized.readLine());
			System.out.println("line::: " + deserialized.readLine());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}