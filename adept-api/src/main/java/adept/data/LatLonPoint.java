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
