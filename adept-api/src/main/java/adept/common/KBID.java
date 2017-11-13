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

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Resource;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a generic KB ID.
 * 
 */
public final class KBID implements Serializable {
	
	private static final long serialVersionUID = -7723969866756350639L;

	/** Unique KB ID */
	private final String objectID;
	
	/** Source KB */
	private final String kbNamespace;
	
	// constructor
	public KBID(String objectID, String kbNamespace)
	{
		checkArgument(objectID!=null);
		checkArgument(kbNamespace!=null);
		this.objectID = objectID;
		this.kbNamespace = kbNamespace;
	}

	/**
	 * Alternative convenience construction from a Resource
	 *
	 * @param object A SPARQL Resource with a URI.
	 */
	public KBID(final Resource object) {
		// String split the URI, because Jena's getNamespace()/getLocalName() don't
		// split at the right point of the URI in some contexts.
		String[] idParts = object.getURI().split("#", 2);
		this.objectID = idParts[1];
		this.kbNamespace = idParts[0];
	}

	/**
     * @return May never be null.
     */
    public String getObjectID()
    {
    	return objectID;
    }
    
    /**
     * 
     */
    public String getKBNamespace()
    {
    	return kbNamespace;
    }

    @Override
    public String toString(){
        return "{KBID: objectID="+this.objectID+", kbNamespace="+this.kbNamespace+"}";
    }	
    
    /**
     * a combination of the source KB name and
     * the KB object URI
     */
	public int hashCode() {
		return Objects.hashCode(this.kbNamespace, this.objectID);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof KBID))
			return false;
		KBID kbID = (KBID) obj;
		return (kbID.getKBNamespace().equals(this.kbNamespace) && kbID.getObjectID().equals(this.objectID));			
	}
}