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

import java.util.List;

import adept.common.*;

// TODO: Auto-generated Javadoc
/**
 * The Interface IQuery.
 */
public interface IQuery {

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @return the hlt content container
	 */
	HltContentContainer get(String aID);

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @param index the type
	 * @param type the type
	 * @return the hlt content container
	 */
	HltContentContainer get(String aID, String index, String type);

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @param index the type
	 * @param type the type
	 * @return the hlt content container
	 */
	String get_JSON(String aID, String index, String type);
	
	/**
	 * Search.
	 *
	 * @param aSearchObject the a search object
	 * @return the list of ids
	 */
	List<String> search(SearchObject aSearchObject);

	/**
	 * Search.
	 *
	 * @param aSearchObject the a search object
	 * @return the list of fields
	 */
	List<String> searchFields(SearchObject aSearchObject);
}