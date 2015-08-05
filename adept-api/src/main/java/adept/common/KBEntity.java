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
 * The Class KBEntity represents a pointer to a Knowledge Base Entity 
 * uniquely identified by KB URI and source KB type. It derives from parent
 * class {@link adept.common.KBID}.
 */
public class KBEntity extends KBID {

	/** The entity type. */
	private final IType entityType;

	/**
	 * Instantiates a new kB entity.
     *
	 * @param kbUri the kb uri
	 * @param entityType the entity type
	 * @param value the value
	 */
	public KBEntity(String kbUri, IType entityType) {
		super(kbUri, null);
                
		checkArgument(entityType!=null);
		this.entityType = entityType;
	}
	
	/**
	 * Instantiates a new kB entity.
	 *
	 * @param kbUri the kb uri
	 * @param entityType the entity type
	 * @param sourceKB the source KB
	 */
	public KBEntity(String kbUri, IType entityType, String sourceKB) {
		super(kbUri, sourceKB);
                
		checkArgument(entityType!=null);
		this.entityType = entityType;
	}

	
	/**
	 * Gets the entity type.
	 *
	 * @return the entity type
	 */
	public IType getEntityType() {
		return entityType;
	}

}