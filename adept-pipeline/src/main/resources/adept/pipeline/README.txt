=====================================================================
 stanford-adept-1.3-SNAPSHOT Package                       README.TXT
=====================================================================

 This file describes the DEFT stanford-adept-1.3-SNAPSHOT package and
 how to install and use it. Specific topics are:
 
  1. System Requirements
  2. Installation
  3. Regression Test
  4. Running from command-line
  5. Running using REST API
  6. Integrating using ADEPT API  
  7. Technical Support

=====================================================================
  1. System Requirements
=====================================================================
  Supported Operating Systems:
   - Red Hat Enterprise Linux, version 5.x
   - Scientific Linux, version 5.x
  
  Recommended CPU, Memory and Storage 
   - 8GB RAM
   - 2 cores 2.9 GHz CPU  
   - 60GB Hard Drive
  
  Runtime Environment:
   - JRE 1.7 Update 17
   - Python 2.6 or greater
   - Set JAVA_HOME Environment Variable to JRE 1.7 
  
=====================================================================
  2. Installation
=====================================================================
 
 The DEFT Stanford-Adept package is contained in the gzipped tar
 file stanford-adept-1.3-SNAPSHOT-$TIMESTAMP.tar.gz.  
 Note that the variable $TIMESTAMP refers to the 13-character time 
 of the file's creation, formatted as "yyyyMMdd-HHmm". 
 Unpack the tar file using the following command:
   
 tar xvzf stanford-adept-1.3-SNAPSHOT-$TIMESTAMP.tar.gz
        
 The result will be the following directory structure:

 stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/
    README.txt         <-    This file
    COPYRIGHT.txt      <-    DEFT software copyright notice
    bin/               <-    command-line applications 
    code-samples/      <-    Java code samples for command-line app and REST API client
    config/            <-    configuration files for regression test
    data/              <-    example data file for command-line application
    docs/              <-    documentation of the ADEPT APIs
    lib/               <-    the JAR files required by this package
    logs/              <-    log files
    out/               <-    output files from example runs
    test/              <-    regression test shell script and data files

 The current Alpha Release version stanford-adept-1.3-SNAPSHOT supports following
 Stanford DEFT Algorithms:
  1. Stanford Core NLP (Tokenization, Mention Detection and Coreference)
  2. Stanford MIML Relation Extraction (MIMLRE)

=====================================================================
  3. Regression Test
=====================================================================
 
  3.1. To run the Stanford Core NLP regression test, navigate to:
          stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/test
        
        Run the following command:
          ./StanfordCoreNlpRegressionTest
        
        Review the STDOUT to verify that the test result:
          SUCCESS - Regression Test Passed          
          FAILURE - Regression Test Failed
          
  3.2. To run the Stanford MIML Relation Extraction (RE) regression test,
       navigate to:
         stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/test
         
       Run the following command:
         ./StanfordMIMLRERegressionTest            

        Review the STDOUT to verify that the test result:
          SUCCESS - Regression Test Passed          
          FAILURE - Regression Test Failed
          
=====================================================================
  4.  Running from command-line
