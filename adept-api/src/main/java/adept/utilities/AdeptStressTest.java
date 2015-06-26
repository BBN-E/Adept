package adept.utilities;
/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/

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
import java.util.Map;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import adept.common.TokenizerType;
import com.google.common.base.Stopwatch;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.io.Reader;
import adept.io.Writer;
import adept.serialization.XMLSerializer;
import adept.serialization.SerializationType;


// TODO: Auto-generated Javadoc
/**
 * The Class AdeptProfiler.
 */
public abstract class AdeptStressTest
{
	// The only site-specific members are config file and processor instance.
	/** The config filename. */
	private static String configFilename;

	/** The logger. */
	private static Logger logger;

	/** The log config file path. */
	private static String logConfigFilePath;

	/** The data directory. */
	private static List<String> inputTypes;

	/** The data directories. */
	private static Map<String, File> dataDirectories;

	/** The package path. */
	private static String packagePath;

	/** The config file path. */
	private static String configFilePath;

	/** the processor configuration string. */
	private static String algorithmConfig;
	//private static boolean doScoring;
	
	// TODO - convert to google guava Stopwatch
	/** The activate. */
	private static Stopwatch activate = new Stopwatch(); //Stopwatch.createUnstarted();
	
	/** The process. */
	private static Stopwatch process = new Stopwatch(); //Stopwatch.createUnstarted();
	
	/** total process time */
	private static Stopwatch totalprocess = new Stopwatch();
	
	/** The process_last recorded. */
	//private static double process_lastRecorded;

    /** The Constant MB. */
    private static final long MB = 1024L * 1024L;
    
    /** The Constant NUM_OF_LOOPS. */
    private static final long NUM_OF_LOOPS = 1;

    /** XML Serializer */
    private static XMLSerializer xmlserializer;

    /** Output directory */
    private static String outputDirectory;

    /** Maintain lists of the various stats to calculate mean, variance, min, max later on */
    private static List<Double> processTimes;
    private static List<Double> heapUsages;
    private static List<Double> fileSizes;
    private static List<Double> numOfChars;

    /** total number of files */
    private static int totalFiles = 0;

    /** Used to define "LOW PERFORMANCE" */
    private static float timeRatio = 5;
    private static float heapRatio = 5;


    /** Nested class handles different kinds of files written by stress test. */
    private static class OutFileWriter {

        /**
         * Summary stats
         */
        private File outFile;
        private FileWriter fwriter;
        private BufferedWriter bwriter;

