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
 * The Class Dependency.
 */
public class Dependency extends HltContent implements Serializable {

	private static final long serialVersionUID = -1974377870460120726L;

	/** The governor. */
	private final Chunk governor;

	/** The dependent. */
	private final Chunk dependent;

	/** The dependency type. */
	private final String dependencyType;

	/**
	 * Instantiates a new dependency.
	 * 
	 * @param governor
	 *            the governor
	 * @param dependent
	 *            the dependent
	 * @param dependencyType
	 *            the dependency type
	 */
	public Dependency(Chunk governor, Chunk dependent, String dependencyType) {
		super();

                checkArgument(governor != null);
                checkArgument(dependent != null);
                checkArgument(dependencyType!=null && dependencyType.trim().length()>0);
		this.governor = governor;
		this.dependent = dependent;
		this.dependencyType = dependencyType;
	}

	/**
	 * Gets the governor.
	 * 
	 * @return the governor
	 */
	public Chunk getGovernor() {
		return governor;
	}

	/**
	 * Gets the dependent.
	 * 
	 * @return the dependent
	 */
	public Chunk getDependent() {
		return dependent;
	}

	/**
	 * Gets the dependency type.
	 * 
	 * @return the dependency type
	 */
	public String getDependencyType() {
		return dependencyType;
	}

}
