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

package adept.pipeline;

import java.lang.Runnable;
import org.apache.hadoop.mapred.jobcontrol.*;

public class JobRunner implements Runnable
{
	private JobControl jobcontrol;
	
	public JobRunner(JobControl jc)
	{
		this.jobcontrol = jc;
	}
	
	public void run()
	{
		this.jobcontrol.run();
	}
}