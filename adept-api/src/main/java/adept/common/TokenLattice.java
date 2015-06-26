package adept.common;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class TokenLattice.
 */
public class TokenLattice {

	/** The start state. */
	private int startState;
	
	/** The end state. */
	private int endState;
		
	/** The arcs. */
	private List<LatticeArc> arcs; 
	
	/** The cached_best_path. */
	private LatticePath cached_best_path;

	/**
	 * Instantiates a new token lattice.
	 */
	public TokenLattice() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the start state.
	 *
	 * @return the start state
	 */
	public int getStartState() {
		return startState;
	}

	/**
	 * Sets the start state.
	 *
	 * @param startState the new start state
	 */
	public void setStartState(int startState) {
		this.startState = startState;
	}

	/**
	 * Gets the end state.
	 *
	 * @return the end state
	 */
	public int getEndState() {
		return endState;
	}

	/**
	 * Sets the end state.
	 *
	 * @param endState the new end state
	 */
	public void setEndState(int endState) {
		this.endState = endState;
	}

	/**
	 * Gets the arcs.
	 *
	 * @return the arcs
	 */
	public List<LatticeArc> getArcs() {
		return arcs;
	}

	/**
	 * Sets the arcs.
	 *
	 * @param arcs the new arcs
	 */
	public void setArcs(List<LatticeArc> arcs) {
                checkArgument(arcs!=null);
		this.arcs = arcs;
	}

	/**
	 * Gets the cached_best_path.
	 *
	 * @return the cached_best_path
	 */
	public LatticePath getCached_best_path() {
		return cached_best_path;
	}

	/**
	 * Sets the cached_best_path.
	 *
	 * @param cached_best_path the new cached_best_path
	 */
	public void setCached_best_path(LatticePath cached_best_path) {
                //TODO: null check
		this.cached_best_path = cached_best_path;
	}
	
	
	
}
