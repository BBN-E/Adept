/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */


package adept.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import adept.common.*;
import adept.module.*;

// TODO: Auto-generated Javadoc
/**
 * Sample NER that is only to be used as an example of how to build NLP modules
 * in terms of what classes to extend etc.
 * 
 * The following code naively assumes that all words starting with an uppercase
 * letter is a Named Entity.
 */
public class ExampleNamedEntityTagger extends NamedEntityTagger {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.AbstractModule#getModuleTraits()
	 */
	@Override
	public ModuleTraits getModuleTraits() throws AdeptModuleException {
		// TODO Auto-generated method stub
		return super.getModuleTraits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.AbstractModule#activate(java.lang.String)
	 */
	@Override
	public void activate(String configFilePath)
			throws InvalidPropertiesFormatException, IOException,
			AdeptModuleException {
		// TODO Auto-generated method stub
		super.activate(configFilePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.AbstractModule#getModuleConfig()
	 */
	@Override
	public ModuleConfig getModuleConfig() throws AdeptModuleException {
		// TODO Auto-generated method stub
		return super.getModuleConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.AbstractModule#deactivate()
	 */
	@Override
	public void deactivate() throws AdeptModuleException {
		// TODO Auto-generated method stub
		super.deactivate();
	}

	// main functionality
	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.NamedEntityTagger#process(adept.common.Sentence)
	 */
	@Override
	public List<EntityMention> process(Sentence sentence) {
		List<EntityMention> entitymentions = new ArrayList<EntityMention>();
		int seqId = 0;
		EntityMention em;
		for (Token token : sentence.getTokenStream()) {
			if (token.getCharOffset().getBegin() >= sentence.getTokenOffset()
					.getBegin()
					&& token.getCharOffset().getEnd() <= sentence
							.getTokenOffset().getEnd()) {
				String value = token.getValue();
				if (value.charAt(0) == value.toUpperCase().charAt(0)) {
					em = new EntityMention(seqId++, sentence.getTokenOffset(),
							sentence.getTokenStream());
					System.out.println("upper case found " + value);
					entitymentions.add(em);
				}
			}
		}
		return entitymentions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.NamedEntityTagger#processAsync(adept.common.Sentence)
	 */
	@Override
	public long processAsync(Sentence sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.NamedEntityTagger#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<EntityMention> hltContents) {
		// TODO Auto-generated method stub
		return null;
	}

}
