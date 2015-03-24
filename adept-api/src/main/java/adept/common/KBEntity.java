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
import adept.data.IEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class KBEntity represents a pointer to a Knowledge Base Entity 
 * uniquely identified by entity ID and KB URI. 
 */
public class KBEntity extends HltContent implements IEntity {

	/** The entity id. */
	private final long entityId;

	/** The kb uri. */
	private String kbUri;

	/** The entity type. */
	private IType entityType;

	/**
	 * Instantiates a new kB entity.
	 *
	 * @param entityId the entity id
	 * @param kbUri the kb uri
	 * @param entityType the entity type
	 * @param value the value
	 */
	public KBEntity(long entityId, String kbUri, IType entityType, String value) {
		this.entityId = entityId;
                checkArgument(kbUri != null && kbUri.trim().length()>0);
		this.kbUri = kbUri;
 
                checkArgument(entityType!=null);
		this.entityType = entityType;

                checkArgument(value!=null && value.trim().length()>0);
		this.value = value;
	}

	/**
	 * Gets the entity id.
	 *
	 * @return the entity id
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * Gets the kb uri.
	 *
	 * @return the kb uri
	 */
	public String getKbUri() {
		return kbUri;
	}

	/**
	 * Sets the kb uri.
	 *
	 * @param kbUri the new kb uri
	 */
	public void setKbUri(String kbUri) {
                checkArgument(kbUri!=null && kbUri.trim().length()>0);
		this.kbUri = kbUri;
	}

	/**
	 * Sets the entity type.
	 *
	 * @param entityType the new entity type
	 */
	public void setEntityType(IType entityType) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.IEntity#getEntity()
	 */
	@Override
	public String getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof KBEntity))
			return false;
		KBEntity kbEntity = (KBEntity) obj;
		return (kbEntity.getEntityId() == this.entityId) && kbEntity.getKbUri().equals(this.kbUri);			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%s_%s", this.entityId, this.kbUri);				
		return code.hashCode();
	}

}