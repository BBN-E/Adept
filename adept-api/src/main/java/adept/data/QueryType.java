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

// TODO: Auto-generated Javadoc
/**
 * The Enum QueryType.
 */
public enum QueryType
{
    /** for boolean queries. no args.**/
    BOOLEAN,
    
    /** for constant score queries*. */
    //    CONSTANT_SCORE,
    //CUSTOM_BOOST_FACTOR,
    //CUSTOM_SCORE_QUERY,
    /** for disjunction max queries. no args.**/
    DIS_MAX,
    /** for field queries. args: String name, String query; String name, Number query**/
    FIELD,
    //FILTERED,
    /** for fuzzy field like this queries*. */
    FUZZY_LIKE_FIELD,
    
    /** for field like this queries*. */
    FUZZY_LIKE_THIS,
    
    /** for fuzzy queries*. */
    FUZZY,
    
    /** for match queries*. */
    MATCH,
    
    /** for match all queries*. */
    MATCH_ALL,
    
    /** for more like this field queries*. */
    MORE_LIKE_FIELD,
    
    /** for more like this queries*. */
    MORE_LIKE_THIS,
    
    /** for prefix queries*. */
    PREFIX,
    
    /** for string queries*. */
    STRING,
    
    /** for range queries*. */
    RANGE,
    
    /** for span first queries*. */
    SPAN_FIRST,
    
    /** for span near queries*. */
    SPAN_NEAR,
    
    /** for span not queries*. */
    SPAN_NOT,
    
    /** for span or queries*. */
    SPAN_OR,
    
    /** for span term queries*. */
    SPAN_TERM,
    
    /** for term queries*. */
    TERM,
    
    /** for wildcard queries*. */
    WILDCARD,

    /** for nested queries. */
    NESTED,

    /** for parent queries. */
    PARENT,

    /** for parent queries. */
    CHILD
}
