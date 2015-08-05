/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2013
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
package adept.common;


/**
 * The Class TimePhrase.
 */
public class TimePhrase extends Chunk {

	/** The type. */
	private IType type;

	/** The resolution type. */
	private IType resolutionType;

	/** The resolution. */
	private String resolution;

	/**
	 * Instantiates a new time phrase.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param type the type
	 */
	public TimePhrase(TokenOffset tokenOffset,
			TokenStream tokenStream, IType type) {
		super(tokenOffset, tokenStream);
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type.getType();
	}

	/**
	 * Gets the resolution type.
	 * 
	 * @return the resolution type
	 */
	public String getResolutionType() {
		return resolutionType.getType();
	}

	/**
	 * Gets the resolution.
	 * 
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(IType type) {
		this.type = type;
	}

	/**
	 * Sets the resolution type.
	 *
	 * @param resolutionType the new resolution type
	 */
	public void setResolutionType(IType resolutionType) {
		this.resolutionType = resolutionType;
	}

	/**
	 * Sets the resolution.
	 * 
	 * @param resolution
	 *            the new resolution
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

}
