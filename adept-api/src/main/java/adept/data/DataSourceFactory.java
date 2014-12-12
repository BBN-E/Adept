/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.data;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSourceFactory has following responsibilies: (1) maintains list
 * one or more available data sources (2) Provides methods to create and add new
 * data source.
 */
public class DataSourceFactory {

	/** The Constant DataSourceFactory. */
	static final DataSourceFactory dataSourceFactory = new DataSourceFactory();

	/** The data source list. */
	Map<String, AbstractDataSource> dataSourceMap;

	/**
	 * Instantiates a new data source factory.
	 */
	private DataSourceFactory() {

	}

	/**
	 * Inits the.
	 */
	public void initialize() {

	}

	/**
	 * Gets the data source factory.
	 * 
	 * @return the data source factory
	 */
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	/**
	 * Creates the data source.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param connectionProperties
	 *            the credentials
	 * @return the data source
	 */
	public AbstractDataSource createDataSource(String dataSourceName,
			ConnectionProperties connectionProperties) {
		// TODO:
		AbstractDataSource abstractDataSource = new FileSource(dataSourceName,
				connectionProperties);
		dataSourceMap.put(dataSourceName, abstractDataSource);
		return abstractDataSource;

	}

	/**
	 * Removes the data source.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 */
	public void removeDataSource(String dataSourceName) {
		dataSourceMap.remove(dataSourceName);
	}

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public AbstractDataSource getDataSource() {
		return null;
	}

}
