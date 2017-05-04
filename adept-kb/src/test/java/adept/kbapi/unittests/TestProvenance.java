package adept.kbapi.unittests;

/*-
 * #%L
 * adept-kb
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

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import adept.common.Chunk;
import adept.kbapi.KBNumber;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

/**
 * 
 * @author dkolas
 */
public class TestProvenance extends KBUnitTest {

	@Test
	public void testDocumentID() throws KBUpdateException, KBQueryException {
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(3);

		builder.addProvenance(generateProvenance("three"));
		KBNumber three = builder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(three);

		Assert.assertEquals("Document ID incorrect", "DocumentID1", ((KBTextProvenance) three
				.getProvenances().iterator().next()).getDocumentID());

		KBNumber queriedThree = kb.getNumberByValue(3).get();
		Assert.assertEquals("Document ID incorrect", "DocumentID1",
				((KBTextProvenance) queriedThree.getProvenances().iterator().next())
						.getDocumentID());
		Assert.assertEquals(
				"Inserted number's provenances differ from queried number's provenances",
				three.getProvenances(), queriedThree.getProvenances());
	}

	@Test
	public void testCorpusID() throws KBUpdateException, KBQueryException {
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(3);

		builder.addProvenance(generateProvenance("three"));
		KBNumber three = builder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(three);

		Assert.assertEquals("Corpus ID incorrect", "CorpusID1", ((KBTextProvenance) three
				.getProvenances().iterator().next()).getCorpusID());

		KBNumber queriedThree = kb.getNumberByValue(3).get();
		Assert.assertEquals("Document ID incorrect", "CorpusID1", ((KBTextProvenance) queriedThree
				.getProvenances().iterator().next()).getCorpusID());
		Assert.assertEquals(
				"Inserted number's provenances differ from queried number's provenances",
				three.getProvenances(), queriedThree.getProvenances());
	}

	/**
	 * This test is incomplete because we don't have easy access to the underlying chunk IDs.
	 *
	 * @throws KBUpdateException
	 * @throws KBQueryException
	 */
	@Test
	public void testProvenanceDeletion() throws KBUpdateException, KBQueryException {
		// Create two provenances with the same underlying chunk
		//   Note that 7 and 77 are distinct values for same text so that we get unique
		//   KBNumber objects
		Chunk sevenChunk = new Chunk(testTokenOffset, testTokenStream);
		KBTextProvenance.InsertionBuilder sevenChunkBuilder = KBTextProvenance.builder(sevenChunk, 1.0f);
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(7);
		builder.addProvenance(sevenChunkBuilder);
		KBNumber seven = builder.insert(kb);
		builder = KBNumber.numberInsertionBuilder(77);
		builder.addProvenance(sevenChunkBuilder);
		KBNumber sevenseven = builder.insert(kb);

		// Check that what we inserted makes sense (distinct objects and provenance, same chunk)
		Assert.assertNotEquals("Number KBIDs match", seven.getKBID(), sevenseven.getKBID());
		Assert.assertEquals("Number values don't match", 7, seven.getNumber());
		Assert.assertEquals("Number values don't match", 77, sevenseven.getNumber());
		KBTextProvenance sevenProvenance = (KBTextProvenance) seven.getProvenances().iterator().next();
		KBTextProvenance sevensevenProvenance = (KBTextProvenance) sevenseven.getProvenances().iterator().next();
		Assert.assertNotEquals("Provenance KBIDs match", sevenProvenance.getKBID(), sevensevenProvenance.getKBID());
		Assert.assertEquals("Chunk text doesn't match", sevenProvenance.getValue(), sevensevenProvenance.getValue());
		Assert.assertEquals("Chunk start offset doesn't match", sevenProvenance.getBeginOffset(), sevensevenProvenance.getBeginOffset());
		Assert.assertEquals("Chunk end offset doesn't match", sevenProvenance.getEndOffset(), sevensevenProvenance.getEndOffset());
		String sevenChunkId = getChunkIDByProvenance(sevenProvenance);
		String sevensevenChunkId = getChunkIDByProvenance(sevensevenProvenance);
		Assert.assertEquals("Chunk IDs don't match", sevenChunkId, sevensevenChunkId);
		List<String> orphanChunkIds = getOrphanChunkIDs();
		Assert.assertEquals("Orphan chunks before deletion", orphanChunkIds.size(), 0);

		// Delete one of the objects and make sure we still have valid provenances
		kb.deleteKBObject(sevenseven.getKBID());
		orphanChunkIds = getOrphanChunkIDs();
		Assert.assertEquals("Orphan chunks after first deletion", orphanChunkIds.size(), 0);
		kb.removeOrphanMetaData();
		orphanChunkIds = getOrphanChunkIDs();
		Assert.assertEquals("Orphan chunks after first orphan deletion", orphanChunkIds.size(), 0);
		KBNumber seven2 = kb.getNumberValueByID(seven.getKBID());
		Assert.assertEquals("Number KBIDs don't match", seven.getKBID(), seven2.getKBID());
		Assert.assertEquals("Number values don't match", seven.getNumber(), seven2.getNumber());
		KBTextProvenance seven2Provenance = (KBTextProvenance) seven.getProvenances().iterator().next();
		Assert.assertEquals("Provenance KBIDs don't match", sevenProvenance.getKBID(), seven2Provenance.getKBID());
		Assert.assertEquals("Chunk text doesn't match", sevenProvenance.getValue(), seven2Provenance.getValue());
		Assert.assertEquals("Chunk start offset doesn't match", sevenProvenance.getBeginOffset(), seven2Provenance.getBeginOffset());
		Assert.assertEquals("Chunk end offset doesn't match", sevenProvenance.getEndOffset(), seven2Provenance.getEndOffset());

		// Clean up
		kb.deleteKBObject(seven.getKBID());
		orphanChunkIds = getOrphanChunkIDs();
		Assert.assertEquals("No orphan chunks after second deletion", orphanChunkIds.size(), 1);
		kb.removeOrphanMetaData();
		orphanChunkIds = getOrphanChunkIDs();
		Assert.assertEquals("Orphan chunks after second orphan deletion", orphanChunkIds.size(), 0);
	}
}
