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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class KBRelation represents a pointer to a Knowledge Base Entity 
 * uniquely identified by KB URI and source KB type. It derives from parent
 * class {@link adept.common.KBID}.
 */
public class KBRelation extends KBID {

	/** The relation type. */
	private final IType relationType;

	/**
	 * Instantiates a new KB relation.
     *
	 * @param kbUri the kb uri
	 * @param relationType the relation type
	 */
	public KBRelation(String kbUri, IType relationType) {
		super(kbUri, null);
                
		checkArgument(relationType!=null);
		this.relationType = relationType;
	}
	
	/**
	 * Instantiates a new kB relation.
	 *
	 * @param kbUri the kb uri
	 * @param relationType the relation type
	 * @param sourceKB the source KB
	 */
	public KBRelation(String kbUri, IType relationType, String sourceKB) {
		super(kbUri, sourceKB);
                
		checkArgument(relationType!=null);
		this.relationType = relationType;
	}

	
	/**
	 * Gets the relation type.
	 *
	 * @return the relation type
	 */
	public IType getRelationType() {
		return relationType;
	}

}