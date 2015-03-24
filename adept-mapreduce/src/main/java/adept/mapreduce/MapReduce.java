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

package adept.mapreduce;

import java.io.IOException;
import java.util.*;
import java.lang.Class;
import java.net.URL;
import java.net.URLClassLoader;

import adept.common.*;
import adept.utilities.*;
import adept.serialization.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;

//import com.bbn.mapreduce.BBNNamedEntityTaggerMapReduce;

public class MapReduce extends Configured implements Tool
{	

	//Singleton instance
	private static MapReduce instance;
		
	public static MapReduce getInstance()
		{
			if(instance==null)
				return new MapReduce();
			else
				return instance;
		}	
	
	
   public int run(String[] args)
   {
	   try
	   {
		   Configuration job = getConfiguration(args[0],args[1],args[2]);
		   JobClient.runJob((JobConf)job);
		   return 0;
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
	   return 1;
   }
   
   public JobConf getConfiguration(String inputPath, String outputPath, String mapClass)
   throws Exception
   {
	      //Configuration conf = getConf();
	      Class thisclass = getClass();
	      JobConf job = new JobConf(new Configuration(), thisclass);
          
	      try
	      {
	    	  Path in = new Path(inputPath);

		      Path out = new Path(outputPath);
		      FileInputFormat.setInputPaths(job, in);
		      FileOutputFormat.setOutputPath(job, out);
		    
		      job.setJobName("Algorithm Map-Reduce");
		      job.setMapperClass((Class<? extends Mapper>)Class.forName(mapClass));

	      }
	      catch(Exception e)
	      {
	    	  throw new RuntimeException("Exception occurred: " + e.getMessage());
	      }
	      
	      job.setReducerClass(AdeptReducer.class);
	      job.setInputFormat(KeyValueTextInputFormat.class);
	      job.setOutputFormat(TextOutputFormat.class);
	      job.setOutputKeyClass(Text.class);
	      job.setOutputValueClass(Text.class);
	      job.set("key.value.separator.in.input.line", "\t");
	      
	      return job;
   }
   
   /*private Class loadClass(String className, URL[] urls)
   throws Exception
   {
	   
	   URLClassLoader urlClassLoader = new URLClassLoader(urls);
	   return urlClassLoader.loadClass(className);
   }*/

}