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

package adept.metadata;

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

import java.io.Serializable;

/**
 *
 * Contains information about the source algorithm
 * generating the HLT annotations.
 */
public class SourceAlgorithm implements Serializable {

	private static final long serialVersionUID = -9098962344372512814L;

	/** The algorithm name */
	private final String algorithmName;

	/** contributing site name */
	private final String contributingSiteName;

	private SourceAlgorithm() {
		// no args constructor for dozer
		algorithmName = null;
		contributingSiteName = null;
	}

	public SourceAlgorithm(String algorithmName, String contributingSiteName)
	{
		this.algorithmName = algorithmName;
		this.contributingSiteName = contributingSiteName;
	}


	// getters
	public String getAlgorithmName()
	{
		return algorithmName;
	}

	public String getContributingSiteName()
	{
		return contributingSiteName;
	}
}