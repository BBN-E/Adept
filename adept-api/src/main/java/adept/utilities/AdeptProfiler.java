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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.common.base.Stopwatch;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.common.Sentence;
import adept.common.TokenizerType;
import adept.io.Reader;
import adept.io.Writer;
import adept.module.AdeptModuleException;
import adept.utilities.DocumentMaker;
import adept.utilities.Grapher;
import adept.serialization.XMLStringSerializer;
import adept.serialization.SerializationType;

/**
 * The Class AdeptProfiler.
 */
public abstract class AdeptProfiler {
	// The only site-specific members are config file and processor instance.
	/** The config filename. */
	private static String configFilename;

	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

	/** The data directory. */
	private static File dataDirectory;
	// private static String heapdumpDirectory;
	/** The package path. */
	private static String packagePath;

	/** The config file path. */
	private static String configFilePath;

	/** the processor configuration string. */
	private static String algorithmConfig;
	// private static boolean doScoring;

	// TODO - convert to google guava Stopwatch
	/** The activate. */
	private static Stopwatch activate = Stopwatch.createUnstarted();

	/** The process. */
	private static Stopwatch process = Stopwatch.createUnstarted();

	/** total process time */
	private static Stopwatch totalprocess = Stopwatch.createUnstarted();

	/** The process_last recorded. */
	private static double process_lastRecorded;

	/** The Constant MB. */
	private static final long MB = 1024L * 1024L;

	/** The Constant NUM_OF_LOOPS. */
	private static final long NUM_OF_LOOPS = 1;

	/** Per document profiling results */
	private static File document_stats;
	private static FileWriter document_stats_fwriter;
	private static BufferedWriter document_stats_writer;

	/** Per document html profiling results */
	private static File document_html;
	private static FileWriter document_html_fwriter;
	private static BufferedWriter document_html_writer;

	/** Machine stats */
	private static File machine_stats;
	private static FileWriter machine_stats_fwriter;
	private static BufferedWriter machine_stats_writer;

	/** Summary stats */
	private static File test_summary;
	private static FileWriter test_summary_fwriter;
	private static BufferedWriter test_summary_writer;

	/** Summary html */
	private static File test_html;
	private static FileWriter test_html_fwriter;
	private static BufferedWriter test_html_writer;

	/** XML Serializer */
	private static XMLStringSerializer xmlStringSerializer;

	/** Output directory */
	private static String outputDirectory;

	/**
	 * Maintain lists of the various stats to calculate mean, variance, min, max
	 * later on
	 */
	private static List<Double> processTimes = new ArrayList<Double>();
	private static List<Double> heapUsages = new ArrayList<Double>();
	private static List<Double> fileSizes = new ArrayList<Double>();
	private static List<Double> numOfChars = new ArrayList<Double>();

	/** total number of files */
	private static int totalFiles = 0;

	private static void setConfigFilename(String name) {
		AdeptProfiler.configFilename = name;
	}
	
	private static void setPackagePath(String name) {
		AdeptProfiler.packagePath = name;
	}
	
	/** The type of tokenizer to be used. */
	protected TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;

	/**
	 * Abstract methods.
	 *
	 * @param algorithmConfig
	 *            the algorithm config
	 */
	protected abstract void doActivate(String algorithmConfig) throws AdeptModuleException;

	/**
	 * Do deactivate.
	 */
	protected abstract void doDeactivate() throws AdeptModuleException;

