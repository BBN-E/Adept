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

package adept.utilities;

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

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.TokenizerType;
import adept.io.Reader;
import adept.io.Writer;
import adept.serialization.JSONBinarySerializer;
import adept.serialization.JSONStringSerializer;
import adept.serialization.XMLStringSerializer;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;



/**
 * The Class RegressionTest.
 */
public abstract class RegressionTest
{
	/** The xml serializer. */
	private static XMLStringSerializer xmlStringSerializer;

	/** The json serializer. */
	private static JSONStringSerializer jsonStringSerializer;

	/** The binary serializer. */
	private static JSONBinarySerializer jsonBinarySerializer;

	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

	/** The input directory. */
	private static String inputDirectory;

	/** The input file list. */
	private static List<String> inputFileList;

	/** The output directory. */
	private static String outputDirectory;

	/** The reference directory. */
	private static String referenceDirectory;

	/** The package path. */
	private static String packagePath;

	/** The working directory. */
	private static String workingDirectory;

	/** The config file path. */
	private static String configFilePath;

	/** The algorithm config. */
	private static String algorithmConfig;

	/** The binary filename. */
	private static String xmlFilename, jsonFilename, binaryFilename;

	/** The reader. */
	private static Reader theReader = new Reader();

	/** The writer. */
	private static Writer theWriter = new Writer();

	/**
	 * Do activate.
	 *
	 * @param workingDirectory the working directory
	 * @param algorithmConfig the algorithm config
	 */
	protected abstract void doActivate(String workingDirectory, String algorithmConfig) throws Exception;

	/**
	 * Do deactivate.
	 */
	protected abstract void doDeactivate() throws Exception;

