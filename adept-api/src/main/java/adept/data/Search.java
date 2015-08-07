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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/

package adept.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * The Class Search.
 */
public class Search {

    /** The description. */
    private String description;
    
    /** The indices. */
    private List<String> indices;
    
    /** The types. */
    private List<String> types;
    
    /** The fields. */
    private List<String> fields;
    
    /** The queries. */
    private List<QueryObject> queries;
    
    /** The from. */
    private int from;
    
    /** The size. */
    private int size;
    
    /** The explain. */
    private boolean explain;
    
    /** The index boosts. */
    private Map<String,Float> indexBoosts;
    
    /** The highlighted fields. */
    private Map<String,Integer> highlightedFields;
    //    private String timeout;
    //    private String queryHint;

    /**
     * Instantiates a new search.
     */
    public Search()
    {
        from = -1;
        size = -1;
        description = "";
        explain = false;
        indices = new ArrayList<String>();
        types = new ArrayList<String>();
        fields = new ArrayList<String>();
        queries = new ArrayList<QueryObject>();
        indexBoosts = new HashMap<String,Float>();
        highlightedFields = new HashMap<String,Integer>();
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() 
    {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return the search
     */
    public Search setDescription(String description) 
    {
        this.description = description;
        return this;
    }

    /**
     * Gets the indices.
     *
     * @return the indices
     */
    public List<String> getIndices() 
    {
        return indices;
    }

    /**
     * Sets the indices.
     *
     * @param indices the indices
     * @return the search
     */
    public Search setIndices(List<String> indices) 
    {
        this.indices = indices;
        return this;
    }

    /**
     * Adds the index.
     *
     * @param index the index
     * @return the search
     */
    public Search addIndex(String index)
    {
        indices.add(index);
        return this;
    }

    /**
     * Gets the types.
     *
     * @return the types
     */
    public List<String> getTypes() 
    {
        return types;
    }

    /**
     * Sets the types.
     *
     * @param types the types
     * @return the search
     */
    public Search setTypes(List<String> types) 
    {
        this.types = types;
        return this;
    }

    /**
     * Adds the type.
     *
     * @param type the type
     * @return the search
     */
    public Search addType(String type)
    {
        types.add(type);
        return this;
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public List<String> getFields() 
    {
        return fields;
    }

    /**
     * Sets the fields.
     *
     * @param fields the fields
     * @return the search
     */
    public Search setFields(List<String> fields) 
    {
        this.fields = fields;
        return this;
    }

    /**
     * Adds the field.
     *
     * @param field the field
     * @return the search
     */
    public Search addField(String field)
    {
        fields.add(field);
        return this;
    }

    /**
     * Gets the query objects.
     *
     * @return the query objects
     */
    public List<QueryObject> getQueryObjects() 
    {
        return queries;
    }

    /**
     * Sets the query objects.
     *
     * @param queries the queries
     * @return the search
     */
    public Search setQueryObjects(List<QueryObject> queries) 
    {
        this.queries = queries;
        return this;
    }

    /**
     * Adds the query object.
     *
     * @param queryObject the query object
     * @return the search
     */
    public Search addQueryObject(QueryObject queryObject)
    {
        queries.add(queryObject);
        return this;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size the size
     * @return the search
     */
    public Search setSize(int size)
    {
        this.size = size;
        return this;
    }

    /**
     * Gets the from.
     *
     * @return the from
     */
    public int getFrom()
    {
        return from;
    }

    /**
     * Sets the from.
     *
     * @param from the from
     * @return the search
     */
    public Search setFrom(int from)
    {
        this.from = from;
        return this;
    }

    /**
     * Gets the explain.
     *
     * @return the explain
     */
    public boolean getExplain()
    {
        return explain;
    }

    /**
     * Sets the explain.
     *
     * @param explain the explain
     * @return the search
     */
    public Search setExplain(boolean explain)
    {
        this.explain = explain;
        return this;
    }

    /**
     * Adds the index boost.
     *
     * @param index the index
     * @param boost the boost
     * @return the search
     */
    public Search addIndexBoost(String index, float boost)
    {
        if(!indices.contains(index))
            indices.add(index);
        indexBoosts.put(index,boost);
        return this;
    }

    /**
     * Sets the index boosts.
     *
     * @param indexBoosts the index boosts
     * @return the search
     */
    public Search setIndexBoosts(Map<String,Float> indexBoosts)
    {
        this.indexBoosts = indexBoosts;
        return this;
    }

    /**
     * Gets the index boosts.
     *
     * @return the index boosts
     */
    public Map<String,Float> getIndexBoosts()
    {
        return indexBoosts;
    }

    /**
     * Adds the highlighted field.
     *
     * @param field the field
     * @param highlight the highlight
     * @return the search
     */
    public Search addHighlightedField(String field, int highlight)
    {
        if(!fields.contains(field))
            fields.add(field);
        highlightedFields.put(field,highlight);
        return this;
    }

    /**
     * Sets the highlighted fields.
     *
     * @param highlightedFields the highlighted fields
     * @return the search
     */
    public Search setHighlightedFields(Map<String,Integer> highlightedFields)
    {
        this.highlightedFields = highlightedFields;
        return this;
    }

    /**
     * Gets the highlighted fields.
     *
     * @return the highlighted fields
     */
    public Map<String,Integer> getHighlightedFields()
    {
        return highlightedFields;
    }

    /**
     * Checks for indices.
     *
     * @return true, if successful
     */
    public boolean hasIndices()
    {
        return !indices.isEmpty();
    }

    /**
     * Checks for types.
     *
     * @return true, if successful
     */
    public boolean hasTypes()
    {
        return !types.isEmpty();
    }
}