        public OutFileWriter(File outDir, String filename) {
            outFile = new File(outDir + "/" + filename);
            if (outFile.exists())
                outFile.delete();
            try {
                fwriter = new FileWriter(outFile.getAbsolutePath(), true);
                bwriter = new BufferedWriter(fwriter);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void write(String text) {
            try {
                bwriter.write(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void flush() {
            try {
                bwriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void close() {
            try {
                bwriter.close();
                fwriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public BufferedReader getBR() {
            try {
                return new BufferedReader(new FileReader(outFile.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static OutFileWriter documentStatsWriter;
    private static OutFileWriter documentHtmlWriter;
    private static OutFileWriter machineStatsWriter;
    private static OutFileWriter testSummaryWriter;
    private static OutFileWriter testHtmlWriter;
    private static OutFileWriter stressSummaryWriter;

    /** The type of tokenizer to be used. */
    protected TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;


    /**
	 * Abstract methods.
	 *
	 * @param algorithmConfig the algorithm config
	 */
	protected abstract void doActivate(String algorithmConfig);
	
	/**
	 * Do deactivate.
	 */
	protected abstract void doDeactivate();
	
	/**
	 * Do process.
	 *
	 * @param document the document
	 * @param hltContentContainer the hlt content container
	 * @return the hlt content container
	 */
	protected abstract HltContentContainer doProcess(Document document, HltContentContainer hltContentContainer) throws Exception;		
	
	/**
	 * Initialize.
	 *
	 * @param filename the filename
	 * @param outputDir the output dir
	 */
	public static void initialize(String filename, String outputDir)
	{
		System.out.println("Current Directory: " + System.getProperty("user.dir"));
		// String packageName = new Object(){}.getClass().getPackage().getName();
		// packagePath = packageName.replace(".", "/") + "/";

		/** Initialize logger instance */
		logger = LoggerFactory.getLogger(new Object(){}.getClass());
		try {
			/** The log config file path. */
			logConfigFilePath = packagePath + "log4j.file.properties";
			DataInputStream rf = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(logConfigFilePath));
			PropertyConfigurator.configure(rf);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		logger.info("JUnit version " + junit.runner.Version.id());

		Properties testAppConfig = new Properties();
		try {
			/** The config file path. */
			configFilePath = packagePath + configFilename;
			DataInputStream dis = new DataInputStream(Reader.findStreamInClasspathOrFileSystem(configFilePath));
			testAppConfig.loadFromXML(dis);
		} catch (InvalidPropertiesFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		outputDirectory = outputDir;

		// set config values
		algorithmConfig = testAppConfig.getProperty("algorithmConfig");
		 //String test = testAppConfig.getProperty("doScoring");
		 //doScoring = ( test == null || test.equalsIgnoreCase("true"));
		dataDirectories = new HashMap<String, File>();		inputTypes = new ArrayList<String>();		List<String> inputDirs = fileToLines(filename);		for (String line : inputDirs) {			String inputType = line.split("\t")[0];			String inputDirPath = line.split("\t")[1];			File inputDir = new File(inputDirPath);			inputTypes.add(inputType);			dataDirectories.put(inputType, inputDir);		}
		if(!inputTypes.get(0).toLowerCase().equals("control"))
		{
			System.out.println("Please verify the input file");
			System.out.println("Input file should be a tab separated list with lines of the form:");
			System.out.println("<inputType>	<inputDirectory>");
			System.out.println("The first line must have inputType \"control\"");
			System.exit(1);
		}
		File outDir = new File(outputDir);
		if (!outDir.exists())
		{
			System.out.println("creating directory: " + outputDir);
			boolean result = outDir.mkdir();
			if(result){
				System.out.println("created directory: " + outputDir);
			}
		}

		try {
			DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			Date date = new Date();
			stressSummaryWriter = new OutFileWriter(outDir,
					"stress_test_summary.html");
			stressSummaryWriter
					.write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable,th,td\n{\nfont-family: Calibri, sans-serif;\nborder-collapse: collapse;\nborder:1px solid black;\nfont-size: 10pt;\n}\ntd\n{\ntext-align:right;\n}\ntable\n{\nwidth: 100%;\n}\n</style>\n</head>\n<body>\n<h2>"
							+ configFilename.replace("ProfilerConfig.xml", "")
							+ " Stress Test Results, "
							+ dateFormat.format(date) + "</h2>\n<table>\n");
			stressSummaryWriter
					.write("<tr><th>Input Type</th><th>Status</th><th>Exceptions Thrown</th><th>Avg Process Time (s)</th><th>Max Process Time</th><th>Avg Heap Usage (MB)</th><th>Max Heap Usage</th><th>Input Profiling Results</th><tr>\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void subInitialize(String outputDir, String subDir) {
        totalFiles = 0;
        processTimes = new ArrayList<Double>();
        heapUsages = new ArrayList<Double>();
        fileSizes = new ArrayList<Double>();
        numOfChars = new ArrayList<Double>();
        File outDir = new File(outputDir + "/" + subDir);
        if (!outDir.exists()) {
            System.out.println("creating directory: " + outDir);
            boolean result = outDir.mkdir();
            if (result) {
                System.out.println("created directory: " + outDir);
            }
        }

        documentStatsWriter = new OutFileWriter(outDir, "document_stats.txt");
        documentHtmlWriter = new OutFileWriter(outDir, "document_stats.html");
        machineStatsWriter = new OutFileWriter(outDir, "machine_stats.txt");
        testSummaryWriter = new OutFileWriter(outDir, "test_summary.txt");
		try {
			testSummaryWriter
					.write("Config File Name\tActivation Time\tTotal File Count\tFailure Count\tTotal Process Time\tAverage File Size\tMax File Size\tMin File Size\tFile Size Variance\tAvg Char Count\tMax Char Count\tMin Char Count\tChar Count Variance\tAvg Process Time (s)\tMax Process Time\tMin Process Time\tProcess Time Variance\tAvg Heap Usage\tMax Heap Usage\tMin Heap Usage\tHeap Usage Variance\t(Final Heap - Initial Heap)\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testHtmlWriter = new OutFileWriter(outDir, "test_summary.html");
        try {
            // TODO - write class name here.
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = new Date();
            testHtmlWriter.write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable,th,td\n{\nfont-family: Calibri, sans-serif;\nborder-collapse: collapse;\nborder:1px solid black;\nfont-size: 10pt;\n}\ntd\n{\ntext-align:right;\n}\ntable\n{\nwidth: 100%;\n}\n</style>\n</head>\n<body>\n<h2>" + configFilename.replace("ProfilerConfig.xml", "") + " " + subDir + " Profiling Results, " + dateFormat.format(date) + "</h2>\n<table>\n");
            String format = "<tr><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><tr>\n";
            String line = String.format(format,
            		"Activation Time (s)",
            		"Total File Count",
            		"Failure Count",
            		"Total Process Time (s)",
            		"Average File Size (Bytes)",
            		"Max File Size",
            		"Min File Size",
            		"File Size Variance",
            		"Avg Char Count",
            		"Max Char Count",
            		"Min Char Count",
            		"Char Count Variance",
            		"Avg Process Time (s)",
            		"Max Process Time",
            		"Min Process Time",
            		"Process Time Variance",
            		"Avg Heap Usage (MB)",
            		"Max Heap Usage",
            		"Min Heap Usage",
            		"Heap Usage Variance",
            		"(Final Heap - Initial Heap)");
            testHtmlWriter.write(line);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //process_lastRecorded = 0;

        xmlserializer = new XMLSerializer(SerializationType.XML);
    }

    public class doProcessClass implements Callable<HltContentContainer> {
        private Document document;
        private HltContentContainer hltContentContainer;

        public doProcessClass(Document document, HltContentContainer hltContentContainer) {
            this.document = document;
            this.hltContentContainer = hltContentContainer;
        }

        public HltContentContainer call() throws Exception {
            try {                // create the HltContentContainer object
                if (hltContentContainer == null) hltContentContainer = new HltContentContainer();
                process.reset();
                process.start();
//				totalprocess.start();
                hltContentContainer = doProcess(document, hltContentContainer);
                process.stop();
                logger.info("hltContentContainer populated with sentences, POS tags and Entity mentions");
                return hltContentContainer;
            } catch (Exception e) {
                if (process.isRunning()) {
                    process.stop();
                }
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Creates the hlt content container.
     *
     * @param document the document
     * @return the hlt content container
     */
    public HltContentContainer createHltContentContainer(Document document, HltContentContainer hltContentContainer) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<HltContentContainer> future = executor.submit(new doProcessClass(document, hltContentContainer));
        HltContentContainer retHltContentContainer = null;
        try {
            retHltContentContainer = future.get(600, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            if (process.isRunning()) {
                process.stop();
            }
        }
	executor.shutdown();
        return retHltContentContainer;
    }
    
    private void writeToSummary(int failureCount) {

        String summaryLineFormat = "%s\t%.2f\t%d\t%d\t%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.1f\t%.1f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n";    	

            String line = String.format(summaryLineFormat,
            		configFilename,
            		activate.elapsedTime(TimeUnit.MILLISECONDS) / 1000.0,
            		totalFiles,
            		failureCount,
            		totalprocess.toString().replace(" s", ""),
            		calculateAverage(fileSizes),
            		calculateMax(fileSizes),
            		calculateMin(fileSizes),
            		calculateVariance(fileSizes),
            		calculateAverage(numOfChars),
            		calculateMax(numOfChars),
            		calculateMin(numOfChars),
            		calculateVariance(numOfChars),
            		calculateAverage(processTimes),
            		calculateMax(processTimes),
            		calculateMin(processTimes),
            		calculateVariance(processTimes),
            		calculateAverage(heapUsages),
            		calculateMax(heapUsages),
            		calculateMin(heapUsages),
            		calculateVariance(heapUsages),
            		(heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)));

            testSummaryWriter.write(line);
            
            testSummaryWriter.flush();
    }
    
    private void finishInputSummary(int failureCount, String inputType, Map<String, Integer> exceptions) {

        double controlTime = 0;
        double controlSize = 0;
        double controlHeap = 0;

        String summaryLineFormat = "%s\t%.2f\t%d\t%d\t%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.1f\t%.1f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n";
        String summaryHtmlFormat = "<tr><td>%.3f</td><td>%d</td><td>%d</td><td>%s</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.1f</td><td>%.1f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td></tr>\n";
        String stressHtmlFormat = "><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td><a href=\"%s/test_summary.html\">Input Profiling Statistics</a></td><tr>";

            documentStatsWriter.flush();
            documentStatsWriter.close();
            documentHtmlWriter.write("</table>\n</body>\n</html>");
            documentHtmlWriter.flush();
            documentHtmlWriter.close();

            // write in the machine_stats file
            String javaVersion = System.getProperty("java.version");
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            long machineMemory = Runtime.getRuntime().maxMemory();
            int machineCores = Runtime.getRuntime().availableProcessors();

            machineStatsWriter.write(machineMemory + "\t" + machineCores + "\t" + osName + "\t"
                    + osVersion + "\t" + javaVersion + "\t" + "yes");
            machineStatsWriter.flush();
            machineStatsWriter.close();

            // write in the test_summary file
            String line = String.format(summaryLineFormat,
            		configFilename,
            		activate.elapsedTime(TimeUnit.MILLISECONDS) / 1000.0,
            		totalFiles,
            		failureCount,
            		totalprocess.toString().replace(" s", ""),
            		calculateAverage(fileSizes),
            		calculateMax(fileSizes),
            		calculateMin(fileSizes),
            		calculateVariance(fileSizes),
            		calculateAverage(numOfChars),
            		calculateMax(numOfChars),
            		calculateMin(numOfChars),
            		calculateVariance(numOfChars),
            		calculateAverage(processTimes),
            		calculateMax(processTimes),
            		calculateMin(processTimes),
            		calculateVariance(processTimes),
            		calculateAverage(heapUsages),
            		calculateMax(heapUsages),
            		calculateMin(heapUsages),
            		calculateVariance(heapUsages),
            		(heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)));

            testSummaryWriter.write(line);
            
            testSummaryWriter.flush();
            testSummaryWriter.close();
            
            line = String.format(summaryHtmlFormat,
            		activate.elapsedTime(TimeUnit.MILLISECONDS) / 1000.0,
            		totalFiles,
            		failureCount,
            		totalprocess.toString().replace(" s", ""),
            		calculateAverage(fileSizes),
            		calculateMax(fileSizes),
            		calculateMin(fileSizes),
            		calculateVariance(fileSizes),
            		calculateAverage(numOfChars),
            		calculateMax(numOfChars),
            		calculateMin(numOfChars),
            		calculateVariance(numOfChars),
            		calculateAverage(processTimes),
            		calculateMax(processTimes),
            		calculateMin(processTimes),
            		calculateVariance(processTimes),
            		calculateAverage(heapUsages),
            		calculateMax(heapUsages),
            		calculateMin(heapUsages),
            		calculateVariance(heapUsages),
            		(heapUsages.get(heapUsages.size() - 1) - heapUsages.get(0)));
            
            testHtmlWriter.write(line);
            testHtmlWriter.write("</table><p>\n<a href=\"document_stats.html\">Full Document Statistics</a>\n<p><img src=\"ProcessTime_vs_FileSize.png\">\n<p><img src=\"ProcessTime_vs_NumberOfTokens.png\">\n<p><img src=\"ProcessTime_vs_NumberOfPassages.png\">\n<p><img src=\"HeapMemoryUsage.png\">\n</body>\n</html>");

            testHtmlWriter.flush();
            testHtmlWriter.close();
            String exceptionStrings = "";
            String status = "SUCCESS";
            double avgTime = calculateAverage(processTimes);
            double avgSize = calculateAverage(fileSizes);
            double avgHeap = calculateAverage(heapUsages);
            if (inputType.toLowerCase().equals("control")) {
                controlTime = avgTime;
                controlSize = avgSize;
                controlHeap = avgHeap;
            }
            stressSummaryWriter.write("<tr");
            if (exceptions.keySet().size() >= 1) {
                status = "FAILURE";
                for (String exception : exceptions.keySet()) {
                    exceptionStrings = exceptionStrings + exception + "\n";
                }
                stressSummaryWriter.write(" bgcolor=\"#FF0000\"");
            } else if (avgTime * controlSize > timeRatio * controlTime * avgSize || avgHeap * controlSize > heapRatio * controlHeap * avgSize) {
                status = "LOW PERFORMANCE";
                stressSummaryWriter.write(" bgcolor=\"#FFFF00\"");
            }
            line = String.format(stressHtmlFormat,
            		inputType,
            		status,
            		exceptionStrings,
            		avgTime,
            		calculateMax(processTimes),
            		avgHeap,
            		calculateMax(heapUsages),
            		inputType);
            stressSummaryWriter.write(line);
    }

    private void writeDocStats(File file, Double[] docStatistics) {

        String lineFormat = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t\n";
        String htmlFormat = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td></tr>\n";

        Runtime runtime = Runtime.getRuntime();
        //runtime.gc();
        double heapUsage = ((double) runtime.totalMemory() - (double) runtime.freeMemory()) / MB;
        heapUsages.add(heapUsage);

            logger.info("now writing profiling statistics to file");
            double elapsed = process.elapsedTime(TimeUnit.MILLISECONDS) / 1000.0;
            processTimes.add(elapsed);
            String line = String.format(lineFormat,
                    file.getName(),
                    docStatistics[0],
                    docStatistics[3],
                    docStatistics[2],
                    docStatistics[1],
                    elapsed,
                    heapUsage);
            documentStatsWriter.write(line);
            documentStatsWriter.flush();
            line = String.format(htmlFormat,
                    file.getName(),
                    docStatistics[0],
                    docStatistics[3],
                    docStatistics[2],
                    docStatistics[1],
                    elapsed,
                    heapUsage);
            documentHtmlWriter.write(line);
            documentHtmlWriter.flush();
    }
    
    private void writeDocFileHeaders(String inputType) {

        String lineFormat = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t\n";
        String htmlTopRowFormat = "<tr><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th></tr>\n";

            //String line = "Time taken in activate(): " + activate.elapsedTime(TimeUnit.MILLISECONDS)/1000.0 + "\n\n";
            //document_stats_writer.write(line);
            String line = String.format(lineFormat,
                    "Doc name",
                    "Document Size(Bytes)",
                    "Number of Characters",
                    "Number of Passages",
                    "Number of Tokens",
                    "Process time(seconds)",
                    "Heap usage(MB)");
            documentStatsWriter.write(line);
            documentStatsWriter.flush();
        //activate();

            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = new Date();
            documentHtmlWriter.write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable,th,td\n{\nfont-family: Calibri, sans-serif;\nborder-collapse: collapse;\nborder:1px solid black;\nfont-size: 10pt;\n}\ntd\n{\ntext-align:right;\n}\ntable\n{\nwidth: 100%;\n}\n</style>\n</head>\n<body>\n<h2>" + configFilename.replace("ProfilerConfig.xml", "") + " " + inputType + " Profiling Results, " + dateFormat.format(date) + "</h2>\n<table>\n");
            line = String.format(htmlTopRowFormat,
                    "Doc name",
                    "Document Size(Bytes)",
                    "Num of Characters",
                    "Num of Passages",
                    "Num of Tokens",
                    "Process time(seconds)",
                    "Heap usage(MB)");
            documentHtmlWriter.write(line);
            documentHtmlWriter.flush();
    }

    /**
     * The main method.
     *
     * @param packagePathIn    the package path in
     * @param configFilenameIn the config filename in
     * @param profileFilename  the profile filename
     * @param outputDir        the output dir
     */
    public void Run(String packagePathIn, String configFilenameIn, String profileFilename, String outputDir) {
        packagePath = packagePathIn;
        configFilename = configFilenameIn;
        initialize(profileFilename, outputDir);

        for (String inputType : inputTypes) {
            Map<String, Integer> exceptions = new HashMap<String, Integer>();
            subInitialize(outputDir, inputType);
            boolean bFirst = true;
            boolean bSuccess;
            Double[] docStatistics = {0.0, 0.0, 0.0, 0.0};
            HltContentContainer HltContentContainer;
            int fileCount = 0, failureCount = 0;
            File dataDirectory = dataDirectories.get(inputType);
            System.out.println("Getting " + inputType + " files from " + dataDirectory.getPath());
            int dirSize = dataDirectory.listFiles().length;
            int summarySize = (dirSize + 1) / 10;
            totalprocess.reset();
            totalprocess.start();
            //
            for (File file : dataDirectory.listFiles()) {
                totalFiles++;
                //bSuccess = true;
                if (bFirst) {
                    //String workingDirectory = System.getProperty("user.dir") + "/" + packagePath;
                    activate.start();
                    doActivate(algorithmConfig);
                    activate.stop();
                    //activate();
                    writeDocFileHeaders(inputType);
                }
                bFirst = false;

                HltContentContainer = new HltContentContainer();
                try {
                    bSuccess = processInputFile(file, docStatistics, HltContentContainer, inputType);
                } catch (Exception e) {
                    bSuccess = false;
                    exceptions.put(e.toString(), 1);
                }
                ++fileCount;

                if (!bSuccess) {
                    failureCount++;
                }

                writeDocStats(file, docStatistics);

                //process_lastRecorded = process.elapsedTime(TimeUnit.MILLISECONDS)/1000.0 ;

                if (totalFiles % summarySize == 0) {
                	writeToSummary(failureCount);
                }
            } // end for listFiles

            finishInputSummary(failureCount, inputType, exceptions);
            
            System.out.println("Average time taken in process: " + (process.elapsedTime(TimeUnit.MILLISECONDS) / 1000.0 / (fileCount * NUM_OF_LOOPS)) / 1000.0);
            System.out.println("Number of failures: " + failureCount);

            makeGraphs(inputType, outputDir);

        } // end for inputTypes

        try {
        	stressSummaryWriter.flush();
        	stressSummaryWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        doDeactivate();
    }

    private void makeGraphs(String inputType, String outputDir) {

        // graph making
        try {
        	BufferedReader br = documentStatsWriter.getBR();
            for (int j = 0; j < 5; j++) {
                br.readLine();
            }
            String line;
            ArrayList<Double> heapUsage = new ArrayList<Double>();
            ArrayList<Double> processTimes = new ArrayList<Double>();
            ArrayList<Double> numOfPassages = new ArrayList<Double>();
            ArrayList<Double> numOfTokens = new ArrayList<Double>();
            ArrayList<Double> fileSizes = new ArrayList<Double>();
            while ((line = br.readLine()) != null) {
                String[] temp = line.split("\t");
                heapUsage.add(Double.parseDouble(temp[6]));
                processTimes.add(Double.parseDouble(temp[5]));
                fileSizes.add(Double.parseDouble(temp[1]));
                numOfPassages.add(Double.parseDouble(temp[3]));
                numOfTokens.add(Double.parseDouble(temp[4]));
            }
            br.close();

            String dir = outputDir + "/" + inputType;
            Grapher.makeHeapUsageGraph(heapUsage, new File(dir + "/HeapMemoryUsage.png"));
            Grapher.makeTimeVsSizeGraph(processTimes, fileSizes, new File(dir + "/ProcessTime_vs_FileSize.png"), "Profiling Corpus", "File Size(Bytes)", "Process Time vs. File Size", "Process Time (s)");
            Grapher.makeTimeVsSizeGraph(processTimes, numOfPassages, new File(dir + "/ProcessTime_vs_NumberOfPassages.png"), "Profiling Corpus", "Num of Passages", "Process Time vs. Passage Count", "Process Time (s)");
            Grapher.makeTimeVsSizeGraph(processTimes, numOfTokens, new File(dir + "/ProcessTime_vs_NumberOfTokens.png"), "Profiling Corpus", "Num Of Tokens", "Process Time vs. Token Count", "Process Time (s)");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Process input file.
     *
     * @param file          the file
     * @param docStatistics the doc statistics
     * @param HltContentContainer  the HltContentContainer
     * @return true, if successful
     */
    private boolean processInputFile(File file, Double[] docStatistics, HltContentContainer HltContentContainer, String inputType) throws Exception {
        boolean bSuccess = true;

        /** Create a document object. */
        //File file = new File(inputDirectory+filename);
        if (file.exists()) docStatistics[0] = (double) file.length();
        fileSizes.add(docStatistics[0]);

        /** Create a document object. */
        String docId = file.getName();
        Corpus corpus = null;
        String docType = "Text";
        String uri = file.getAbsolutePath();
        String language = "English";
        adept.common.Document document = DocumentMaker.getInstance().createDefaultDocument(
                docId,
                corpus,
                docType,
                uri,
                language,
                uri,        // filename
                HltContentContainer,
                tokenizerType);
        if (document != null) {
            docStatistics[1] = (double) document.getDefaultTokenStream().size();
            if (HltContentContainer.getPassages() != null) {
                System.out.println("Number of passages: " + HltContentContainer.getPassages().size());
                docStatistics[2] = (double) HltContentContainer.getPassages().size();
            } else {
                System.out.println("NO PASSAGES FOUND");
                //docStatistics[1] = 0.0;
            }

            docStatistics[3] = (double) document.getValue().length();
            numOfChars.add(docStatistics[3]);

            // create the HltContentContainer object
            HltContentContainer = createHltContentContainer(document, HltContentContainer);

            // serialize hltcc and save to output directory
            if (HltContentContainer == null) {
//				totalprocess.stop();
                bSuccess = false;
            } else {
                try {
                    String serialized = xmlserializer.serializeAsString(HltContentContainer);
                    Writer.getInstance().writeToFile(outputDirectory + "/" + inputType + "/" + file.getName(), serialized);
//					totalprocess.stop();
                } catch (Exception e) {
//					totalprocess.stop();
                    e.printStackTrace();
                }
            }

            return bSuccess;
        } else return false;

    }

    /**
     * File to lines.
     *
     * @param filename the filename
     * @return the list
     */
    private static List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line;
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

    // TODO -replace these methods with Guava DoubleMath calls
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
