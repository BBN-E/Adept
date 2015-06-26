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
