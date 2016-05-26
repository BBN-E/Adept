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

package adept.utilities;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

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

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * The Class OpenNLPSentenceSegmenter.
 */
public class OpenNLPSentenceSegmenter implements ISentenceSegmenter 
{
	
	/** The tokenizer instance. */
	private static OpenNLPSentenceSegmenter instance;

	/**
	 * Constructor to instantiate a new Apache OpenNLP sentence segmenter.
	 */
	private OpenNLPSentenceSegmenter() {
		System.out.println("Instantiating singleton instance for sentence segmenter");
	}

	/**
	 * Gets the single instance of OpenNLPSentenceSegmenter.
	 * 
	 * @return single instance of OpenNLPSentenceSegmenter
	 */
	public static OpenNLPSentenceSegmenter getInstance() {
		if (instance == null)
			instance = new OpenNLPSentenceSegmenter();
		return instance;
	}
	
	// Apache Open NLP sentence segmenter
	/* (non-Javadoc)
	 * @see adept.utilities.ISentenceSegmenter#getSentences(java.lang.String, adept.common.TokenStream)
	 */
	@Override
	public List<Sentence> getSentences(String text, TokenStream tokenstream) 
		{

			SentenceDetector sentenceDetector = null;

			InputStream modelIn = null;

			try {
			  modelIn = Reader.findStreamInClasspathOrFileSystem("en-sent.bin");
			  SentenceModel model = new SentenceModel(modelIn);
			  modelIn.close();
	
			  sentenceDetector = new SentenceDetectorME(model);

			}
			catch (IOException e) {
			  e.printStackTrace();
			}
			finally {
			  if (modelIn != null) {
			    try {
			      modelIn.close();
			    }
			    catch (IOException e) {
			    }
			  }
			}

			String sentenceStrings[] = sentenceDetector.sentDetect(text);

			// now create sentences annotation
			List<Sentence> sentences = new ArrayList<Sentence>();
			int tokenStartIndex=0, sentenceSequenceId = 0, tokenEndIndex = 0;
			
			
			for( String sentenceString : sentenceStrings ) 
			{		

				TokenStream tokenStream = OpenNLPTokenizer.getInstance().tokenize(sentenceString, null);
				 tokenEndIndex = tokenStartIndex
						+ (tokenStream.size()-1 );
				 
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
