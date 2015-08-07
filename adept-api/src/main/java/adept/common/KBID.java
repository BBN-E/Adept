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

/**
 * Represents a generic KB ID.
 * 
 */
public class KBID {
	
	/** Unique KB ID */
	private final String kbUri;
	
	/** Source KB */
	private final String sourceKB;
	
	// constructor
	public KBID(String kbUri, String sourceKB)
	{
		this.kbUri = kbUri;
		this.sourceKB = sourceKB;
	}
	
    /**
     * @return May never be null.
     */
    public String getKBUri()
    {
    	return kbUri;
    }
    
    /**
     * 
     */
    public String getSourceKB()
    {
    	return sourceKB;
    }
    
    /**
     * a combination of the source KB name and
     * the KB object URI
     */
	public int hashCode() {
		String code = String.format("%s_%s", this.sourceKB, this.kbUri);				
		return code.hashCode();
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
		return (kbID.getSourceKB().equals(this.sourceKB) && kbID.getKBUri().equals(this.kbUri));			
	}
}