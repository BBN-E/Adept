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

// TODO: Auto-generated Javadoc
/**
 * The Class LatLonPoint.
 */
public class LatLonPoint {

	/** The lat. */
	private final double lat;

	/** The lon. */
	private final double lon;

	/**
	 * Instantiates a new lat lon point.
	 * 
	 * @param lat
	 *            the lat
	 * @param lon
	 *            the lon
	 */
	public LatLonPoint(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Gets the lat.
	 * 
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Gets the lon.
	 * 
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

}