=====================================================================
  
  The app reads one or more documents and processes each using the 
  specified algorithm, writing the output of each as a serialized 
  HltContentContainer. By default, it looks for the input files in 
  the current directory, and writes the output files to the current 
  directory. The default input format is SGML and the default output 
  format is XML. The default input language is English which is the 
  only language currently supported. You can change the default 
  values (see OPTIONS, below).

  4.1. To run the Stanford Core NLP command-line app on one document, navigate to:
         stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
        
       Run the following example command:
         ./StanfordCoreNlpProcessorApp -i ../data/AFP_ENG_20030530.0132.sgm -o ../out/AFP_ENG_20030530.0132.out.xml
         
  4.2. To run the Stanford Core NLP command-line app on a list of documents, navigate to:
         stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
        
       Run the following example command:
         ./StanfordCoreNlpProcessorApp --input_directory ../data -l ../data/FileList.txt --output_directory ../out
                  
  4.3. To run the Stanford MIML RE command-line app, navigate to:
         stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
        
       Run the following example command:
         ./StanfordMIMLREApp -i ../data/AFP_ENG_19960823.0651.xml -o ../out/AFP_ENG_19960823.0651.out.xml
  
  4.4. To run the Stanford MIML RE command-line app on a list of documents, navigate to:
         stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
        
       Run the following example command:
         ./StanfordMIMLREApp --input_directory ../data -l ../data/FileList.txt --output_directory ../out  
  
  4.5. Command-line App Options    

    This app has a set of standard options that are supported on the current release and 
    will continue to be supported in future releases.
    
    Standard Options
    -h, --help                     Display this usage message.
    --algorithm_config <filename>  The name of a non-default configuration file to be passed to the algorithm.
                                   The file will be read from the current directory.
    -i, --input_file <filename>    A single input file to be run.
    --input_directory <directory>  Input files will be read from this directory.
    --input_format <format>        Valid input file formats are 'SGML' or 'text'.
    --input_language <language>    Sets the language of the input files.
                                   The only supported value at this time is 'english'.
    -l, --input_list <filename>    A file containing a list of input file names without path, one per line.
    --output_directory <directory> Output files will be written to this directory.
    -o, --output_file <filename>   The output file if a single input file is run with the '-i' option.
    --output_format <format>       Valid output file formats are 'JSON' or 'XML'.
    -v, -verbose                   Print out additional information.
    -version                       Print product version and exit.

=====================================================================
  5. Running using REST API
=====================================================================
 The Stanford Core NLP Algorithm also supports Client-Server interface
 via REST API. The "ADEPT_REST_Interface_Specficiations_v1.pdf" document
 available under the "docs" directory has detailed information on the 
 supported interface functions, data formats and error codes.

 5.1 To run the Stanford Core NLP Server, open a terminal window and navigate to:
       stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
        
     Run the following example command:                     
       StanfordCoreNlpServer 5500        

     This will start the Stanford Core NLP HTTP Server on port 5500

 5.2 To run the Stanford Core NLP Client, open a terminal window and navigate to:
       stanford-adept-1.3-SNAPSHOT-$TIMESTAMP/bin
       
     Run the following example command:
       StanfordCoreNlpClient ../data/AFP_ENG_20030530.0132.sgm localhost 5500 out_example.xml
       
 An example Java client implementation is provided under "code-samples"
 directory. The example Java client uses jetty library to perform HTTP 
 functions.
 
 To view this example, navigate to:
    code-samples/StanfordCoreNlpClient.java
	
 5.3 Use the same steps to run the StanfordMIMLREClient REST API.

=====================================================================
  6. Integrating using ADEPT API
=====================================================================
 The Stanford Core NLP and Relation Extraction Algorithms conform to 
 BBN-defined ADEPT APIs. The "ADEPT_API_Specifications_v2_9.pdf" document
 available under the "docs" directory has detailed information on 
 supported interface functions and input/output data structures.

 The Java code samples of "StanfordCoreNlpProcessorApp" and "StanfordMIMLREApp" 
 included under "code-samples" directory serve as examples of using the ADEPT 
 API to integrate with Stanford DEFT algorithms.
  
 Additional documentation is available at following links on the DEFT website:
 - http://www.deftdocs.bbn.com/SitePages/Software%20API%20Specifications.aspx
   
=====================================================================
  7. Technical Support
=====================================================================
  When contacting technical support, make sure you have included
  the following information:

  - Log files if available.
  - Whether you can reproduce the problem.
  - A description of the problem.
  - A description of what operation is being performed.
  - Operating system and relevant software packages installed.

  Email deft-support@bbn.com.

=====================================================================
                                END OF FILE
=====================================================================
