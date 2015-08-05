package adept.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


/**
 * The Class QueryObject.
 */
public class QueryObject {

    /** The query type. */
    protected QueryType queryType;
    
    /** The name. */
    protected String name;
    
    /** The value_str. */
    protected String value_str;
    
    /** The query string. */
    protected String queryString;
    
    /** The query_str. */
    protected String query_str;
    
    /** The value. */
    protected Number value;
    
    /** The query. */
    protected Number query;

    /** The extras. */
    protected boolean extras;

    /** The boost. */
    protected Float boost = null;

    /** The default field. */
    protected String defaultField = null;
    
    /** The from_str. */
    protected String from_str = null;
    
    /** The gt_str. */
    protected String gt_str = null;
    
    /** The gte_str. */
    protected String gte_str = null;
    
    /** The lt_str. */
    protected String lt_str = null;
    
    /** The lte_str. */
    protected String lte_str = null;
    
    /** The to_str. */
    protected String to_str = null;
    
    /** The from. */
    protected Number from = null;
    
    /** The gt. */
    protected Number gt = null;
    
    /** The gte. */
    protected Number gte = null;
    
    /** The lt. */
    protected Number lt = null;
    
    /** The lte. */
    protected Number lte = null;
    
    /** The to. */
    protected Number to = null;
    
    /** The include lower. */
    protected boolean includeLower = true; //default specified in ElasticSearch Java api
    
    /** The include upper. */
    protected boolean includeUpper = true;


    /**
     * Instantiates a new query object.
     *
     * @param queryType the query type
     * @param str the str
     */
    public QueryObject(QueryType queryType, String str)
    {
        this.queryType = queryType;
        this.extras = false;
        switch(queryType)
        {
        case STRING:
            queryString = str;
            break;
        case RANGE:
            name = str;
            break;
        }
    }

    /**
     * Instantiates a new query object.
     *
     * @param queryType the query type
     * @param str1 the str1
     * @param str2 the str2
     */
    public QueryObject(QueryType queryType, String str1, String str2)
    {
        this.queryType = queryType;
        this.extras = false;
        switch(queryType)
        {
        case FIELD:
            name = str1;
            query = null;
            query_str = str2;
            break;
        case WILDCARD:
            name = str1;
            query_str = str2;
            break;
        }
    }
    
    /**
     * Instantiates a new query object.
     *
     * @param queryType the query type
     * @param str the str
     * @param num the num
     */
    public QueryObject(QueryType queryType, String str, Number num)
    {
        this.queryType = queryType;
        this.extras = false;
        switch(queryType)
        {
        case FIELD:
            name = str;
            query = num;
            query_str = null;
            break;
        }
    }
    
