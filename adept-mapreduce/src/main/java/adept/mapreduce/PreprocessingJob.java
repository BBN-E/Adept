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

import adept.common.*;
import adept.utilities.DocumentMaker;
import adept.utilities.StanfordSentenceSegmenter;
import adept.serialization.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;

public class PreprocessingJob extends AdeptMapper
{
	protected void doActivate(String algorithmConfig)
	{
		//NO-OP
	}
	
	
   protected void doDeactivate()
	{
		//NO-OP
	}
	
	
   public void map(Text key, Text value,
           OutputCollector<Text, Text> output,
           Reporter reporter) throws IOException 
   {
     HltContentContainer hltcontentcontainer = new HltContentContainer();
     Document doc = DocumentMaker.getInstance().createDefaultDocument(key.toString(),
    		 null,null,null,null,value.toString(),hltcontentcontainer);

     // sentence segmentation.For now, consider all text as a single sentence.
     List<Sentence> sentences = new ArrayList<Sentence>();
     sentences.addAll(StanfordSentenceSegmenter.getInstance().getSentences(doc.getValue(),doc.getDefaultTokenStream()));
     hltcontentcontainer.setSentences(sentences);

     XMLSerializer xmlserializer = new XMLSerializer(SerializationType.XML);
     String serializedHltContainer = xmlserializer.serializeAsString(hltcontentcontainer);
     serializedHltContainer = serializedHltContainer.replaceAll("\r\n"," ");
     serializedHltContainer = serializedHltContainer.replaceAll("\n"," ");
     output.collect(key, new Text(serializedHltContainer));
   }
   
   protected HltContentContainer doProcess(HltContentContainer hltcontentcontainer)
	{
	   //NO-OP
	   return null;
	}
   
   protected String getConfigFileName()
	{
		return null;
	}

}