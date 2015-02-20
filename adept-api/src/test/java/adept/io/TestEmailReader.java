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

import adept.common.HltContentContainer;
import adept.serialization.XMLSerializer;
import adept.serialization.JSONSerializer;
import adept.serialization.SerializationType;
import adept.utilities.printHltContent;

import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * Simple test class to test methods in adept.io.Reader
 */
public class TestEmailReader {


	/**
	 * Test read email.
	 *
	 * @param messagePath the message path
	 * @param headerPath the header path
	 * @param outPath the out path
	 * @param pruneFooter the prune footer
	 * @return the document
	 */
	public HltContentContainer testReadEmail(String messagePath, String headerPath, String outPath, boolean pruneFooter) {
        HltContentContainer hltcc =  EmailReader.getInstance().readEmail(messagePath, headerPath, pruneFooter);
        try
        {
            JSONSerializer json = new JSONSerializer(SerializationType.JSON);
            String jsonString = json.serializeAsString(hltcc);
            jsonString = jsonString.replaceAll("-LRB-", "(")
                .replaceAll("-RRB-", ")")
                .replaceAll("-LSB-", "[")
                .replaceAll("-RSB-", "]")
                .replaceAll("-LCB-", "{")
                .replaceAll("-RCB-", "}")
                .replaceAll("``","\"")
                .replaceAll("&apos;&apos;", "\"")
                .replaceAll("&apos;", "\'")
                .replaceAll("\\.\\.\\.", ".");
            PrintStream console = System.out;
            try {
                File out = new File(outPath);
                System.setOut(new PrintStream(out));
                System.setErr(new PrintStream(out));
                System.out.println(jsonString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally
                {
                    System.setErr(console);
                    System.setOut(console);
                }

            //            printHltContent.writeFile(outPath,outPath+".txt");
        } catch(Exception e)
            {
                e.printStackTrace();
            }

        return hltcc;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		TestEmailReader ter = new TestEmailReader();
        /*        String messagePath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/Argosy/ArgosyMessages/Argosy/Dahilia Abdul Rahman/Sent Items/message_6/Sent Items_part-006.ksh";
        String headerPath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/Argosy/ArgosyMessages/Argosy/Dahilia Abdul Rahman/Sent Items/message_6/message_6.head";
        String outPath =  "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/SerializedMessages/Dahilia Abdul Rahman.xml";
        System.out.println(ter.testReadEmail(messagePath,headerPath,outPath,true));

        messagePath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/Argosy/ArgosyMessages/Argosy/David Millott/Sent Items/message_1/Sent Items_part-001.ksh";
        headerPath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/Argosy/ArgosyMessages/Argosy/David Millott/Sent Items/message_1/message_1.head";
        outPath =  "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/SerializedMessages/David Millott.xml";
        System.out.println(ter.testReadEmail(messagePath,headerPath,outPath,true));
        */
        String dirPath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/Argosy/ArgosyMessages/Argosy/Dahilia Abdul Rahman/Sent Items/";
        File dir = new File(dirPath);
        System.out.println(dirPath);
        for(File subdir : dir.listFiles())
        {
            if(subdir.isDirectory())
            {
                String messagePath = null;
                String headerPath = null;
                String outPath = "/nfs/mercury-04/u40/deft/ftp_drops/SAIC/user_data/SerializedMessages/" + subdir.getAbsolutePath().replaceAll(".*/Argosy/","").replaceAll(" ","_").replaceAll("/","-") + ".hcc.json";
                for(File f : subdir.listFiles())
                {
                    System.out.println("\t\t" + f.getName());
                    if(f.getName().endsWith(".ksh"))
                        messagePath = f.getAbsolutePath();
                    else if(f.getName().endsWith(".head"))
                        headerPath = f.getAbsolutePath();
                }
                System.out.println("Processing: " + outPath);
                System.out.println("\t: " + messagePath);
                System.out.println("\t: " + headerPath);
                if(messagePath != null && headerPath != null)
                    System.out.println(ter.testReadEmail(messagePath,headerPath,outPath,false));
            }
        }
        
	}
}