    /**
     * Gets the query type.
     *
     * @return the query type
     */
    public QueryType getQueryType()
    {
        return queryType;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the value_ str.
     *
     * @return the value_ str
     */
    public String getValue_Str()
    {
        return value_str;
    }

    /**
     * Gets the query string.
     *
     * @return the query string
     */
    public String getQueryString()
    {
        return queryString;
    }

    /**
     * Gets the query_ str.
     *
     * @return the query_ str
     */
    public String getQuery_Str()
    {
        return query_str;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Number getValue()
    {
        return value;
    }

    /**
     * Gets the query.
     *
     * @return the query
     */
    public Number getQuery()
    {
        return query;
    }

    /**
     * Gets the boost.
     *
     * @return the boost
     */
    public Float getBoost()
    {
        return boost;
    }

    /**
     * Gets the from_ str.
     *
     * @return the from_ str
     */
    public String getFrom_Str()
    {
        return from_str;
    }

    /**
     * Gets the gt_ str.
     *
     * @return the gt_ str
     */
    public String getGt_Str()
    {
        return gt_str;
    }

    /**
     * Gets the gte_ str.
     *
     * @return the gte_ str
     */
    public String getGte_Str()
    {
        return gte_str;
    }

    /**
     * Gets the lt_ str.
     *
     * @return the lt_ str
     */
    public String getLt_Str()
    {
        return lt_str;
    }

    /**
     * Gets the lte_ str.
     *
     * @return the lte_ str
     */
    public String getLte_Str()
    {
        return lte_str;
    }

    /**
     * Gets the to_ str.
     *
     * @return the to_ str
     */
    public String getTo_Str()
    {
        return to_str;
    }

    /**
     * Gets the from.
     *
     * @return the from
     */
    public Number getFrom()
    {
        return from;
    }

    /**
     * Gets the gt.
     *
     * @return the gt
     */
    public Number getGt()
    {
        return gt;
    }

    /**
     * Gets the gte.
     *
     * @return the gte
     */
    public Number getGte()
    {
        return gte;
    }

    /**
     * Gets the lt.
     *
     * @return the lt
     */
    public Number getLt()
    {
        return lt;
    }

    /**
     * Gets the lte.
     *
     * @return the lte
     */
    public Number getLte()
    {
        return lte;
    }

    /**
     * Gets the to.
     *
     * @return the to
     */
    public Number getTo()
    {
        return to;
    }

    /**
     * Gets the include lower.
     *
     * @return the include lower
     */
    public boolean getIncludeLower()
    {
        return includeLower;
    }

    /**
     * Gets the include upper.
     *
     * @return the include upper
     */
    public boolean getIncludeUpper()
    {
        return includeUpper;
    }

    /**
     * Sets the boost.
     *
     * @param b the b
     * @return the query object
     */
    public QueryObject setBoost(float b)
    {
        extras = true;
        boost = b;
        return this;
    }

    /**
     * Sets the from_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setFrom_Str(String f)
    {
        extras = true;
        from_str = f;
        return this;
    }

    /**
     * Sets the gt_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGt_Str(String f)
    {
        extras = true;
        gt_str = f;
        return this;
    }

    /**
     * Sets the gte_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGte_Str(String f)
    {
        extras = true;
        gte_str = f;
        return this;
    }

    /**
     * Sets the lt_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLt_Str(String f)
    {
        extras = true;
        lt_str = f;
        return this;
    }

    /**
     * Sets the lte_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLte_Str(String f)
    {
        extras = true;
        lte_str = f;
        return this;
    }

    /**
     * Sets the to_ str.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setTo_Str(String f)
    {
        extras = true;
        to_str = f;
        return this;
    }

    /**
     * Sets the from.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setFrom(String f)
    {
        extras = true;
        from_str = f;
        return this;
    }

    /**
     * Sets the gt.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGt(String f)
    {
        extras = true;
        gt_str = f;
        return this;
    }

    /**
     * Sets the gte.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGte(String f)
    {
        extras = true;
        gte_str = f;
        return this;
    }

    /**
     * Sets the lt.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLt(String f)
    {
        extras = true;
        lt_str = f;
        return this;
    }

    /**
     * Sets the lte.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLte(String f)
    {
        extras = true;
        lte_str = f;
        return this;
    }

    /**
     * Sets the to.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setTo(String f)
    {
        extras = true;
        to_str = f;
        return this;
    }

    /**
     * Sets the from.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setFrom(Number f)
    {
        extras = true;
        from = f;
        return this;
    }

    /**
     * Sets the gt.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGt(Number f)
    {
        extras = true;
        gt = f;
        return this;
    }

    /**
     * Sets the gte.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setGte(Number f)
    {
        extras = true;
        gte = f;
        return this;
    }

    /**
     * Sets the lt.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLt(Number f)
    {
        extras = true;
        lt = f;
        return this;
    }

    /**
     * Sets the lte.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setLte(Number f)
    {
        extras = true;
        lte = f;
        return this;
    }

    /**
     * Sets the to.
     *
     * @param f the f
     * @return the query object
     */
    public QueryObject setTo(Number f)
    {
        extras = true;
        to = f;
        return this;
    }

    /**
     * Sets the include upper.
     *
     * @param b the b
     * @return the query object
     */
    public QueryObject setIncludeUpper(boolean b)
    {
        extras = true;
        includeUpper = b;
        return this;
    }

    /**
     * Sets the include lower.
     *
     * @param b the b
     * @return the query object
     */
    public QueryObject setIncludeLower(boolean b)
    {
        extras = true;
        includeLower = b;
        return this;
    }

    /**
     * Checks for extras.
     *
     * @return true, if successful
     */
    public boolean hasExtras()
    {
        return extras;
    }

}