	/**
	 * Do process.
	 *
	 * @param document the document
	 * @param hltContentContainer the HLT content container
	 * @return the hlt content container
	 */
	protected abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer) throws Exception;

	/**
	 * The Class TestFile.
	 */
	static class TestFile
	{
		/** The name. */
		public String name;

		/** The in bytes. */
		public long inBytes;

		/** The out chars xml. */
		public long outCharsXml;

		/** The out chars json. */
		public long outCharsJson;

		/** The out bytes bin. */
		public long outBytesBin;

		/** The total seconds. */
		public long totalSeconds;

		/** The process seconds. */
		public long processSeconds;

		/** The result. */
		public String result;

    	/**
	     * Instantiates a new test file.
	     *
	     * @param name the name
	     */
    	public TestFile(String name) {
	        this.name = name;
	        this.result = "";
	    }

    	/**
	     * Plus.
	     *
	     * @param tf the test file
	     */
    	public void plus(TestFile tf)
	    {
	      this.inBytes += tf.inBytes;
	      this.outCharsXml += tf.outCharsXml;
	      this.outCharsJson += tf.outCharsJson;
	      this.outBytesBin += tf.outBytesBin;
	      this.totalSeconds += tf.totalSeconds;
	      this.processSeconds += tf.processSeconds;
	      if ( !tf.result.equals("pass")) this.result = "fail";
	    }

    	/**
	     * Divide.
	     *
	     * @param tf the test file
	     * @param divisor the divisor
	     */
    	public void divide(TestFile tf, int divisor)
	    {
	      if ( divisor == 0 ) return;
	      this.inBytes = tf.inBytes / divisor;
	      this.outCharsXml = tf.outCharsXml / divisor;
	      this.outCharsJson = tf.outCharsJson / divisor;
	      this.outBytesBin = tf.outBytesBin / divisor;
	      this.totalSeconds = tf.totalSeconds / divisor;
	      this.processSeconds = tf.processSeconds / divisor;
	    }
	}

	/** The test file list. */
	private static List<TestFile> testFileList  = new ArrayList<TestFile>();

	/** The test file. */
	private static TestFile testFile;

	/** The format stats. */
	private static String formatStats = "%40s %8d %6d %6d %4s %8d %8d %8d%n";

	/** The header. */
	private static String[] header = { "Test input file name", "Length", "TotSec", "PrcSec", "P/F", "Out xml ", "Out json", "Out bin "};

	/** The separator. */
	private static String[] separator = { "-------------------------------", "---------", "------", "------", "----", "---------","---------","---------"};

	/** The out line list. */
	private static List<String> outLineList = new ArrayList<String>();

	/** The statistics filename. */
	private static String statisticsFilename = "TestStatistics.txt";

	/** The diff line list. */
	private static List<String> diffLineList = new ArrayList<String>();

	/** The differences filename. */
	private static String differencesFilename = "TestDifferences.txt";

    /** The type of tokenizer to be used. */
    protected TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;

	/**
	 * Run.
	 *
	 * @param packagePathIn the package path in
	 * @param configFilename the config filename
	 * @param args additional arguments to pass to the specific regression test instance
	 */
	public void Run(String packagePathIn, String configFilename, String[] args)
	{
		/** initialize and configure */
		long totalStart = System.nanoTime();
        boolean bSuccess = true;
        int fileCount = 0;
        String className = this.getClass().getName();
        String endTime;
        String result = null;
        boolean redirect = false;
        String language = "en-US";
        System.out.println("[INFO] Started Regression Test");
        PrintStream console = System.out;
        try {
            if (args != null) {
                if (args.length > 0) {
                    // args are "language" and "redirect" so far.
                    //System.out.println("redirect is " + args[0]);
                	for(String arg : args)
                	{
                		if(arg.matches("[a-zA-z][a-zA-z]-[a-zA-z][a-zA-z]"))
                		{
                		    language = arg;
                		}
                		else
                		{
                			redirect = (arg.toLowerCase().equals("true"));
                		}
                	}

                }
            }
            if (redirect) {
                File outFile;
                int index = 0;
                while (true) {
                    String outFilename = String.format("regression_test.%d.log", index);
                    outFile = new File(outFilename);
                    if (!outFile.exists()) break;
                    ++index;
                }
                System.out.println("Subsequent output goes to log file: " + outFile.getAbsolutePath());
                try {
                  System.setOut(new PrintStream(new PrintStream(outFile), true));
                  System.setErr(new PrintStream(new PrintStream(outFile), true));
                } catch (Exception e) {
                  logger.error("Can not create output file {} - ", outFile.getAbsoluteFile(), e);
                  System.err.format("Exception %s occurred attempting to create output file %s%n", e.getClass().getName(), outFile.getAbsoluteFile());
                  System.exit(1);
                }
            }
            packagePath = packagePathIn;
            System.out.flush();
            System.out.format("In %s config is %s.\n", packagePath, configFilename);
            try {
            initialize(configFilename);
              boolean bFirst = true;
              for (String filename : inputFileList) {
                  if (filename.startsWith("#")) continue;    // commented out
                  if (bFirst) doActivate(workingDirectory, algorithmConfig);
                  bFirst = false;
                  //
                  if (!processInputFile(filename)) {
                      bSuccess = false;
                  }
                  ++fileCount;
              }
              printStatistics(className);
              String filename = outputDirectory + differencesFilename;
              theWriter.linesToFile(filename, diffLineList);
              doDeactivate();
            } catch (Exception e) {
              logger.error("Exception caught: ", e);
              bSuccess = false;
            }
        }
        finally{
            if (redirect) {
                System.setErr(console);
                System.setOut(console);
            }
        }
        long totalSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - totalStart, TimeUnit.NANOSECONDS);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        endTime=dateFormat.format(new Date());
        result = bSuccess ? "SUCCESS" : "ERROR";
        String report = String.format("%s  %s processed %d files in %d seconds: %s\n",
                                endTime, className, fileCount, totalSeconds, bSuccess ? "SUCCESS" : "FAILURE");

        System.out.format(report);
      	System.out.println(result);
        
        try
        {
            // Append to shared log file in DEFT folder.  Go up enough levels in directory tree.
            // HACK.  Only a few sites have submodules.
            String filename= "RegressionTest.log";
            int count;
            if ( className.startsWith("edu.columbia")
                    || className.startsWith("edu.cornell")
                    || className.startsWith("edu.qccuny")
                    || className.startsWith("edu.rpi")) {
                count = 4;
            }
            else {
                count = 3;
            }
            for (int i=0; i< count; ++i) filename = "../" + filename;
            File f = new File(filename);
            System.out.println("Log file: " + f.getAbsolutePath());
            FileWriter fw = new FileWriter(filename,true);
            fw.write(report);
            fw.close();
        }
        catch(Exception ex)
        {
            System.err.println("Exception: " + ex.getMessage());
        }

		if (!bSuccess) {
			System.exit(1);
		}
	}

	/**
	 * Initialize.
	 *
	 * @param configFilename the config filename
	 */
	public static void initialize(String configFilename) throws Exception
	{
		System.out.flush();
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
//		String packageName = new Object(){}.getClass().getPackage().getName();
//		packagePath = packageName.replace(".", "/") + "/";

		/** Initialize logger instance */
		logger = LoggerFactory.getLogger(new Object(){}.getClass());
  	/** The log config file path. */
		logConfigFilePath = packagePath + "log4j.file.properties";
		DataInputStream rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
		PropertyConfigurator.configure(rf);
		
		logger.info("JUnit version " + junit.runner.Version.id());

		/** Initialize serializer instances */
		xmlStringSerializer = new XMLStringSerializer();
		jsonStringSerializer = new JSONStringSerializer();
		jsonBinarySerializer = new JSONBinarySerializer();

		Properties testAppConfig = new Properties();
		/** The config file path. */
		configFilePath = packagePath + configFilename;
		DataInputStream dis = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(configFilePath));
		testAppConfig.loadFromXML(dis);
		// set config values
		// Must set working directory in Eclipse to /stanford/target/test-classes
		workingDirectory = System.getProperty("user.dir") + "/" + packagePath;

		if (!new File(workingDirectory).exists()) {
			workingDirectory = System.getProperty("user.dir") + "/target/test-classes/" + packagePath;
		}
		algorithmConfig = testAppConfig.getProperty("algorithmConfig");
		String name = testAppConfig.getProperty("inputFileList");
		String listFile = workingDirectory + "/" + name;
		System.out.println("Input List File: " + listFile);
		// TODO - generalize
		inputFileList = theReader.fileToLines( listFile );
		inputDirectory = workingDirectory + "input/";
		File inDir = new File(inputDirectory);
		if ( inDir.exists())
		{
			File[] files = inDir.listFiles();
			if (files != null) {
				int count = files.length;
				System.out.println("count=" + count);
			} else {
				throw new RuntimeException("input directory is not a directory");
			}
		}
		else
		{
			throw new RuntimeException("input directory does not exist");
		}
		outputDirectory = workingDirectory + "output/";
		File outDir = new File(outputDirectory);
		if (!outDir.exists())
		{
			System.out.println("creating directory: " + outputDirectory);
			boolean result = outDir.mkdir();
			if(result){
				System.out.println("created directory: " + outputDirectory);
			}
		}
		referenceDirectory = workingDirectory + "reference/";
	}

	/**
	 * Creates the hlt content container.
	 *
	 * @param document the document
	 * @return the hlt content container
	 */
	public HltContentContainer createHltContentContainer(Document document) throws Exception
	{
	  // create the HltContentContainer object
		HltContentContainer hltContentContainer = new HltContentContainer();
		hltContentContainer = doProcess(document,hltContentContainer);
		//logger.info("hltContentContainer populated with sentences, POS tags and Entity mentions");

		return hltContentContainer;
	}

	/**
	 * Serialize.
	 *
	 * @param hltContentContainer the hltContentContainer
	 */
	public static void serialize(HltContentContainer hltContentContainer) throws Exception
	{
		if ( hltContentContainer == null) return;
			String xmlTemp = xmlStringSerializer.serializeToString(hltContentContainer);
			testFile.outCharsXml = xmlTemp.length();
			Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
			// TEMP debug
			//Writer.getInstance().writeToFile("/tmp/jwatson.xml",xmlTemp);

			String jsonTemp = jsonStringSerializer.serializeToString(hltContentContainer);
			testFile.outCharsJson = jsonTemp.length();
			Writer.getInstance().writeToFile(jsonFilename,jsonTemp);
			byte[] binTemp = jsonBinarySerializer.serializeToByteArray(hltContentContainer);
			testFile.outBytesBin = binTemp.length;
			Writer.getInstance().writeToFile(binaryFilename,binTemp);
			logger.info("files written with serialized values");
	}

	/**
	 * Process input file.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	private boolean processInputFile( String filename) throws Exception {
		boolean bSuccess = true;
		long totalStart = System.nanoTime();
		testFile = new TestFile(filename);
		String baseFilename = filename.replace(".sgm", "");
		String baseName = outputDirectory + baseFilename;
		String baseOutputName = baseName + ".output.";
		xmlFilename = baseOutputName + "xml";
		jsonFilename = baseOutputName + "json";
		binaryFilename = baseOutputName + "bin";

		/** Create a document object. */
		File file = new File(inputDirectory+filename);
		testFile.inBytes = file.length();
		String docId = filename;
		Corpus corpus = null;
		String docType = "Text";
		String uri = packagePath + "input/" + file.getName();
		String language = "English";
		HltContentContainer hltcc = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDocument(
				docId,
				corpus,
				docType,
				uri,
				language,
				file.getPath(),		// filename
				hltcc,
                tokenizerType);

		// create the HltContentContainer object
		long processStart = System.nanoTime();
		HltContentContainer hltContentContainer = doProcess(document, hltcc);
		testFile.processSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - processStart, TimeUnit.NANOSECONDS);

		/** serialization */
		serialize(hltContentContainer);
		if(hltContentContainer ==null)
		{
			bSuccess = false;
			testFile.totalSeconds = testFile.processSeconds;
			testFile.result = "error";
		}
		else
		{
			System.out.println("Serialized file: " + filename);
			testFile.totalSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - totalStart, TimeUnit.NANOSECONDS);

			// Load reference. Compute diff. Get the Patch object. Patch is the container for computed deltas.
			List<String> xmlOutput = theReader.fileToLines(xmlFilename);
			List<String> xmlOutputNormalized = normalizeGuidAndUri( xmlOutput);
			String xmlReferenceName = referenceDirectory + baseFilename + ".reference.xml";
			List<String> xmlReference  = theReader.fileToLines(xmlReferenceName);
			System.err.println("## normalizing guids/uris for " + filename);
			List<String> xmlReferenceNormalized = normalizeGuidAndUri(xmlReference);
			System.err.println("## calling diffutils for " + filename);

			Patch patch = DiffUtils.diff(xmlOutputNormalized , xmlReferenceNormalized );
			boolean bIdentical = ( patch.getDeltas().size() == 0 );
			System.err.println("## done with diffutils for " + filename);
			if ( bIdentical )
			{
				testFile.result = "pass";
			}
			else
			{
				bSuccess = false;
				testFile.result =  "fail";
				diffLineList.add(filename);
				for (Delta delta: patch.getDeltas()) {
					String d = delta.toString();
					{
						diffLineList.add(d);
						//System.out.println(delta);
					}
				}
			}
		}

		testFileList.add(testFile);
		return bSuccess;
	}

	/**
	 * Normalize list guids.
	 *
	 * @param inList the lines from the serialized XML file
	 * @return the list
	 */
	static List<String>  normalizeGuidAndUri(List<String> inList){
		// Must ignore GUID differences.
		Pattern guidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
		// Also ignore URI differences.
        Pattern uriPattern = Pattern.compile("<uri>.*</uri>");
        // Size of TokenStream varies depending on how Java treats ArrayList.
        Pattern intPattern = Pattern.compile("<int>[0-9]*</int>");
	Pattern floatPattern = Pattern.compile("<float>[0-9]*[\\.][0-9]*</float>");
		boolean uriFound = false;
		for ( int i =0; i < inList.size(); ++i)
		{
			String temp = inList.get(i);
			Matcher guidMatcher = guidPattern.matcher(temp);
            Matcher intMatcher = intPattern.matcher(temp);
		  Matcher floatMatcher = floatPattern.matcher(temp);
            if ( intMatcher.find())
            {
                temp = intMatcher.replaceAll("##");
                inList.set(i, temp);
            }else if(floatMatcher.find()){
		//round float value to 4 decimal places
		String matchedPattern = floatMatcher.group(0);
		String floatVal = matchedPattern.replace("<float>","").replace("</float>","").trim();
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String roundedVal = df.format(Double.parseDouble(floatVal));
		temp = "<float>"+roundedVal+"</float>";
		inList.set(i, temp);
	    }
            else if ( guidMatcher.find())
			{
				temp = guidMatcher.replaceAll("##########-#####-#####-#####-###############");
				inList.set(i, temp);
			}
			else if (!uriFound )
			{
				Matcher uriMatcher = uriPattern.matcher(temp);
				if ( uriMatcher.find()) {
					inList.remove(i);
					uriFound = true;
				}
			}
		}

		return inList;
	}

	/**
	 * Prints the statistics.
	 */
	private static void printStatistics(String className){
		String filename = outputDirectory + statisticsFilename;
		String formatHeader = formatStats.replace("d",  "s");
        outLineList.add(String.format("Regression Test - %s", className));
		outLineList.add(String.format(formatHeader, (Object[]) header));
		outLineList.add(String.format(formatHeader, (Object[]) separator));
		TestFile totalFile = new TestFile("TOTAL");
		totalFile.result = "pass";
		TestFile averageFile = new TestFile("AVERAGE");
		for ( TestFile tf : testFileList)
		{
			printTestFileStatistics( tf );
			totalFile.plus(tf);
		}
		outLineList.add(String.format(formatHeader, (Object[]) separator));
		printTestFileStatistics(totalFile);
		outLineList.add(String.format("%n"));
		averageFile.divide(totalFile, testFileList.size());
		printTestFileStatistics( averageFile );
		theWriter.linesToFile(filename ,outLineList);
	}

	/**
	 * Prints the test file statistics.
	 *
	 * @param tf the tf
	 */
	private static void  printTestFileStatistics( TestFile tf ) {
		String name = tf.name;
		int chunk = 40;	// Corresponds to offset format string
		int index = 0;
		while ( name.length() > chunk )
		{
			outLineList.add( String.format("%s%n",name.substring(index, index + chunk)));
			name = name.substring(chunk);
		}
		outLineList.add( String.format(formatStats,
			name,
			tf.inBytes,
			tf.totalSeconds,tf.processSeconds,tf.result,
			tf.outCharsXml, tf.outCharsJson, tf.outBytesBin));
	}

}