	/**
	 * Do process.
	 *
	 * @param document
	 *            the document
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the hlt content container
	 */
	protected abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer) throws AdeptModuleException;

	/**
	 * Initialize.
	 *
	 * @param filename
	 *            the filename
	 * @param outputDir
	 *            the output dir
	 */
	public static void initialize(String filename, String outputDir)  throws Exception {
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
		// String packageName = new
		// Object(){}.getClass().getPackage().getName();
		// packagePath = packageName.replace(".", "/") + "/";

		/** Initialize logger instance */
		logger = LoggerFactory.getLogger(new Object() {
		}.getClass());

		/** The log config file path. */
		logConfigFilePath = packagePath + "log4j.file.properties";
		DataInputStream rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
		PropertyConfigurator.configure(rf);

		logger.info("JUnit version " + junit.runner.Version.id());

		Properties testAppConfig = new Properties();
		/** The config file path. */
		configFilePath = packagePath + configFilename;
		DataInputStream dis = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(configFilePath));
		testAppConfig.loadFromXML(dis);

		outputDirectory = outputDir;

		// set config values
		algorithmConfig = testAppConfig.getProperty("algorithmConfig");
		// String test = testAppConfig.getProperty("doScoring");
		// doScoring = ( test == null || test.equalsIgnoreCase("true"));

		// set the data directory
		dataDirectory = new File(filename);

		File outDir = new File(outputDir);
		if (!outDir.exists()) {
			System.out.println("creating directory: " + outputDir);
			boolean result = outDir.mkdir();
			if (result) {
				System.out.println("created directory: " + outputDir);
			}
		}

		document_stats = new File(outputDir + "/document_stats.txt");
		if (document_stats.exists())
			document_stats.delete();
		document_stats_fwriter = new FileWriter(document_stats.getAbsolutePath(), true);
		// TODO - write class name here.
		document_stats_writer = new BufferedWriter(document_stats_fwriter);

		document_html = new File(outputDir + "/document_stats.html");
		if (document_html.exists())
			document_html.delete();
		document_html_fwriter = new FileWriter(document_html.getAbsolutePath(), true);
		// TODO - write class name here.
		document_html_writer = new BufferedWriter(document_html_fwriter);

		machine_stats = new File(outputDir + "/machine_stats.txt");
		if (machine_stats.exists())
			machine_stats.delete();
		machine_stats_fwriter = new FileWriter(machine_stats.getAbsolutePath(), true);
		// TODO - write class name here.
		machine_stats_writer = new BufferedWriter(machine_stats_fwriter);
		// TODO Auto-generated catch block

		test_summary = new File(outputDir + "/test_summary.txt");
		if (test_summary.exists())
			test_summary.delete();
		test_summary_fwriter = new FileWriter(test_summary.getAbsolutePath(), true);
		// TODO - write class name here.
		test_summary_writer = new BufferedWriter(test_summary_fwriter);

		test_summary_writer.write(
					"Config File Name\tActivation Time\tTotal File Count\tFailure Count\tTotal Process Time\tAverage File Size\tMax File Size\tMin File Size\tFile Size Variance\tAvg Char Count\tMax Char Count\tMin Char Count\tChar Count Variance\tAvg Process Time (s)\tMax Process Time\tMin Process Time\tProcess Time Variance\tAvg Heap Usage\tMax Heap Usage\tMin Heap Usage\tHeap Usage Variance\t(Final Heap - Initial Heap)\n");

		test_html = new File(outputDir + "/test_summary.html");
		if (test_html.exists())
			test_html.delete();
		test_html_fwriter = new FileWriter(test_html.getAbsolutePath(), true);
		// TODO - write class name here.

		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Date date = new Date();

		test_html_writer = new BufferedWriter(test_html_fwriter);

		test_html_writer .write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable,th,td\n{\nfont-family: Calibri, sans-serif;\nborder-collapse: collapse;\nborder:1px solid black;\nfont-size: 10pt;\n}\ntd\n{\ntext-align:right;\n}\ntable\n{\nwidth: 100%;\n}\n</style>\n</head>\n<body>\n<h2>"
							+ configFilename.replace("ProfilerConfig.xml", "") + " Profiling Results, "
							+ dateFormat.format(date) + "</h2>\n<table>\n");

		test_html_writer.write(
					"<tr><th>Activation Time (s)</th><th>Total File Count</th><th>Failure Count</th><th>Total Process Time (s)</th><th>Average File Size (Bytes)</th><th>Max File Size</th><th>Min File Size</th><th>File Size Variance</th><th>Avg Char Count</th><th>Max Char Count</th><th>Min Char Count</th><th>Char Count Variance</th><th>Avg Process Time (s)</th><th>Max Process Time</th><th>Min Process Time</th><th>Process Time Variance</th><th>Avg Heap Usage (MB)</th><th>Max Heap Usage</th><th>Min Heap Usage</th><th>Heap Usage Variance</th><th>(Final Heap - Initial Heap)</th><tr>\n");

		process_lastRecorded = 0;

		xmlStringSerializer = new XMLStringSerializer();
	}

	/**
	 * Creates the hlt content container.
	 *
	 * @param document
	 *            the document
	 * @return the hlt content container
	 */
	public HltContentContainer createHltContentContainer(Document document, HltContentContainer hltContentContainer) throws Exception {
		try {
			// create the HltContentContainer object
			if (hltContentContainer == null)
				hltContentContainer = new HltContentContainer();
			process.reset();
			process.start();
			totalprocess.start();
			hltContentContainer = doProcess(document, hltContentContainer);
			process.stop();
			logger.info("hltContentContainer populated with annotations.");
			return hltContentContainer;
		} catch (Exception e) {
			if (process.isRunning()) {
				process.stop();
			}
			throw e;
		}
	}

	/**
	 * The main method.
	 *
	 * @param packagePathIn
	 *            the package path in
	 * @param configFilenameIn
	 *            the config filename in
	 * @param profileFilename
	 *            the profile filename
	 * @param outputDir
	 *            the output dir
	 */
	public void Run(String packagePathIn, String configFilenameIn, String profileFilename, String outputDir) {
		AdeptProfiler.setPackagePath(packagePathIn);
		AdeptProfiler.setConfigFilename(configFilenameIn);
		try {
  		initialize(profileFilename, outputDir);
  
  		boolean bFirst = true;
  		boolean bSuccess;
  		int fileCount = 0, failureCount = 0;
  		String lineFormat = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%n";
  		String htmlFormat = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td></tr>%n";
  		Double[] docStatistics = { 0.0, 0.0, 0.0, 0.0 };
  		HltContentContainer hltcontainer;
  		if (null == dataDirectory ) {
  			logger.warn("data directory %s is not set");
  		} else {
  			File[] fileList = dataDirectory.listFiles();
  			if (null == fileList) {
  				logger.warn("File %s is not a directory or cannot be read", dataDirectory.getAbsolutePath());
  			} else {
  				int dirSize = fileList.length;
  				int summarySize = dirSize >= 9 ? (dirSize + 1) / 10 : 1;
  				for (File file : fileList) {
  					totalFiles++;
  					bSuccess = true;
  					if (bFirst) {
  						// String workingDirectory = System.getProperty("user.dir")
  						// + "/" + packagePath;
  						activate.start();
  						doActivate(algorithmConfig);
  						activate.stop();
  						// activate();
  						// String line = "Time taken in activate(): " +
  						// activate.elapsed(TimeUnit.MILLISECONDS)/1000.0 +
  						// "\n\n";
  						// document_stats_writer.write(line);
  						String line = String.format(lineFormat, "Doc name", "Document Size(Bytes)",
  								"Number of Characters", "Number of Passages", "Number of Tokens",
  								"Process time(seconds)", "Heap usage(MB)");
  						document_stats_writer.write(line);
  						document_stats_writer.flush();
  						
  						// activate();
  						DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
  						Date date = new Date();
  
  						document_html_writer
  								.write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable,th,td\n{\nfont-family: Calibri, sans-serif;\nborder-collapse: collapse;\nborder:1px solid black;\nfont-size: 10pt;\n}\ntd\n{\ntext-align:right;\n}\ntable\n{\nwidth: 100%;\n}\n</style>\n</head>\n<body>\n<h2>"
  										+ configFilename.replace("ProfilerConfig.xml", "") + " Profiling Results, "
  										+ dateFormat.format(date) + "</h2>\n<table>\n");
  
  						document_html_writer.write(
  								"<tr><th>Doc name</th><th>Document Size(Bytes)</th><th>Num of Characters</th><th>Num of Passages</th><th>Number of Tokens</th><th>Process time(seconds)</th><th>Heap usage(MB)</th></tr>\n");
  						document_html_writer.flush();
  					}
  					bFirst = false;
  	
  					hltcontainer = new HltContentContainer();
  					if (!processInputFile(file, docStatistics, hltcontainer)) {
  						logger.info("Failure in profiler on file " + file.getAbsoluteFile());
  						bSuccess = false;
  					}
  					++fileCount;
  	
  					if (!bSuccess) {
  						failureCount++;
  					}
  	
  					Runtime runtime = Runtime.getRuntime();
  					// runtime.gc();
  					double heapUsage = ((double) runtime.totalMemory() - (double) runtime.freeMemory()) / MB;
  					heapUsages.add(heapUsage);
  	
  					logger.info("Now writing profiling statistics to file..");
  					double elapsed = process.elapsed(TimeUnit.MILLISECONDS) / 1000.0;
  					processTimes.add(elapsed);
  					String line = String.format(lineFormat, file.getName(), docStatistics[0], docStatistics[3],
  							docStatistics[2], docStatistics[1], elapsed, heapUsage);
  					document_stats_writer.write(line);
  					document_stats_writer.flush();
  
  					line = String.format(htmlFormat, file.getName(), docStatistics[0], docStatistics[3],
  							docStatistics[2], docStatistics[1], elapsed, heapUsage);
  					document_html_writer.write(line);
  					document_html_writer.flush();
  	
  					// process_lastRecorded =
  					// process.elapsed(TimeUnit.MILLISECONDS)/1000.0 ;
  	
  					if (totalFiles % summarySize == 0) {
  						// write in the test_summary file
  
  						test_summary_writer.write(configFilename + "\t"
  								+ activate.elapsed(TimeUnit.MILLISECONDS) / 1000.0 + "\t" + totalFiles + "\t"
  								+ failureCount + "\t" + totalprocess.toString().replace(" s", "") + "\t"
  								+ calculateAverage(fileSizes) + "\t" + calculateMax(fileSizes) + "\t"
  								+ calculateMin(fileSizes) + "\t" + calculateVariance(fileSizes) + "\t"
  								+ calculateAverage(numOfChars) + "\t" + calculateMax(numOfChars) + "\t"
  								+ calculateMin(numOfChars) + "\t" + calculateVariance(numOfChars) + "\t"
  								+ calculateAverage(processTimes) + "\t" + calculateMax(processTimes) + "\t"
  								+ calculateMin(processTimes) + "\t" + calculateVariance(processTimes) + "\t"
  								+ calculateAverage(heapUsages) + "\t" + calculateMax(heapUsages) + "\t"
  								+ calculateMin(heapUsages) + "\t" + calculateVariance(heapUsages) + "\t"
  								+ (heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)) + "%n");
  
  						test_summary_writer.flush();
  
  					}
  				}
  			}
  		}
  		document_stats_writer.flush();
  		document_stats_writer.close();
  		document_stats_fwriter.close();
  
  		document_html_writer.write("</table>%n</body>%n</html>");
  
  		document_html_writer.flush();
  		document_html_writer.close();
  		document_html_fwriter.close();
  
  		// write in the machine_stats file
  		String javaVersion = System.getProperty("java.version");
  		String osName = System.getProperty("os.name");
  		String osVersion = System.getProperty("os.version");
  		long machineMemory = Runtime.getRuntime().maxMemory();
  		int machineCores = Runtime.getRuntime().availableProcessors();
  
  		machine_stats_writer.write(machineMemory + "\t" + machineCores + "\t" + osName + "\t" + osVersion + "\t"
  				+ javaVersion + "\t" + "yes");
  		machine_stats_writer.flush();
  		machine_stats_writer.close();
  
  		// write in the test_summary file
  
  		test_summary_writer.write(configFilename + "\t" + activate.elapsed(TimeUnit.MILLISECONDS) / 1000.0 + "\t"
  				+ totalFiles + "\t" + failureCount + "\t" + totalprocess.toString().replace(" s", "") + "\t"
  				+ calculateAverage(fileSizes) + "\t" + calculateMax(fileSizes) + "\t" + calculateMin(fileSizes)
  				+ "\t" + calculateVariance(fileSizes) + "\t" + calculateAverage(numOfChars) + "\t"
  				+ calculateMax(numOfChars) + "\t" + calculateMin(numOfChars) + "\t" + calculateVariance(numOfChars)
  				+ "\t" + calculateAverage(processTimes) + "\t" + calculateMax(processTimes) + "\t"
  				+ calculateMin(processTimes) + "\t" + calculateVariance(processTimes) + "\t"
  				+ calculateAverage(heapUsages) + "\t" + calculateMax(heapUsages) + "\t" + calculateMin(heapUsages)
  				+ "\t" + calculateVariance(heapUsages) + "\t"
  				+ (heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)));
  
  		test_summary_writer.flush();
  		test_summary_writer.close();
  
  		test_html_writer.write("<tr><td>" + activate.elapsed(TimeUnit.MILLISECONDS) / 1000.0 + "</td><td>"
  				+ totalFiles + "</td><td>" + failureCount + "</td><td>" + totalprocess.toString().replace(" s", "")
  				+ "</td><td>" + String.format("%.2f", calculateAverage(fileSizes)) + "</td><td>"
  				+ String.format("%.2f", calculateMax(fileSizes)) + "</td><td>"
  				+ String.format("%.2f", calculateMin(fileSizes)) + "</td><td>"
  				+ String.format("%.2f", calculateVariance(fileSizes)) + "</td><td>"
  				+ String.format("%.2f", calculateAverage(numOfChars)) + "</td><td>"
  				+ String.format("%s", calculateMax(numOfChars)) + "</td><td>"
  				+ String.format("%s", calculateMin(numOfChars)) + "</td><td>"
  				+ String.format("%.2f", calculateVariance(numOfChars)) + "</td><td>"
  				+ String.format("%.2f", calculateAverage(processTimes)) + "</td><td>"
  				+ String.format("%.2f", calculateMax(processTimes)) + "</td><td>"
  				+ String.format("%.2f", calculateMin(processTimes)) + "</td><td>"
  				+ String.format("%.2f", calculateVariance(processTimes)) + "</td><td>"
  				+ String.format("%.2f", calculateAverage(heapUsages)) + "</td><td>"
  				+ String.format("%.2f", calculateMax(heapUsages)) + "</td><td>"
  				+ String.format("%.2f", calculateMin(heapUsages)) + "</td><td>"
  				+ String.format("%.2f", calculateVariance(heapUsages)) + "</td><td>"
  				+ String.format("%.2f", (heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)))
  				+ "</td></tr>%n");
  
  		test_html_writer.write(
  				"</table><p>%n<a href=\"document_stats.html\">Full Document Statistics</a>%n<p><img src=\"ProcessTime_vs_FileSize.png\">%n<p><img src=\"ProcessTime_vs_NumberOfTokens.png\">%n<p><img src=\"ProcessTime_vs_NumberOfPassages.png\">%n<p><img src=\"HeapMemoryUsage.png\">%n</body>%n</html>");
  
  		test_html_writer.flush();
  		test_html_writer.close();
  
  		System.out.println("Average time taken in process: "
  				+ (process.elapsed(TimeUnit.MILLISECONDS) / 1000.0 / (fileCount * NUM_OF_LOOPS)) / 1000.0);
  		System.out.println("Number of failures: " + failureCount);
  
  		// graph making
      ArrayList<Double> heapUsage = new ArrayList<Double>();
      ArrayList<Double> processTimes = new ArrayList<Double>();
      ArrayList<Double> numOfPassages = new ArrayList<Double>();
      ArrayList<Double> numOfTokens = new ArrayList<Double>();
      ArrayList<Double> fileSizes = new ArrayList<Double>();
  		try (BufferedReader br = new BufferedReader(new FileReader(document_stats.getAbsolutePath()))) {
  			for (int j = 0; j < 5; j++) {
  				br.readLine();
  			}
  			String line;
  			while ((line = br.readLine()) != null) {
  				String[] temp = line.split("\t");
  				heapUsage.add(Double.parseDouble(temp[6]));
  				processTimes.add(Double.parseDouble(temp[5]));
  				fileSizes.add(Double.parseDouble(temp[1]));
  				numOfPassages.add(Double.parseDouble(temp[3]));
  				numOfTokens.add(Double.parseDouble(temp[4]));
  			}
  		}
    	Grapher.makeHeapUsageGraph(heapUsage, new File(outputDir + "/HeapMemoryUsage.png"));
    	Grapher.makeTimeVsSizeGraph(processTimes, fileSizes, new File(outputDir + "/ProcessTime_vs_FileSize.png"),
    			"Profiling Corpus", "File Size(Bytes)", "Process Time vs. File Size", "Process Time (s)");
    	Grapher.makeTimeVsSizeGraph(processTimes, numOfPassages,
    			new File(outputDir + "/ProcessTime_vs_NumberOfPassages.png"), "Profiling Corpus", "Num of Passages",
    			"Process Time vs. Passage Count", "Process Time (s)");
    	Grapher.makeTimeVsSizeGraph(processTimes, numOfTokens,
    			new File(outputDir + "/ProcessTime_vs_NumberOfTokens.png"), "Profiling Corpus", "Num Of Tokens",
    			"Process Time vs. Token Count", "Process Time (s)");
  		doDeactivate();
		} catch (Exception e) {
		  logger.error("Caught Exception: ", e);
		  System.out.format("Exception %s caught", e.getClass().getName());
		  System.exit(1);
		}
	}

	/**
	 * Process input file.
	 *
	 * @param file
	 *            the file
	 * @param docStatistics
	 *            the doc statistics
	 * @param hltcontainer
	 *            the hltcontainer
	 * @return true, if successful
	 * @throws Exception 
	 */
	private boolean processInputFile(File file, Double[] docStatistics, HltContentContainer hltcontainer) throws Exception {
		boolean bSuccess = true;

		/** Create a document object. */
		// File file = new File(inputDirectory+filename);
		if (file.exists())
			docStatistics[0] = (double) file.length();
		fileSizes.add(docStatistics[0]);

		/** Create a document object. */
		String docId = file.getName();
		Corpus corpus = null;
		String docType = "Text";
		String uri = file.getAbsolutePath();
		String language = "English";
		adept.common.Document document = DocumentMaker.getInstance().createDocument(docId, corpus, docType, uri,
				language, uri, // filename
				hltcontainer, tokenizerType);

		if (document != null) {
			docStatistics[1] = (double) document.getDefaultTokenStream().size();
			if (hltcontainer.getPassages() != null) {
				System.out.println("Number of passages: " + hltcontainer.getPassages().size());
				docStatistics[2] = (double) hltcontainer.getPassages().size();
			} else {
				System.out.println("NO PASSAGES FOUND");
				// docStatistics[1] = 0.0;
			}

			docStatistics[3] = (double) document.getValue().length();
			numOfChars.add(docStatistics[3]);

			// create the HltContentContainer object
			hltcontainer = createHltContentContainer(document, hltcontainer);

			// serialize hltcc and save to output directory
			if (hltcontainer == null) {
				totalprocess.stop();
				bSuccess = false;
			} else {
				try {
					String serialized = xmlStringSerializer.serializeToString(hltcontainer);
					Writer.getInstance().writeToFile(outputDirectory + "/" + file.getName(), serialized);
					totalprocess.stop();
				} catch (Exception e) {
					totalprocess.stop();
					throw e;
				}
			}
			return bSuccess;
		}
		else
			return false;
	}

	/**
	 * File to lines.
	 *
	 * @param filename
	 *            the filename
	 * @return the list
	 */
	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(new java.io.File(filename).getAbsolutePath()));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
			in.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return lines;
	}

	private static double calculateAverage(List<Double> values) {
		double sum = 0;
		for (Double value : values) {
			sum += value;
		}
		return sum / values.size();
	}

	private static double calculateMin(List<Double> values) {
		double min = 100000000;
		for (Double value : values) {
			if (value <= min)
				min = value;
		}
		return min;
	}

	private static double calculateMax(List<Double> values) {
		double max = 0;
		for (Double value : values) {
			if (value >= max)
				max = value;
		}
		return max;
	}

	private static double calculateVariance(List<Double> values) {
		double sumsq = 0;
		for (Double value : values) {
			sumsq += value * value;
		}
		return sumsq / values.size();
	}
}