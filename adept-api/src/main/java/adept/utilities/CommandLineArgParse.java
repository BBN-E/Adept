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

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Parse DEFT command-line arguments using args4j.
 *
 * @author
 */
public class CommandLineArgParse {

	/** The b help. */
	@Option(name="--help",usage="help    Display this usage message.\n")
	public boolean bHelp;
	
	/** The b help2. */
	@Option(name="-h",usage="help    Display this usage message.\n")
	public boolean bHelp2;
	//
	/** The algorithm config. */
	@Option(name="--algorithm_config",usage="--algorithm_config <filename>\n"
			+ "The name of a non-default configuration file to be passed to the algorithm.  The file will be read from the current directory.\n")
	public String algorithmConfig;
	//
	/** The b cross doc. */
	@Option(name="--cross_doc",usage="--cross_doc   Process list of input files for cross-document references.\n")
	public boolean bCrossDoc;
	//
	/** The input file. */
	@Option(name="-i",usage="-i, --input_file <filename>   A single input file to be run.\n")
	public String inputFile;
	//
	/** The input directory. */
	@Option(name="--input_directory",usage=" --input_directory <directory>    Input files will be read from this directory.\n")
	public String inputDirectory;
	//
	/** The input format. */
	@Option(name="--input_format",usage=" --input_format <format>   Valid input file formats are 'SGML' or 'text' or 'amr'.\n")
	public String inputFormat;
	//
	/** The input language. */
	@Option(name="--input_language",usage="--input_language <language>\n"
			+ "Sets the language of the input files.  The supported values are 'en' (English) for all algorithms, and 'es' (Spanish) for a few.\n")
	public String inputLanguage;
	//
	/** The input list. */
	@Option(name="-l",usage="-l, --input_list <filename>\n"
			+ "A file containing a list of input file names without path, one per line.\n")
	public String inputList;
	
	/** The input list2. */
	@Option(name="--input_list",usage="-l, --input_list <filename>\n"
			+ "A file containing a list of input file names without path, one per line.\n")
	public String inputList2;
	//
	/** The output directory. */
	@Option(name="--output_directory",usage="--output_directory <directory>   Output files will be written to this directory.\n")
	public String outputDirectory;
	//
	/** The output file. */
	@Option(name="-o",usage="-o, --output_file <filename>\n"
			+ "The output file if a single input file is run with the '-i' option.\n")
	public String outputFile;
	//
	/** The output format. */
	@Option(name="--output_format",usage="--output_format <format>   Valid output file formats are 'JSON' or 'XML'.\n")
	public String outputFormat;
	//
	/** The b table output. */
	@Option(name="--table_output",usage="--table_output   Write out a tabular version of each HltContentContainer.\n")
	public boolean bTableOutput;
	//
	/** The b validate xml. */
	@Option(name="--validate_xml",usage="--validate_xml   Validate output XML using an XSD if available.\n")
	public boolean bValidateXml;
	//
	/** The b verbose. */
	@Option(name="-v",usage="-v, --verbose   Print out additional information.\n")
	public boolean bVerbose;
	
	/** The b verbose2. */
	@Option(name="--verbose",usage="-v, --verbose   Print out additional information.\n")
	public boolean bVerbose2;
	//
	/** The b version. */
	@Option(name="--version",usage="--version   Print product version and exit.\n")
	public boolean bVersion;
	//
	/** The b redirect */
	@Option(name="--redirect",usage=" --redirect   Redirect output log to file.\n")
	public boolean bRedirect;
	//
	/** The b run one file. */
	public boolean bRunOneFile = false;
	
	/** The b run list. */
	public boolean bRunList = false;

	// receives other command line parameters than options
	/** The arguments. */
	@Argument
	public List<String> arguments = new ArrayList<String>();
	
	/** The b quiet. */
	protected boolean bQuiet = false;

  private static final Logger logger = LoggerFactory.getLogger(CommandLineArgParse.class);
	
