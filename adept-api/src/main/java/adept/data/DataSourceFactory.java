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