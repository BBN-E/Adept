package adept.data;

import java.util.List;

import adept.common.*;


/**
 * The Interface IQuery.
 */
public interface IQuery {

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @return the hlt content container
	 */
	HltContentContainer get(String aID);

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @param index the type
	 * @param type the type
	 * @return the hlt content container
	 */
	HltContentContainer get(String aID, String index, String type);

	/**
	 * Gets the.
	 *
	 * @param aID the a id
	 * @param index the type
	 * @param type the type
	 * @return the hlt content container
	 */
	String get_JSON(String aID, String index, String type);
	
	/**
	 * Search.
	 *
	 * @param aSearchObject the a search object
	 * @return the list of ids
	 */
	List<String> search(SearchObject aSearchObject);

	/**
	 * Search.
	 *
	 * @param aSearchObject the a search object
	 * @return the list of fields
	 */
	List<String> searchFields(SearchObject aSearchObject);
}
