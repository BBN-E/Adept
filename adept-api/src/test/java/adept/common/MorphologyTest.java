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

package adept.common;

import adept.metadata.SourceAlgorithm;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests morphology classes.
 */
@SuppressWarnings("EqualsWithItself")
public final class MorphologyTest {

	private static final String TEST = "test";
	private static final String TEXT = "Hello worlds.";
	private static final String HELLO = "Hello";
	private static final String WORLDS = "worlds";
	private static final String PERIOD = ".";
	private static final String WORLDS_LEMMA = "world";
	private static final String WORLDS_NOTE = "world s";
	private static final String NOUN = "NOUN";
	private static final String ROOT = "ROOT";
	private static final String SUFFIX = "SUFFIX";

	private static final SourceAlgorithm ALGORITHM = new SourceAlgorithm(TEST, TEST);

	private TokenStream ts;

	private static void assertAbsent(final Optional opt) {
		assertFalse(opt.isPresent());
	}

	private static void assertSelfEqual(final Object o) {
		assertCompareEqual(o, o);
	}

	private static void assertCompareEqual(final Object o1, final Object o2) {
		// Verify symmetric equality
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		// Verify hash codes match
		assertTrue(o1.hashCode() == o1.hashCode());
	}

	private static void assertCompareNotEqual(final Object o1, final Object o2) {
		// Verify symmetric inequality
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
	}

	@Before
	public void setUp() {
		final Document doc = new Document(TEST, new Corpus(TEST, TEST, TEST, TEST), TEST, TEST, TEST);
		doc.setValue(TEXT);
		ts = new TokenStream(TokenizerType.ADEPT, TranscriptType.SOURCE, "English", ChannelName.NONE,
				ContentType.TEXT, doc);
		ts.add(0, new Token(0, new CharOffset(0, 5), HELLO));
		ts.add(1, new Token(1, new CharOffset(6, 12), WORLDS));
		ts.add(2, new Token(2, new CharOffset(12, 13), PERIOD));
	}

	/**
	 * Tests creating a morph feature.
	 */
	@Test
	public void testMorphFeature() {
		final MorphFeature feature1 = MorphFeature.create("NUM", "PL");
		assertEquals("NUM", feature1.property());
		assertEquals("PL", feature1.value());
		assertSelfEqual(feature1);

		final MorphFeature feature2 = MorphFeature.create("NUM", "PL");
		assertCompareEqual(feature1, feature2);

		final MorphFeature feature3 = MorphFeature.create("NUM", "SING");
		assertCompareNotEqual(feature1, feature3);
	}

	/**
	 * Tests creating a morph type.
	 */
	@Test
	public void testMorphType() {
		final MorphType type1 = MorphType.create(ROOT);
		assertEquals(ROOT, type1.type());
		assertSelfEqual(type1);

		final MorphType type2 = MorphType.create(ROOT);
		assertCompareEqual(type1, type2);

		final MorphType type3 = MorphType.create(SUFFIX);
		assertCompareNotEqual(type1, type3);
	}

	/**
	 * Tests creating a morph.
	 */
	@Test
	public void testMorph() {
		final MorphType root = MorphType.create(ROOT);
		final MorphType suffix = MorphType.create(SUFFIX);
		final MorphFeature feature = MorphFeature.create("NUM", "PL");
		final CharOffset rootOffset = new CharOffset(6, 11);
		final CharOffset suffixOffset = new CharOffset(11, 12);

		final Morph stemMorph = Morph.builder(WORLDS_LEMMA, root)
				.setSourceOffsets(ImmutableList.of(rootOffset))
				.build();
		assertEquals(WORLDS_LEMMA, stemMorph.form());
		assertEquals(root, stemMorph.morphType());
		assertEquals(ImmutableList.of(rootOffset), stemMorph.sourceOffsets().get());
		assertAbsent(stemMorph.features());
		assertSelfEqual(stemMorph);

		// Make an identical one to test equality and also that setSingleSourceOffset produces the same
		// result
		final Morph stemMorph2 = Morph.builder(WORLDS_LEMMA, root)
				.setSingleSourceOffset(rootOffset)
				.build();
		assertCompareEqual(stemMorph, stemMorph2);

		final Morph suffixMorph = Morph.builder("s", suffix)
				.setFeatures(ImmutableList.of(feature))
				.setSourceOffsets(ImmutableList.of(suffixOffset))
				.build();
		assertEquals("s", suffixMorph.form());
		assertEquals(suffix, suffixMorph.morphType());
		assertEquals(ImmutableList.of(suffixOffset), suffixMorph.sourceOffsets().get());
		assertEquals(ImmutableSet.of(feature), suffixMorph.features().get());
		assertCompareNotEqual(stemMorph, suffixMorph);
	}

