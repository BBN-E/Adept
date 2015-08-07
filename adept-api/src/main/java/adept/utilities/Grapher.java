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

package adept.utilities;

import java.io.File;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;


/**
 * The Class Grapher.
 */
public class Grapher
{
	 
 	/**
 	 * Make heap usage graph.
 	 *
 	 * @param values the values
 	 * @param filename the filename
 	 */
 	public static void makeHeapUsageGraph(ArrayList<Double> values, File filename)
	    {
	        try
	        {
	            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
	            for(int i=1; i<=values.size(); i++)
	            {
	            	line_chart_dataset.addValue(values.get(i-1), "MB", ""+i);
	            }              
	            
	             /* Step -2:Define the JFreeChart object to create line chart */
	             JFreeChart lineChartObject=ChartFactory.createLineChart("Heap Memory Usage","Run Number","Heap Memory Used",line_chart_dataset,PlotOrientation.VERTICAL,true,true,false);               
	                      
	             /* Step -3 : Write line chart to a file */              
	              int width=640; /* Width of the image */
	              int height=480; /* Height of the image */                           
	              ChartUtilities.saveChartAsPNG(filename,lineChartObject,width,height);
	        }
	       
	       catch (Exception e)
	        {
	          e.printStackTrace();
	        }

	}  
	 
	 /**
 	 * Make time vs size graph.
 	 *
 	 * @param timevalues the timevalues
 	 * @param sizevalues the sizevalues
 	 * @param filename the filename
 	 * @param linelabel the linelabel
 	 * @param Xlabel the xlabel
 	 * @param Ylabel the ylabel
 	 * @param title the title
 	 */
 	public static void makeTimeVsSizeGraph(ArrayList<Double> timevalues, ArrayList<Double> sizevalues, File filename, String linelabel, String Xlabel, String Ylabel, String title)
    {
		 try
		 {
		 
	            XYSeriesCollection scatter_plot_dataset = new XYSeriesCollection();
	            XYSeries data = new XYSeries(linelabel);
	            for(int i=0; i<sizevalues.size(); i++)
	            {
	            	data.add(sizevalues.get(i),timevalues.get(i));
	            }              
	            scatter_plot_dataset.addSeries(data);
	            
	             /* Step -2:Define the JFreeChart object to create line chart */
	             JFreeChart scatterPlotObject=ChartFactory.createScatterPlot(Ylabel,Xlabel,title,scatter_plot_dataset,PlotOrientation.VERTICAL,true,true,false);               
	                      
	             /* Step -3 : Write line chart to a file */              
	              int width=640; /* Width of the image */
	              int height=480; /* Height of the image */                           
	              ChartUtilities.saveChartAsPNG(filename,scatterPlotObject,width,height);
	        }
	       
	       catch (Exception e)
	        {
	          e.printStackTrace();
	        }

	}  
}