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

import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//import adept.module.AdeptModuleException;
import junit.framework.TestCase;

import adept.common.*;



/**
 * The Class AdeptExceptionTest.
 */
public class ChunkTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	// Space at end allows for end+1 character offset.
	static protected String _Text = "This is a test. ";
	
	/**
	 * Test.
	 */
	@Test
	public void test() {
		String corpusId = "corpusId";
		String type = "corpusType";
		String name = "corpusName";
		String uri = "corpusUri";
		Corpus corpus = new Corpus(corpusId, type, name, uri);
		String docId = "docId";
		String docType = "docType";
		String docUri = "docUri";
		String language = "English";
		Document doc = new Document(docId, corpus, docType, docUri, language );
		doc.setValue(_Text);
		String[] spaceTokens = _Text.split(" ");
		TokenStream tokenStream = new TokenStream(TokenizerType.WHITESPACE, 
													TranscriptType.SOURCE, 
													language, 
													ChannelName.NONE, 
													ContentType.TEXT, 
													doc);
//		tokenStream.setDocument( doc );
		int startOffset = 0;
		int sequenceId = 0;
		for( String s : spaceTokens)
		{
			int endOffset = startOffset + s.length()+1;
			CharOffset charOffset = new CharOffset( startOffset, endOffset );
			Token t = new Token( sequenceId, charOffset, s);
			startOffset = endOffset;
			++ sequenceId;
			tokenStream.add(t);
		}
		List<Chunk> frontChunkList = new ArrayList<Chunk>();
		List<Chunk> backChunkList = new ArrayList<Chunk>();
		int len = tokenStream.size();
		for ( int i = 0; i < len-1; ++i)
		{
			{
				TokenOffset to = new TokenOffset(0,i);
				Chunk c = new Chunk(to, tokenStream);
				frontChunkList.add(c);
			}
			{
				TokenOffset to = new TokenOffset(i+1,len-1);
				Chunk c = new Chunk(to, tokenStream);
				backChunkList.add(c);
			}			
		}
		
//		public boolean Contains(Chunk chunk) {
		testContains(  frontChunkList.get(2), frontChunkList.get(1));//yes
		testContains(  frontChunkList.get(2), backChunkList.get(2)); //no
		
//		public Chunk getContainingChunk(List<Chunk> chunks) {
		testContaining(  frontChunkList.get(2), frontChunkList);//yes
		testContaining(  frontChunkList.get(2), backChunkList); //no			
			
//		public boolean Overlaps(Chunk chunk) {
		testOverlaps(  frontChunkList.get(2), backChunkList.get(0));//yes
		testOverlaps(  frontChunkList.get(1), backChunkList.get(2)); //no

//		public Chunk getOverlappingChunk(List<Chunk> chunks) {
		testOverlapping(  frontChunkList.get(2), backChunkList);//yes
		testOverlapping(  frontChunkList.get(0), backChunkList); //no			

//		public Chunk getMatchingChunk(List<Chunk> chunks) {
		testMatching(  frontChunkList.get(2), frontChunkList);//yes
		testMatching(  frontChunkList.get(2), backChunkList); //no		
		
//		public boolean equals(Object obj) {
		testEquals(  frontChunkList.get(2), frontChunkList.get(2));//yes
		testEquals(  frontChunkList.get(2), frontChunkList.get(1)); //no			
	}
	
protected void testContains( Chunk c1, Chunk c2 )
{
	String ret = c1.Contains(c2) ? "contains" : "does not contain";
	printResult( c1, ret, c2);
}

protected void testContaining( Chunk c1, List<Chunk> list )
{
	Chunk c2 = c1.getContainingChunk(list);
	if ( c2 == null )
	{
		for ( Chunk c : list) printResult( c1, "is not contained by", c);
	}
	else
	{
		printResult( c1, "contained by", c2);
	}
}

	protected void testOverlaps( Chunk c1, Chunk c2 )
	{
		String ret = c1.Overlaps(c2) ? "overlaps" : "does not overlap";
		printResult( c1, ret, c2);
	}
	
	protected void testOverlapping( Chunk c1, List<Chunk> list )
	{
		Chunk c2 = c1.getOverlappingChunk(list);
		if ( c2 == null )
		{
			for ( Chunk c : list) printResult( c1, "is not overlapped by", c);
		}
		else
		{
			printResult( c1, "overlapped by", c2);
		}
	}
	
	protected void testMatching( Chunk c1, List<Chunk> list )
	{
		Chunk c2 = c1.getMatchingChunk(list);
		if ( c2 == null )
		{
			for ( Chunk c : list) printResult( c1, "is not matched by", c);
		}
		else
		{
			printResult( c1, "matched by", c2);
		}
	}
		
	protected void testEquals( Chunk c1, Chunk c2 )
	{
		String ret = c1.equals(c2) ? "equals" : "does not equal";
		printResult( c1, ret, c2);
	}
	
	protected void printResult( Chunk c1, String ret, Chunk c2)
	{
		System.out.format("\"%s\" %s \"%s\"\n", c1.getValue(), ret, c2.getValue());
	}

}
