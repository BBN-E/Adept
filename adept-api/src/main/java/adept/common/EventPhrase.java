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

package adept.common;


/**
 * The Class EventPhrase.  Provided for legacy support.  To represent event presence, it
 * is now better to use {@link EventText}.
 */
@Deprecated
public class EventPhrase extends Chunk {

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
}