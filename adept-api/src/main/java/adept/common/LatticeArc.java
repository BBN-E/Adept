package adept.common;

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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

/**
 * The Class LatticeArc.
 */
public class LatticeArc implements Serializable {

	private static final long serialVersionUID = -3826853795625736588L;

	/** The src. */
	private int src;
	
	/** The dst. */
	private int dst;
	
	/** The token. */
	private Token token;
	
	/** The weight. */
	private double weight;
     
	/**
	 * Instantiates a new lattice arc.
	 */
	public LatticeArc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the src.
	 *
	 * @return the src
	 */
	public int getSrc() {
		return src;
	}

	/**
	 * Sets the src.
	 *
	 * @param src the new src
	 */
	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * Gets the dst.
	 *
	 * @return the dst
	 */
	public int getDst() {
		return dst;
	}

	/**
	 * Sets the dst.
	 *
	 * @param dst the new dst
	 */
	public void setDst(int dst) {
		this.dst = dst;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(Token token) {
                checkArgument(token != null);
		this.token = token;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
          
}