	/**
	 * Tests creating a MorphToken.
	 */
	@Test
	public void testMorphToken() {
		final MorphType root = MorphType.create(ROOT);
		final MorphType suffix = MorphType.create(SUFFIX);

		// Basic text
		final MorphToken mt1 = MorphToken.builder(HELLO, ts).build();
		assertEquals(HELLO, mt1.text());
		assertEquals(ts, mt1.tokenStream());
		assertSelfEqual(mt1);

		// With fields filled
		final TokenOffset worldsOffset = new TokenOffset(1, 1);
		final MorphFeature feature = MorphFeature.create("NUM", "PL");
		final Morph stemMorph = Morph.builder(WORLDS_LEMMA, root).build();
		final Morph suffixMorph = Morph.builder("s", suffix)
				.setFeatures(ImmutableList.of(feature))
				.build();
		final MorphToken mt2 = MorphToken.builder(WORLDS, ts)
				.setLemma(WORLDS_LEMMA)
				.setConfidence(1.0F)
				.setHeadTokenAndTokenOffsets(worldsOffset)
				.setNotes(WORLDS_NOTE)
				.setPos(NOUN)
				.setRoots(ImmutableList.of(WORLDS_LEMMA))
				.setMorphs(ImmutableList.of(stemMorph, suffixMorph))
				.setFeatures(ImmutableList.of(feature))
				.build();
		assertEquals(WORLDS, mt2.text());
		assertEquals(ts, mt2.tokenStream());
		assertEquals(WORLDS_LEMMA, mt2.lemma().get());
		// Explicit Float creation to avoid ambiguous method call
		assertEquals(Float.valueOf(1.0f), mt2.confidence().get());
		assertEquals(worldsOffset, mt2.headTokenOffset().get());
		assertEquals(ImmutableList.of(worldsOffset), mt2.tokenOffsets().get());
		assertEquals(WORLDS_NOTE, mt2.notes().get());
		assertEquals(NOUN, mt2.pos().get());
		assertEquals(ImmutableList.of(WORLDS_LEMMA), mt2.roots().get());
		assertEquals(ImmutableList.of(stemMorph, suffixMorph), mt2.morphs().get());
		assertEquals(ImmutableSet.of(feature), mt2.features().get());
		assertSelfEqual(mt2);
		assertCompareNotEqual(mt1, mt2);

		// Test that things are absent when not specified
		final MorphToken mt3 = MorphToken.builder(PERIOD, ts).build();
		assertEquals(PERIOD, mt3.text());
		assertEquals(ts, mt3.tokenStream());
		assertAbsent(mt3.lemma());
		assertAbsent(mt3.confidence());
		assertAbsent(mt3.headTokenOffset());
		assertAbsent(mt3.tokenOffsets());
		assertAbsent(mt3.notes());
		assertAbsent(mt3.pos());
		assertAbsent(mt3.roots());
		assertAbsent(mt3.features());
		assertAbsent(mt3.morphs());
		assertSelfEqual(mt3);
		assertCompareNotEqual(mt1, mt3);
		assertCompareNotEqual(mt2, mt3);

		final MorphToken mt4 = MorphToken.builder(PERIOD, ts).build();
		assertCompareEqual(mt3, mt4);
	}

