/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.io;

import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
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
