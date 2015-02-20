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

import java.util.ArrayList;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentList.
 */
public class DocumentListHandler extends ArrayList<Document> implements DocumentListService.Iface {

	private DocumentList myDocumentList;

	/** The id. */
	protected IDHandler idHandler;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 651655831447893195L;

	/**
	 * Instantiates a new document list.
	 */
	public DocumentListHandler() {
		myDocumentList = new DocumentList();
		IDHandler idHandler = new IDHandler();
		myDocumentList.id = idHandler.getID();
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myDocumentList.the id string
	 */
	public String getIdString() {
		return idHandler.getIdString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return myDocumentList.the id
	 */
	public String getId() {
		return idHandler.getId();
	}

	public DocumentList getDocumentList() {
		return myDocumentList;
	}

}