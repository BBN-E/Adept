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


/**
 * The Class FileSource.
 */
public class FileSource extends AbstractDataSource {

	/**
	 * Instantiates a new file source.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param connectionProperties
	 *            the connection properties
	 */
	public FileSource(String dataSourceName,
			ConnectionProperties connectionProperties) {
		super(dataSourceName, DataSourceType.FILE, connectionProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.AbstractDataSource#connect()
	 */
	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.AbstractDataSource#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.AbstractDataSource#getStatus()
	 */
	@Override
	public AbstractDataSource.DataSourceConnectionStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
