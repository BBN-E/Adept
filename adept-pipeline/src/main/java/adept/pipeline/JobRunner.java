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