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

package adept.data;

import adept.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


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
