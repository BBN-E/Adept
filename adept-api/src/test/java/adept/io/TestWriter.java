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

import java.io.UnsupportedEncodingException;


/**
 * Simple test class to test methods in adept.io.Writer
 */
public class TestWriter {

	/**
	 * Test write string to file.
	 * 
	 * @param path
	 *            the path
	 * @param data
	 *            the data
	 */
	public void testWriteStringToFile(String path, String data) {
		Writer.getInstance().writeToFile(path, data);
	}

	/**
	 * Test write bytes to file.
	 * 
	 * @param path
	 *            the path
	 * @param data
	 *            the data
	 */
	public void testWriteBytesToFile(String path, byte[] data) {
		Writer.getInstance().writeToFile(path, data);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		TestWriter tw = new TestWriter();
		String data = "hello world\nhello world, again!";
		tw.testWriteStringToFile("WriteString.txt", data);
		tw.testWriteBytesToFile("WriteBytes.txt", data.getBytes("UTF-8"));
	}
}