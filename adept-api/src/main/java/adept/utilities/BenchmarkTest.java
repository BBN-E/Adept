// Next line is site-specific.
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.TokenizerType;
import adept.io.Reader;
import adept.io.Writer;



/**
 * The Class StanfordCoreNlpRegressionTest.
 */
public abstract class BenchmarkTest
{
	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

	/** The input file path. And list of file names. */
	private static String inputDirectory;

	/** The input file list. */
	private static List<String> inputFileList;

	/** The output directory. */
	private static String outputDirectory;

	/** The package path. */
	private static String packagePath;

	/** The working directory. */
	private static String workingDirectory;

	/** The config file path. */
	private static String configFilePath;

	/** The algorithm config. */
	private static String algorithmConfig;

	/** files to be written on serialization. */
	private static Reader theReader = new Reader();

	/** The writer. */
	private static Writer theWriter = new Writer();

	/** The type of tokenizer to be used. */
	protected TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;


	/**
	 * Abstract methods.
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
	 * @param hltContentContainer the hlt content container
	 * @return the hlt content container
	 */
	protected abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer) throws Exception;

	/**
	 * Do score.
	 *
	 * @param document the document
	 * @param hltContentContainer the hlt content container
	 * @return the float
	 */
	protected abstract float doScore(Document document, HltContentContainer hltContentContainer) throws Exception;

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
		 * @param tf the tf
		 */
		public void plus(TestFile tf)
		{
			this.inBytes += tf.inBytes;
			this.outCharsXml += tf.outCharsXml;
			this.outCharsJson += tf.outCharsJson;
			this.outBytesBin += tf.outBytesBin;
			this.totalSeconds += tf.totalSeconds;
			this.processSeconds += tf.processSeconds;
			if ( tf.result == "fail") this.result = tf.result;
		}

		/**
		 * Divide.
		 *
		 * @param tf the tf
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


	/**
	 * The main method.
	 *
	 * @param packagePathIn the package path in
	 * @param configFilename the config filename
	 */
	public void Run(String packagePathIn, String configFilename)
	{
		/** initialize and configure */
		long totalStart = System.nanoTime();
		packagePath = packagePathIn;
		System.out.format("In %s config is %s.\n", packagePath,  configFilename);
		boolean bFirst = true;
		boolean bSuccess = true;
		int fileCount = 0;
		try {
			initialize(configFilename);
			for ( String filename : inputFileList) {
  				if ( filename.startsWith("#")) continue;	// commented out
  				if ( bFirst ) doActivate(workingDirectory, algorithmConfig);
  				bFirst = false;
  				//
  				float score = processInputFile( filename );
  				++fileCount;
  				System.out.format("File %d %s score=%f\n", fileCount, filename, score);
  			}
  			printStatistics();
  			String filename = outputDirectory + differencesFilename;
  			theWriter.linesToFile(filename,diffLineList);
  			doDeactivate();
		} catch (Exception e) {
		  logger.error("Exception caught: ", e);
		  bSuccess = false;
		}
		long totalSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - totalStart, TimeUnit.NANOSECONDS);
		System.out.format("%s  Processed %d files in %d seconds.\n", bSuccess?"SUCCESS!":"ERROR!", fileCount, totalSeconds);
		if (! bSuccess) {
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
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
		//		String packageName = new Object(){}.getClass().getPackage().getName();
		//		packagePath = packageName.replace(".", "/") + "/";

		/** Initialize logger instance */
		logger = LoggerFactory.getLogger(new Object(){}.getClass());
		/** The log config file path. */
		logConfigFilePath = packagePath + "log4j.file.properties";
		DataInputStream rf;
		try {
			rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
		} catch (java.io.FileNotFoundException ex) {
			rf = new DataInputStream(BenchmarkTest.class.getClassLoader().getResourceAsStream("log4j.properties"));
		}
		PropertyConfigurator.configure(rf);

		logger.info("JUnit version " + junit.runner.Version.id());

		Properties testAppConfig = new Properties();
		/** The config file path. */
		configFilePath = packagePath + configFilename;
		DataInputStream dis = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(configFilePath));
		testAppConfig.loadFromXML(dis);

		// set config values
		// Must set working directory in Eclipse to /stanford/target/test-classes
		workingDirectory = System.getProperty("user.dir") + "/" + packagePath;
		algorithmConfig = testAppConfig.getProperty("algorithmConfig");
		String name = testAppConfig.getProperty("inputFileList");
		String listFile = workingDirectory + "/" + name;
		System.out.println("Input List File: " + listFile);
		// TODO - generalize
		inputFileList = theReader.fileToLines( listFile );
		inputDirectory = workingDirectory + "input.benchmark/";
		File inDir = new File(inputDirectory);
		if ( inDir.exists())
		{
			if (inDir.isDirectory()) {
				String[] list = inDir.list();
				if (null != list) {
					int count = list.length;
					System.out.println("count=" + count);
				}
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
	}

	/**
	 * Process input file.
	 *
	 * @param filename the filename
	 * @return the float
	 */
	private float processInputFile( String filename) throws Exception
	{
		long totalStart = System.nanoTime();
		testFile = new TestFile(filename);

		/** Create a document object. */
		File file = new File(inputDirectory+filename);
		testFile.inBytes = file.length();
		String docId = filename;
		Corpus corpus = null;
		String docType = "Text";
		String uri = file.getAbsolutePath();
		String language = "English";
		HltContentContainer hltcc = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDocument(
				docId,
				corpus,
				docType,
				uri,
				language,
				uri,		// filename
				hltcc,
                tokenizerType);

		// create the HltContentContainer object
		long processStart = System.nanoTime();
		HltContentContainer hltContentContainer = doProcess(document,hltcc);
                if (null == hltContentContainer) {
 			logger.error("doProcess returned null - some error in processing!");
			throw new Exception("doProcess returned null");
		}
		testFile.processSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - processStart, TimeUnit.NANOSECONDS);

		/** serialization */
		float score = doScore( document, hltContentContainer);
		testFile.totalSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - totalStart, TimeUnit.NANOSECONDS);

		testFileList.add(testFile);
		return score;
	}

	/**
	 * Prints the statistics.
	 */
	private static void printStatistics() throws Exception {
		String filename = outputDirectory + statisticsFilename;
		String formatHeader = formatStats.replace("d",  "s");
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
		printTestFileStatistics( totalFile );
		outLineList.add(String.format("%n"));
		averageFile.divide(totalFile, testFileList.size());
		printTestFileStatistics( averageFile );
		theWriter.linesToFile(filename , outLineList);
	}

	/**
	 * Prints the test file statistics.
	 *
	 * @param tf the tf
	 */
	private static void  printTestFileStatistics( TestFile tf ) {
		String name = tf.name;
		int chunk = 40;	// Corresponds to format string
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
