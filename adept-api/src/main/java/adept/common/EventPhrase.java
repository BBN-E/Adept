/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

// TODO: Auto-generated Javadoc
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
