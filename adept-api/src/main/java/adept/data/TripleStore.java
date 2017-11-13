/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

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