	/**
	 * Prints the message.
	 *
	 * @param message the message
	 */
	protected void printMessage( String message )
	{
		if ( !bQuiet) System.out.println(message);
	}

	/**
	 * Instantiates a new command line arg parse.
	 *
	 * @param args the args
	 */
	public CommandLineArgParse(String[] args) {
		// Note args4j requires an Object as a parameter, so must create "this".
		//CommandLineArgParse theProcessor = new CommandLineArgParse();
		boolean bContinue = false;
		try {
			bContinue = this.parseCommandLine(args);
		} catch (IOException e) {
			logger .error("Exception caught: ", e);
		}
		if ( bVerbose )
		{
			this.dumpArguments();
		}		
		if ( !bContinue )
		{
			if ( ! ( bVerbose || bHelp || bVersion ))
			{
				this.dumpArguments();
			}
			throw new IllegalArgumentException("Invalid Command Line Parameter");
		}
		// User has not requested early exit.
		bQuiet = true;
		bRunOneFile = this.validateArgumentsSingle();
		bRunList = this.validateArgumentsList();
		bQuiet = false;
		// Show messages for args that succeeded, or else errors for both if no valid args.
		if ( !bRunOneFile && !bRunList )
		{
			bContinue = false;
		}
		if ( bRunOneFile || !bContinue )			
		{
			this.validateArgumentsSingle();
		}
		if ( bRunList || !bContinue )			
		{
			this.validateArgumentsList();
		}
	}

	/**
	 * Validate arguments list.
	 *
	 * @return true, if successful
	 */
	public boolean validateArgumentsList() 
	{
		if ( isNullOrEmpty(inputDirectory) ) 
		{
			printMessage("No input directory");
			return false;
		}
		File fInDir = new File(inputDirectory);
		if(!fInDir.exists()) 
		{
			printMessage("Input directory not found:  " + inputDirectory);
			return false;
		}
		if ( isNullOrEmpty(inputList) )
		{
			printMessage("No input list. ");
			if ( bCrossDoc ) printMessage("Cannot use cross-document flag without input list. ");
			return false;
		}
		File fInList = new File(inputList);
		if ( !fInList.exists())
		{		
			printMessage("Input list not found:  " + inputList );
			return false;
		}
		if ( !isNullOrEmpty(algorithmConfig))
		{
			File fConfig = new File(algorithmConfig);
			if ( !fConfig.exists())
			{		
				printMessage("Config file not found:  " + algorithmConfig );
				return false;
			}		
		}
		if ( isNullOrEmpty(outputDirectory) ) 
		{
			printMessage("No output directory");
			return false;
		}
		File fOutDir = new File(outputDirectory);
		if(!fOutDir.exists()) 			
		{
			boolean wasDirectoryMade = fOutDir.mkdirs();
			if(wasDirectoryMade)
			{
				printMessage("Directory created:  " + outputDirectory);
			}
			else 
			{
				printMessage("Unable to create output directory:  " + outputDirectory);
				return false;
			}
		}
		printMessage("Running list of files.");
		return true;
	}

	/**
	 * Validate arguments single.
	 *
	 * @return true, if successful
	 */
	public boolean validateArgumentsSingle() 
	{
		if ( isNullOrEmpty(inputFile) ) 
		{
			printMessage("No input file. ");
			return false;
		}
		File fInFile = new File(inputFile);
		if(!fInFile.exists()) 
		{
			printMessage("Input file not found:  " + inputFile);
			return false;
		}
		if ( isNullOrEmpty(outputFile)) 
		{
			printMessage("No output file. ");
			return false;
		}
		File fOutFile = new File(outputFile);
		if(fOutFile.exists()) 			printMessage("Output file will be overwritten.");
		printMessage("Running single file.");
		return true;
	}
	
