package adept.kbapi.unittests;

import adept.serialization.*;
import adept.utilities.*;
import adept.common.*;
import adept.kbapi.*;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Unit test class to test deletion
 */
public class TestDeleteKBObject {
	
	@Test
	public void testDeleteKBObject()
	{
		try
		{
		    KBUpdateProcessor updateProcessor = new KBUpdateProcessor();
						
			boolean success = updateProcessor.deleteKBObject(TestKBUri.getKBEntityUri());
			assertTrue(success);
			
			success = updateProcessor.deleteKBObject(TestKBUri.getKBRelationUri());
			assertTrue(success);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}