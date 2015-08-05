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


/**
 * The Class TripleStore.
 */
public class TripleStore extends AbstractDataSource implements ITripleStore {

	/**
	 * Instantiates a new triple store.
	 * 
	 * @param dataSourceName
	 *            the data source name
	 * @param connectionProperties
	 *            the connection properties
	 */
	public TripleStore(String dataSourceName,
			ConnectionProperties connectionProperties) {
		super(dataSourceName, DataSourceType.TRIPLE, connectionProperties);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.ITripleStore#getValues(adept.data.IEntity,
	 * adept.data.ISlot)
	 */
	@Override
	public List<IValue> getValues(IEntity entity, ISlot slot) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.ITripleStore#getSlots(adept.data.IEntity,
	 * adept.data.IValue)
	 */
	@Override
	public List<ISlot> getSlots(IEntity entity, IValue value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.ITripleStore#getEntities(adept.data.ISlot,
	 * adept.data.IValue)
	 */
	@Override
	public List<IEntity> getEntities(ISlot slot, IValue value) {
		// TODO Auto-generated method stub
		return null;
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
