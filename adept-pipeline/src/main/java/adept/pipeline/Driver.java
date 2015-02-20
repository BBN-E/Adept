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

package adept.pipeline;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.*;
import java.net.URL;

import adept.common.*;
import adept.utilities.*;
import adept.serialization.*;
import adept.io.Reader;
import adept.mapreduce.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.jobcontrol.*;
import org.apache.hadoop.conf.*;


public class Driver 
{
     public static void main(String[] args) throws Exception
     {
    	 JobControl jc = new JobControl("Job Control");
    	 
    	 /** mapping from classname to actual Hadoop Job instance 
    	     that represents this job*/
    	 Map<String,Job> classnamesToJobs = new HashMap<String,Job>();
    	 
    	 if(args.length < 1)
    	 {
    		 System.out.println("Please provide the configuration file path as a command line argument.");
    		 System.exit(1);
    	 }
    	 
    	 
    	 // read configuration file
    	 //BufferedReader br = new BufferedReader(new InputStreamReader(Reader.findStreamInClasspathOrFileSystem("pipeline/pipelineconfig.txt")));
    	 BufferedReader br = new BufferedReader(new InputStreamReader(Reader.findStreamInClasspathOrFileSystem(args[0])));
    	 
    	 String line;
    	 
    	 
    	 //add jobs to job control
    	 while((line=br.readLine())!=null)
    	 {
    		 String[] temp = line.split("\t");
    		 if(temp.length != 4)
    		 {
    			 System.out.println("The pipeline configuration file format is incorrect.");
    			 System.out.println("The correct format is: ");
    			 System.out.println("classname	dependency classnames	inputfile	outputdir");
    			 System.out.println("Please correct this issue and try again!");
    		 }
    		 
        	 Job job = new Job(
    		         MapReduce.getInstance().getConfiguration(temp[2],temp[3],
    		        		 temp[0]));

        	 // add dependencies
        	 if(!temp[1].equals(""))
        	 {
        		 String[] dependencies = temp[1].split(",");
        		 //TODO: Modify this for complex dependencies to 
        		 // add fusion job
        		 for(String dependency : dependencies)
        		 {
        			 //System.out.println("dependency is: " + dependency);
        			 if(classnamesToJobs.get(dependency) == null)
        			 {
        				 System.out.println("Dependency job not found. Please check your pipeline configuration file." +
        				 		"All dependencies need to have been created first.");
        			     System.exit(1);
        			 }
        			 job.addDependingJob(classnamesToJobs.get(dependency));
        		 }
        	 }
        	 
        	// populate map
        	 classnamesToJobs.put(temp[0],job);
        	 
        	 jc.addJob(job);
    	 }

    	 handleRun(jc);
     }
     
     
     public static void handleRun(JobControl control) 
     {
    	 try 
    	 {
    		 JobRunner runner = new JobRunner(control);
    		 Thread t = new Thread(runner);
    		 t.start();
    		 
    		 while(!control.allFinished()) {
    			 //System.out.println("Still running...");
    			 Thread.sleep(2000);
    		 }
    		 
    		 t.stop();
    	 }
    	 catch(Exception e)
    	 {
    		 System.out.println("in catch block");
    		 System.out.println(e.getMessage());
    		 System.exit(1);
    	 }
     }
}