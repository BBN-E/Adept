package adept.io;

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.io.Reader;

// TODO: Auto-generated Javadoc
/**
 * The Class TestLDCCorpusReader.
 */
public class TestCoNLLReader {

	/**
	 * Test.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void test(String filename) {
        HltContentContainer hltcc = Reader.getInstance().CoNLLtoHltContentContainer( filename );
		System.out.println("Document: " + hltcc.getDocument() + "  Doc text: " + hltcc.getDocument().getValue());
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		try
		{
			TestCoNLLReader reader = new TestCoNLLReader();
			reader.test(Reader.getInstance().getAbsolutePathFromClasspathOrFileSystem("adept/module/wsj_0020.v2_gold_skel"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

}