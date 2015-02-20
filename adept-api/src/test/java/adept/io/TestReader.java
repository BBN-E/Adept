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

package adept.io;

import adept.serialization.*;
import adept.utilities.printHltContent;
import adept.common.HltContentContainer;
import adept.common.Argument;
import adept.common.Event;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.*;

// TODO: Auto-generated Javadoc
/**
 * Simple test class to test methods in adept.io.Reader
 */
public class TestReader {

	/**
	 * Test read into string.
	 * 
	 * @param path
	 *            the path
	 * @return the string
	 */
	public String testReadIntoString(String path) {
		return Reader.getInstance().readFileIntoString(path);
	}

	/**
	 * Test read into bytes.
	 * 
	 * @param path
	 *            the path
	 * @return the byte[]
	 */
	public byte[] testReadIntoBytes(String path) {
		return Reader.getInstance().readFileIntoByteArray(path);
	}

	/**
	 * Test read xml.
	 * 
	 * @param path
	 *            the path
	 * @return the document
	 */
	public org.w3c.dom.Document testReadXML(String path) {
		return Reader.getInstance().readXML(path);

	}

	/**
	 * Test read ere.
	 *
	 * @return the document
	 */
    @Test
    public void testEREtoHltContentContainer()
    {
        Reader r = Reader.getInstance();
        HltContentContainer hltcc = r.EREtoHltContentContainer(ClassLoader.getSystemResource("adept/io/PROXY_AFP_ENG_20040210.0365.txt").getPath(),ClassLoader.getSystemResource("adept/io/PROXY_AFP_ENG_20040210.0365.ere.xml").getPath(), "English");
        for(Event e : hltcc.getEvents())
        {
            for(Argument arg : e.getArguments())
            {
                System.out.println("original:" + arg.getArgumentType());
            }
        }

        try
        {
        XMLSerializer xmlSerializer = new XMLSerializer(SerializationType.XML);
        String serialized = xmlSerializer.serializeAsString(hltcc);
        
        HltContentContainer deserialized = (HltContentContainer)xmlSerializer.deserializeString(serialized, HltContentContainer.class);
        System.out.println("after serialization/deserialization");
        for(Event e : deserialized.getEvents())
        {
            for(Argument arg : e.getArguments())
            {
                System.out.println("after serialization/deserialization:" + arg.getArgumentType());
            }
        }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("No exception caught while reading ERE to HltCC");
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		TestReader tr = new TestReader();
        //        String path = ClassLoader.getSystemResource("adept/test/sample.txt")
        //        				.getFile();
		// String xmlpath = "C:\\Users\\ssriniva\\input.txt.xml";
        //		System.out.println(tr.testReadIntoString(path));
        String erePath = args[0];
        String xmlPath = args[1];
        String outPath = args[2];

	}
}