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

package adept.kbapi.unittests;

import adept.serialization.*;
import adept.utilities.*;
import adept.common.*;
import adept.kbapi.*;

import org.junit.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.util.*;

/**
 * Main class to run all adept-kb unit tests.
 */
public class RunAllKBUnitTests
{
	public static void main(String[] args) 
	{
		
	      Result result = JUnitCore.runClasses(AllKBUnitTests.class);
	      if(result.getFailures().size() == 0)
	      {
	    	  AllKBUnitTests.print();
	      }
	      for (Failure failure : result.getFailures()) 
	      {
	         System.out.println("Failure log: " + failure.toString());
	      }
	      System.out.println("Number of tests run: " + result.getRunCount());
	      System.out.println("Time taken to run all tests: " + result.getRunTime()/1000+"s");
	      System.out.println("DID ALL TESTS PASS? : " + result.wasSuccessful());
	      
	     if(!result.wasSuccessful())
	      {
	    	  // delete TestKbUri.getEntityUri() and TestKbUri.getRelationUri()
	    	  KBUpdateProcessor updateProcessor = new KBUpdateProcessor();
	    	  boolean entityDeleted = updateProcessor.deleteKBObject(TestKBUri.getKBEntityUri());
	    	  boolean relationDeleted = updateProcessor.deleteKBObject(TestKBUri.getKBRelationUri());
	    	  
	    	  if(!entityDeleted)
	    	  {
	    		  System.out.println("Deleting entity failed. Please manually delete entity ID " + TestKBUri.getKBEntityUri());
	    	  }
	    	  
	    	  if(!relationDeleted)
	    	  {
	    		  System.out.println("Deleting relation failed. Please manually delete relation ID " + TestKBUri.getKBRelationUri());
	    	  }
	      }
	   }
}