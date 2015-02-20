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

// TODO: Auto-generated Javadoc
/**
 * The Class DataSource.
 */
public abstract class AbstractDataSource {

	/**
	 * The Enum DataSourceType.
	 */
	public enum DataSourceType {

		/** The file. */
		FILE,
		/** The nosql. */
		NOSQL,
		/** The sql. */
		SQL,
		/** The triple. */
		TRIPLE,
	}

	/**
	 * The Enum DataSourceConnectionStatus.
	 */
	public enum DataSourceConnectionStatus {

		/** The connected. */
		CONNECTED,
		/** The unavailable. */
		UNAVAILABLE,
		/** The connection error. */
		CONNECTION_ERROR
	};

	/** The database name. */
	private final String dataSourceName;

	/** The credentials. */
	private final ConnectionProperties connectionProperties;

	/** The data source type. */
	protected final DataSourceType dataSourceType;

	/**
	 * Instantiates a new abstract data source.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param dataSourceType
	 *            the data source type
	 * @param connectionProperties
	 *            the connection properties
	 */
	public AbstractDataSource(String dataSourceName,
			DataSourceType dataSourceType,
			ConnectionProperties connectionProperties) {
		super();
		this.dataSourceName = dataSourceName;
		this.connectionProperties = connectionProperties;
		this.dataSourceType = dataSourceType;
	}

	/**
	 * Connect.
	 */
	public abstract void connect();

	/**
	 * Close.
	 */
	public abstract void close();

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public abstract DataSourceConnectionStatus getStatus();

	/**
	 * Gets the data source type.
	 * 
	 * @return the data source type
	 */
	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}

	/**
	 * Gets the data source name.
	 * 
	 * @return the data source name
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * Gets the connection properties.
	 * 
	 * @return the connection properties
	 */
	public ConnectionProperties getConnectionProperties() {
		return connectionProperties;
	}

}