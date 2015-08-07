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

import java.util.List;

import adept.common.Paraphrase;


/**
 * The Class ParaphraseStore.
 */
public abstract class ParaphraseStore extends AbstractDataSource implements
		IParaphraseStore {

	/**
	 * Instantiates a new paraphrase store.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param connectionProperties
	 *            the connection properties
	 */
	public ParaphraseStore(String dataSourceName,
			ConnectionProperties connectionProperties) {
		super(dataSourceName, DataSourceType.SQL, connectionProperties);
		// TODO Auto-generated constructor stub
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

	/* (non-Javadoc)
	 * @see adept.data.IParaphraseStore#getParaphrases(java.lang.String)
	 */
	@Override
	public List<Paraphrase> getParaphrases(String phrase) {
		// TODO Auto-generated method stub
		return null;
	}



}