	/**
	 * Checks if is null or empty.
	 *
	 * @param s the s
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmpty(String s)
	{
		if ( s == null ) return true;
		return s.isEmpty();
	}

	/**
	 * Parses the command line.
	 *
	 * @param args the args
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean parseCommandLine(String[] args) throws IOException {
		CmdLineParser parser = new CmdLineParser(this);

		// if you have a wider console, you could increase the value;
		// here 80 is also the default
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");
			bHelp = ( bHelp || bHelp2 );
			if ( bHelp ) 
			{
				printMessage(helpMsg);
				return false;
			}
			bVerbose = ( bVerbose || bVerbose2 );
			if ( isNullOrEmpty( inputList ))
			{
				inputList = inputList2;
			}
			//
			if ( bVerbose ) printMessage("Verbose mode.");
			if ( bVersion ) 
			{
				printMessage("Version 0.0");
				return false;
			}

			// after parsing arguments, you should check
			// if enough arguments are given.
//			if( arguments.isEmpty() )
//				throw new CmdLineException(parser,"No argument is given");

		} catch( CmdLineException e ) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java hltProcessDocuments [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			// print option sample. This is useful some time
			//System.err.println(" Example: java hltProcessDocuments"+parser.printExample(ALL));
			return false;
		}
		return true;
	}
	
	/**
	 * Dump arguments.
	 */
	private void dumpArguments()
	{
		printMessage("algorithmConfig = " + algorithmConfig);
		printMessage("inputFile = " +       inputFile);
		printMessage("inputDirectory = " +  inputDirectory);
		printMessage("inputFormat = " +     inputFormat);
		printMessage("inputLanguage = " +   inputLanguage);
		printMessage("inputList = " +       inputList);
		printMessage("outputDirectory = " + outputDirectory);
		printMessage("outputFile = " +      outputFile);
		printMessage("outputFormat = " +    outputFormat);


		// access non-option arguments
		printMessage("other arguments are:");
		for( String s : arguments )
			printMessage(s);
		
	}

	/** The help msg. */
	private String helpMsg = 
			"    This app reads one or more documents and processes each using the specified algorithm, writing the output of each as a serialized HltContentContainer.  "
					+ "    By default, it looks for the input files in the current directory, and writes the output files to the current directory.  The default input format is SGML and the default output format is XML.  The default input language is English which is the only language currently supported.  You can change the default values (see OPTIONS, below).\n"
					+ "\n"
					+ "OPTIONS\n"
					+ "    This app has a set of standard options that are supported on the current release and will continue to be supported in future releases.\n"
					+ "    \n"
					+ "    Standard Options\n"
					+ "    -h, --help                     Display this usage message.\n"
					+ "    --algorithm_config <filename>  The name of a non-default configuration file to be passed to the algorithm.\n"
					+ "                                   The file will be read from the current directory.\n"
					+ "    --cross_doc                    Process list of input files for cross-document references.\n"
					+ "    -i, --input_file <filename>    A single input file to be run.\n"
					+ "    --input_directory <directory>  Input files will be read from this directory.\n"
					+ "    --input_format <format>        Valid input file formats are 'SGML' or 'text' or 'amr'.\n"
					+ "    --input_language <language>    Sets the language of the input files.\n" 
					+ "                                   The only supported value at this time is 'english'.\n"
					+ "    -l, --input_list <filename>    A file containing a list of input file names without path, one per line.\n"
					+ "    --output_directory <directory> Output files will be written to this directory.\n"
					+ "    -o, --output_file <filename>   The output file if a single input file is run with the '-i' option.\n"
					+ "    --output_format <format>       Valid output file formats are 'JSON' or 'XML'.\n"
					+ "    --table_output                 Write out a tabular version of each HltContentContainer.\n"
					+ "    --validate_xml                 Validate output XML using an XSD if available.\n"
					+ "    -v, --verbose                  Print out additional information.\n"
					+ "    --version                      Print product version and exit.\n";

}

