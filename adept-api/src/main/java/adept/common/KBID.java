/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.common;

import com.google.common.base.Objects;
import static com.google.common.base.Preconditions.checkArgument;
/**
 * Represents a generic KB ID.
 * 
 */
public final class KBID {
	
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
