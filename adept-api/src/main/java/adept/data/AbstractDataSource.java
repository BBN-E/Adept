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