	/**
	 * Tests that you cannot create a morph token with an incorrect head token offset.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testMorphTokenHeadOffset() {
		MorphToken.builder(HELLO, ts).setHeadToken(new TokenOffset(0, 1));
	}

	/**
	 * Tests creating a morph token sequence.
	 */
	@Test
	public void testMorphTokenSequence() {
		final MorphToken mt1 = MorphToken.builder(HELLO, ts).build();
		final MorphToken mt2 = MorphToken.builder(WORLDS, ts).build();
		final MorphToken mt3 = MorphToken.builder(PERIOD, ts).build();
		// Test adding one-by-one
		final MorphTokenSequence seq1 = MorphTokenSequence.builder(ALGORITHM)
				.addToken(mt1)
				.addToken(mt2)
				.addToken(mt3)
				.build();
		assertEquals(ImmutableList.of(mt1, mt2, mt3), seq1.tokens());
		assertAbsent(seq1.confidence());
		assertSelfEqual(seq1);

		// Test adding all
		final MorphTokenSequence seq2 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt2, mt3))
				.build();
		assertEquals(ImmutableList.of(mt1, mt2, mt3), seq2.tokens());
		assertAbsent(seq1.confidence());
		assertSelfEqual(seq2);
		assertCompareEqual(seq1, seq2);

		// Check setting confidence and that it voids equality
		final MorphTokenSequence seq3 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt2, mt3))
				.setConfidence(1.0f)
				.build();
		// Explicit Float creation to avoid ambiguous method call
		assertEquals(Float.valueOf(1.0f), seq3.confidence().get());
		assertSelfEqual(seq3);
		assertCompareNotEqual(seq2, seq3);

		// Check that different token order makes them unequal
		final MorphTokenSequence seq4 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt3, mt2))
				.build();
		assertCompareNotEqual(seq2, seq4);
	}

	/**
	 * Tests creating a morph sentence.
	 */
	@Test
	public void testMorphSentence() {
		final MorphToken mt1 = MorphToken.builder(HELLO, ts).build();
		final MorphToken mt2 = MorphToken.builder(WORLDS, ts).build();
		final MorphToken mt3 = MorphToken.builder(PERIOD, ts).build();
		final MorphTokenSequence seq1 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt2, mt3))
				.build();
		final MorphTokenSequence seq2 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt2))
				.build();

		// Check a simple sentence
		final MorphSentence sent1 = MorphSentence.builder().addSequence(seq1).build();
		assertEquals(ImmutableList.of(seq1), sent1.morphTokenSequences());
		assertEquals(seq1, sent1.morphTokenSequences().get(0));
		assertSelfEqual(sent1);

		// Make a duplicate to test equality
		final MorphSentence sent2 = MorphSentence.builder().addSequence(seq1).build();
		assertCompareEqual(sent1, sent2);

		// Make two sequence versions and test for equality
		final MorphSentence sent3 = MorphSentence.builder()
				.addSequence(seq1)
				.addSequence(seq2)
				.build();
		assertEquals(ImmutableList.of(seq1, seq2), sent3.morphTokenSequences());
		assertEquals(seq1, sent3.morphTokenSequences().get(0));
		assertEquals(seq2, sent3.morphTokenSequences().get(1));
		assertSelfEqual(sent3);
		assertCompareNotEqual(sent1, sent3);

		final MorphSentence sent4 = MorphSentence.builder()
				.addAllSequences(ImmutableList.of(seq1, seq2))
				.build();
		assertSelfEqual(sent4);
		assertCompareEqual(sent3, sent4);
	}

	/**
	 * Tests adding a morph sentence to a sentence.
	 */
	@Test
	public void testSentence() {
		final MorphToken mt1 = MorphToken.builder(HELLO, ts).build();
		final MorphToken mt2 = MorphToken.builder(WORLDS, ts).build();
		final MorphToken mt3 = MorphToken.builder(PERIOD, ts).build();
		final MorphTokenSequence seq1 = MorphTokenSequence.builder(ALGORITHM)
				.addAllTokens(ImmutableList.of(mt1, mt2, mt3))
				.build();
		final MorphSentence morphSentence = MorphSentence.builder().addSequence(seq1).build();
		final Sentence sentence = new Sentence(0L, new TokenOffset(0, 2), ts);
		// Check that things are absent with nothing set
		assertAbsent(sentence.getMorphSentence());
		// Check that setting works
		sentence.setMorphSentence(morphSentence);
		assertEquals(sentence.getMorphSentence().get(), morphSentence);
	}
}
