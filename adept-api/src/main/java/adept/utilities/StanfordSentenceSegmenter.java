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

package adept.utilities;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Corpus;
import adept.common.Document;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.Sentence;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.io.LDCCorpusReader;
import adept.io.Reader;

// TODO: Auto-generated Javadoc
/**
 * The Class StanfordSentenceSegmenter.
 */
public class StanfordSentenceSegmenter implements ISentenceSegmenter 
{
	
	/** The tokenizer instance. */
	private static StanfordSentenceSegmenter instance;

	/**
	 * Constructor to instantiate a new Stanford sentence segmenter.
	 */
	private StanfordSentenceSegmenter() {
		System.out.println("Instantiating singleton instance for sentence segmenter");
	}

	/**
	 * Gets the single instance of StanfordTokenizer.
	 * 
	 * @return single instance of StanfordTokenizer
	 */
	public static StanfordSentenceSegmenter getInstance() {
		if (instance == null)
			instance = new StanfordSentenceSegmenter();
		return instance;
	}
	
	// Stanford Core NLP sentence segmenter
	/* (non-Javadoc)
	 * @see adept.utilities.ISentenceSegmenter#getSentences(java.lang.String, adept.common.TokenStream)
	 */
	@Override
	public List<Sentence> getSentences(String text, TokenStream tokenstream) 
		{
			Properties props = new Properties();
		    props.put("annotators", "tokenize,ssplit");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			Annotation annotation = new Annotation(text);
			pipeline.annotate(annotation);
			
			// now create sentences annotation
			List<Sentence> sentences = new ArrayList<Sentence>();
			int tokenStartIndex=0, sentenceSequenceId = 0, tokenEndIndex = 0;
			
			List<CoreMap> annotations = annotation.get(CoreAnnotations.SentencesAnnotation.class) ;
			
			for( CoreMap sentenceAnn : annotations ) 
			{		
				List<CoreLabel> sentenceTokenAnns = sentenceAnn
						.get(TokensAnnotation.class);
				 tokenEndIndex = tokenStartIndex
						+ (sentenceTokenAnns.size()-1 );
				 
				try
				{
					Sentence sentence = new Sentence(sentenceSequenceId++,
							new TokenOffset(tokenStartIndex, tokenEndIndex),
							tokenstream);
					sentences.add(sentence);
					tokenStartIndex = tokenEndIndex + 1 ;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			return sentences ;

			
	    }
}