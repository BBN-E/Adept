package adept.metadata;


/**
 * 
 * Contains information about the source algorithm
 * generating the HLT annotations.
 */
public class SourceAlgorithm {
	
	/** The algorithm name */
	private final String algorithmName;
	
	/** contributing site name */
	private final String contributingSiteName;
	
	
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