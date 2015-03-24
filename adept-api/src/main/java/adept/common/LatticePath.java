package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class LatticePath.
 */
public class LatticePath {

	/** The weight. */
	private float weight;
	
	/** The token stream list. */
	private List<TokenStream> tokenStreamList;

	/**
	 * Instantiates a new lattice path.
	 */
	public LatticePath() {
		super();
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Gets the token stream list.
	 *
	 * @return the token stream list
	 */
	public List<TokenStream> getTokenStreamList() {
		return tokenStreamList;
	}

	/**
	 * Sets the token stream list.
	 *
	 * @param tokenStreamList the new token stream list
	 */
	public void setTokenStreamList(List<TokenStream> tokenStreamList) {
                checkArgument(tokenStreamList != null);
		this.tokenStreamList = tokenStreamList;
	}
	
	
	
}
