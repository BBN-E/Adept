/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

package adept.data;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import adept.common.Item;


/**
 * The Class Justification.
 */
public class Justification extends Item{

    /** The entity name. */
    private String ent1;

    /** The second entity name. */
    private String ent2;

    /** The predicate. */
    private String predicate;

    /** The score. */
    private double score;

    /** The agent. */
    private String agent;

    /** The agentType. */
    private String agentType;

    /** The date. */
    private String date;

    /** The iteration. */
    private long iteration;

    /** The comment. */
    private String comment;

    /** The update iteration. */
    private long updateIteration; 

    /**
     * Instatiates a new Justification.
     *
     * @param ent1 the value of ent1 associated with this justification
     * @param predicate the value of predicate associated with this justification
     */
    public Justification(String ent1, String predicate)
    {
        this.ent1 = ent1;
        this.predicate = predicate;
        this.ent2 = null;
    }

    /**
     * Instatiates a new Justification.
     *
     * @param ent1 the value of ent1 associated with this justification
     * @param predicate the value of predicate associated with this justification
     * @param ent2 the value of ent1 associated with this justification
     */
    public Justification(String ent1, String predicate, String ent2)
    {
        this.ent1 = ent1;
        this.predicate = predicate;
        this.ent2 = ent2;
    }

    /**
     * Gets the associated ent1.
     *
     * @return the ent1
     */
    public String getEnt1()
    {
        return ent1;
    }

    /**
     * Gets the associated predicate.
     *
     * @return the predicate
     */
    public String getPredicate()
    {
        return predicate;
    }

    /**
     * Gets the associated ent2.
     *
     * @return the ent2
     */
    public String getEnt2()
    {
        return ent2;
    }

    /**
     * Gets the score.
     *
     * @return the score
     */
    public double getScore()
    {
        return score;
    }

    /**
     * Gets the agent.
     *
     * @return the agent
     */
    public String getAgent()
    {
        return agent;
    }

    /**
     * Gets the agent type.
     *
     * @return the agent type
     */
    public String getAgentType()
    {
        return agentType;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Gets the iteration.
     *
     * @return the iteration
     */
    public long getIteration()
    {
        return iteration;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Gets the updated iteration.
     *
     * @return the updated iteration
     */
    public long getUpdateIteration()
    {
        return updateIteration;
    }

	/**
	 * Sets the score.
	 * 
	 * @param score
	 *            the new score
	 */
    public void setScore(double score)
    {
        this.score = score;
    }

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
    public void setAgent(String agent)
    {
        this.agent = agent;
    }

	/**
	 * Sets the agent type.
	 * 
	 * @param agentType
	 *            the new agent type
	 */
    public void setAgentType(String agentType)
    {
        this.agentType = agentType;
    }

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
    public void setDate(String date)
    {
        this.date = date;
    }

	/**
	 * Sets the iteration.
	 * 
	 * @param iteration
	 *            the new iteration
	 */
    public void setIteration(long iteration)
    {
        this.iteration = iteration;
    }

	/**
	 * Sets the comment.
	 * 
	 * @param comment
	 *            the new comment
	 */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

	/**
	 * Sets the update iteration.
	 * 
	 * @param updateIteration
	 *            the new update iteration
	 */
    public void setUpdateIteration(long updateIteration)
    {
        this.updateIteration = updateIteration;
    }

}