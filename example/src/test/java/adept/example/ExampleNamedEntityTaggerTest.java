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
