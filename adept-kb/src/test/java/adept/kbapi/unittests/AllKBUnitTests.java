package adept.kbapi.unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.*;


@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestInsertEntity.class,
   TestInsertRelation.class,
   TestKBQueries.class,
   TestUpdateEntity.class,
   TestUpdateRelation.class,
   TestDeleteKBObject.class,  
})

/**
 * JUnit test suite for adding all adept-kb
 * unit tests.
 *
 */
public class AllKBUnitTests 
{   
	public static void print()
	{
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertEntity run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertRelation run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertNumber run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestInsertDate run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestKbQueries run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestUpdateEntity run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestUpdateRelation run as expected.");
		System.out.println("All methods in class adept.kbapi.unittests.TestDeleteKbObject run as expected.");
	}
	
}  	
