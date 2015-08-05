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


/**
 * The Interface IIngest.
 */
public interface IIngest {

    /**
     * Create index.
     *
     * @param indexName name for new index
     */
    void createIndex(String indexName);

	/**
	 * Upload.
	 *
	 * @param aHltContentContainer the a hlt content container
	 * @return the string
	 */
	String upload(HltContentContainer aHltContentContainer);

	/**
	 * Upload.
	 *
	 * @param aHltContentContainer the a hlt content container
     * @param index the index
     * @param type the type
	 * @return the string
	 */
	String upload(HltContentContainer aHltContentContainer, String index, String type);

	/**
	 * Upload.
	 *
	 * @param json the json
     * @param index the index
     * @param type the type

	 * @return the string
	 */
	String upload_JSON(String json, String index, String type);
	
	/**
	 * Bulk upload.
	 *
	 * @param aHltContentContainers the a hlt content containers
	 * @return the list
	 */
	List<String> bulkUpload(List<HltContentContainer> aHltContentContainers);

	/**
	 * Bulk upload.
	 *
	 * @param aHltContentContainers the a hlt content containers
	 * @param index the index
	 * @param type the type
	 * @return the string
	 */
	List<String> bulkUpload(List<HltContentContainer> aHltContentContainers, String index, String type);

	/**
	 * Bulk upload.
	 *
	 * @param jsons the jsons
	 * @param index the index
	 * @param type the type
	 * @return the string
	 */
	List<String> bulkUpload_JSON(List<String> jsons, String index, String type);

}