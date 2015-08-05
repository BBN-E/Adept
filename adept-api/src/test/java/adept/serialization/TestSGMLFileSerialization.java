/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.serialization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


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
