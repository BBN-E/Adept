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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
// Next line is site-specific.
package adept.utilities;


import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.io.FileNotFoundException;

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.module.AdeptModuleException;
import adept.utilities.RegressionTest;



/**
 * Test class.
 */
public class TestRegressionTestClass extends RegressionTest 
{
	// The only site-specific members are config file and processor instance.
	private static String configFilename = "RegressionTestConfig.xml";
	
	private static boolean testErrorCase = false;
	
	/**
	 * Initialize.
	 */
	public static void main(String[] args)
	{
		System.out.println("Tests SUCCESS case by default. Provide cmdline arg \"ERROR\" to test ERROR case.");
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
		String packageName = new Object(){}.getClass().getPackage().getName();
		String packagePath = packageName.replace(".", "/") + "/";
		try {
			if(args.length>0 && args[0].equals("ERROR"))
			{
				testErrorCase=true;
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			new TestRegressionTestClass().Run(packagePath, configFilename, args);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// The rest is applicable to any subclass of RegressionTest.

	protected void doActivate(String workingDirectory, String algorithmConfig)
	{
		// no-op
	}

	protected void doDeactivate()
	{
	    // no-op
	}

	/**
	 * Creates the hlt content container.
	 *
	 * @param document the document
	 * @return the hlt content container
	 */
	protected HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer)
	{
		try {
		if(testErrorCase) {
			throw new RuntimeException("simulated exception");
			//return null;
		}
		} catch (Exception e) {
			System.out.println("in catch");
			e.printStackTrace();
			return null;
		}
		return new HltContentContainer();
	}

}