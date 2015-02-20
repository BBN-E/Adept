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

package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * An abstract definition of an item, which is composed of an id and a value.
 */
public abstract class ItemHandler implements ItemService.Iface {

	protected Item myItem;

	/** The id. */
	protected IDHandler idHandler;

	/**
	 * Instantiates a new item.
	 */
	public ItemHandler() {
		myItem = new Item();
		IDHandler idHandler = new IDHandler();
		myItem.id = idHandler.getID();
	}

	/**
	 * Gets the value.
	 * 
	 * @return myItem.the value
	 */
	public String getValue() {
		return myItem.value;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myItem.the id string
	 */
	public String getIdString() {
		return idHandler.getIdString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return myItem.the id
	 */
	public String getId() {
		return idHandler.getId();
	}

	public Item getItem() {
		return myItem;
	}

}