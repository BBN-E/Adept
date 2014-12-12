/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.mapreduce;

import java.io.IOException;
import java.util.*;
import java.lang.Class;

import adept.common.*;
import adept.utilities.*;
import adept.serialization.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;

public class AdeptReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>
{
	public void reduce(Text key, Iterator<Text> values,
            OutputCollector<Text, Text> output,
            Reporter reporter) throws IOException 
            {
               while (values.hasNext()) 
               {
	              output.collect(key, values.next());
               }

            }
}
