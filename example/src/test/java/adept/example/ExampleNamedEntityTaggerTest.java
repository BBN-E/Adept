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
 
import org.junit.Test;

import java.util.List;

import adept.common.Document;
import adept.common.EntityMention;
import adept.common.Sentence;
import adept.common.TokenOffset;
import adept.common.TokenizerType;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.utilities.DocumentMaker;


// TODO: Auto-generated Javadoc
/**
 * Simple class to test the dummy NER module.
 */
public class ExampleNamedEntityTaggerTest {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	@Test
	public void Test() {
	    HltContentContainer hltContainer = new HltContentContainer();
		Document doc = DocumentMaker.getInstance().createDefaultDocument(null, null, null, null, "English",
				ClassLoader.getSystemResource("ExampleNamedEntityTaggerTest.txt").getFile(),hltContainer);
		for(Passage p : hltContainer.getPassages())
			System.out.println("PASSAGE::: " + p.getTokenOffset().getBegin() + "  " + p.getTokenOffset().getEnd());

		// create a sentence from the document. Note that the offsets are random
		// here,
		// and do not reflect an actual grammatical sentence.
		Sentence s = new Sentence(0, new TokenOffset(0, 30), doc.getDefaultTokenStream());

		ExampleNamedEntityTagger dummy = new ExampleNamedEntityTagger();
		List<EntityMention> mentions = dummy.process(s);
		System.out.println("After process method");
		for (EntityMention mention : mentions) {
			System.out.println(mention.getValue());
		}
	}

}
