package adept.utilities;

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

import adept.common.*;
import adept.io.Writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implements the white space tokenizer, simple text file parser and provides
 * functionality to create a Document object using the aforementioned utilities.
 */
public class DocumentSaver {
	// Whether or not to make this a singleton class?
	// Singleton instance
	/** The instance. */
	private static DocumentSaver instance;

	/**
	 * Instantiates a new document maker.
	 */
	private DocumentSaver() {
		System.out
				.println("Instantiating singleton instance for document creator");
	}

	/**
	 * Gets the single instance of DocumentSaver.
	 * 
	 * @return single instance of DocumentSaver
	 */
	public static DocumentSaver getInstance() {
		if (instance == null)
			instance = new DocumentSaver();
		return instance;
	}

	/**
	 * Creates the default document. The argument filename *must* be non-null.
	 *
	 * @param document the document
	 * @param sentenceList the sentence list
	 * @param posList the pos list
	 * @param synChunkList the syn chunk list
	 * @param entityList the entity list
	 * @param filename the filename
	 */
	
	// Save the HltContentContainer object in CoNLL input format.  
	// See http://www.cnts.ua.ac.be/conll2003/ner/
	// Each word has been put on a separate line and there is an empty line after each sentence. 
	// The first item on each line is a word, the second a part-of-speech (POS) tag, 
	// the third a syntactic chunk tag and the fourth the named entity tag.
	// Example output:
	//	CRICKET NNP I-NP O
	//	- : O O
	//	LEICESTERSHIRE NNP I-NP I-ORG
	//	TAKE NNP I-NP O
	//	OVER IN I-PP O
	public void saveDefaultDocument(Document document, 
									List<Sentence> sentenceList,
									List<PartOfSpeech> posList,
									List<SyntacticChunk> synChunkList,
									List<EntityMention> entityList,
									String filename) {		

		Map<Integer,String> posMap = new HashMap<Integer, String>();
		if ( posList != null ) for( int i = 0; i < posList.size(); ++i)
		{
			PartOfSpeech pos = posList.get(i);
			TokenOffset offset = pos.getTokenOffset();
			Type type = (Type) pos.getPartOfSpeechTag();
			if ( type != null ) for( int j = offset.getBegin(); j < offset.getEnd(); ++j)
			{
				// No I- prefix
				String tag = type.getType();
				posMap.put(j, tag); 
			}						
		}
		Map<Integer,String> synMap = new HashMap<Integer, String>();
		if ( synChunkList != null ) for( int i = 0; i < synChunkList.size(); ++i)
		{
			SyntacticChunk synChunk = synChunkList.get(i);
			TokenOffset offset = synChunk.getTokenOffset();
			Type type = (Type) synChunk.getSyntacticChunkType();
			if ( type != null ) for( int j = offset.getBegin(); j < offset.getEnd(); ++j)
			{		
				String tag;
				// Doc disagrees with reference.  Reference uses "B" for first token always.
				if ( j == offset.getBegin() /*&& synMap.containsKey(j-1)*/) tag = "B-";
				else tag = "I-";
				tag += type.getType();
				synMap.put(j, tag); 
			}						
		}
		Map<Integer,String> entMap = new HashMap<Integer, String>();
		if ( entityList!= null ) for( int i = 0; i < entityList.size(); ++i)
		{
			EntityMention entity = entityList.get(i);
			TokenOffset offset = entity.getTokenOffset();
			Type type = (Type) entity.getEntityType();
			if ( type != null ) for( int j = offset.getBegin(); j < offset.getEnd(); ++j)
			{		
				// Recode OTHER to MISC to match original CoNLL examples.
				String tag;
				if ( j == offset.getBegin() /*&& entMap.containsKey(j-1)*/) tag = "B-";
				else tag = "I-";
				if ( type.getType().equals("OTHER")) tag += "MISC";
				else tag += type.getType();
				entMap.put(j, tag); 
			}					
		}
		//
		StringBuffer sb = new StringBuffer();
		sb.append("-DOCSTART- -X- O O\n");
		for( int i = 0; i < sentenceList.size(); ++i)
		{
			sb.append("\n");				
			Sentence sentence = sentenceList.get(i);
			TokenStream tokenStream = sentence.getTokenStream();
			TokenOffset offset = sentence.getTokenOffset();
			for( int j = offset.getBegin(); j < offset.getEnd(); ++j)
			{			
				Token token = tokenStream.get(j);
				String pos;
				if ( posMap.containsKey(j)) pos = posMap.get(j);
				else pos = "O";
				// syntactic chunks are not yet implemented.
				String syn;
				if ( synMap.containsKey(j)) syn = synMap.get(j);
				else syn = "I-NP";// TEMP "O";
				String ent;
				if ( entMap.containsKey(j)) ent = entMap.get(j);
				else ent = "O";
				String line = String.format("%s %s %s %s\n", token.getValue(), pos, syn, ent);
				sb.append(line);
			}
		}
		Writer.getInstance().writeToFile(filename, sb.toString());
	}


        public void saveSemanticTextSimDocument(Document document, 
			List<SentenceSimilarity> sentenceSimilarityList,
			String filename) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < sentenceSimilarityList.size(); ++i){
			SentenceSimilarity sentenceSimilarity = sentenceSimilarityList.get(i);
			sb.append ( sentenceSimilarity.getSentence1().getValue()
					+ "\t" + sentenceSimilarity.getSentence2().getValue() + "\n");
			Map<String,Float> similarity = sentenceSimilarity.getSimilarity();
			for (Map.Entry<String, Float> entry : similarity.entrySet()){
				sb.append(entry.getKey() + ":" + entry.getValue() + " ");
			}
			sb.append("\n");
		}
		Writer.getInstance().writeToFile(filename, sb.toString());
	}

    public void saveDeceptionDetectionDocument (Document document,
       List<DeceptionTheory> deceptionTheoryList,
       String filename){
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < deceptionTheoryList.size(); ++i){
            DeceptionTheory deceptionTheory = deceptionTheoryList.get(i);
            sb.append ( deceptionTheory.isDeceptive()?"lie":"truth" + "|" + deceptionTheory.getChunk().getValue()+"\n");
        }
        Writer.getInstance().writeToFile(filename, sb.toString());
    }
}
