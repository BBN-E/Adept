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

import com.google.common.testing.EqualsTester;
import java.util.List;
import junit.framework.AssertionFailedError;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class EqualsTest {
    @Test
    public void testEntityEqualsSuccess() {
        TokenOffset tokenOffset = new TokenOffset(0, 0);
        EntityMention entityMention = new EntityMention(123, tokenOffset, getSampleTokenStream());
        
        Entity entity1 = new Entity(1, new Type("Location"));
        entity1.setCanonicalMentions(entityMention);
        Entity entity2 = new Entity(1, new Type("Location"));
        entity2.setCanonicalMentions(entityMention);
        try {
            testEquality(entity1, entity2);
        } catch (AssertionFailedError ex) {
            System.out.println("Entity .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testEntityEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        TokenOffset tokenOffset = new TokenOffset(0, 0);
        EntityMention entityMention = new EntityMention(123, tokenOffset, getSampleTokenStream());
        
        Entity entity1 = new Entity(1, new Type("Person"));
        entity1.setCanonicalMentions(entityMention);
        Entity entity2 = new Entity(1, new Type("Location"));
        entity2.setCanonicalMentions(entityMention);
        try {
            testEquality(entity1, entity2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("Entity .equals() method fails to return false on differing entities");
            fail();
        }
    }
    
    @Test
    public void testChunkEqualsSuccess() {
        TokenStream tokenStream = getSampleTokenStream();
        Chunk chunk1 = new Chunk(new TokenOffset(0, 0), tokenStream);
        Chunk chunk2 = new Chunk(new TokenOffset(0, 0), tokenStream);
        
        try {
            testEquality(chunk1, chunk2);
        } catch (AssertionFailedError ex) {
            System.out.println("Chunk .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testChunkEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        TokenStream tokenStream = getSampleTokenStream();
        Chunk chunk1 = new Chunk(new TokenOffset(0, 0), tokenStream);
        Chunk chunk2 = new Chunk(new TokenOffset(0, 1), tokenStream);
        
        try {
            testEquality(chunk1, chunk2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("Chunk .equals() method fails to return false on differing chunks");
            fail();
        }
    }
    
    @Test
    public void testEntityMentionEqualsSuccess() {
        TokenOffset tokenOffset = new TokenOffset(0, 0);
        TokenStream tokenStream = getSampleTokenStream();
        EntityMention entityMention1 = new EntityMention(123, tokenOffset, tokenStream);
        EntityMention entityMention2 = new EntityMention(123, tokenOffset, tokenStream);
        
        try {
            testEquality(entityMention1, entityMention2);
        } catch (AssertionFailedError ex) {
            System.out.println("EntityMention .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testEntityMentionEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        TokenStream tokenStream = getSampleTokenStream();
        EntityMention entityMention1 = new EntityMention(123, new TokenOffset(0, 0), tokenStream);
        EntityMention entityMention2 = new EntityMention(123, new TokenOffset(0, 1), tokenStream);
        
        try {
            testEquality(entityMention1, entityMention2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("EntityMention .equals() method fails to return false on differing EntityMentions");
            fail();
        }
    }
    
    @Test
    public void testEventEqualsSuccess() {
        Event event1 = new Event(123, new Type("TestType"));
        Event event2 = new Event(123, new Type("TestType"));
        
        try {
            testEquality(event1, event2);
        } catch (AssertionFailedError ex) {
            System.out.println("Event .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testEventEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        Event event1 = new Event(123, new Type("TestType1"));
        Event event2 = new Event(123, new Type("TestType2"));
        
        try {
            testEquality(event1, event2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("Event .equals() method fails to return false on differing Events");
            fail();
        }
    }
    
    @Test
    public void testKBIDEqualsSuccess() {
        KBID kbid1 = new KBID("TestKBUri", "TestSourceKB");
        KBID kbid2 = new KBID("TestKBUri", "TestSourceKB");
        
        try {
            testEquality(kbid1, kbid2);
        } catch (AssertionFailedError ex) {
            System.out.println("KBID .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testKBIDEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        KBID kbid1 = new KBID("TestKBUri1", "TestSourceKB");
        KBID kbid2 = new KBID("TestKBUri2", "TestSourceKB");
        int code1 = kbid1.hashCode();
        int code2 = kbid2.hashCode();
        if (code1 == code2) {
            System.out.println("PROBLEM");
        }
        try {
            testEquality(kbid1, kbid2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("KBID .equals() method fails to return false on differing KBIDs");
            fail();
        }
    }
    
    @Test
    public void testRelationEqualsSuccess() {
        Relation relation1 = new Relation(123, new Type("TestRelationType"));
        Relation relation2 = new Relation(123, new Type("TestRelationType"));
        
        try {
            testEquality(relation1, relation2);
        } catch (AssertionFailedError ex) {
            System.out.println("Relation .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testRelationEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        Relation relation1 = new Relation(123, new Type("TestRelationType1"));
        Relation relation2 = new Relation(123, new Type("TestRelationType2"));
        
        try {
            testEquality(relation1, relation2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("Relation .equals() method fails to return false on differing Relations");
            fail();
        }
    }
    
    @Test
    public void testTokenOffsetEqualsSuccess() {
        TokenOffset tokenOffset1 = new TokenOffset(0, 5);
        TokenOffset tokenOffset2 = new TokenOffset(0, 5);
        
        try {
            testEquality(tokenOffset1, tokenOffset2);
        } catch (AssertionFailedError ex) {
            System.out.println("TokenOffset .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testTokenOffsetEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        TokenOffset tokenOffset1 = new TokenOffset(0, 5);
        TokenOffset tokenOffset2 = new TokenOffset(0, 3);
        
        try {
            testEquality(tokenOffset1, tokenOffset2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("TokenOffset .equals() method fails to return false on differing TokenOffsets");
            fail();
        }
    }
    
    @Test
    public void testTypeEqualsSuccess() {
        Type type1 = new Type("TestType");
        Type type2 = new Type("TestType");
        
        try {
            testEquality(type1, type2);
        } catch (AssertionFailedError ex) {
            System.out.println("Type .equals()/.hashCode() methods do not match.");
            fail();
        }
    }
    
    @Test
    public void testTypeEqualsFailure() {
        boolean doesTestFailAsExpected = false;
        
        Type type1 = new Type("TestType1");
        Type type2 = new Type("TestType2");
        
        try {
            testEquality(type1, type2);
        } catch (AssertionFailedError ex) {
            doesTestFailAsExpected = true;
        }
        
        if (!doesTestFailAsExpected) {
            System.out.println("Type .equals() method fails to return false on differing Types");
            fail();
        }
    }
        
    private static void testEquality(Object o1, Object o2) {
        new EqualsTester().addEqualityGroup(o1, o2).testEquals();
    }
    
    private static TokenStream getSampleTokenStream() {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ut est a mauris venenatis congue.";
        String corpusId = "corpusId";
        String type = "corpusType";
        String name = "corpusName";
        String uri = "corpusUri";
        Corpus corpus = new Corpus(corpusId, type, name, uri);
        String docId = "docId";
        String docType = "docType";
        String docUri = "docUri";
        String language = "English";
        Document doc = new Document(docId, corpus, docType, docUri, language);
        doc.setValue(text);
        String[] spaceTokens = text.split(" ");
        TokenStream tokenStream = new TokenStream(TokenizerType.WHITESPACE, 
                                                    TranscriptType.SOURCE, 
                                                    language, 
                                                    ChannelName.NONE, 
                                                    ContentType.TEXT, 
                                                    doc);
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
        
        return tokenStream;
    }
}