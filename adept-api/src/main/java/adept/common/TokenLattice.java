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
import java.util.List;



/**
 * The Class TokenLattice.
 */
public class TokenLattice implements Serializable {

	private static final long serialVersionUID = 7846181392721268089L;

	/** The start state. */
	private int startState;
	
	/** The end state. */
	private int endState;
		
	/** The arcs. */
	private List<LatticeArc> arcs; 
	
	/** The cached_best_path. */
	private LatticePath cached_best_path;

	/**
	 * Instantiates a new token lattice.
	 */
	public TokenLattice() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the start state.
	 *
	 * @return the start state
	 */
	public int getStartState() {
		return startState;
	}

	/**
	 * Sets the start state.
	 *
	 * @param startState the new start state
	 */
	public void setStartState(int startState) {
		this.startState = startState;
	}

	/**
	 * Gets the end state.
	 *
	 * @return the end state
	 */
	public int getEndState() {
		return endState;
	}

	/**
	 * Sets the end state.
	 *
	 * @param endState the new end state
	 */
	public void setEndState(int endState) {
		this.endState = endState;
	}

	/**
	 * Gets the arcs.
	 *
	 * @return the arcs
	 */
	public List<LatticeArc> getArcs() {
		return arcs;
	}

	/**
	 * Sets the arcs.
	 *
	 * @param arcs the new arcs
	 */
	public void setArcs(List<LatticeArc> arcs) {
                checkArgument(arcs!=null);
		this.arcs = arcs;
	}

	/**
	 * Gets the cached_best_path.
	 *
	 * @return the cached_best_path
	 */
	public LatticePath getCached_best_path() {
		return cached_best_path;
	}

	/**
	 * Sets the cached_best_path.
	 *
	 * @param cached_best_path the new cached_best_path
	 */
	public void setCached_best_path(LatticePath cached_best_path) {
                //TODO: null check
		this.cached_best_path = cached_best_path;
	}
	
	
	
}