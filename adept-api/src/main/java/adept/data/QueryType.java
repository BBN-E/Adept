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
