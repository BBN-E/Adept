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

package adept.common;

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

import java.io.Serializable;


/**
 * The Class EventPhrase.  Provided for legacy support.  To represent event presence, it
 * is now better to use {@link EventText}.
 */
@Deprecated
public class EventPhrase extends Chunk implements Serializable {

	private static final long serialVersionUID = 3510099544275098443L;

	/** The tense. */
	private IType tense;

	/** The aspect. */
	private IType aspect;

	/** The event class. */
	private IType eventClass;

	/**
	 * Instantiates a new event phrase.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param tense the tense
	 * @param aspect the aspect
	 * @param eventClass the event class
	 */
	public EventPhrase(TokenOffset tokenOffset,
			TokenStream tokenStream, IType tense, IType aspect, IType eventClass) {
		super(tokenOffset, tokenStream);
		this.tense = tense;
		this.aspect = aspect;
		this.eventClass = eventClass;
	}

	/**
	 * Gets the tense.
	 * 
	 * @return the tense
	 */
	public String getTense() {
		return tense.getType();
	}

	/**
	 * Gets the aspect.
	 * 
	 * @return the aspect
	 */
	public String getAspect() {
		return aspect.getType();
	}

	/**
	 * Gets the event class.
	 * 
	 * @return the event class
	 */
	public String getEventClass() {
		return eventClass.getType();
	}

    /**
     * Returns an {@link EventText} which approximates this
     * event phrase.  The result will have the same event type as this and its
     * {@link EventText#getProvenanceChunks()}
     * will return a single-element set consisting of this {@code EventPhrase} itself. If
     * this {@code EventPhrase} has non-null tense or aspect, it will be added
     * as an attribute of the returned {@code EventOccurrence}.
     */
    public EventText asEventOccurrence() {
        final EventText.Builder ret = EventText.builder(eventClass, this);
        if (tense != null) {
            ret.addAttribute(tense, 1.0f);
        }
        if (aspect != null) {
            ret.addAttribute(aspect, 1.0f);
        }
        return ret.build();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((aspect == null) ? 0 : aspect.hashCode());
		result = prime * result + ((eventClass == null) ? 0 : eventClass.hashCode());
		result = prime * result + ((tense == null) ? 0 : tense.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventPhrase other = (EventPhrase) obj;
		if (aspect == null) {
			if (other.aspect != null)
				return false;
		} else if (!aspect.equals(other.aspect))
			return false;
		if (eventClass == null) {
			if (other.eventClass != null)
				return false;
		} else if (!eventClass.equals(other.eventClass))
			return false;
		if (tense == null) {
			if (other.tense != null)
				return false;
		} else if (!tense.equals(other.tense))
			return false;
		return true;
	}
}