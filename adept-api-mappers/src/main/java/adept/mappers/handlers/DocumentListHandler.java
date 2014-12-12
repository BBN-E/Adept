/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
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
