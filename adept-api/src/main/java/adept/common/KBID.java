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
