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

public abstract class AdeptMapper extends MapReduceBase implements Mapper<Text, Text, Text, Text>
{
	private static String configFileName;
	private static XMLSerializer xmlserializer = new XMLSerializer(SerializationType.XML);
	
	/** Abstract methods */
	protected abstract void doActivate(String algorithmConfig);
	protected abstract String getConfigFileName();
	protected abstract void doDeactivate();
	protected abstract HltContentContainer doProcess(HltContentContainer hltcontainer);
	
	public void configure(JobConf job)
    {
    	super.configure(job);
    	doActivate(getConfigFileName());
    	System.out.println("ACTIVATE DONE");
    }


    public void map(Text key, Text value,
                    OutputCollector<Text, Text> output,
                    Reporter reporter) throws IOException
    {
	
    	try
    	{
    		HltContentContainer hltcontentcontainer = (HltContentContainer)xmlserializer.deserializeString(value.toString(),HltContentContainer.class);
    		hltcontentcontainer = doProcess(hltcontentcontainer);
    		
    		String serializedHltContainer = xmlserializer.serializeAsString(hltcontentcontainer);
        	serializedHltContainer = serializedHltContainer.replaceAll("\\r\\n"," ");
        	serializedHltContainer = serializedHltContainer.replaceAll("\\n"," ");
            output.collect(key, new Text(serializedHltContainer));
    	}
    	catch(Exception e)
    	{
    		//System.out.println(e.getMessage());
    		System.out.println("Exception thrown in map function: " + e.getLocalizedMessage());
    	}
    	
    	
    }
 

    public void close()
    {
    	try
    	{
    		super.close();
        	doDeactivate();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    

}
