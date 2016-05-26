/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
