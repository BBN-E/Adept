package adept.utilities;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Parse DEFT command-line arguments using args4j.
 *
 * @author
 */
public class DeftReaderArgParse extends CommandLineArgParse {

	/** The annotation file. */
	@Option(name="-a",usage="-a, --annotation_file <filename>   A single annotation file to be run.\n")
	public String annotationFile;
	//
	/** The annotation directory. */
	@Option(name="--annotation_directory",usage=" --annotation_directory <directory>    Annotation files will be read from this directory.\n")
	public String annotationDirectory;
	//
	/** The annotation format. */
	@Option(name="--annotation_format",usage=" --annotation_format <format>   Valid annotation file formats are 'SGML' or 'text' or 'ERE' or 'CoNLL'.\n")
	public String annotationFormat;


    /**
     * Instantiates a new deft reader arg parse.
     *
     * @param args the args
     */
    public DeftReaderArgParse(String[] args)
    {
        super(args);
        bRunList = bRunList || validateArgumentsListAnnotations();
    }
    
	/**
	 * Validate arguments list annotations.
	 *
	 * @return true, if successful
	 */
	public boolean validateArgumentsListAnnotations() 
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
		if ( isNullOrEmpty(outputDirectory) ) 
		{
			printMessage("No output directory");
			return false;
		}
		return true;
	}

	/**
	 * Dump arguments.
	 */
	private void dumpArguments()
	{
		printMessage("inputFile = " +            inputFile);
		printMessage("inputDirectory = " +       inputDirectory);
		printMessage("inputFormat = " +          inputFormat);
		printMessage("inputLanguage = " +        inputLanguage);
		printMessage("annotationFile = " +       annotationFile);
		printMessage("annotationDirectory = " +  annotationDirectory);
		printMessage("outputDirectory = " +      outputDirectory);
		printMessage("outputFile = " +           outputFile);
		printMessage("outputFormat = " +         outputFormat);


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
					+ "                                   The file will be read from the current directory.\n"
					+ "    -i, --input_file <filename>    A single input file to be run.\n"
					+ "    --input_directory <directory>  Input files will be read from this directory.\n"
					+ "    --input_format <format>        Valid input file formats are 'SGML' or 'text' or 'ERE' or 'CoNLL'.\n"
					+ "    --input_language <language>    Sets the language of the input files.\n" 
					+ "                                   The only supported value at this time is 'english'.\n"
					+ "    -a, --annotation_file <filename>    A single annotation file to be run.\n"
					+ "    --annotation_directory <directory>  Annotation files will be read from this directory.\n"
					+ "    --output_directory <directory> Output files will be written to this directory.\n"
					+ "    -o, --output_file <filename>   The output file if a single input file is run with the '-i' option.\n"
					+ "    --output_format <format>       Valid output file formats are 'JSON' or 'XML'.\n"
					+ "    --table_output                 Write out a tabular version of each HltContentContainer.\n"
					+ "    -v, --verbose                  Print out additional information.\n"
					+ "    --version                      Print product version and exit.\n";

}

