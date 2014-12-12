package adept.data;

import adept.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchObject.
 */
public class SearchObject {

    /** The searches. */
    private List<Search> searches;

    /**
     * Instantiates a new search object.
     */
    public SearchObject()
    {
        searches = new ArrayList<Search>();
    }

    /**
     * Instantiates a new search object.
     *
     * @param s the s
     */
    public SearchObject(List<Search> s)
    {
        searches = s;
    }

    /**
     * Gets the searches.
     *
     * @return the searches
     */
    public List<Search> getSearches() {
        return searches;
    }

    /**
     * Sets the searches.
     *
     * @param searches the searches
     * @return the search object
     */
    public SearchObject setSearches(List<Search> searches) {
        this.searches = searches;
        return this;
    }

    /**
     * Adds the search.
     *
     * @param search the search
     * @return the search object
     */
    public SearchObject addSearch(Search search)
    {
        searches.add(search);
        return this;
    }

}
